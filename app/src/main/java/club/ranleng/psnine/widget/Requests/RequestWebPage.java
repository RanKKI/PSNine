package club.ranleng.psnine.widget.Requests;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import club.ranleng.psnine.Listener.RequestWebPageListener;
import club.ranleng.psnine.activity.Assist.SettingActivity;
import club.ranleng.psnine.util.AndroidUtilCode.LogUtils;
import club.ranleng.psnine.widget.ParseWebpage;
import club.ranleng.psnine.widget.UserStatus;

import static club.ranleng.psnine.widget.Requests.RequestClient.okhttpclient;

public class RequestWebPage {

    private RequestWebPageListener listener;
    private HashMap url_list = new HashMap<String,String>(){{
        put("gene","http://psnine.com/gene");
        put("topic","http://psnine.com/topic");
        put("plus","http://psnine.com/node/plus");
        put("news","http://psnine.com/news");
        put("openbox","http://psnine.com/node/openbox");
        put("guide","http://psnine.com/node/guide");

        put("notice","http://psnine.com/my/notice");
        put("photo","http://psnine.com/my/photo");
    }};

    private ArrayList<String> normaltype = new ArrayList<String>(){{
        add("topic");
        add("plus");
        add("openbox");
        add("guide");
    }};

    public RequestWebPage(String type,RequestWebPageListener listener){
        this.listener = listener;
        String url = (String) url_list.get(type);
        url = url + "?ob="+ SettingActivity.PREF_OB;
        new Info().execute(url, type);
    }

    public RequestWebPage(RequestWebPageListener listener,String type,String id){
        this.listener = listener;
        String url;
        if (type.contentEquals("gene")) {
            url = "http://psnine.com/gene/" + id ;
        }else {
            url = "http://psnine.com/topic/" + id;
        }
        new Info().execute(url,"article",type);
    }

    public RequestWebPage(RequestWebPageListener listener,String type,Boolean search, String key){
        this.listener = listener;
        String url = (String) url_list.get(type);
        url = url + "?ob="+ SettingActivity.PREF_OB + "&title=" + key;
        LogUtils.d(url);
        new Info().execute(url, type);
    }

    public RequestWebPage(RequestWebPageListener listener,String type,String id,String username){
        this.listener = listener;
        String url = "";
        if(type.contentEquals("personal")){
            url = String.format("http://psnine.com/psnid/%s",id);
            if(username.contentEquals("psngame")){
                url += "/";
            }else if(username.contentEquals("msg")){
                url += "/comment";
            }else if(username.contentEquals("topic")){
                url += "/topic";
            }else if(username.contentEquals("gene")){
                url += "/gene";
            }
            new Info().execute(url,"personal",username);
        }else if(type.contentEquals("psngame")){
            url = "http://psnine.com/psngame/" + id ;
            if(username != null){
                url = url + "?psnid=" + username;
            }
            new Info().execute(url,type);

        }else if(type.contentEquals("psngametrophytips")){
            url = "http://psnine.com/trophy/" + id ;
            new Info().execute(url,type);
        }


    }

    private class Info extends AsyncTask<String, Void, ArrayList<Map<String, Object>>> {

        @Override
        protected void onPreExecute() {
            listener.onPrepare();
        }

        @Override
        protected ArrayList<Map<String, Object>> doInBackground(String... params) {

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
            switch (params[1]){
                case "gene":
                    return ParseWebpage.parseGene(result);
                case "notice":
                    return ParseWebpage.parseNotice(result);
                case "article":
                    ArrayList<Map<String, Object>> listItems = new ArrayList<>();
                    if(params[2].contentEquals("gene")){
                        listItems.add(ParseWebpage.parseGeneArticleBody(result));
                    }else if(normaltype.contains(params[2])){
                        listItems.add(ParseWebpage.parseTopicArticleBody(result));
                    }
                    listItems.add(ParseWebpage.parseReplies(result));
                    listItems.add(ParseWebpage.parseGameList(result));
                    return listItems;
                case "photo":
                    return ParseWebpage.parsePhoto(result);
                case "personal":
                    if(params[2].contentEquals("psngame")){
                        return ParseWebpage.parsePersonal(result);
                    }else if(params[2].contentEquals("gene")){
                        return ParseWebpage.parseGene(result);
                    }else if(params[2].contentEquals("topic")){
                        return ParseWebpage.parseNormal(result);
                    }else if(params[2].contentEquals("msg")){
                        return ParseWebpage.parseBReplies(result);
                    }
                case "psngame":
                    return ParseWebpage.parsePSNGame(result);
                case "psngametrophytips":
                    return ParseWebpage.parsePSNGameTrophy(result);
            }
            return ParseWebpage.parseNormal(result);
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String, Object>> result) {
            super.onPostExecute(result);
            if(result != null){
                listener.onSuccess(result);
            }else{
                listener.on404();
            }
        }
    }

}
