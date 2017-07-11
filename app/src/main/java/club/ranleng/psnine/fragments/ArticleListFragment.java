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
import club.ranleng.psnine.activity.Main.ArticleActivity;
import club.ranleng.psnine.adapter.Common.ArticleListAdapter;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.Common.ArticleList;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import me.drakeet.support.about.Line;
import me.drakeet.support.about.LineViewBinder;

public class ArticleListFragment extends BaseFragment
        implements RequestWebPageListener, SwipeRefreshLayout.OnRefreshListener,
        ArticleListAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private FinishLoadListener finishLoadListener;
    private MultiTypeAdapter adapter;
    private Items items;

    private String key;
    private String type;
    private Boolean search;

    private int current_page = 1;
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

                if (lastItemCount != itemCount && lastPosition == itemCount - 1) {
                    current_page++;
                    initData();
                    lastItemCount = itemCount;
                }
            }
        });

        adapter = new MultiTypeAdapter();
        adapter.register(ArticleList.class, new ArticleListAdapter(this));
        adapter.register(Line.class, new LineViewBinder());
        items = new Items();

        return view;
    }

    @Override
    public void initData() {
        if (current_page == 1) {
            type = getArguments().getString("type");
            search = getArguments().getBoolean("search");
            key = getArguments().getString("key");
        }
        new RequestWebPage(this, type, search, key, String.valueOf(current_page));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishLoadListener = (FinishLoadListener) context;
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
        MakeToast.str("加载失败");
    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        if (current_page == 1) {
            adapter.setItems(items);
            recyclerView.setAdapter(adapter);
        }

        for (Map<String, Object> map : result) {
            items.add(new ArticleList(map));
            items.add(new Line());
        }

        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        finishLoadListener.onFinish();
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra("id", (String) view.getTag(R.id.tag_list_id));
        intent.putExtra("type", (String) view.getTag(R.id.tag_list_type));
        startActivity(intent);
    }

    public interface FinishLoadListener {
        void onFinish();
    }
}
