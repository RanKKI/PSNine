package club.ranleng.psnine.fragment.main;

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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.ImageGalleryActivity;
import club.ranleng.psnine.adapter.Binder.ImageGalleryBinder;
import club.ranleng.psnine.model.Image;
import club.ranleng.psnine.utils.ImageUtils;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.widget.Internet;
import club.ranleng.psnine.widget.KEY;
import club.ranleng.psnine.widget.ParseWeb;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static android.app.Activity.RESULT_OK;
import static club.ranleng.psnine.widget.KEY.REQUEST_PERMISSION_READ_EXTERNAL;

public class MainImageGalleryFragment extends Fragment implements ImageGalleryBinder.OnClick {

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;

    private Uri upload_temp_uri;
    private File tmpFile;
    private Boolean from_main;
    private Context context;
    private Photo photo;
    private MultiTypeAdapter adapter;
    private Items items;
    private View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.view_recycler, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        context = inflater.getContext();
        swipeRefreshLayout.setEnabled(false);
        photo = Internet.retrofit.create(Photo.class);
        initData();
        return view;
    }

    private void initData(){
        photo.getPhoto().subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, String>>>() {
                    @Override
                    public ArrayList<Map<String, String>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ParseWeb.parsePhoto(responseBody.string());
                    }
                })
                .flatMapIterable(new Function<ArrayList<Map<String, String>>, Iterable<Map<String, String>>>() {
                    @Override
                    public Iterable<Map<String, String>> apply(@NonNull ArrayList<Map<String, String>> maps) throws Exception {
                        return maps;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        adapter = new MultiTypeAdapter();
                        adapter.register(Image.class, new ImageGalleryBinder(MainImageGalleryFragment.this, null));

                        items = new Items();
                        adapter.setItems(items);

                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
                    }

                    @Override
                    public void onNext(@NonNull Map<String, String> map) {
                        items.add(new Image(map.get("url"), map.get("id")));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.activity_image_gallery, menu);
        menu.findItem(R.id.action_finish).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_upload_local) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!(context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL);
                }else{
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, KEY.FORM_LOCAL);
                }
            }else{
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, KEY.FORM_LOCAL);
            }
        } else if (id == R.id.action_upload_camera) {
            tmpFile = new File(context.getCacheDir(), System.currentTimeMillis() + ".jpg");
            upload_temp_uri = FileProvider.getUriForFile(context, "club.ranleng.psnine.provider", tmpFile);
            Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, upload_temp_uri);
            startActivityForResult(mIntent, KEY.FORM_CARAMA);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            swipeRefreshLayout.setRefreshing(true);
            File file = null;
            if (requestCode == KEY.FORM_CARAMA) {
                file = tmpFile;
            } else if (requestCode == KEY.FORM_LOCAL) {
                file = ImageUtils.getImageFile(context,data.getData());
            }

            assert file != null;
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upimg", file.getName(), requestFile);
            photo.upload(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    initData();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    MakeToast.str("上传失败");
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }
    }

    @Override
    public void onClick(View v, String url) {
        //Do nothing
    }

    @Override
    public void onLongClick(View v, final String id, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要删除这个图片么")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        swipeRefreshLayout.setRefreshing(true);
                        FormBody body = new FormBody.Builder()
                                .add("delimg", id).build();
                        photo.del(body).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    MakeToast.str("删除成功");
                                    items.remove(pos);
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                MakeToast.str("出错了..");
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null);
        builder.create().show();
    }

    interface Photo {
        @Multipart
        @POST("my/photo")
        Call<ResponseBody> upload(@Part MultipartBody.Part file);

        @POST("my/photo")
        Call<ResponseBody> del(@Body FormBody body);

        @GET("my/photo")
        Observable<ResponseBody> getPhoto();
    }
}