package club.ranleng.psnine.ui.topics.base;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;

public interface TopicsContract {

    interface View extends BaseView<Presenter>{

        void setupList(RecyclerView.Adapter adapter);

        void loading(boolean loading);

        Fragment getFragment();

        int getType();

        String getQuery();

        String getPSNID();

        void scrollTo(int pos);

        void setMenu(int menuID);

    }

    interface Presenter extends BasePresenter{

        void load();

        void loadMore();

        void refresh();

        boolean menuItemSelected(int id);

    }
}
