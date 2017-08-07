package club.ranleng.psnine.module.element;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.module.topics.TopicsActivity;
import me.drakeet.multitype.MultiTypeAdapter;

public class ElementFragment extends Fragment implements ElementContract.View {


    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private Context context;
    private ElementContract.Presenter mPresenter;

    public static ElementFragment newInstance() {
        return new ElementFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ElementPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.load();
            }
        });
        mPresenter.start();
        return view;
    }


    @Override
    public void setPresenter(ElementContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setAdapter(MultiTypeAdapter adapter) {
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void setLoading(Boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public void openTopics(String element) {
        Intent intent = new Intent(context, TopicsActivity.class);
        intent.putExtra("type", KEY.GENE);
        intent.putExtra("ele",element);
        startActivity(intent);
    }

}
