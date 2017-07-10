package club.ranleng.psnine.activity.Post;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.Listener.ReplyPostListener;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Assist.PickImgActivity;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.util.TextUtils;
import club.ranleng.psnine.widget.Requests.RequestPost;
import club.ranleng.psnine.widget.UserStatus;
import okhttp3.FormBody;

public class NewGeneActivity extends BaseActivity implements View.OnClickListener, ReplyPostListener {

    @BindView(R.id.new_gene_waning) TextView waning;
    @BindView(R.id.new_gene_selected_img) TextView selected_img;
    //Data
    @BindView(R.id.new_gene_main_edittext) EditText main_edit;
    @BindView(R.id.new_gene_ele) EditText ele;
    @BindView(R.id.new_gene_video_url) EditText video_url;
    @BindView(R.id.new_gene_music_id) EditText music_id;
    @BindView(R.id.new_gene_url) EditText url;

    @BindView(R.id.new_gene_select_img) Button select_img;
    @BindView(R.id.new_gene_select_music_type) Button music_type;
    @BindView(R.id.new_gene_submit) Button submit;

    private Context context;
    private ArrayList<String> photo_list = new ArrayList<>();
    private Map<String, Object> body = new HashMap<>();
    private String[] music_type_list = {"单曲", "专辑", "电台", "歌单"};
    private String[] music_type_list_key = {"mu", "al", "dj", "pl"};

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_new_gene);
        if(!UserStatus.isLogin()){
            MakeToast.plzlogin();
            finish();
        }
    }

    @Override
    public void findViews() {
        context = this;
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        select_img.setOnClickListener(this);
        music_type.setOnClickListener(this);
        submit.setOnClickListener(this);
        String text = "提问题请发到「<font color='blue' >问答</font>」板块，否则将被<font color='red' >关闭处理</font>";
        waning.setText(Html.fromHtml(text));
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    private void putAllData(){
        putData("content",TextUtils.toS(main_edit));
        putData("element",TextUtils.toS(ele));
        putData("video", TextUtils.toS(video_url));
        putData("muid", TextUtils.toS(music_id));
        putData("url", TextUtils.toS(url));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.new_gene_select_img) {
            Intent intent = new Intent(context, PickImgActivity.class);
            intent.putExtra("list", photo_list);
            startActivityForResult(intent, 1);
        } else if (id == R.id.new_gene_select_music_type) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(music_type_list, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    music_type.setText(music_type_list[which]);
                    putData("muparam",music_type_list_key[which]);
                }
            });
            builder.create().show();
        } else if (id == R.id.new_gene_submit) {
            putAllData();
            FormBody.Builder b = new FormBody.Builder();
            for (Map.Entry entry : body.entrySet()) {
                b.add((String) entry.getKey(),(String) entry.getValue());
            }
            String p = "";
            for (String s : photo_list) {
                p += s + ",";
            }
            if(!p.contentEquals("")){
                p = p.substring(0, p.length() - 1);
            }
            b.add("photo",p);
            b.add("addgene","");


            new RequestPost(this,context,"newgene",b.build());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 10){
            photo_list = data.getExtras().getStringArrayList("result");
            String selected = photo_list.size() + " 张";
            selected_img.setText(selected);
        }
    }

    private void putData(String key, Object data){
        if(body.containsKey(key)){
            body.remove(key);
        }

        body.put(key,data);
    }

    @Override
    public void ReplyPostFinish() {
        finish();
    }
}
