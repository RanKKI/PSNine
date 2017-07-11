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
import club.ranleng.psnine.adapter.PSNGame.PSNGameHeaderAdapter;
import club.ranleng.psnine.adapter.PSNGame.PSNGameTrophyAdapter;
import club.ranleng.psnine.adapter.PSNGame.PSNGameUserAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.PSNGame.PSNGameHeader;
import club.ranleng.psnine.model.PSNGame.PSNGameTrophy;
import club.ranleng.psnine.model.PSNGame.PSNGameUser;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class GameTrophyActivity extends BaseActivity
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
        if(!intent.hasExtra("game_id")){
            finish();
        }
        String game_id = intent.getStringExtra("game_id");
        String username = intent.getStringExtra("username");
        if(intent.hasExtra("game_name")){
            setTitle(intent.getStringExtra("game_name"));
        }else{
            setTitle(game_id);
        }
        new RequestWebPage(this,"psngame",game_id,username);
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
        adapter.register(PSNGameHeader.class, new PSNGameHeaderAdapter());
        adapter.register(PSNGameUser.class, new PSNGameUserAdapter());
        adapter.register(Line.class,new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());

        Map<String, Object> header = result.get(0);
        items.add(new PSNGameHeader(header));
        result.remove(0);
        items.add(new Line());
        if((Boolean) header.get("is_user")){
            items.add(new PSNGameUser(result.get(0)));
            items.add(new Line());
            result.remove(0);
        }

        items.add(new Category("奖杯"));
        for(Map<String, Object> i : result){
            items.add(new PSNGameTrophy(i));
            items.add(new Line());
        }
        
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(context, GameTrophyTipsActivity.class);
        intent.putExtra("trophy_id",view.getTag().toString());
        startActivity(intent);
    }
}
