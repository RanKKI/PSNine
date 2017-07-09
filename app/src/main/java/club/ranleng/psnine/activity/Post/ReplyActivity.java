package club.ranleng.psnine.activity.Post;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.ReplyPostListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Assist.PickImgActivity;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.fragments.EmojiDialogFragment;
import club.ranleng.psnine.util.KeyboardUtils;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestPost;
import okhttp3.FormBody;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

public class ReplyActivity extends BaseActivity implements EmojiDialogFragment.EmojiDialogListener,ReplyPostListener{

    @BindView(R.id.reply_activity_button) Button reply_button;
    @BindView(R.id.reply_activity_edittext) EditText editText;
    @BindView(R.id.reply_activity_emoji) ImageView emoji;
    @BindView(R.id.reply_activity_image) ImageView imageView;
    private Context context;
    private String type;
    private String a_id;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_reply);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        reply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormBody body = new FormBody.Builder()
                        .add("type",type)
                        .add("param",a_id)
                        .add("old","yes")
                        .add("com","")
                        .add("content",editText.getText().toString())
                        .build();
                new RequestPost(ReplyActivity.this,context,"reply",body);

            }
        });

        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiDialogFragment a = new EmojiDialogFragment();
                a.show(getSupportFragmentManager(),"reply_emoji");
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, PickImgActivity.class),1);
            }
        });

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        KeyboardUtils.showSoftInput(editText);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 10){
            for(String i : data.getExtras().getStringArrayList("result")){
                editText.append(String.format("[img]%s[/img]\n",i));
            }
        }
    }


    @Override
    public void getData() {
        type = getIntent().getStringExtra("type");
        a_id = getIntent().getStringExtra("id");
    }

    @Override
    public void showContent() {

    }

    @Override
    public void onSelected(String name) {
        editText.append(String.format("[%s]",name));
    }

    @Override
    public void ReplyPostFinish() {
        setResult(RESULT_OK);
        finish();
    }
}
