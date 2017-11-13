package club.ranleng.psnine.ui.imageGallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.onRequestPermissionCallback;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.Images;
import club.ranleng.psnine.ui.ImageViewActivity;
import club.ranleng.psnine.utils.FileUtils;
import club.ranleng.psnine.utils.TextUtils;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class ImagesGalleryActivity extends BaseActivity implements ImageGalleryAdapter.OnClick {

    private final int Camera = 16;
    private final int Gallery = 17;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private File tmpFile;
    private ArrayList<String> photos;
    private boolean selectable;
    private Context context;
    private ImageGalleryAdapter adapter;

    @Override
    public void setContentView() {
        setContentView(R.layout.layout_toolbar_recyclerview);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        context = this;
        setSupportActionBar(toolbar);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ImageGalleryAdapter(context, photos, ImagesGalleryActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getData() {
        photos = getIntent().getStringArrayListExtra("photos");
        selectable = (photos != null);
        freshTitle();
        load();
    }

    private void load() {
        refreshLayout.setRefreshing(true);
        ApiManager.getDefault().getAllPhotos().subscribe(new Consumer<Images>() {
            @Override
            public void accept(Images images) throws Exception {
                adapter.update(images);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_images_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (refreshLayout.isRefreshing()) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_upload_local) {
            openGallery();
            return true;
        } else if (id == R.id.action_upload_camera) {
            openCamera();
            return true;
        } else if (id == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("photos", photos);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Camera) {
                uploadFile(tmpFile);
            } else if (requestCode == Gallery) {
                uploadFile(FileUtils.getImageFile(this, data.getData()));
            }
        }
    }

    private void openCamera() {
        tmpFile = new File(getCacheDir(), System.currentTimeMillis() + ".jpg");
        Uri upload_temp_uri = FileProvider.getUriForFile(this, getString(R.string.authProvider), tmpFile);
        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, upload_temp_uri);
        startActivityForResult(mIntent, Camera);
    }

    private void openGallery() {
        requestRuntimePermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                new onRequestPermissionCallback() {
                    @Override
                    public void onGranted() {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, Gallery);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort(R.string.permissionDenied);
                    }
                });

    }

    private void uploadFile(File file) {
        refreshLayout.setRefreshing(true);
        ApiManager.getDefault().uploadPhoto(file)
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        refreshLayout.setRefreshing(false);
                        load();
                    }
                });
    }

    private void freshTitle() {
        if (!selectable) {
            setTitle("图床");
            return;
        }
        setTitle("图床 (已选: " + TextUtils.toString(photos.size()) + ")");
    }

    @Override
    public void onClick(View v, View root, String url) {
        if (!selectable) {
            return;
        }
        boolean contain = photos.contains(url);
        v.setVisibility(contain ? View.INVISIBLE : View.VISIBLE);
        if (contain) {
            photos.remove(url);
        } else {
            photos.add(url);
        }
        freshTitle();
    }

    @Override
    public void onLongClick(View v, final String id, final int pos, final String url) {
        String[] items = new String[]{"删除", "查看大图"};
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            ApiManager.getDefault().deletePhoto(id);
                            adapter.delete(pos);
                        } else if (which == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            ActivityUtils.startActivity(bundle, ImageViewActivity.class);
                        }
                    }
                }).show();
    }

}
