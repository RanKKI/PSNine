package club.ranleng.psnine.topic;

import android.content.Context;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;

public interface TopicActivityContract {

    interface View extends BaseView<Presenter> {

        void setupList(TopicListAdapter adapter);

        Context getContext();
    }

    interface Presenter extends BasePresenter {

        void loadTopic();

        void loadComment();

    }
}
