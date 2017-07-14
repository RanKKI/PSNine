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
import club.ranleng.psnine.activity.Assist.FragActivity;
import club.ranleng.psnine.adapter.ViewBinder.PSNGamesListBinder;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.KEY;
import club.ranleng.psnine.model.PSNGames;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import club.ranleng.psnine.widget.UserStatus;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class PSNGameListFragment extends BaseFragment
        implements RequestWebPageListener, SwipeRefreshLayout.OnRefreshListener,
        PSNGamesListBinder.OnItemClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private MultiTypeAdapter adapter;
    private Items items;

    private String key;
    private Boolean search;

    private int current_page = 1;
    private int max_page = 1;
    private int itemCount;
    private int lastPosition;
    private int lastItemCount;


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
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    itemCount = mLayoutManager.getItemCount();
                    lastPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                } else {
                    Log.e("OnLoadMoreListener", "The OnLoadMoreListener only support LinearLayoutManager");
                    return;
                }

                if (lastItemCount != itemCount && lastPosition == itemCount - 1 && current_page != max_page) {
                    current_page++;
                    initData();
                    lastItemCount = itemCount;
                }
            }
        });

        adapter = new MultiTypeAdapter();
        adapter.register(PSNGames.class, new PSNGamesListBinder(this));
        adapter.register(Line.class, new LineViewBinder());
        items = new Items();

        return view;
    }

    @Override
    public void initData() {
        if (current_page == 1) {
            search = getArguments().getBoolean("search");
            key = getArguments().getString("key");
        }
        new RequestWebPage(this, "psngames", "newest" , "all", "all", search, key, String.valueOf(current_page));
    }

    @Override
    public void onRefresh() {
        items = new Items();
        initData();
    }

    @Override
    public void onPrepare() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void on404() {
        MakeToast.str("加载失败");
    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        Map<String, Object> header = result.get(0);
        max_page = (int) header.get("max_page");
        result.remove(0);

        if (current_page == 1) {
            adapter.setItems(items);
            recyclerView.setAdapter(adapter);
        }

        for (Map<String, Object> map : result) {
            items.add(new PSNGames(map));
            items.add(new Line());
        }

        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(context, FragActivity.class);
        intent.putExtra("key", KEY.TROPHY);
        intent.putExtra("game_id",view.getTag(R.id.tag_game_id).toString());
        startActivity(intent);
    }

}
