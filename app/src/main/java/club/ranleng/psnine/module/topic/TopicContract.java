package club.ranleng.psnine.module.topic;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.module.topics.TopicsContract;
import me.drakeet.multitype.MultiTypeAdapter;

public interface TopicContract {

    interface View extends BaseView<Presenter> {

        void showTopic(MultiTypeAdapter adapter);

        void scrollTo(int pos);

        void showLoading(boolean loading);

        void setMenu(Topic topic);

        void showTopicsSelect(String[] topics);

        void hidePanel();

        Boolean getPanel();

        void showReplyLayout(Boolean b);

        Boolean getReplyLayout();

        void setReply(String content);

        void addReply(String content);

        String getReply();

        void cantEmpty();

        void tooShort();

        void openPSN(String username);

        Topic getTopic();

        void setSubtitle(String subtitle);

        void setEmojiAdapter(RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter);

    }

    interface Presenter extends BasePresenter {

        void reFresh();

        void loadTopic();

        void loadTopic(int page);

        void MenuSelected(int id);

        void onContextMenu(int id, int position);

        void at(String username);

        void reply();

        Boolean onBackPress(KeyEvent event);

    }

}
