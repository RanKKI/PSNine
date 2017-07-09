package club.ranleng.psnine.model.Article;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Map;


public class ArticleHeader {

    public final String title;
    public final String content;
    public final String username;
    public final String icon;
    public final String time;
    public final String replies;
    public final String original;
    public final ArrayList<String> img = new ArrayList<>();
    public final ArrayList<String> pages = new ArrayList<>();

    public ArticleHeader(@NonNull Map<String, Object> map) {
        this.title = (String) map.get("title");
        this.content = (String) map.get("content");
        this.username = (String) map.get("username");
        this.icon = (String) map.get("icon");
        this.time = (String) map.get("time");
        this.replies = (String) map.get("replies");
        this.original = (String) map.get("original");
        for (int i = 0; i < (int) map.get("img_size"); i++) {
            img.add((String) map.get("img_" + String.valueOf(i)));
        }

        for (int i = 1; i < (int) map.get("page_size"); i++) {
            pages.add((String) map.get("page_" + String.valueOf(i + 1)));
        }

    }

}