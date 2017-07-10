package club.ranleng.psnine.widget.Requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.util.MakeToast;
import club.ranleng.psnine.widget.ParseWebpage;
import club.ranleng.psnine.widget.UserStatus;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

/**
 * Created by ran on 10/07/2017.
 */

public class checkUpdate {
    private SharedPreferences sharedPreferences;
    private String version = "v0.0.0";

    public checkUpdate(Context context){
        sharedPreferences = context.getSharedPreferences("update",Context.MODE_PRIVATE);
        String lasttime = sharedPreferences.getString("lasttime",null);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(lasttime == null){
            sharedPreferences.edit().putString("lasttime",String.valueOf(System.currentTimeMillis())).apply();
            LogUtils.d("第一次使用");
            new Info().execute();
        }else{
            if(System.currentTimeMillis() - Long.valueOf(lasttime) > 3 * 60 * 60){
                new Info().execute();
                sharedPreferences.edit().putString("lasttime",String.valueOf(System.currentTimeMillis())).apply();
            }
        }
    }


    private class Info extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            okhttp3.Request build = new okhttp3.Request.Builder()
                    .url("https://raw.githubusercontent.com/RanKKI/PSNine/master/version").build();
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
            return result.equals(version);

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(!result){
                MakeToast.str("有新版本了");
            }else{
                LogUtils.d("还是 " + version);
            }
        }
    }
}
