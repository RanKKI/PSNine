package club.ranleng.psnine.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import club.ranleng.psnine.Listener.ReplyPostListener;
import club.ranleng.psnine.activity.Main.PersonInfoActivity;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.Requests.RequestPost;
import okhttp3.FormBody;

/**
 * Created by ran on 09/07/2017.
 */

public class RepliesDialog{

    public RepliesDialog(final Context context,Boolean editable, final String comment_id, final String username){
        final String[] list;
        if(editable){
            list = new String[]{"回复", "修改", "顶", "查看用户"};
        }else{
            list = new String[]{"回复", "顶", "查看用户"};
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (list[which]) {
                    case "回复":
                        if(!UserStatus.isLogin()){
                            MakeToast.plzlogin();
                            break;
                        }else{
                            //do sth
                        }
                        break;
                    case "修改":
                        break;
                    case "查看用户":
                        Intent intent = new Intent(context,  PersonInfoActivity.class);
                        intent.putExtra("psnid",username);
                        context.startActivity(intent);
                        break;
                    case "顶":
                        if(!UserStatus.isLogin()){
                            MakeToast.plzlogin();
                            break;
                        }else{
                            builder.setMessage("要付出4铜币来顶一下吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            FormBody body = new FormBody.Builder()
                                                    .add("type","comment")
                                                    .add("param",comment_id)
                                                    .add("updown","up")
                                                    .build();
                                            new RequestPost(null,context,"updown",body);
                                        }
                                    });
                            builder.create().show();
                        }
                        break;
                }
            }
        });
        builder.create().show();
    }

}
