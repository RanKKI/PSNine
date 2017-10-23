package club.ranleng.psnine.topic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.view.SmartRecyclerView;

public class TopicActivity extends BaseActivity implements TopicActivityContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView) SmartRecyclerView recyclerView;

    private TopicActivityContract.Presenter presenter;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_topic);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        new TopicActivityPresenter(this);
    }

    @Override
    public void getData() {
        presenter.start();
    }

    @OnClick(R.id.fab)
    public void fabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
