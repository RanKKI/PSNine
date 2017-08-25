package club.ranleng.psnine.module.psn.psnitem;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.module.topic.TopicActivity;
import me.drakeet.multitype.MultiTypeAdapter;

public class PSNItemFragment extends Fragment implements PSNItemContract.View {


    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private Context context;
    private String psnid;
    private int type;
    private View view;

    private PSNItemContract.Presenter mPresenter;

    public static PSNItemFragment newInstance(String psnid, int type) {
        PSNItemFragment fragment = new PSNItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("psnid", psnid);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PSNItemPresenter(this);
        psnid = getArguments().getString("psnid");
        type = getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null){
            return view;
        }
        view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        swipeRefreshLayout.setEnabled(false);
        mPresenter.start();
        return view;
    }

    @Override
    public void setPresenter(PSNItemContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setAdapter(MultiTypeAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void openTopic(Topic topic) {
        Intent intent = new Intent(getActivity(), TopicActivity.class);
        intent.putExtra("type", topic.getType());
        intent.putExtra("topic_id", topic.getTopic_id());
        startActivity(intent);
    }

    @Override
    public void Loading(Boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public String getPSNID() {
        return psnid;
    }

    @Override
    public int getType() {
        return type;
    }
}
