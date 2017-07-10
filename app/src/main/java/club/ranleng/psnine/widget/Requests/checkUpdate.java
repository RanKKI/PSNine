package club.ranleng.psnine.widget.Requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.widget.ParseWebpage;
import club.ranleng.psnine.widget.UserStatus;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

/**
 * Created by ran on 10/07/2017.
 */

public class checkUpdate {
    private SharedPreferences sharedPreferences;
    private String version;

    public checkUpdate(Context context){
        sharedPreferences = context.getSharedPreferences("update",Context.MODE_PRIVATE);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private class Info extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            okhttp3.Request build = new okhttp3.Request.Builder().url(params[0]).build();
            String result = null;

            try {
                okhttp3.Response response = okhttpclient.newCall(build).execute();
                if(response.code() == 404){
                    return null;
                }
                result = response.body().string();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            UserStatus.Check(result);

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){

            }
        }
    }
}
