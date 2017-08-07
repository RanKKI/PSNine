package club.ranleng.psnine.module.login;

import android.view.Menu;
import android.widget.EditText;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import club.ranleng.psnine.bean.Topic;
import club.ranleng.psnine.module.topic.TopicContract;
import me.drakeet.multitype.MultiTypeAdapter;

public interface LoginContract {


    interface View extends BaseView<Presenter> {

        void Success();

        void Failure();

        void Loading(Boolean loading);

        void UsernameEmpty();

        void PasswordEmpty();

        void UIable(Boolean b);

    }

    interface Presenter extends BasePresenter {

        void Login(EditText username, EditText password);
    }

}
