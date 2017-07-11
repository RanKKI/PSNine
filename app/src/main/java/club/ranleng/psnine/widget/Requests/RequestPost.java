package club.ranleng.psnine.widget.Requests;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import club.ranleng.psnine.Listener.ReplyPostListener;
import club.ranleng.psnine.Listener.RequestGetListener;
import club.ranleng.psnine.util.MakeToast;
import okhttp3.FormBody;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

/**
 * Created by ran on 02/07/2017.
 */

public class RequestPost {

    private Map<String, String> url = new HashMap<String, String>(){{
        //unfav params
        //type
        //param
        //unfav
        put("unfav","http://psnine.com/my/fav");

        //fav params
        //type
        //param
        put("fav","http://psnine.com/set/fav/ajax");

        //upforpsn params
        //no params
        put("upforpsn","http://psnine.com/psnid/%s/up");

        //updown params
        //type
        //param
        //updown
        put("updown","http://psnine.com/set/updown/ajax");

        //editreply params
        //type
        //id
        //content
        put("editreply","http://psnine.com/set/edit/post");

        //del img from photos
        //params
        //delimg : photo id
        put("photo","http://psnine.com/my/photo");

        put("reply","http://psnine.com/set/comment/post");
        put("editreply","http://psnine.com/set/edit/ajax");
        put("newgene","http://psnine.com/set/gene/post");

        put("newtopic","http://psnine.com/set/topic/post");
    }};

    private static String type;
    protected static Context context;
    private static ReplyPostListener replyPostListener;

    // 其他操作
    public RequestPost(ReplyPostListener r,Context context, String type, FormBody body) {
        replyPostListener = r;
        RequestPost.type = type;
        RequestPost.context = context;
        new post().execute(url.get(type), body);
    }

    // 顶 某个人
    public RequestPost(Context context, String type, FormBody body, String psnid){
        RequestPost.type = type;
        RequestPost.context = context;
        new post().execute(String.format(url.get(type),psnid), body);
    }

    public static class post extends AsyncTask<Object, Void, Integer> {
        /**
         * @param objects [0] is URL, [1] is body
         * @return
         */
        @Override
        protected Integer doInBackground(Object... objects) {
            int code = 0;
            okhttp3.Request build = new okhttp3.Request.Builder()
                    .post((FormBody) objects[1])
                    .url((String) objects[0])
                    .build();

            try {
                okhttp3.Response response = okhttpclient.newCall(build).execute();
                code = response.code();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return code;
        }

        @Override
        protected void onPostExecute(Integer result){
            if(type.contentEquals("editreply")){
                if (result == 200) {
                    MakeToast.str("修改成功");
                    replyPostListener.ReplyPostFinish();
                } else if (result == 404) {
                    MakeToast.str("请认真回复");
                }
            }else if(type.contentEquals("reply")){
                if (result == 200) {
                    MakeToast.str("回复成功");
                } else if (result == 404) {
                    MakeToast.str("请认真回复");
                }
                if(replyPostListener != null){
                    replyPostListener.ReplyPostFinish();
                }
            }else if(type.contentEquals("newgene") || type.contentEquals("newtopic")){
                if (result == 200) {
                    MakeToast.str("发帖成功");
                    if(replyPostListener != null){
                        replyPostListener.ReplyPostFinish();
                    }
                } else if (result == 404) {
                    MakeToast.str("发帖失败");
                }
            }else{
                if(replyPostListener != null){
                    replyPostListener.ReplyPostFinish();
                }
            }
        }
    }
}
