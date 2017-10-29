package club.ranleng.psnine.ui.topic;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.model.Topic;
import club.ranleng.psnine.model.TopicGene;
import club.ranleng.psnine.utils.ParseUrl;
import club.ranleng.psnine.view.SmartRecyclerView;

public class TopicActivity extends BaseActivity implements TopicActivityContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView) SmartRecyclerView<Activity> recyclerView;

    private TopicActivityContract.Presenter presenter;
    private String url;
    private int type;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_topic);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("主题");
        url = getIntent().getStringExtra("url");
        type = ParseUrl.getType(url);
        String title = getIntent().getStringExtra("content");
        if (title != null) toolbar.setSubtitle(title);
    }

    @Override
    public void getData() {
        if (type == Key.GENE) {
            new TopicActivityPresenter<>(this, TopicGene.class);
        } else if (type == Key.QA) {
            //TODO
            ToastUtils.showShort("暂不支持查看问与答.");
            finish();
            return;
//            new TopicActivityPresenter<>(this, TopicQA.class);
        } else {
            new TopicActivityPresenter<>(this, Topic.class);
        }
        presenter.start();
    }

    @Override
    public void setPresenter(TopicActivityContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setupList(TopicListAdapter adapter) {
        recyclerView.setDivider();
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
        if (toolbar.getSubtitle() == null) {
            toolbar.setSubtitle(subtitle);
        }
    }

    @Override
    public int getType() {
        return type;
    }
}
