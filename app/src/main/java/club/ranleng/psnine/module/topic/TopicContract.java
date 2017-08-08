package club.ranleng.psnine.module.topic;

import android.view.Menu;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.module.topics.TopicsContract;
import me.drakeet.multitype.MultiTypeAdapter;

public interface TopicContract {

    interface View extends BaseView<Presenter> {

        void showTopic(MultiTypeAdapter adapter);

        void showLoading(boolean loading);

        void setMenu(Topic topic);

        void hidePanel();

        void cleanReply();

        void setReply(String content);

        void addReply(String content);


        Topic getTopic();

    }

    interface Presenter extends BasePresenter {

        void reFresh();

        void loadTopic();

        void loadTopic(int page);

        void setMenu(Menu menu);

        void MenuSelected(int id);

        void hidePanel();

        void onContextMenu(int id, int position);

        void at(String username);

    }

}
