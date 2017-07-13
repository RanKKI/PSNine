package club.ranleng.psnine.widget.Requests;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import club.ranleng.psnine.Listener.YetanotherListener;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

/**
 * Created by ran on 03/07/2017.
 */

public class RequestGet {

    private Map<String, String> url = new HashMap<String, String>(){{
        put("upgame","http://psnine.com/psnid/%s/upgame");
        put("upbase","http://psnine.com/psnid/%s/upbase");
        put("dao","http://psnine.com/set/qidao/ajax");
        put("edittopic","http://psnine.com/topic/%s/edit");
        put("editgene","http://psnine.com/gene/%s/edit");
    }};
    private YetanotherListener yetanotherListener;

    public void execute(YetanotherListener listener,String type,String id){
        this.yetanotherListener = listener;
        new Info().execute(String.format(url.get(type),id));
    }

    public void execute(String type){
        new Info().execute(url.get(type));
    }

    public void execute(String type, String psnid){
//        LogUtils.d(String.format(url.get(type),psnid));
        new Info().execute(String.format(url.get(type),psnid));
    }

    private class Info extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            if(yetanotherListener != null){
                yetanotherListener.onPrepare();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            okhttp3.Request build = new okhttp3.Request.Builder().url(params[0]).build();
            String result = null;

            try {
                okhttp3.Response response = okhttpclient.newCall(build).execute();
                result = response.body().string();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(yetanotherListener != null){
                yetanotherListener.onSuccess(result);
            }
        }
    }
}
