package club.ranleng.psnine.activity.Main;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Common.ArticleListAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.Common.ArticleList;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import club.ranleng.psnine.widget.UserStatus;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class NoticeActivity extends BaseActivity
        implements RequestWebPageListener,SwipeRefreshLayout.OnRefreshListener,ArticleListAdapter.OnItemClickListener{

    @BindView(R.id.fragment_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void setContentView() {
        if(!UserStatus.isLogin()){
            finish();
        }
        setContentView(R.layout.toolbar_recyclerview);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        setSupportActionBar(toolbar);
        setTitle("短消息");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void getData() {
        new RequestWebPage(this,"notice");
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
        MakeToast.notfound();
        finish();
    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();

        adapter.register(ArticleList.class, new ArticleListAdapter(this));
        adapter.register(Line.class,new LineViewBinder());
        for(Map<String, Object> map : result){
            items.add(new ArticleList(map));
            items.add(new Line());
        }
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(this,ArticleActivity.class);
        intent.putExtra("id",(String) view.getTag(R.id.tag_list_id));
        intent.putExtra("type",(String) view.getTag(R.id.tag_list_type));
        startActivity(intent);
    }
}
