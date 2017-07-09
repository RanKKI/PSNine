package club.ranleng.psnine.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Main.GameTrophyActivity;
import club.ranleng.psnine.adapter.PSNGameListAdapter;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.model.GameList;
import club.ranleng.psnine.widget.Requests.RequestWebPage;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by ran on 03/07/2017.
 */

public class PSNFragment extends BaseFragment implements RequestWebPageListener, PSNGameListAdapter.OnItemClickListener {

    private Context context;
    private String type;
    private String psnid;
    @BindView(R.id.personinforecyclerview) RecyclerView recyclerView;

    private FinishLoadListener finishLoadListener;

    public interface FinishLoadListener {
        void onFinish(Map<String, Object> results);
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.adapter_person_info_viewpager, null);
        context = inflater.getContext();
        ButterKnife.bind(this,view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    @Override
    public void initData() {
        psnid = getArguments().getString("psnid");
        type = getArguments().getString("type");
        new RequestWebPage(this,psnid);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishLoadListener = (FinishLoadListener) context;
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void on404() {

    }

    @Override
    public void onSuccess(ArrayList<Map<String, Object>> result) {
        if(type.contentEquals("psngame")){
            finishLoadListener.onFinish(result.get(0));
            MultiTypeAdapter adapter = new MultiTypeAdapter();
            Items items = new Items();

            adapter.register(GameList.class, new PSNGameListAdapter(this));
            result.remove(0);
            for(Map<String, Object> map : result){
                items.add(new GameList(map));
            }
            adapter.setItems(items);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(context, GameTrophyActivity.class);
        intent.putExtra("game_id",view.getTag(R.id.tag_game_id).toString());
        intent.putExtra("game_name",view.getTag(R.id.tag_game_name).toString());
        intent.putExtra("username",psnid);
        startActivity(intent);
    }
}
