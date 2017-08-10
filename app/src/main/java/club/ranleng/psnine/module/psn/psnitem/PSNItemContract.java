package club.ranleng.psnine.module.psn.psnitem;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import club.ranleng.psnine.bean.Topic;
import me.drakeet.multitype.MultiTypeAdapter;

public interface PSNItemContract {

    interface View extends BaseView<Presenter> {

        void setAdapter(MultiTypeAdapter adapter);

        void openTopic(Topic topic);

        void Loading(Boolean b);

        String getPSNID();

        int getType();

    }

    interface Presenter extends BasePresenter {

        void load();

    }

}
