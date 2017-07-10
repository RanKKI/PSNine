package club.ranleng.psnine.activity.Assist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Main.ArticleActivity;
import club.ranleng.psnine.activity.Main.PersonInfoActivity;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.UserStatus;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intents = getIntent();
        Intent intent = null;
        Uri uri = intents.getData();
        if (uri != null) {
            String path = uri.getPath();
            if(path.contains("/psnid/")){
                intent = new Intent(this, PersonInfoActivity.class);
                intent.putExtra("psnid", path.replace("/psnid/",""));
            }else if(path.contains("/gene/")){
                intent = new Intent(this,ArticleActivity.class);
                intent.putExtra("id",path.replace("/gene/",""));
                intent.putExtra("type","gene");
            } else if(path.contains("/topic/")){
                intent = new Intent(this,ArticleActivity.class);
                intent.putExtra("id",path.replace("/topic/",""));
                intent.putExtra("type","topic");
            } else if(path.contains("/t/")){
                intent = new Intent(this,ArticleActivity.class);
                intent.putExtra("id",path.replace("/t/",""));
                intent.putExtra("type","topic");
            } else {
                MakeToast.str("暂时还不支持");
                finish();
            }

            if(intent != null){
                startActivity(intent);
                finish();
            }
        }
    }
}
