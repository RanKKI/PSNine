package club.ranleng.psnine.activity.Main;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Main.ArticleActivity;
import club.ranleng.psnine.adapter.NoticeListAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.widget.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import club.ranleng.psnine.widget.UserStatus;

public class NoticeActivity extends BaseActivity
        implements RequestWebPageListener,SwipeRefreshLayout.OnRefreshListener,NoticeListAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void setContentView() {
        if(!UserStatus.isLogin()){
            finish();
        }
        setContentView(R.layout.recyclerview);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("短消息");
    }

    @Override
    public void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.fragment_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    }

    @Override
    public void setupViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void getData() {
        new RequestWebPage("notice",this);
    }

    @Override
    public void showContent() {

    }

    @Override
    public void onRefresh() {
        getData();
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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        /*
         * result格式. ArrayList<Map<String, Object>>
         * 文章ID : id
         * 文章类型 : type
         * 文章标题 : title
         * 文章时间 : time
         * 文章回复 : reply
         * 文章作者 : username
         * 文章作者头像 : user_icon
         */
        NoticeListAdapter mAdapter = new NoticeListAdapter(this, result);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view, int position) {
        String url = view.getTag().toString();
        String id;
        String type;
        if (url.contains("gene")) {
            type = "gene";
            id = url.replace("http://psnine.com/gene/", "");
        } else {
            type = "topic";
            id = url.replace("http://psnine.com/topic/", "");
        }
        Intent intent = new Intent(this,ArticleActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("type",type);
        startActivity(intent);
    }
}
