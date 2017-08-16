package club.ranleng.psnine.module.psn;

import android.app.DialogFragment;

import java.util.Map;

import club.ranleng.psnine.R;
import club.ranleng.psnine.common.DialogConfirmHelper;
import club.ranleng.psnine.data.moudle.SimpleCallBack;
import club.ranleng.psnine.data.moudle.SimpleReturn;
import club.ranleng.psnine.data.remote.ApiManager;

public class PSNPresenter implements PSNContract.Presenter, PSNFragment.DialogClickListener {

    private PSNContract.View mPSNView;

    public PSNPresenter(PSNContract.View view) {
        this.mPSNView = view;
        this.mPSNView.setPresenter(this);
    }

    @Override
    public void start() {
        mPSNView.replaceFragment(PSNTabsFragment.newInstance(mPSNView.getPSNID()));
        load();
    }

    @Override
    public void load() {
        ApiManager.getDefault().getPSNINFO(new SimpleReturn<Map<String, String>>() {
            @Override
            public void accept(Map<String, String> map) {
                mPSNView.setBackground(map.get("bg"));
            }
        },mPSNView.getPSNID());
    }

    @Override
    public Boolean MenuSelect(int id) {
        if (id == R.id.activity_psn_block) {
            mPSNView.confirm(id,"确定要屏蔽" + mPSNView.getPSNID() + "么?", this);
            return true;
        } else if (id == R.id.activity_psn_fav) {
            mPSNView.confirm(id,"确定要关注" + mPSNView.getPSNID() + "么?",this);
            return true;
        } else if (id == R.id.activity_psn_up) {
            mPSNView.confirm(id,"确定要花4铜币感谢" + mPSNView.getPSNID() + "么?",this);
            return true;
        } else if(id == R.id.activity_psn_upgame || id == R.id.activity_psn_upbase){
            call(id);
        }
        return false;
    }

    @Override
    public void onCick(int type, Boolean b) {
        if(b){
            call(type);
        }
    }

    private void call(int id){
        ApiManager.getDefault().psnAction(new SimpleCallBack() {
            @Override
            public void Success() {
                mPSNView.Sncak_Success();
            }

            @Override
            public void Failed() {

            }
        }, id, mPSNView.getPSNID());
    }
}
