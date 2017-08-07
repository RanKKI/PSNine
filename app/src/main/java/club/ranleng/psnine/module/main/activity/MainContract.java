package club.ranleng.psnine.module.main.activity;

import android.app.Fragment;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import club.ranleng.psnine.module.topics.TopicsContract;
import me.drakeet.multitype.MultiTypeAdapter;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void setNotLogin();

        void setLogin();

        void setUsername(String username);

        void setUserIcon(String icon_src);

        void newMsg();

        void onSignin();

        void openFragment(Fragment fragment, String title);

    }

    interface Presenter extends BasePresenter {

        void onDestory();

        void openMain();

        void openPSN();

        void openNotice();

        void openEle();

        void openFav();

        void openPhoto();

        void openSetting();

        void openAbout();

        void Logout();

        Boolean isMain(Fragment fragment);
    }

}
