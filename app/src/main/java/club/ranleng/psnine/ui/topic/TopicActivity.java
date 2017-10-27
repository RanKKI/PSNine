package club.ranleng.psnine.ui.topic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.view.SmartRecyclerView;

public class TopicActivity extends BaseActivity implements TopicActivityContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView) SmartRecyclerView recyclerView;

    private TopicActivityContract.Presenter presenter;
    private String url;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_topic);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("主题");
        new TopicActivityPresenter(this);
    }

    @Override
    public void getData() {
        url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("content");
        if(title != null) toolbar.setSubtitle(title);
        presenter.start();
    }

    @Override
    public void setPresenter(TopicActivityContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupList(TopicListAdapter adapter) {
        Drawable d_divider = getDrawable(R.drawable.recyclerview_divider);
        if (d_divider != null) {
            DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            divider.setDrawable(d_divider);
            recyclerView.addItemDecoration(divider);
        }
        recyclerView.setAutoLoadListener(this);
        recyclerView.setOnLoadMore(new SmartRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                presenter.loadMoreComment();
            }

            @Override
            public boolean isLoading() {
                return refreshLayout.isRefreshing();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loading(boolean loading) {
        refreshLayout.setRefreshing(loading);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public void setSubtitle(String subtitle) {
        if(toolbar.getSubtitle() == null){
            toolbar.setSubtitle(subtitle);
        }
    }
}
