package club.ranleng.psnine.module.trophy;

import android.app.Fragment;
import android.content.Context;
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
import me.drakeet.multitype.MultiTypeAdapter;

public class TrophyFragment extends Fragment implements TrophyContract.View {


    @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private TrophyContract.Presenter mPresenter;
    private Context context;
    private String game_id;

    public TrophyFragment() {

    }

    public static TrophyFragment newInstance(String game_id, String username) {
        TrophyFragment fragment = new TrophyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("game_id", game_id);
        bundle.putString("username", username);
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
        setHasOptionsMenu(true);
        new TrophyPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mPresenter.start();
        return view;
    }

    @Override
    public void setPresenter(TrophyContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showTopic(MultiTypeAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }
}
