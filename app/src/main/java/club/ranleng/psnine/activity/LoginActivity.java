package club.ranleng.psnine.activity;

import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.event.LoadEvent;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.utils.TextUtils;
import club.ranleng.psnine.widget.Internet;
import club.ranleng.psnine.widget.UserStatus;
import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.avi) AVLoadingIndicatorView avi;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.LoginUsername) EditText username;
    @BindView(R.id.LoginPassword) EditText pass;
    @BindView(R.id.LoginButton) Button login_button;

    private User user;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        stopAnim();
    }

    @Override
    public void getData() {
        user = Internet.retrofit.create(User.class);
    }

    @OnClick(R.id.LoginButton)
    public void LoginBtn() {
        LogUtils.d("do login btu click");
        startAnim();
        switchUI();
        FormBody body = new FormBody.Builder()
                .add("psnid", TextUtils.toS(username))
                .add("pass", TextUtils.toS(pass))
                .add("signin", "")
                .build();

        user.Login(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    stopAnim();
                    if (UserStatus.Check(response.body().string())) {
                        EventBus.getDefault().post(new LoadEvent(true));
                        finish();
                    } else {
                        MakeToast.str("登陆失败");
                        switchUI();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void switchUI(){
        login_button.setClickable(!login_button.isClickable());
        username.setEnabled(!username.isEnabled());
        pass.setEnabled(!pass.isEnabled());
    }

    void startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }

    interface User {
        @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.93 Safari/537.36")
        @POST("sign/in")
        Call<ResponseBody> Login(@Body FormBody body);

    }

}
