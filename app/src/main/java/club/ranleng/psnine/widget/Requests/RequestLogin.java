package club.ranleng.psnine.widget.Requests;

import android.os.AsyncTask;
import android.widget.EditText;

import java.io.IOException;

import club.ranleng.psnine.Listener.LoginListener;
import club.ranleng.psnine.widget.UserStatus;
import okhttp3.FormBody;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;


public class RequestLogin {

    private LoginListener listener;

    public RequestLogin(EditText username, EditText password, LoginListener listener){
        this.listener = listener;
        String name = username.getText().toString();
        String pwd = password.getText().toString();
        if(name.isEmpty()){
            listener.isEmpty(username);
        }else if(pwd.isEmpty()){
            listener.isEmpty(password);
        }else{
            new RequestLogin.Info().execute(name,pwd);
        }
    }

    private class Info extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            listener.onPrepare();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            FormBody body = new FormBody.Builder()
                    .add("psnid", params[0])
                    .add("pass", params[1])
                    .add("signin", "")
                    .build();

            String login_url = "http://psnine.com/sign/in";
            okhttp3.Request build = new okhttp3.Request.Builder()
                    .url(login_url)
                    .post(body)
                    .build();

            try {
                okhttp3.Response response = okhttpclient.newCall(build).execute();
                String results = response.body().string();
                if (results.contains("个人主页")) {
                    UserStatus.isLogin(true);
                    return true;
                }
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                listener.OnSuccess();
            }else{
                listener.OnFailed();
            }
        }
    }
}
