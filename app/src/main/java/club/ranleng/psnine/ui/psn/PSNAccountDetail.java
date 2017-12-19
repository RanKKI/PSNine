package club.ranleng.psnine.ui.psn;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.model.TransInfo;
import io.reactivex.functions.Consumer;

public class PSNAccountDetail extends BaseFragment {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.ZBCount) TextView ZBCount;
    @BindView(R.id.VIPLevel) TextView VIPLevel;
    @BindView(R.id.NBAmount) TextView NBAmount;


    private PSNAccountDetailTransInfoAdapter adapter;

    public static PSNAccountDetail newInstance() {
        return new PSNAccountDetail();
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        return view;
    }

    @Override
    public void initData() {
        ApiManager.getDefault().getMy(TransInfo.class)
                .subscribe(new Consumer<TransInfo>() {
                    @Override
                    public void accept(TransInfo transInfo) throws Exception {
                        adapter.add(transInfo);
                        setupInfo(transInfo);
                    }
                });
    }

    private void setupInfo(TransInfo transInfo) {
        ZBCount.setText(transInfo.getZBamount());
        VIPLevel.setText(transInfo.getVIPlevel());
        NBAmount.setText(transInfo.getNBamount());
    }

    private void setupRecyclerView() {
        adapter = new PSNAccountDetailTransInfoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }

}
