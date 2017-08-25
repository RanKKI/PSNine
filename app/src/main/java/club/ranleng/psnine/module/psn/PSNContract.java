package club.ranleng.psnine.module.psn;

import android.app.Fragment;

import java.util.Map;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;

public interface PSNContract {

    interface View extends BaseView<Presenter> {

        void replaceFragment(Fragment fragment);

        void Sncak_Success();

        void confirm(int type, String message, PSNFragment.DialogClickListener listener);

        void setIcon(Map<String, String> map);

        String getPSNID();

    }

    interface Presenter extends BasePresenter {

        void load();

        Boolean MenuSelect(int id);


    }

}
