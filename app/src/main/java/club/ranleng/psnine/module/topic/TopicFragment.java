package club.ranleng.psnine.module.topic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.UserState;
import me.drakeet.multitype.MultiTypeAdapter;

public class TopicFragment extends Fragment implements TopicContract.View {

    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private TopicContract.Presenter mPresenter;
    private Topic topic;
    private Menu menu;

    public TopicFragment() {

    }

    public static TopicFragment newInstance(int type, int topic_id) {
        TopicFragment fragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("topic_id", topic_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new TopicPresenter(this);
        topic = new Topic();
        topic.setTopic_id(getArguments().getInt("topic_id"));
        topic.setType(getArguments().getInt("type"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.reFresh();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPresenter.start();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.topic, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.MenuSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(TopicContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showTopic(MultiTypeAdapter adapter) {
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading(boolean loading) {
        swipeRefreshLayout.setRefreshing(loading);
    }

    @Override
    public void setMenu(Topic topic) {
        menu.findItem(R.id.action_article_reply).setVisible(UserState.isLogin());
        menu.findItem(R.id.action_artivle_fav).setVisible(UserState.isLogin());
        menu.findItem(R.id.action_artivle_up).setVisible(topic.getType() != KEY.GENE && UserState.isLogin());
        menu.findItem(R.id.action_article_edit).setVisible(topic.getEditable() && UserState.isLogin());
        menu.findItem(R.id.action_article_original).setVisible(topic.getOriginal() != null);
    }

    @Override
    public Topic getTopic() {
        return topic;
    }
}
