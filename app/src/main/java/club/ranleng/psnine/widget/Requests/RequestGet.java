package club.ranleng.psnine.widget.Requests;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import club.ranleng.psnine.Listener.RequestGetListener;
import club.ranleng.psnine.util.LogUtil;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

/**
 * Created by ran on 03/07/2017.
 */

public class RequestGet {

    private Map<String, String> url = new HashMap<String, String>(){{
        put("upgame","http://psnine.com/psnid/%s/upgame");
        put("upbase","http://psnine.com/psnid/%s/upbase");
    }};

    public Void execute(String type, String psnid){
//        LogUtil.d(String.format(url.get(type),psnid));
        new Info().execute(String.format(url.get(type),psnid));
        return null;
    }

    private class Info extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            okhttp3.Request build = new okhttp3.Request.Builder().url(params[0]).build();
            String result = null;

            try {
                okhttp3.Response response = okhttpclient.newCall(build).execute();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
