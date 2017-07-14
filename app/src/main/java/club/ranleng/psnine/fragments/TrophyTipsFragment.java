package club.ranleng.psnine.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.ViewBinder.Article.ArticleReplyBinder;
import club.ranleng.psnine.adapter.ViewBinder.PSNGame.PSNGameTrophyBinder;
import club.ranleng.psnine.adapter.ViewBinder.PSNGamesListBinder;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.PSNGame.PSNGameTrophy;
import club.ranleng.psnine.model.PSNGames;
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

public class TrophyTipsFragment extends BaseFragment
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
        adapter.register(PSNGameTrophy.class, new PSNGameTrophyBinder(null));
        adapter.register(Line.class,new LineViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ArticleReply.class, new ArticleReplyBinder());
        return view;
    }

    @Override
    public void initData() {
        items = new Items();
        String trophy_id = getArguments().getString("trophy_id");
        new RequestWebPage(this,"psngametrophytips",trophy_id,null);
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
        items.add(new PSNGameTrophy(header));
        result.remove(0);
        items.add(new Line());

        if(result.size() != 0){
            items.add(new Category("奖杯TIPS"));
        }else{
            items.add(new Category("没有奖杯TIPS"));
        }

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
