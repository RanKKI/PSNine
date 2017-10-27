package club.ranleng.psnine.ui.topics;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.model.TopicsGene;
import club.ranleng.psnine.model.TopicsNormal;
import club.ranleng.psnine.model.TopicsQA;
import club.ranleng.psnine.view.SmartRecyclerView;

public class TopicsFragment extends BaseFragment implements TopicsFragmentContract.View {

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView) SmartRecyclerView recyclerView;

    private TopicsFragmentContract.Presenter presenter;
    private int type;

    public static TopicsFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        TopicsFragment fragment = new TopicsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.recycler_view_with_swipe_refreash, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        type = getArguments().getInt("type");
        if (type == Key.GENE) {
            new TopicsFragmentPresenter<>(this, TopicsGene.class);
        } else if (type == Key.QA) {
            new TopicsFragmentPresenter<>(this, TopicsQA.class);
        } else {
            new TopicsFragmentPresenter<>(this, TopicsNormal.class);
        }
        presenter.start();
    }

    @Override
    public void setPresenter(TopicsFragmentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupList(RecyclerView.Adapter adapter) {
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
        Drawable d_divider = getActivity().getDrawable(R.drawable.recyclerview_divider);
        if (d_divider != null) {
            DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(d_divider);
            recyclerView.addItemDecoration(divider);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.clean();
                presenter.load();
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
}
