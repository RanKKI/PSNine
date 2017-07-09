package club.ranleng.psnine.widget.Requests;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

/**
 * Created by ran on 09/07/2017.
 */

public class Reply {

    public Reply(){

    }


    private class post extends AsyncTask<Object, Void, Integer> {

        @Override
        protected Integer doInBackground(Object... objects) {
            int code = 0;
            okhttp3.Request build = new okhttp3.Request.Builder()
                    .post((FormBody) objects[1])
                    .url("http://psnine.com/set/comment/post")
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

        }
    }

}
