package club.ranleng.psnine.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Assist.FragActivity;
import club.ranleng.psnine.adapter.ViewBinder.Article.ArticleReplyBinder;
import club.ranleng.psnine.adapter.ViewBinder.PSNGame.PSNGameHeaderBinder;
import club.ranleng.psnine.adapter.ViewBinder.PSNGame.PSNGameTrophyBinder;
import club.ranleng.psnine.adapter.ViewBinder.PSNGame.PSNGameUserBinder;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.KEY;
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

/**
 * Created by ran on 14/07/2017.
 */

public class TrophyFragment extends BaseFragment
        implements RequestWebPageListener,SwipeRefreshLayout.OnRefreshListener,PSNGameTrophyBinder.OnItemClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private MultiTypeAdapter adapter;
    private Items items;


    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.view_recyclerview, null);
        context = inflater.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new MultiTypeAdapter();
        adapter.register(PSNGameTrophy.class, new PSNGameTrophyBinder(this));
        adapter.register(PSNGameHeader.class, new PSNGameHeaderBinder());
        adapter.register(PSNGameUser.class, new PSNGameUserBinder());
        adapter.register(Line.class,new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        return view;
    }

    @Override
    public void initData() {
        items = new Items();
        String game_id = getArguments().getString("game_id");
        String username = getArguments().getString("username");
        new RequestWebPage(this,"psngame",game_id,username);
    }

    @Override
    public void onRefresh() {
        initData();
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
        Intent intent = new Intent(context, FragActivity.class);
        intent.putExtra("key", KEY.TROPHYTIPS);
        intent.putExtra("trophy_id",view.getTag().toString());
        context.startActivity(intent);
    }
}
