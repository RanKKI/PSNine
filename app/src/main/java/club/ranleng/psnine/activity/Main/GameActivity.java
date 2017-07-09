package club.ranleng.psnine.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Common.ArticleListAdapter;
import club.ranleng.psnine.adapter.NoticeListAdapter;
import club.ranleng.psnine.adapter.PSNGame.PSNGameTrophyAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.Common.ArticleList;
import club.ranleng.psnine.model.PSNGame.PSNGameTrophy;
import club.ranleng.psnine.util.LogUtils;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class GameActivity extends BaseActivity
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
    }

    @Override
    public void getData() {
        context = this;
        Intent intent = getIntent();
        if(!intent.hasExtra("game_id")){
            finish();
        }
        String game_id = intent.getStringExtra("game_id");
        String username = intent.getStringExtra("username");
        new RequestWebPage(this,"psngame",game_id,username);
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
    //
    //result.get(0) is game header
    //result.get(1) is game trophy
    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();

        adapter.register(PSNGameTrophy.class, new PSNGameTrophyAdapter(this));

        Map<String, Object> header = result.get(0);
        result.remove(0);
        if((Boolean) header.get("is_user")){
            Map<String, Object> user = result.get(0);
            result.remove(0);
        }

        for(Map<String, Object> i : result){
            items.add(new PSNGameTrophy());
        }
        
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(context,ArticleActivity.class);
        intent.putExtra("id",view.getTag().toString());
        intent.putExtra("type","topic");
        startActivity(intent);
    }
}
