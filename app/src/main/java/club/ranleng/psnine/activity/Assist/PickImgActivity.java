package club.ranleng.psnine.activity.Assist;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.RequestGetListener;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.PickImgAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestPost;
import club.ranleng.psnine.widget.Requests.RequestUpload;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import okhttp3.FormBody;

public class PickImgActivity extends BaseActivity
        implements RequestWebPageListener, PickImgAdapter.OnItemClickListener, RequestGetListener{

    @BindView(R.id.fragment_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private ArrayList<String> photo_list = new ArrayList<>();
    private PickImgAdapter pickImgAdapter;
    private Uri upload_temp_uri;
    private File tmpFile;
    private Boolean form_main;
    private String[] upload_pic_form = {"相机","本地"};
    private int KEY_CARMA = 13;
    private int KEY_IMAGE= 14;
    private int REQUEST_PERMISSION_READ_EXTERNAL = 21;
    private Context context;

    @Override
    public void setContentView() {
        setContentView(R.layout.toolbar_recyclerview);

    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        context = this;
        setSupportActionBar(toolbar);
        setTitle("图库");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void getData() {
        form_main = getIntent().getBooleanExtra("form_main",false);
        if(getIntent().hasExtra("list")){
            photo_list = getIntent().getStringArrayListExtra("list");
        }
        new RequestWebPage(this,"photo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pickimg, menu);

        if(form_main){
            menu.findItem(R.id.action_finish).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_finish) {
            Intent intent = new Intent();
            intent.putExtra("result", photo_list);
            setResult(10, intent);
            finish();
        }else if(id == R.id.action_upload){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(upload_pic_form, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        tmpFile = new File(getCacheDir(),System.currentTimeMillis() + ".jpg");
                        upload_temp_uri = FileProvider.getUriForFile(context, "club.ranleng.psnine.provider",tmpFile);
                        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, upload_temp_uri);
                        startActivityForResult(mIntent,KEY_CARMA);
                    }else if(which == 1){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_READ_EXTERNAL);
                            }else{
                                pickimg();
                            }
                        }else{
                            pickimg();
                        }
                    }
                }
            });
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == KEY_CARMA){
                new RequestUpload(tmpFile,this);
            }else if(requestCode == KEY_IMAGE){
                File image = new File(getImagePath(data.getData()));
                new RequestUpload(image,this);
            }
        }

    }

    private void pickimg(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,KEY_IMAGE);
    }

    private String getImagePath(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        return imagePath;
    }

    @Override
    public void onPrepare() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onSuccess() {
        getData();
    }

    @Override
    public void on404() {
        MakeToast.notfound();
        finish();
    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        pickImgAdapter = new PickImgAdapter(result);
        pickImgAdapter.setClickListener(this);
        recyclerView.setAdapter(pickImgAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    //加载中 判断是否已经选中.
    @Override
    public void onShow(View view, View root, int position) {
        FrameLayout f= (FrameLayout) root.findViewById(R.id.photoviewmaskroot);
        String key = (String) view.getTag(R.id.tag_pick_img_second);
        if(photo_list.contains(key)){
            f.setVisibility(View.VISIBLE);
        }
    }

    //Tag 0 = id 删除用的
    //Tag 1 = rel 给p9的
    @Override
    public void onClick(View view, View root, int position) {
        if(!form_main){
            FrameLayout f= (FrameLayout) root.findViewById(R.id.photoviewmaskroot);
            String key = (String) view.getTag(R.id.tag_pick_img_second);
            if(photo_list.contains(key)){
                f.setVisibility(View.INVISIBLE);
                photo_list.remove(key);
            }else{
                f.setVisibility(View.VISIBLE);
                photo_list.add(key);
            }
        }
    }

    @Override
    public void onLongClick(final View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("确定要删除这个图片么? ")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FormBody body = new FormBody.Builder().add("delimg",String.valueOf(view.getTag(R.id.tag_pick_img_first))).build();
                        new RequestPost(null,view.getContext(),"photo",body);
                        pickImgAdapter.rmData(position);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dismiss
                    }
                });
        builder.create().show();
    }

}
