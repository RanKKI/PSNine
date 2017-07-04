package club.ranleng.psnine.activity.Post;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Assist.PickImgActivity;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.model.TextItem;
import club.ranleng.psnine.util.LogUtils;
import club.ranleng.psnine.widget.Requests.RequestUpload;

public class NewTopicActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnClickListener {

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
    private String[] mode_name ={"发布文章","保存草稿"};
    private String[] magic_dialog ={"加粗","彩色字体","居中","链接","Flash","引用","图片","刮刮卡","删除线","分页"};
    private String[] magic_font_color ={"red","orange","green","rown","blue","deeppink"};
    private String[] magic_font_color_zh ={"红","橙","绿","棕","蓝","粉"};
    private String[] magic_photo ={"图库","URL"};
    private AlertDialog.Builder builder;
    /**
     * 0 mode
     * 1 magic
     * 2 Choose color
     * 3 Choose photo
     */
    private int type;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_new_topic);
    }

    @Override
    public void findViews() {
        context = this;
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        select_mode.setOnClickListener(this);
        submit.setOnClickListener(this);
        magic.setOnClickListener(this);
        builder = new AlertDialog.Builder(context);

        String waning_a = "提问题请发到「<font color='blue'>问答</font>」板块，闲聊请发到「<font color='blue'>机因</font>」，否则将被关闭+<font color='red'>扣分处理</font>";
        waning_top.setText(Html.fromHtml(waning_a));


    }

    @Override
    public void getData() {
        if(getIntent().hasExtra("edit") && getIntent().getBooleanExtra("edit",false)){
            //TODO 修改文章.
        }
    }

    @Override
    public void showContent() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.new_topic_mode){
            showDialog(mode_name,0);
        }else if(id == R.id.new_topic_submit){

        }else if(id == R.id.new_topic_magic){
            showDialog(magic_dialog,1);
        }
    }

    private void showDialog(String[] s, int t){
        builder.setItems(s,this);
        type = t;
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 10){
            for(String i : data.getStringArrayListExtra("result")){
                content.setText(content.getText() + "[img]http://ww4.sinaimg.cn/large/"+i+".jpg[/img]\n");
            }
            content.setSelection(content.length());
        }
    }

    private void CAA(String params){
        String param = params + params.replace("[","[/");
        content.setText(content.getText() + param);
        content.setSelection(content.length() - params.length() - 1);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(type == 0){
            mode = which;
            select_mode.setText(mode_name[which]);
        }else if(type == 1){
            content.requestFocus();
            switch (which){
                case 0:
                    CAA("[b]");
                    break;
                case 1:
                    showDialog(magic_font_color_zh,2);
                    break;
                case 2:
                    content.setText(content.getText() + "[align=center][/align]");
                    content.setSelection(content.length() - 8);
                    break;
                case 3:
                    CAA("[url]");
                    break;
                case 4:
                    CAA("[flash]");
                    break;
                case 5:
                    CAA("[quote]");
                    break;
                case 6:
                    showDialog(magic_photo,3);
                    break;
                case 7:
                    CAA("[mark]");
                    break;
                case 8:
                    CAA("[s]");
                    break;
                case 9:
                    CAA("[title]");

            }
        }else if(type == 2){
            content.requestFocus();
            content.setText(content.getText() + String.format("[color=%s][/color]",magic_font_color[which]));
            content.setSelection(content.length() - 8);
        }else if(type == 3){
            content.requestFocus();
            if(which == 0){
                startActivityForResult(new Intent(context, PickImgActivity.class),1);
            }else{
                CAA("[img]");
            }
        }

    }
}
