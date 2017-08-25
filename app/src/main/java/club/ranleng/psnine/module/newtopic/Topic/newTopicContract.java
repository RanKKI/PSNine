package club.ranleng.psnine.module.newtopic.Topic;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import okhttp3.FormBody;

public interface newTopicContract {

    interface View extends BaseView<Presenter> {

        void setup();

        void finish();

        FormBody getData();

    }

    interface Presenter extends BasePresenter {

        void submit();

    }

}
