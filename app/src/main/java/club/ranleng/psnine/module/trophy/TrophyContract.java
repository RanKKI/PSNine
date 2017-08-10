package club.ranleng.psnine.module.trophy;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import me.drakeet.multitype.MultiTypeAdapter;

public interface TrophyContract {

    interface View extends BaseView<Presenter> {

        void showTopic(MultiTypeAdapter adapter);

    }

    interface Presenter extends BasePresenter {

        void load();
    }
}
