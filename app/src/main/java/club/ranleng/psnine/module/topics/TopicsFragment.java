package club.ranleng.psnine.module.topics;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.bean.Topics;
import club.ranleng.psnine.common.listener.RecViewLoadMoreL;
import club.ranleng.psnine.module.topic.TopicActivity;
import me.drakeet.multitype.MultiTypeAdapter;

public class TopicsFragment extends BaseFragment implements TopicsContract.View, RecViewLoadMoreL.onScrollToBottomListener {

    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private TopicsContract.Presenter mPresenter;
    private RecViewLoadMoreL recViewLoadMoreL;

    public TopicsFragment() {

    }

    public static TopicsFragment newInstance(int type, String query, String ele) {
        TopicsFragment fragment = new TopicsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("query", query);
        bundle.putString("ele", ele);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setPresenter(TopicsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        new TopicsPresenter(this);
        setHasOptionsMenu(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTopics();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recViewLoadMoreL = new RecViewLoadMoreL(this);
        recyclerView.addOnScrollListener(recViewLoadMoreL);
//        if (getActivity().getClass() == TopicsActivity.class || getArguments().getInt("type") == KEY.NOTICE) {
//            mPresenter.start();
//        }
        return view;
    }

    @Override
    public void initData() {
        mPresenter.start();
    }

    @Override
    public void showTopics(MultiTypeAdapter adapter) {
        recyclerView.setAdapter(adapter);
//        if (recyclerView.getAdapter() == null) {
//            recyclerView.setAdapter(adapter);
//        } else {
//            recyclerView.getAdapter().notifyDataSetChanged();
//        }
    }

    @Override
    public void showLoading(boolean loading) {
        swipeRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void setMaxPage(int maxPage) {
        recViewLoadMoreL.setMaxPage(maxPage);
    }

    @Override
    public void setCurrent_page(int current_page) {
        recViewLoadMoreL.setCurrent_page(current_page);
    }

    @Override
    public void openTopic(Topic topic, View icon) {
        Intent intent = new Intent(getActivity(), TopicActivity.class);
        intent.putExtra("type", topic.getType());
        intent.putExtra("topic_id", topic.getTopic_id());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), icon, getString(R.string.trans_user_icon)).toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public Topics getTopicsInfo() {
        Topics topics = new Topics();
        topics.setType(getArguments().getInt("type"));
        topics.setEle(getArguments().getString("ele"));
        topics.setQuery(getArguments().getString("query"));
        return topics;
    }

    @Override
    public void LoadMore(int page) {
        mPresenter.loadTopics(page);
    }
}
