package club.ranleng.psnine.widget.Requests;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        put("fav","http://psnine.com/set/fav/post");

        //upforpsn params
        //no params
        put("upforpsn","http://psnine.com/psnid/%s/up");

        //updown params
        //type
        //param
        //updown
        put("updown","http://psnine.com/set/updown/post");

        //editreply params
        //type
        //id
        //content
        put("editreply","http://psnine.com/set/edit/post");

        //del img from photos
        //params
        //delimg : photo id
        put("photo","http://psnine.com/my/photo");
    }};

    private static String type;
    protected static Context context;

    // 其他操作
    public RequestPost(Context context, String type, FormBody body) {
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
                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                } else if (result == 404) {
                    Toast.makeText(context, "请认真回复", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
