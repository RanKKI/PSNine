package club.ranleng.psnine.module.element;

import android.content.Intent;

import java.io.File;
import java.util.List;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import me.drakeet.multitype.MultiTypeAdapter;

public interface ElementContract {

    interface View extends BaseView<Presenter> {

        void setAdapter(MultiTypeAdapter adapter);

        void setLoading(Boolean b);

        void openTopics(String element);

    }

    interface Presenter extends BasePresenter {

        void load();

    }
}
