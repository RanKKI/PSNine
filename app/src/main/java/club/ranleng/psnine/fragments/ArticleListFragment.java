package club.ranleng.psnine.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.activity.Main.ArticleActivity;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.Common.ArticleListAdapter;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.Common.ArticleList;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class ArticleListFragment extends BaseFragment
        implements RequestWebPageListener,SwipeRefreshLayout.OnRefreshListener,ArticleListAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private String type;
    private FinishLoadListener finishLoadListener;

    public interface FinishLoadListener {
        void onFinish();
    }


    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.view_recyclerview, null);
        context = inflater.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }


    @Override
    public void initData() {
        type = getArguments().getString("type");
        Boolean search = getArguments().getBoolean("search");
        if(search){
            new RequestWebPage(this,type,search,getArguments().getString("key"));
        }else{
            new RequestWebPage(type,this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishLoadListener = (FinishLoadListener) context;
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onPrepare() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void on404() {

    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        MultiTypeAdapter adapter = new MultiTypeAdapter();
        Items items = new Items();

        adapter.register(ArticleList.class, new ArticleListAdapter(this));

        for(Map<String, Object> map : result){
            items.add(new ArticleList(map));
        }
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
        finishLoadListener.onFinish();

    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(context,ArticleActivity.class);
        intent.putExtra("id",view.getTag().toString());
        intent.putExtra("type",type);
        startActivity(intent);
    }
}
