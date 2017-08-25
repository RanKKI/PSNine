package club.ranleng.psnine.module.newtopic.Gene;

import android.app.Fragment;

import java.util.Map;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import club.ranleng.psnine.module.psn.PSNFragment;
import okhttp3.FormBody;

public interface newGeneContract {

    interface View extends BaseView<Presenter> {


        void setup();

        void finish();

        FormBody getData();

    }

    interface Presenter extends BasePresenter {

        void submit();

    }

}
