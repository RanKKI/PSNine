package club.ranleng.psnine.ui.topic;

import android.content.Context;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;

public interface TopicActivityContract {

    interface View extends BaseView<Presenter> {

        void setupList(TopicListAdapter adapter);

        void loading(boolean loading);

        Context getContext();

        String getURL();

        int getType();

        void setSubtitle(String subtitle);

        void setReplyLayout(boolean opening);

        void setReplyContent(String content, boolean clean);
    }

    interface Presenter extends BasePresenter {

        void loadTopic();

        void loadComment();

        void loadMoreComment();

        void refresh();

        void submitComment(String content);

    }
}
