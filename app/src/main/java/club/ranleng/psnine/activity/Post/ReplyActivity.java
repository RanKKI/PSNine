package club.ranleng.psnine.activity.Post;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.ReplyPostListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Assist.PickImgActivity;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.fragments.EmojiDialogFragment;
import club.ranleng.psnine.util.AndroidUtilCode.KeyboardUtils;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.util.ReadFile;
import club.ranleng.psnine.util.TextUtils;
import club.ranleng.psnine.widget.EmojiUrlToStr;
import club.ranleng.psnine.widget.Requests.RequestPost;
import okhttp3.FormBody;

public class ReplyActivity extends BaseActivity implements EmojiDialogFragment.EmojiDialogListener, ReplyPostListener {

    @BindView(R.id.reply_activity_button) Button reply_button;
    @BindView(R.id.reply_activity_edittext) EditText editText;
    @BindView(R.id.reply_activity_emoji) ImageView emoji;
    @BindView(R.id.reply_activity_image) ImageView imageView;
    private Context context;
    private String type;
    private String a_id;
    private String comment_id;
    private Boolean edit = false;
    private String original_content;

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
                if (edit) {
                    FormBody body = new FormBody.Builder()
                            .add("type", "comment")
                            .add("id", comment_id)
                            .add("content", editText.getText().toString())
                            .build();
                    new RequestPost(ReplyActivity.this, context, "editreply", body);
                } else {
                    FormBody body = new FormBody.Builder()
                            .add("type", type)
                            .add("param", a_id)
                            .add("old", "yes")
                            .add("com", "")
                            .add("content", editText.getText().toString())
                            .build();
                    new RequestPost(ReplyActivity.this, context, "reply", body);
                }


            }
        });

        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiDialogFragment a = new EmojiDialogFragment();
                a.show(getSupportFragmentManager(), "reply_emoji");
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, PickImgActivity.class), 1);
            }
        });

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        KeyboardUtils.showSoftInput(editText);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 10) {
            for (String i : data.getExtras().getStringArrayList("result")) {
                editText.append(String.format("[img]%s[/img]\n", i));
            }
        }
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
                        ReadFile.save(a_id,TextUtils.toS(editText));
                        finish();
                    }
                }).setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(getFilesDir() + "/" + a_id);
                        if(file.exists()){
                            if(file.delete()){

                            }
                        }
                        finish();
                    }
                }).create();
        if (!TextUtils.toS(editText).equals("")) {
            alertDialog.show();
        }else{
            finish();
        }
    }

    private void read() {
        try {
            FileInputStream inputStream = this.openFileInput(String.valueOf(a_id));
            byte[] bytes = new byte[1024];

            int len = inputStream.read(bytes);
            inputStream.close();
            String content = new String(bytes, 0, len);
            editText.append(content);
            editText.setSelection(editText.length());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void getData() {
        type = getIntent().getStringExtra("type");
        a_id = getIntent().getStringExtra("id");

        editText.append(ReadFile.read(a_id));
        editText.setSelection(editText.length());

        if (getIntent().hasExtra("content")) {

            EmojiUrlToStr emojiUrlToStr = new EmojiUrlToStr();
            edit = true;
            comment_id = getIntent().getStringExtra("comment_id");
            String content = getIntent().getStringExtra("content");
            content = content.replace("\n","").replace("\r","");
            content = content.replace("&nbsp;", " ").replace("<br>", "\n");
            String pattern = "<a href=\"http://psnine.com/psnid/.*\">(@.*)</a>";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(content);

            while (m.find()) {
                content = content.replace(m.group(0), m.group(1).replace("\n","").replace("\r",""));
            }

            pattern = "<img src=\"http://photo.psnine.com/face/(.*?).gif\">";
            r = Pattern.compile(pattern);
            m = r.matcher(content);
            while (m.find()) {
                content = content.replace(m.group(0), emojiUrlToStr.convert(m.group(1)));
            }
            editText.append(content);
            original_content = content;
        }
        if (getIntent().hasExtra("username") && getIntent().getStringExtra("username") != null) {
            editText.append(String.format("@%s ", getIntent().getStringExtra("username")));
        }
    }

    @Override
    public void onSelected(String name) {
        editText.append(String.format("[%s]", name));
    }

    @Override
    public void ReplyPostFinish() {
        editText.setText("");
        setResult(RESULT_OK);
        finish();
    }
}
