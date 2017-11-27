package club.ranleng.psnine.ui.topics.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.model.Notice;
import club.ranleng.psnine.model.Topics.Topics;
import club.ranleng.psnine.model.Topics.TopicsGene;
import club.ranleng.psnine.model.Topics.TopicsQA;
import club.ranleng.psnine.ui.topics.discount.TopicsDiscountPresenter;
import club.ranleng.psnine.ui.topics.normal.TopicsPresenter;
import club.ranleng.psnine.ui.topics.psngame.TopicsPSNGamePresenter;
import club.ranleng.psnine.view.SmartRecyclerView;

public class TopicsFragment extends BaseFragment implements TopicsContract.View {

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView) SmartRecyclerView recyclerView;

    private TopicsContract.Presenter presenter;
    private String query;
    private int type;
    private String psnid;
    private int menuID = 0;

    public static TopicsFragment newInstance(int type) {
        return newInstance(type, null, null);
    }

    public static TopicsFragment newInstance(int type, String query) {
        return newInstance(type, query, null);
    }


    public static TopicsFragment newInstance(int type, String query, String psnid) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("query", query);
        args.putString("psnid", psnid);
        TopicsFragment fragment = new TopicsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.swiperefrsh_recyclerview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        type = getArguments().getInt("type");
        query = getArguments().getString("query");
        psnid = getArguments().getString("psnid");
        if (type == Key.GENE) {
            new TopicsPresenter<>(this, TopicsGene.class);
        } else if (type == Key.QA) {
            new TopicsPresenter<>(this, TopicsQA.class);
        } else if (type == Key.NOTICE) {
            new TopicsPresenter<>(this, Notice.class);
        } else if (type == Key.DISCOUNT) {
            new TopicsDiscountPresenter(this);
        } else if (type == Key.PSNGAMES) {
            new TopicsPSNGamePresenter(this);
        } else {
            new TopicsPresenter<>(this, Topics.class);
        }
        presenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menuID != 0) {
            inflater.inflate(menuID, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return presenter.menuItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(TopicsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupList(RecyclerView.Adapter adapter) {
        recyclerView.setDivider();
        recyclerView.setAutoLoadListener(this);
        recyclerView.setOnLoadMore(new SmartRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                presenter.loadMore();
            }

            @Override
            public boolean isLoading() {
                return refreshLayout.isRefreshing();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
    }

    @Override
    public void loading(boolean loading) {
        refreshLayout.setRefreshing(loading);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getQuery() {
        return query == null ? "" : query;
    }

    @Override
    public String getPSNID() {
        return psnid;
    }

    @Override
    public void scrollTo(int pos) {
        recyclerView.scrollToPosition(pos);
    }

    @Override
    public void setMenu(int menuID) {
        this.menuID = menuID;
        getFragmentManager().invalidateOptionsMenu();
    }
}
