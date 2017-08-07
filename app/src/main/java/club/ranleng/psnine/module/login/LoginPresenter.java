package club.ranleng.psnine.module.login;

import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;

import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.data.moudle.SimpleCallBack;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.utils.TextUtils;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;

    public LoginPresenter(LoginContract.View view){
        this.mLoginView = view;
        this.mLoginView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoginView.Loading(false);
    }


    @Override
    public void Login(EditText username, EditText password) {
        if(TextUtils.isEmpty(username)){
            mLoginView.UsernameEmpty();
            return;
        }else if(TextUtils.isEmpty(password)){
            mLoginView.PasswordEmpty();
            return;
        }
        mLoginView.Loading(true);
        mLoginView.UIable(false);
        ApiManager.getDefault().Login(new SimpleCallBack() {
            @Override
            public void Success() {
                mLoginView.Success();
            }

            @Override
            public void Failed() {
                mLoginView.Failure();
                mLoginView.Loading(false);
                mLoginView.UIable(true);
            }
        },TextUtils.toS(username), TextUtils.toS(password));
    }
}
