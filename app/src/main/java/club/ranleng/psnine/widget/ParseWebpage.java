package club.ranleng.psnine.widget;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.ranleng.psnine.util.LogUtil;

public class ParseWebpage {

    public static ArrayList<Map<String, Object>> parseNormal(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        Elements elements = doc.select("ul.list").select("li");

        for (Element e : elements) {
            String icon = e.select("a.l").select("img").attr("src");
            String title = e.select("div.ml64").select("div.title").select("a").text();
            String username = e.select("div.meta").select("a").first().ownText();
            String time = e.select("div.meta").first().ownText();
            String id = e.select("div.ml64").select("div.title").select("a").attr("href").replace("http://psnine.com/topic/", "");
            String reply = e.select("a.rep.r").text();

            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("username", username);
            map.put("id", id);
            map.put("icon", icon);
            map.put("time",time);
            map.put("reply",reply+"评论");
            listItems.add(map);
        }

        return listItems;
    }

    public static ArrayList<Map<String, Object>> parseNews(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        Elements elements = doc.select("ul.newslist").select("li");

        for (Element e : elements) {
            String icon = "";
            if(!e.select("a.ava").select("img").isEmpty()){
                icon = e.select("a.ava").select("img").attr("src");
            }
            String title = e.select("div.mr64").select("div.content.pd10").select("a").text();
            String username = e.select("div.meta").select("a").first().ownText();
            String time = e.select("div.meta").first().ownText();
            String id = e.select("div.ml64").select("div.title").select("a").attr("href").replace("http://psnine.com/topic/", "");
            String reply = e.select("a.rep.r").text();

            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("username", username);
            map.put("id", id);
            map.put("icon", icon);
            map.put("time",time);
            map.put("reply",reply+"评论");
            listItems.add(map);
        }

        return listItems;
    }

    public static ArrayList<Map<String, Object>> parseGene(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        Elements elements = doc.select("ul.list.genelist").select("li");

        for (Element e : elements) {
            String icon = e.select("a.l").select("img").attr("src");
            String content = e.select("div.content.pb10").text();
            String username = e.select("div.meta").select("a").text();
            String id = "";
            Elements a = e.select("a");
            for(Element i : a){
                if(i.attr("href").contains("ttp://psnine.com/gene/")){
                    id = i.attr("href").replace("http://psnine.com/gene/","");
                }
            }
            String[] tr = e.select("div.meta").text().replace(username,"").replace(" ","").split("前");

            Map<String, Object> map = new HashMap<>();
            map.put("title", content);
            map.put("username", username);
            map.put("id", id);
            map.put("icon", icon);
            map.put("time",tr[0]+"前");
            map.put("reply",tr[1]);
            listItems.add(map);
        }

        return listItems;
    }

    public static Map<String, Object> parseTopicArticleBody(String results) {

        Map<String, Object> map = new HashMap<>();
        Document doc = Jsoup.parse(results);

        String post = doc.select("div.content.pd10").html();
        String title = doc.select("div.pd10").first().select("h1").text();
        String username = doc.select("a.title2").text();
        Elements icon_e = doc.select("div.side").select("div.box").select("p");
        String icon = "";
        if(!icon_e.isEmpty()){
            icon = doc.select("div.side").select("div.box").select("p").get(0).select("a").select("img").attr("src");
        }

        Elements page = doc.select("div.page").select("ul").select("li");
        if (!page.isEmpty()) {
            map.put("page_size", page.size());

            for (int i = 0; i < page.size(); i++) {
                String p_name = page.get(i).select("a").text();
                map.put("page_" + String.valueOf(i + 1), p_name);
            }
        } else {
            map.put("page_size", 1);
        }

        map.put("img_size", 0);
        map.put("title", title);
        map.put("content", "<html><body>" + post + "</body></html>".replace("class=\"imgbgnb\">","class=\"imgbgnb\"/>").replace("alt=\"\">","alt=\"\"/>"));
        map.put("username", username);
        map.put("icon", icon);
        map.put("original", "");
        map.put("time","");
        map.put("replies","");
        return map;
    }

    public static Map<String, Object> parseGeneArticleBody(String results) {

        Map<String, Object> map = new HashMap<>();
        Document doc = Jsoup.parse(results);

        String content = doc.select("div.content.pb10").first().html();
        String username = doc.select("div.meta").select("a.psnnode").first().ownText();
        String original = "";
        if(!doc.select("a.text-info").isEmpty()) {
            original = doc.select("a.text-info").attr("href");
        }
        String icon = doc.select("div.side").select("div.box").select("p").select("a").select("img").attr("src");
        String[] temp = doc.select("div.meta").first().ownText().replace(" ","").split("前");
        Elements img = doc.select("div.content.pd10").select("a").select("img");

        map.put("img_size", img.size());
        for (int i = 0; i < img.size(); i++) {
            map.put("img_" + String.valueOf(i), img.get(i).attr("src"));
        }
        map.put("title", "");
        map.put("content", "<html><body>" + content + "</body></html>");
        map.put("username", username);
        map.put("icon", icon);
        map.put("original", original);
        map.put("time",temp[0]+"前");
        map.put("replies",temp[1]);
        map.put("page_size", 1);
        return map;
    }

