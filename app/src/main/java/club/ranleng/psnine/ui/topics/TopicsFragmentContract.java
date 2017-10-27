package club.ranleng.psnine.ui.topics;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;

public interface TopicsFragmentContract {

    interface View extends BaseView<Presenter>{

        void setupList(RecyclerView.Adapter adapter);

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
