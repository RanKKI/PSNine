package club.ranleng.psnine.module.topics;

import android.view.View;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.bean.Topics;
import me.drakeet.multitype.MultiTypeAdapter;

public interface TopicsContract {

    interface View extends BaseView<Presenter> {

        void showTopics(MultiTypeAdapter adapter);

        void showLoading(boolean loading);

        void setMaxPage(int maxPage);

        void setCurrent_page(int current_page);

        void openTopic(Topic topic, android.view.View icon);

        Topics getTopicsInfo();

    }

    interface Presenter extends BasePresenter{

        void loadTopics();

        void loadTopics(int page);

    }

}
