package club.ranleng.psnine.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.PSNActivityPagerAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.widget.Internet;
import club.ranleng.psnine.widget.KEY;
import club.ranleng.psnine.widget.UserStatus;
import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class PSNActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
//    @BindView(R.id.tabs) TabLayout tabLayout;

    private String psnid;
    private Context context;
    private User_Info userInfo;
    private boolean upbase = true;
    private boolean upgame = true;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_psn);
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
        context = this;
        psnid = getIntent().getStringExtra("psnid");
        viewPager.setAdapter(new PSNActivityPagerAdapter(getFragmentManager(), psnid));  //設定Adapter給viewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(psnid.toUpperCase() + "-" + viewPager.getAdapter().getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void getData() {
        userInfo = Internet.retrofit.create(User_Info.class);
        setTitle(psnid.toUpperCase() + "-" + viewPager.getAdapter().getPageTitle(0));
    }

    void confirm_action(final Call<ResponseBody> call, String message) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Call_enqueue(call);
                    }
                })
                .setNegativeButton("取消", null).create().show();
    }

    void Call_enqueue(Call<ResponseBody> call) {
        assert call != null;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    MakeToast.str("成功");
                }
                try {
                    LogUtils.d(response.body().string());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.activity_psn, menu);
        if (UserStatus.isLogin()) {
            menu.findItem(R.id.activity_psn_block).setVisible(true);
            menu.findItem(R.id.activity_psn_fav).setVisible(true);
            menu.findItem(R.id.activity_psn_up).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.activity_psn_upbase) {
            Call_enqueue(userInfo.upBase(psnid));
        } else if (id == R.id.activity_psn_upgame) {
            Call_enqueue(userInfo.upGame(psnid));
        } else if (id == R.id.activity_psn_fav) {
            FormBody.Builder body = new FormBody.Builder();
            body.add("type", "psnid")
                    .add("param", psnid)
                    .add("updown", "up");
            confirm_action(userInfo.Fav(body.build()), "确定要关注" + psnid + "么?");
        } else if (id == R.id.activity_psn_up) {
            FormBody.Builder body = new FormBody.Builder();
            body.add("type", "psnid")
                    .add("param", psnid)
                    .add("updown", "up");
            confirm_action(userInfo.Up(body.build()), "确定要花4铜币感谢" + psnid + "么?");
        } else if (id == R.id.activity_psn_block) {
            FormBody.Builder body = new FormBody.Builder();
            body.add("type", "psnid")
                    .add("param", psnid);
            confirm_action(userInfo.Block(body.build()), "确定要屏蔽" + psnid + "么?");
        }

        return super.onOptionsItemSelected(item);
    }

    interface User_Info {

        @GET("psnid/{id}/upbase")
        Call<ResponseBody> upBase(@Path("id") String psnid);

        @GET("psnid/{id}/upgame")
        Call<ResponseBody> upGame(@Path("id") String psnid);

        @POST("set/fav/ajax")
        Call<ResponseBody> Fav(@Body FormBody body);

        @POST("set/updown/ajax")
        Call<ResponseBody> Up(@Body FormBody body);

        @POST("set/blocked/ajax")
        Call<ResponseBody> Block(@Body FormBody body);
    }
}
