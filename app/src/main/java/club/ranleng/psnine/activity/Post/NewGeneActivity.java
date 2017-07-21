package club.ranleng.psnine.activity.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.ImageGalleryActivity;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.utils.TextUtils;
import club.ranleng.psnine.widget.Internet;
import club.ranleng.psnine.widget.KEY;
import club.ranleng.psnine.widget.ParseWeb;
import club.ranleng.psnine.widget.UserStatus;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class newGeneActivity extends BaseActivity {

    @BindView(R.id.new_gene_waning) TextView waning;
    @BindView(R.id.new_gene_selected_img) TextView selected_img;
    //Data
    @BindView(R.id.new_gene_main_edittext) EditText main_edit;
    @BindView(R.id.new_gene_ele) EditText ele;
    @BindView(R.id.new_gene_video_url) EditText video_url;
    @BindView(R.id.new_gene_music_id) EditText music_id;
    @BindView(R.id.new_gene_url) EditText url;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipelayout) SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<String> photo_list = new ArrayList<>();

    private Boolean edit = false;
    private int edit_id;
    private Gene gene;
    private String[] music_type_list = {"单曲", "专辑", "电台", "歌单"};
    private String[] music_type_list_key = {"mu", "al", "dj", "pl"};
    private String muparam = "";

    @Override
    public void setContentView() {
        if (!UserStatus.isLogin()) {
            finish();
        }
        setContentView(R.layout.activity_new_gene);
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
        setTitle("发基因");
        String text = "提问题请发到「<font color='blue' >问答</font>」板块，否则将被<font color='red' >关闭处理</font>";
        waning.setText(Html.fromHtml(text));
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void getData() {
        edit = getIntent().getBooleanExtra("edit", false);
        edit_id = getIntent().getIntExtra("id", -1);

        gene = Internet.retrofit.create(Gene.class);

        if (edit) {
            swipeRefreshLayout.setRefreshing(true);
            gene.GetEditGene(edit_id)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<ResponseBody, Map<String, String>>() {
                        @Override
                        public Map<String, String> apply(@NonNull ResponseBody responseBody) throws Exception {
                            return ParseWeb.parseGeneEdit(responseBody.string());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(@NonNull Map<String, String> map) throws Exception {
                            main_edit.setText(map.get("content"));
                            ele.setText(map.get("element"));
                            String[] temp = map.get("photo").split(",");
                            Collections.addAll(photo_list, temp);
                            video_url.setText(map.get("video"));
                            music_id.setText(map.get("muid"));
                            url.setText(map.get("url"));
                            String selected = photo_list.size() + " 张";
                            selected_img.setText(selected);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
        }
    }

    @OnClick(R.id.new_gene_select_img)
    public void select_img() {
        Intent intent = new Intent(this, ImageGalleryActivity.class);
        intent.putExtra("list", photo_list);
        startActivityForResult(intent, KEY.REQUEST_PICKIMG);
    }

    @OnClick(R.id.new_gene_submit)
    public void submit() {

        if(TextUtils.toS(main_edit).length() <= 8){
            MakeToast.str("输入内容太少啦");
            return;
        }

        swipeRefreshLayout.setRefreshing(true);
        FormBody.Builder body = new FormBody.Builder();

        body.add("content", TextUtils.toS(main_edit))
                .add("element", TextUtils.toS(ele))
                .add("video", TextUtils.toS(video_url))
                .add("muid", TextUtils.toS(music_id))
                .add("url", TextUtils.toS(url))
                .add("muparam", muparam);
        String p = "";
        for (String i : photo_list) {
            p += i + ",";
        }

        if (!p.contentEquals("")) {
            p = p.substring(0, p.length() - 1);
        }

        body.add("photo", p);


        if (edit) {
            body.add("geneid", String.valueOf(edit_id))
                    .add("editgene", "");
        } else {
            body.add("addgene", "");
        }


        gene.newGene(body.build()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                MakeToast.str("发送成功");
                swipeRefreshLayout.setRefreshing(false);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MakeToast.str("发送失败");
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @OnClick(R.id.new_gene_select_music_type)
    public void setMusic_type(final Button button) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(music_type_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                button.setText(music_type_list[which]);
                muparam = music_type_list_key[which];
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == KEY.REQUEST_PICKIMG) {
            photo_list = data.getExtras().getStringArrayList("result");
            String selected = photo_list.size() + " 张";
            selected_img.setText(selected);
        }
    }

    interface Gene {
        @GET("gene/{id}/edit")
        Observable<ResponseBody> GetEditGene(@Path("id") int id);

        @POST("set/gene/post")
        Call<ResponseBody> newGene(@Body FormBody body);
    }

}
