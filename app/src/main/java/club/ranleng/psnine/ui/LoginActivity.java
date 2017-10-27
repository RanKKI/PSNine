package club.ranleng.psnine.ui;

import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.data.module.Callback;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.data.remote.ApiTopic;
import club.ranleng.psnine.model.Topic;
import club.ranleng.psnine.model.Topics;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity implements Callback {

    @BindView(R.id.username) TextInputEditText username;
    @BindView(R.id.password) TextInputEditText password;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout refreshLayout;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void find_setup_Views() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        refreshLayout.setEnabled(false);
    }

    @Override
    public void getData() {
        if(UserState.isLogin()){
            finish();
        }
    }

    @OnClick(R.id.submit)
    public void login() {
        if (refreshLayout.isRefreshing()) {
            return;
        }
        String name = username.getText().toString();
        String pwd = password.getText().toString();
        if (RegexUtils.isEmail(name)) {
            username.setError(getString(R.string.error));
            return;
        }
        refreshLayout.setRefreshing(true);
        ApiManager.getDefault().Login(this, name, pwd);
    }

    @Override
    public void onSuccess() {
        new ApiTopic<Topic>().getTopics(Key.TOPIC,1,Topic.class)
                .subscribe(new Consumer<Topic>() {
                    @Override
                    public void accept(Topic topic) throws Exception {
                        refreshLayout.setRefreshing(false);
                        if (UserState.isLogin()) {
                            finish();
                        } else {
                            ToastUtils.showShort(R.string.error);
                        }
                    }
                });
    }

    @Override
    public void onFailure() {
        refreshLayout.setRefreshing(false);
    }
}
