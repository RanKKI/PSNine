package club.ranleng.psnine.fragment.main;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.adapter.PSNActivityPagerAdapter;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.widget.Internet;
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

public class MainPSNFragment extends Fragment {


    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    private View view;
    private String psnid;
    private AppCompatActivity activity;
    private User_Info userInfo;

    public static MainPSNFragment newInstance(String psnid) {
        MainPSNFragment fragment = new MainPSNFragment();
        Bundle bundle = new Bundle();
        bundle.putString("psnid", psnid);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.view_pager_tabs, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        ButterKnife.bind(this, view);
        userInfo = Internet.retrofit.create(User_Info.class);
        activity = ((AppCompatActivity) getActivity());

        psnid = getArguments().getString("psnid");
        viewPager.setAdapter(new PSNActivityPagerAdapter(getChildFragmentManager(), psnid));  //設定Adapter給viewPager
        tabLayout.setupWithViewPager(viewPager); //绑定viewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(viewPager.getAdapter().getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setTitle(viewPager.getAdapter().getPageTitle(0));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.activity_psn, menu);
        menu.findItem(R.id.activity_psn_block).setVisible(UserStatus.isLogin());
        menu.findItem(R.id.activity_psn_fav).setVisible(UserStatus.isLogin());
        menu.findItem(R.id.activity_psn_up).setVisible(UserStatus.isLogin());

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

    void setTitle(CharSequence k) {
        activity.setTitle(psnid.toUpperCase() + "-" + k);
    }

    void confirm_action(final Call<ResponseBody> call, String message) {
        new android.support.v7.app.AlertDialog.Builder(activity)
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
