package club.ranleng.psnine.topics;

import android.app.Fragment;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;

public interface TopicsFragmentContract {

    interface View extends BaseView<Presenter>{

        void setupList(TopicsListAdapter adapter);

        void loading(boolean loading);

        Fragment getFragment();

        int getType();

    }

    interface Presenter extends BasePresenter{

        void load();

        void loadMore();

        void clean();

    }
}
