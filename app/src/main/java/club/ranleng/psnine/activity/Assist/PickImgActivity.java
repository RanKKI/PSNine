package club.ranleng.psnine.activity.Assist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.PickImgAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.util.LogUtil;
import club.ranleng.psnine.widget.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestPost;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import okhttp3.FormBody;

public class PickImgActivity extends BaseActivity implements RequestWebPageListener, PickImgAdapter.OnItemClickListener{

    @BindView(R.id.fragment_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<String> photo_list = new ArrayList<>();
    private PickImgAdapter pickImgAdapter;

    @Override
    public void setContentView() {
        setContentView(R.layout.recyclerview);
        setTitle("图库");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {

    }

    @Override
    public void getData() {
        new RequestWebPage("photo",this);
    }

    @Override
    public void showContent() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done_all_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_finish) {
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("result", photo_list);
            //设置返回数据
            setResult(RESULT_OK, intent);
            LogUtil.d(photo_list);
            //关闭Activity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepare() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void on404() {
        MakeToast.notfound(this);
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

    //Tag 0 = id 删除用的
    //Tag 1 = rel 给p9的
    @Override
    public void onClick(View view, View root, int position) {
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

    @Override
    public void onLongClick(final View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("确定要删除这个图片么? ")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FormBody body = new FormBody.Builder().add("delimg",String.valueOf(view.getTag(R.id.tag_pick_img_first))).build();
                        new RequestPost(view.getContext(),"photo",body);
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