    public static Map<String, Object> parseReplies(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        results = results.replace("<img src=\"http://ww4.sinaimg.cn","<br/><img src=\"http://ww4.sinaimg.cn");

        Document doc = Jsoup.parse(results);
        Elements post = doc.select("div.post");
        for (Element c : post) {
            if (!c.select("a").hasClass("btn")) {
                String content = c.select("div.content.pb10").html();
                String username = c.select("a.psnnode").text();
                String icon = c.select("a.l").select("img").attr("src");
                String comment_id = c.select("div.content.pb10").attr("id").replace("comment-content-", "");
                Map<String, Object> map = new HashMap<>();

                if (!c.select("span.r").select("a.text-info").isEmpty()) {
                    map.put("editable", true);
                } else {
                    map.put("editable", false);
                }
                map.put("title", content);
                map.put("username", username);
                map.put("id", comment_id);
                map.put("icon", icon);
                map.put("time", "");
                listItems.add(map);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list",listItems);
        return map;
    }

    public static Boolean parseIsMoreReplies(String results) {
        Document doc = Jsoup.parse(results);
        Elements but = doc.select("a.btn.btn-gray");
        return !but.isEmpty();
    }


    public static int parseIsMaxReplies(String results) {
        Document doc = Jsoup.parse(results);
        Elements page = doc.select("div.page").first().select("ul").select("li");
        Elements at = page.get(page.size() - 2).select("a");
        return Integer.valueOf(at.text());
    }

    public static Map<String, Object> parsePeronalHeader(String results) {
        Document doc = Jsoup.parse(results);

        String icon = doc.select("img.avabig").attr("src");
        String region = doc.select("img.icon-region").attr("src");
        String auth = doc.select("img.icon-auth").attr("src");
        String plus = doc.select("img.icon-plus").attr("src");
        String trophy = doc.select("div.psntrophy").html();
        String level = doc.select("span.text-level").text();
        String rank = doc.select("a.text-rank").text();

        Elements psninfo = doc.select("div.psninfo").get(1).select("table").select("tbody").select("tr").select("td");
        String total = psninfo.get(0).ownText();
        String perfect = psninfo.get(1).ownText();
        String failed = psninfo.get(2).select("span").text();
        String percent = psninfo.get(3).ownText();
        String watched = psninfo.get(4).ownText();





        Map<String, Object> map = new HashMap<>();
        map.put("icon", icon);
        map.put("region", region);
        map.put("auth", auth);
        map.put("plus", plus);
        map.put("trophy", trophy);
        map.put("level", level);
        map.put("rank", rank);

        map.put("total", total);
        map.put("perfect", perfect);
        map.put("failed", failed);
        map.put("percent", percent);
        map.put("watched", watched);

        String pattern = "http://ww4.sinaimg.cn/large/(.*).jpg";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(results);
        if(m.find()){
            map.put("bgurl",m.group());
        }
        return map;
    }

    public static ArrayList<Map<String, Object>> parsePersonal(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        listItems.add(parsePeronalHeader(results));

        Document doc = Jsoup.parse(results);
        Elements game_list = doc.select("table.list").select("tbody").select("tr");
        for (Element c : game_list) {
            String game_icon = c.select("img.imgbgnb").attr("src");
            String progress = c.select("div.mb10.progress").text().replace("%", "");
            String game_name = c.select("td").select("p").select("a").text();
            String last_time = c.select("td").get(1).select("small").text();
            String difficulty = c.select("td").get(3).select("span").text();
            String perfection = c.select("td").get(3).select("em").text();
            String trophy = c.select("small.h-p").html();

            Map<String, Object> map = new HashMap<>();
            map.put("game_icon", game_icon);
            map.put("progress", Integer.valueOf(progress));
            map.put("game_name", game_name);
            map.put("spent_time", last_time);
            map.put("difficulty", difficulty);
            map.put("perfection", perfection);
            map.put("trophy", trophy);
            listItems.add(map);
        }
        return listItems;
    }

    public static ArrayList<Map<String, Object>> parseNotice(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        Elements game_list = doc.select("ul.list").select("li");

        for (Element c : game_list) {
            Map<String, Object> map = new HashMap<>();
            Elements qa = c.select("div.content.pb10");
            if(!qa.isEmpty()){
                String user_icon = c.select("a").select("img").attr("src");
                String title = c.select("div.content.pb10").html();
                String username = c.select("a.psnnode").text();
                String time = c.select("div.meta").text().replace(" ", "").replace("查看出处","").replace(username,"");
                String url = c.select("div.meta").select("a").get(0).attr("href");

                map.put("user_icon", user_icon);
                map.put("title", title);
                map.put("username", username);
                map.put("time", time);
                map.put("url", url);
                listItems.add(map);
            }

        }
        return listItems;
    }

    public static Map<String, Object> parseGameList(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        Elements game_list = doc.select("div.box");
        if(game_list.size() == 4){
            Elements c = game_list.select("tr");
            for(Element i : c){
                Map<String, Object> map = new HashMap<>();
                String game_icon = i.select("img.imgbgnb").attr("src");
                String game_name = i.select("td.pd10").select("p").select("a").text();
                String game_mode = i.select("td.twoge").select("span").text();
                String game_percent = i.select("td.twoge").select("em").text();
                String game_trophy = i.select("td.pd10").select("div.meta").html();

                map.put("game_icon",game_icon);
                map.put("game_name",game_name);
                map.put("game_mode",game_mode);
                map.put("game_percent",game_percent);
                map.put("game_trophy",game_trophy);
                listItems.add(map);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("gamelist",listItems);
        return map;
    }

    public static ArrayList<Map<String, Object>> parsePhoto(String results){
        ArrayList<Map<String, Object>>listItems = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        Elements photo = doc.select("div.imgbox");
        for(Element i : photo){
            Map<String, Object> map = new HashMap<>();
            String img = i.select("img.imgbgnb").attr("src");
            int id = Integer.valueOf(i.select("input[name=delimg]").attr("value"));
            map.put("url",img);
            map.put("id",id);
            listItems.add(map);
        }

        return listItems;
    }
}
