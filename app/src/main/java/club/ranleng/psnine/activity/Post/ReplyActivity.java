package club.ranleng.psnine.activity.post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.ImageGalleryActivity;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.event.EmojiEvent;
import club.ranleng.psnine.fragment.EmojiDialogFragment;
import club.ranleng.psnine.utils.LocalFile;
import club.ranleng.psnine.utils.MakeToast;
import club.ranleng.psnine.utils.TextUtils;
import club.ranleng.psnine.widget.KEY;
import club.ranleng.psnine.widget.Internet;
import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ReplyActivity extends BaseActivity {

    @BindView(R.id.reply_activity_edittext) EditText editText;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Context context;
    private int type;
    private int id;
    private String comment_id;
    private Boolean edit = false;
    private String original_content;
    private EmojiDialogFragment emojiDialogFragment;
    private Comment comment;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_reply);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
        emojiDialogFragment = new EmojiDialogFragment();
    }

    @Override
    public void setupViews() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        KeyboardUtils.showSoftInput(editText);
    }

    @Override
    public void getData() {
        EventBus.getDefault().register(this);
        comment = Internet.retrofit.create(Comment.class);

        Intent intent = getIntent();
        type = getIntent().getIntExtra("type",-1);
        id = getIntent().getIntExtra("id",-1);

        if (intent.hasExtra("edit") && intent.getBooleanExtra("edit", false)) {
            edit = true;
            comment_id = intent.getStringExtra("comment_id");
            type = KEY.TYPE_COMMENT;
            String content = intent.getStringExtra("content");

            content = content.replace("\n", "").replace("\r", "");
            content = content.replace("&nbsp;", " ").replace("<br>", "\n");
            String pattern = "<a href=\"http://psnine.com/psnid/.*\">(@.*)</a>";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(content);

            while (m.find()) {
                content = content.replace(m.group(0), m.group(1).replace("\n", "").replace("\r", ""));
            }

            pattern = "<img src=\"http://photo.psnine.com/face/(.*?).gif\">";
            r = Pattern.compile(pattern);
            m = r.matcher(content);
            while (m.find()) {
                content = content.replace(m.group(0), KEY.EMOJI_URL_STR.get(m.group(1)));
            }
            original_content = content;
            editText.append(content);
        } else if (LocalFile.isFileExists(String.valueOf(id))) {
            editText.setText(LocalFile.read(String.valueOf(id)));
        }


        if (getIntent().hasExtra("username") && getIntent().getStringExtra("username") != null) {
            editText.append(String.format("@%s ", getIntent().getStringExtra("username")));
        }
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            askForSave();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            askForSave();
        }
        return false;
    }

    private void askForSave() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage("是否保存成草稿 (下次打开时自动加载")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocalFile.save(String.valueOf(id), editText);
                        finish();
                    }
                }).setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();

        if (TextUtils.toS(editText).equals("") || TextUtils.toS(editText).equals(original_content)) {
            LocalFile.del(String.valueOf(id));
            finish();
        } else {
            alertDialog.show();
        }
    }

    @OnClick(R.id.reply_activity_emoji)
    public void emoji() {
        emojiDialogFragment.show(getFragmentManager(), "reply_emoji");
    }


    @OnClick(R.id.reply_activity_image)
    public void pickImg() {
        startActivityForResult(new Intent(this, ImageGalleryActivity.class), KEY.REQUEST_PICKIMG);
    }



    @OnClick(R.id.reply_activity_button)
    public void submit() {
        Call<ResponseBody> call;
        FormBody.Builder body = new FormBody.Builder();
        final String str;

        if (edit) {
            body.add("type", "comment")
                    .add("id", comment_id)
                    .add("content", TextUtils.toS(editText));
            call = comment.editReply(body.build());
            str = "修改成功";
        } else {
            body.add("type", KEY.INT_TYPE(type))
                    .add("param", String.valueOf(id))
                    .add("old", "yes")
                    .add("com","")
                    .add("content",TextUtils.toS(editText));
            call = comment.Reply(body.build());
            str = "回复成功";
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                MakeToast.str(str);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Subscribe
    public void onEmojiSelect(EmojiEvent emojiEvent) {
        editText.append(String.format("[%s]", emojiEvent.getEmoji()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == KEY.REQUEST_PICKIMG) {
            for (String i : data.getExtras().getStringArrayList("result")) {
                editText.append(String.format("[img]%s[/img]\n", i));
            }
        }
    }

    interface Comment {

        @POST("set/comment/post")
        Call<ResponseBody> Reply(@Body FormBody body);

        @POST("set/edit/ajax")
        Call<ResponseBody> editReply(@Body FormBody body);

    }
}
