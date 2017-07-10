package club.ranleng.psnine.activity.Main;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Article.ArticleReplyAdapter;
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

    @Override
    public void setContentView() {
        if(!UserStatus.isLogin()){
            finish();
        }
        setContentView(R.layout.view_recyclerview);
        setTitle("短消息");
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
