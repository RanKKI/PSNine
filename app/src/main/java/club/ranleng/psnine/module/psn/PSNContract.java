package club.ranleng.psnine.module.psn;

import android.app.Fragment;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import okhttp3.ResponseBody;
import retrofit2.Call;

public interface PSNContract {

    interface View extends BaseView<Presenter> {

        void replaceFragment(Fragment fragment);

        void Sncak_Success();

        void confirm(int type, String message, PSNFragment.DialogClickListener listener);

        void setBackground(String url);

        String getPSNID();



    }

    interface Presenter extends BasePresenter {

        void load();

        Boolean MenuSelect(int id);


    }

}
