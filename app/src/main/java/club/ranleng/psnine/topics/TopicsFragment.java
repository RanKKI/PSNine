package club.ranleng.psnine.topics;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.view.SmartRecyclerView;

public class TopicsFragment extends BaseFragment implements TopicsFragmentContract.View {

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView) SmartRecyclerView recyclerView;

    private TopicsFragmentContract.Presenter presenter;

    public static TopicsFragment newInstance() {
        return new TopicsFragment();
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.recycler_view_with_swipe_refreash, container, false);
        ButterKnife.bind(this, view);
        new TopicsFragmentPresenter(this);
        return view;
    }

    @Override
    public void initData() {
        presenter.start();
    }

    @Override
    public void setPresenter(TopicsFragmentContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupList(TopicsListAdapter adapter) {
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
        if(d_divider != null){
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
        return 0;
    }
}
