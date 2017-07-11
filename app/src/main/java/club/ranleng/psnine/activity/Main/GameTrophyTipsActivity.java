package club.ranleng.psnine.activity.Main;

import android.content.Context;
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
import club.ranleng.psnine.adapter.PSNGame.PSNGameTrophyAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.PSNGame.PSNGameTrophy;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class GameTrophyTipsActivity extends BaseActivity
        implements RequestWebPageListener,SwipeRefreshLayout.OnRefreshListener,PSNGameTrophyAdapter.OnItemClickListener{

    @BindView(R.id.fragment_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    private Context context;

    @Override
    public void setContentView() {
        setContentView(R.layout.view_recyclerview);
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    public void getData() {
        context = this;
        Intent intent = getIntent();
        String trophy_id = intent.getStringExtra("trophy_id");
        setTitle("奖杯: " + trophy_id);
        new RequestWebPage(this,"psngametrophytips",trophy_id,null);
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
    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();

        adapter.register(PSNGameTrophy.class, new PSNGameTrophyAdapter(null));
        adapter.register(Line.class,new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ArticleReply.class, new ArticleReplyAdapter());

        Map<String, Object> header = result.get(0);
        items.add(new PSNGameTrophy(header));
        result.remove(0);
        items.add(new Line());

        items.add(new Category("奖杯TIPS"));
        for(Map<String, Object> i : result){
            items.add(new ArticleReply(i));
            items.add(new Line());
        }
        
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view, int position) {

    }
}
