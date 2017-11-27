package club.ranleng.psnine.ui.newTopic;

import android.content.Context;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import okhttp3.FormBody;

public interface newTopicContact {

    interface View extends BaseView<Presenter> {

        void setup();

        int getType();

        void finishPosted();

        Context getCtx();

    }

    interface Presenter extends BasePresenter {

        void post(FormBody body);

    }
}
