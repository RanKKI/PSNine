package club.ranleng.psnine.activity;

import android.support.v4.widget.SwipeRefreshLayout;

import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import club.ranleng.psnine.Listener.LoginListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.widget.Requests.RequestLogin;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginListener{

    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.LoginUsername) EditText username;
    @BindView(R.id.LoginPassword) EditText password;

    private Unbinder unbinder;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("登陆");
    }

    @Override
    public void findViews() {
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    protected void onStop(){
        super.onStop();
        unbinder.unbind();
    }

    @OnClick(R.id.LoginButton)
    public void login(){
        new RequestLogin(username,password,this);
    }

    @Override
    public void onPrepare() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void isEmpty(EditText editText) {
        editText.setError(getResources().getString(R.string.error_field_required));
    }

    @Override
    public void OnSuccess() {
        finish();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void OnFailed() {
        Toast.makeText(this,"登陆失败",Toast.LENGTH_LONG).show();
    }
}

