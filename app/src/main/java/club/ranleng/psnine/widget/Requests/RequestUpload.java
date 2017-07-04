package club.ranleng.psnine.widget.Requests;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.Listener.RequestGetListener;
import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.util.EncodeUtils;
import club.ranleng.psnine.util.LogUtils;
import club.ranleng.psnine.widget.ParseWebpage;
import club.ranleng.psnine.widget.UserStatus;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

/**
 * Created by ran on 04/07/2017.
 */

public class RequestUpload {

    private RequestGetListener listener;

    public RequestUpload(File file, RequestGetListener listener){
        this.listener = listener;
        new Info().execute(file);
    }

    private class Info extends AsyncTask<File, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            listener.onPrepare();
        }

        @Override
        protected Boolean doInBackground(File... params) {
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(Headers.of("Content-Disposition","form-data; name=\"upimg\"; filename=\""+params[0].getName()+"\""),
                            RequestBody.create(MediaType.parse("image/jpg"), params[0]))
                    .build();

            okhttp3.Request build = new okhttp3.Request.Builder()
                    .url("http://psnine.com/my/photo")
                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .header("Origin","http://psnine.com")
                    .header("Referer","http://psnine.com/my/photo")
                    .header("Content-Type","multipart/form-data")
                    .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                    .post(formBody)
                    .build();

            try {
                okhttp3.Response response = okhttpclient.newCall(build).execute();
                if(response.code() == 404){
                    return false;
                }
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                listener.onSuccess();
            }
        }
    }
}
