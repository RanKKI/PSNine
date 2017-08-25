package club.ranleng.psnine.module.login;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;

public class LoginFragment extends Fragment implements LoginContract.View {

    @BindView(R.id.avi) AVLoadingIndicatorView avi;
    @BindView(R.id.LoginUsername) EditText username;
    @BindView(R.id.LoginPassword) EditText pass;
    @BindView(R.id.LoginButton) Button login_button;

    private LoginContract.Presenter mPresenter;

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoginPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        mPresenter.start();
        return view;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void Success() {
        getActivity().finish();
    }

    @Override
    public void Failure() {
        LogUtils.d("login failed");
        ToastUtils.showShort("login failed");
    }

    @Override
    public void Loading(Boolean loading) {
        if (loading) {
            avi.show();
        } else {
            avi.hide();
        }
    }

    @Override
    public void UsernameEmpty() {
        username.setError(getString(R.string.empty));
    }

    @Override
    public void PasswordEmpty() {
        pass.setError(getString(R.string.empty));
    }

    @Override
    public void UIable(Boolean b) {
        login_button.setClickable(b);
        username.setEnabled(b);
        pass.setEnabled(b);
    }

    @OnClick(R.id.LoginButton)
    public void LoginBtn() {
        mPresenter.Login(username, pass);
    }
}
