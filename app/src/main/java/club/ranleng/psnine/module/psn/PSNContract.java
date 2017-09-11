package club.ranleng.psnine.module.psn;

import android.app.Fragment;

import java.util.Map;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;

public interface PSNContract {

    interface View extends BaseView<Presenter> {

        void Snack_Success();

        void Snack_Fail();

        void confirm(int type, String message);

        void setIcon(Map<String, String> map);

        String getPSNID();

    }

    interface Presenter extends BasePresenter {

        void load();

        void call(int type);

        Boolean MenuSelect(int id);

    }

}
