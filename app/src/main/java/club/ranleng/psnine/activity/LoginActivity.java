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
import retrofit2.http.GET;
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
        if(getIntent().getBooleanExtra("logout",false) && UserStatus.isLogin()){
            switchUI();
            startAnim();
            user.Logout().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    MakeToast.str("登出成功");
                    stopAnim();
                    try {
                        UserStatus.Check(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    EventBus.getDefault().post(new LoadEvent());
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @OnClick(R.id.LoginButton)
    public void LoginBtn() {
        if(TextUtils.isEmpty(username)){
            username.setError("不能为空");
            return;
        }
        if(TextUtils.isEmpty(pass)){
            pass.setError("不能为空");
            return;
        }
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

                if(response.isSuccessful()){
                    stopAnim();
                    try {
                        if (UserStatus.Check(response.body().string())) {
                            EventBus.getDefault().post(new LoadEvent());
                            MakeToast.str("成功");
                            finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    MakeToast.str("登陆失败");
                    switchUI();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                MakeToast.str("登陆失败");
                switchUI();
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

        @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.93 Safari/537.36")
        @GET("sign/out")
        Call<ResponseBody> Logout();

    }

}
