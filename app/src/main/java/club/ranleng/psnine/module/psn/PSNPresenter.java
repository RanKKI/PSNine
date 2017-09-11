package club.ranleng.psnine.module.psn;

import java.util.HashMap;
import java.util.Map;

import club.ranleng.psnine.R;
import club.ranleng.psnine.data.moudle.SimpleCallBack;
import club.ranleng.psnine.data.moudle.SimpleReturn;
import club.ranleng.psnine.data.remote.ApiManager;

public class PSNPresenter implements PSNContract.Presenter {

    private PSNContract.View mPSNView;

    private Map<Integer, String> msg = new HashMap<Integer, String>() {{
        put(R.id.activity_psn_block, "确定要屏蔽%s么?");
        put(R.id.activity_psn_fav, "确定要关注%s么?");
        put(R.id.activity_psn_up, "确定要花4铜币感谢%s么?");

    }};

    public PSNPresenter(PSNContract.View view) {
        this.mPSNView = view;
        this.mPSNView.setPresenter(this);
    }

    @Override
    public void start() {
        load();
    }

    @Override
    public void load() {
        ApiManager.getDefault().getPSNINFO(new SimpleReturn<Map<String, String>>() {
            @Override
            public void accept(Map<String, String> map) {
                mPSNView.setIcon(map);
            }
        }, mPSNView.getPSNID());
    }

    @Override
    public Boolean MenuSelect(int id) {
//        if (id == R.id.activity_psn_block) {
//            mPSNView.confirm(id, "确定要屏蔽" + mPSNView.getPSNID() + "么?");
//            return true;
//        } else if (id == R.id.activity_psn_fav) {
//            mPSNView.confirm(id, "确定要关注" + mPSNView.getPSNID() + "么?");
//            return true;
//        } else if (id == R.id.activity_psn_up) {
//            mPSNView.confirm(id, "确定要花4铜币感谢" + mPSNView.getPSNID() + "么?");
//            return true;
//        }

        //TODO Test.
        if (id == R.id.activity_psn_upgame || id == R.id.activity_psn_upbase) {
            call(id);
            return true;
        } else if (id == R.id.activity_psn_block || id == R.id.activity_psn_fav || id == R.id.activity_psn_up) {
            mPSNView.confirm(id, String.format(msg.get(id), mPSNView.getPSNID()));
            return true;
        }
        return false;
    }

    @Override
    public void call(int id) {
        ApiManager.getDefault().psnAction(new SimpleCallBack() {
            @Override
            public void Success() {
                mPSNView.Snack_Success();
            }

            @Override
            public void Failed() {
                mPSNView.Snack_Fail();
            }

        }, id, mPSNView.getPSNID());
    }
}
