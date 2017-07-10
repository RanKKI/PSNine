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

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Main.ArticleActivity;
import club.ranleng.psnine.activity.Main.GameTrophyActivity;
import club.ranleng.psnine.adapter.Article.ArticleReplyAdapter;
import club.ranleng.psnine.adapter.Common.ArticleListAdapter;
import club.ranleng.psnine.adapter.PSNGameListAdapter;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.Article.ArticleReply;
import club.ranleng.psnine.model.Common.ArticleList;
import club.ranleng.psnine.model.GameList;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.CategoryViewBinder;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

/**
 * Created by ran on 03/07/2017.
 */

public class PSNFragment extends BaseFragment implements RequestWebPageListener, PSNGameListAdapter.OnItemClickListener {

    private Context context;
    private String type;
    private String psnid;
    @BindView(R.id.personinforecyclerview) RecyclerView recyclerView;
    @BindView(R.id.personinfoswip) SwipeRefreshLayout swipeRefreshLayout;

    private FinishLoadListener finishLoadListener;

    public interface FinishLoadListener {
        void onFinish(Map<String, Object> results);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.adapter_person_info_viewpager, null);
        context = inflater.getContext();
        ButterKnife.bind(this,view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        psnid = getArguments().getString("psnid");
        type = getArguments().getString("type");
        new RequestWebPage(this,"personal",psnid,type);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishLoadListener = (FinishLoadListener) context;
    }

    @Override
    public void onPrepare() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void on404() {

    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(Line.class,new LineViewBinder());
        if(type.contentEquals("psngame")){
            finishLoadListener.onFinish(result.get(0));
            adapter.register(GameList.class, new PSNGameListAdapter(this));
            result.remove(0);
            for(Map<String, Object> map : result){
                items.add(new GameList(map));
            }
        }else if(type.contentEquals("topic") || type.contentEquals("gene")){
            adapter.register(ArticleList.class, new ArticleListAdapter(new ArticleListAdapter.OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(context,ArticleActivity.class);
                    intent.putExtra("id",view.getTag().toString());
                    intent.putExtra("type",type);
                    startActivity(intent);
                }
            }));
            adapter.register(Line.class,new LineViewBinder());
            for(Map<String, Object> map : result){
                items.add(new ArticleList(map));
                items.add(new Line());
            }
        }else if(type.contentEquals("msg")){
            adapter.register(ArticleReply.class, new ArticleReplyAdapter(null));
            items.add(new Line());
            for(Map<String, Object> map: result){
                ArticleReply articleReply = new ArticleReply(map);
                items.add(articleReply);
                items.add(new Line());
            }
        }
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(context, GameTrophyActivity.class);
        intent.putExtra("game_id",view.getTag(R.id.tag_game_id).toString());
        intent.putExtra("game_name",view.getTag(R.id.tag_game_name).toString());
        intent.putExtra("username",psnid);
        startActivity(intent);
    }
}
