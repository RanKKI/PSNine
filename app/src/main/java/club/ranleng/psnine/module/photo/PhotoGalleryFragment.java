package club.ranleng.psnine.module.photo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.module.main.activity.MainActivity;
import club.ranleng.psnine.utils.FileUtils;
import me.drakeet.multitype.MultiTypeAdapter;

import static android.app.Activity.RESULT_OK;

public class PhotoGalleryFragment extends Fragment implements PhotoGalleryContract.View {

    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private PhotoGalleryContract.Presenter mPresenter;
    private Context context;
    private File tmpFile;
    private Boolean selectable = false;
    private ArrayList<String> photo_list = new ArrayList<>();

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new PhotoGalleryPresenter(this);
        selectable = !(getActivity().getClass() == MainActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.load();
            }
        });
        mPresenter.start();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photo_gallery, menu);
        menu.findItem(R.id.action_finish).setVisible(selectable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.menuSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File file = null;
            if (requestCode == KEY.PHOTO_CAMERA) {
                file = tmpFile;
            } else if (requestCode == KEY.PHOTO_LOCAL) {
                file = FileUtils.getImageFile(context,data.getData());
            }
            mPresenter.upload(file);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.requestPermissions(requestCode, grantResults);
    }

    @Override
    public void setPresenter(PhotoGalleryContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setPhotoGallery(MultiTypeAdapter adapter) {
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setLoading(Boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public List<String> getPhotos() {
        return photo_list;
    }

    @Override
    public Boolean getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, KEY.REQUEST_PERMISSION_READ_EXTERNAL);
                return false;
            }
        }
        return true;
    }

    @Override
    public void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, KEY.PHOTO_LOCAL);
    }

    @Override
    public void openCamera() {
        tmpFile = new File(context.getCacheDir(), System.currentTimeMillis() + ".jpg");
        Uri upload_temp_uri = FileProvider.getUriForFile(context, "club.ranleng.psnine.provider", tmpFile);
        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, upload_temp_uri);
        startActivityForResult(mIntent, KEY.PHOTO_CAMERA);
    }

    @Override
    public void onClick(View view, String url) {
        if (!selectable) {
            return;
        }
        if (photo_list.contains(url)) {
            view.setVisibility(View.INVISIBLE);
            photo_list.remove(url);
        } else {
            view.setVisibility(View.VISIBLE);
            photo_list.add(url);
        }
    }

    @Override
    public void onLongClick(final String id, final int pos) {
        new AlertDialog.Builder(context)
                .setMessage("确定要删除这个图片么?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.delPhoto(id, pos);
                    }
                })
                .setNegativeButton("取消", null).create().show();
    }

}
