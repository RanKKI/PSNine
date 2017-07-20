package club.ranleng.psnine.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;


import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.PSNActivityPagerAdapter;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.widget.Internet;
import club.ranleng.psnine.widget.ParseWeb;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class PSNActivity extends BaseActivity {

    @BindView(R.id.personal_bg) ImageView bg;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private String psnid;
    private Context context;
    private User_Info userInfo;
    private boolean upbase = false;
    private boolean upgame = false;

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
        viewPager.setAdapter(new PSNActivityPagerAdapter(getFragmentManager(),psnid));  //設定Adapter給viewPager
        tabLayout.setupWithViewPager(viewPager); //绑定viewPager
    }

    @Override
    public void getData() {
        userInfo = Internet.retrofit.create(User_Info.class);
        setTitle(psnid.toUpperCase());
        userInfo.getInfo(psnid)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Map<String, String>>() {
                    @Override
                    public Map<String, String> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ParseWeb.parsePeronalHeader(responseBody.string());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Map<String, String>>() {
                    @Override
                    public void accept(@NonNull Map<String, String> map) throws Exception {
                        Glide.with(context).load(map.get("bgurl")).into(bg);
                        upbase = Boolean.valueOf(map.get("upbase"));
                        upgame = Boolean.valueOf(map.get("upgaem"));
                    }
                });
    }


    @OnClick(R.id.fab)
    public void fab_Click(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] mode = {"等级同步","游戏同步"};
        builder.setItems(mode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<ResponseBody> call = null;
                if(which == 0){
                    if(!upbase){
                        MakeToast.str("暂时还不能用");
                        return;
                    }
                    call = userInfo.upBase(psnid);
                }else if(which == 1){
                    if(!upgame){
                        MakeToast.str("暂时还不能用");
                        return;
                    }
                    call = userInfo.upGame(psnid);
                }

                assert call != null;
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
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
        }).create().show();
    }


    interface User_Info {
        @GET("psnid/{id}")
        Observable<ResponseBody> getInfo(@Path("id") String psnid);

        @GET("psnid/{id}/upbase")
        Call<ResponseBody> upBase(@Path("id") String psnid);

        @GET("psnid/{id}/upgame")
        Call<ResponseBody> upGame(@Path("id") String psnid);
    }
}
