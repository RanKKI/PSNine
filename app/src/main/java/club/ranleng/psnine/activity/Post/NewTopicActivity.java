package club.ranleng.psnine.activity.post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

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

public class newTopicActivity extends BaseActivity implements DialogInterface.OnClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipelayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.new_topic_content) EditText content;
    @BindView(R.id.new_topic_title) EditText title;

    @BindView(R.id.new_topic_waning_top) TextView waning_top;

    @BindView(R.id.new_topic_mode) Button select_mode;
    @BindView(R.id.new_topic_submit) Button submit;
    @BindView(R.id.new_topic_magic) Button magic;


    private Context context;
    /**
     * 0 发布文章（2分钟内会在首页展示）
     * 1 保存草稿（仅自己可见）(默认)
     */
    private int mode = 1;
    private String[] mode_name = {"发布文章", "保存草稿"};
    private String[] magic_dialog_value = {"[b]", "", "", "[url]", "[flash]", "[quote]", "", "[mark]", "[s]", "[title]"};
    private String[] magic_dialog = {"加粗", "彩色字体", "居中", "链接", "Flash", "引用", "图片", "刮刮卡", "删除线", "分页"};
    private String[] magic_font_color = {"red", "orange", "green", "rown", "blue", "deeppink"};
    private String[] magic_font_color_zh = {"红", "橙", "绿", "棕", "蓝", "粉"};
    private String[] magic_photo = {"图库", "URL"};
    private AlertDialog.Builder builder;
    private Boolean edit = false;
    /**
     * 0 mode
     * 1 magic
     * 2 Choose color
     * 3 Choose photo
     */
    private int type;
    private Topic topic;
    private int edit_id;


    @Override
    public void setContentView() {
        if (!UserStatus.isLogin()) {
            MakeToast.plzlogin();
            finish();
        }
        setContentView(R.layout.activity_new_topic);
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
        setTitle("发帖");
        swipeRefreshLayout.setEnabled(false);
        builder = new AlertDialog.Builder(this);
        String waning_a = "提问题请发到「<font color='blue'>问答</font>」板块，闲聊请发到「<font color='blue'>机因</font>」，否则将被关闭+<font color='red'>扣分处理</font>";
        waning_top.setText(Html.fromHtml(waning_a));
        context = this;
    }

    @Override
    public void getData() {
        topic = Internet.retrofit.create(Topic.class);
        edit = getIntent().getBooleanExtra("edit", false);
        edit_id = getIntent().getIntExtra("id", -1);

        if (edit) {
            swipeRefreshLayout.setRefreshing(true);
            topic.editTopic(edit_id)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<ResponseBody, Map<String, String>>() {
                        @Override
                        public Map<String, String> apply(@NonNull ResponseBody responseBody) throws Exception {
                            return ParseWeb.parseTopicEdit(responseBody.string());
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Map<String, String>>() {
                        @Override
                        public void accept(@NonNull Map<String, String> map) throws Exception {
                            mode = Integer.valueOf(map.get("open"));
                            title.setText(map.get("title"));
                            content.setText(map.get("content"));
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
        }


    }

    @OnClick(R.id.new_topic_mode)
    public void set_topic_mode(final Button button) {
        builder.setItems(mode_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mode = which;
                button.setText(mode_name[mode]);
            }
        }).create().show();
    }

    @OnClick(R.id.new_topic_magic)
    public void magic() {
        builder.setItems(magic_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content.requestFocus();
                switch (which) {
                    case 1:
                        showDialog(magic_font_color_zh, 2);
                        break;
                    case 2:
                        content.setText(content.getText() + "[align=center][/align]");
                        content.setSelection(content.length() - 8);
                        break;
                    case 6:
                        showDialog(magic_photo, 3);
                        break;
                    default:
                        CAA(magic_dialog_value[which]);

                }
            }
        }).create().show();
    }

    @OnClick(R.id.new_topic_submit)
    public void submit() {
        swipeRefreshLayout.setRefreshing(true);
        FormBody.Builder body = new FormBody.Builder();
        body.add("open", String.valueOf(mode))
                .add("title", TextUtils.toS(title))
                .add("content", TextUtils.toS(content))
                .add("node", "talk");
        if (edit) {
            body.add("topicid", String.valueOf(edit_id))
                    .add("edittopic", "");
        } else {
            body.add("addtopic", "");
        }
        topic.newTopic(body.build()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                swipeRefreshLayout.setRefreshing(false);
                finish();
                MakeToast.str("发送成功");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showDialog(String[] s, int t) {
        builder.setItems(s, this);
        type = t;
        builder.create().show();
    }

    private void CAA(String params) {
        String param = params + params.replace("[", "[/");
        content.setText(content.getText() + param);
        content.setSelection(content.length() - params.length() - 1);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (type == 2) {
            content.requestFocus();
            content.append(String.format("[color=%s][/color]", magic_font_color[which]));
            content.setSelection(content.length() - 8);
        } else if (type == 3) {
            content.requestFocus();
            if (which == 0) {
                startActivityForResult(new Intent(context, ImageGalleryActivity.class), 1);
            } else {
                CAA("[img]");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            for (String i : data.getStringArrayListExtra("result")) {
                content.setText(content.getText() + "[img]http://ww4.sinaimg.cn/large/" + i + ".jpg[/img]\n");
            }
            content.setSelection(content.length());
        }
    }


    interface Topic {
        @POST("set/topic/post")
        Call<ResponseBody> newTopic(@Body FormBody body);


        @GET("topic/{id}/edit")
        Observable<ResponseBody> editTopic(@Path("id") int topic_id);
    }
}
