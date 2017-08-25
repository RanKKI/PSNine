package club.ranleng.psnine.utils.HTML;

import com.blankj.utilcode.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.UserState;

public class ConvertHtml {

    public static ArrayList<Map<String, Object>> parseArticle(String results, int type) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        listItems.add(parseArticleHeader(results, type));

        for (Map<String, Object> i : parseArticleGameList(results)) {
            listItems.add(i);
        }
        for (Map<String, Object> i : parseAReplies(results)) {
            listItems.add(i);
        }
        if (type == KEY.QA) {
            for (Map<String, Object> i : parseBReplies(results)) {
                listItems.add(i);
            }
        }

        return listItems;
    }

    public static Map<String, Object> parseArticleHeader(String results, int type) {
        Map<String, Object> map = new HashMap<>();
        if (!KEY.PREF_IMAGESQUALITY) {
            results = results.replace("sinaimg.cn/large", "sinaimg.cn/small");
        }
        Document doc = Jsoup.parse(results);
        String username;
        String icon = "https://static-resource.np.community.playstation.net/avatar/3RD/UP10631304010_B041E9E7D6C08ADC46E7_L.png";
        String time;
        String replies;
        String content;
        String original = null;
        String iframe;
        String title = "";
        Boolean editable = false;
        int img_size = 0;
        int page_size = 1;

        iframe = doc.select("embed").attr("src");
        if (iframe.isEmpty()) {
            iframe = null;
        }

        iframe = doc.select("iframe").attr("src");
        if (iframe.isEmpty()) {
            iframe = null;
        }

        if (type == KEY.GENE) {
            content = doc.select("div.content.pb10").first().html();
            username = doc.select("div.meta").select("a.psnnode").first().ownText();
            Elements img = doc.select("div.content.pd10").select("a").select("img");
            Elements or = doc.select("a.text-info");
            if (or.size() != 0) {
                if (or.get(0).text().equals("出处")) {
                    original = or.get(0).attr("href");
                }
            }

            img_size = img.size();
            for (int i = 0; i < img.size(); i++) {
                map.put("img_" + String.valueOf(i), img.get(i).attr("src"));
            }
            if (doc.select("div.pd10").select("div.meta").size() == 2) {
                if (doc.select("div.meta").get(1).select("span").select("a").size() != 2) {
                    editable = true;
                }
            }

            icon = doc.select("div.side").select("div.box").select("p").select("a").select("img").attr("src");
            String[] temp = doc.select("div.meta").first().ownText().replace(" ", "").split("前");
            time = temp[0] + "前";
            replies = temp[1];

        } else if (type == KEY.QA) {
            content = "<h5>" + doc.select("div.pd10").select("h1").text() + "</h5><br/>" + doc.select("div.content.pd10").text();
            username = doc.select("a.title2").text();
            Elements icon_e = doc.select("div.side").select("div.box").select("p");
            if (!icon_e.isEmpty()) {
                icon = icon_e.get(0).select("a").select("img").attr("src");
            }
            time = doc.select("div.ml64").select("div.meta").get(1).select("span").get(1).text();
            Elements s = doc.select("div.alert-warning.pd10.font12").select("span");
            replies = s.get(s.size() - 1).text();
            if (replies.contains("已采纳")) {
                replies = "已采纳";
            }
//            replies = doc.select("div.meta").select("span").get(2).text();
        } else {
            title = doc.select("div.pd10").select("h1").text();
            content = doc.select("div.content.pd10").html();
            username = doc.select("a.title2").text();
            Elements page = doc.select("div.page").select("ul").select("li");
            if (!page.isEmpty()) {
                page_size = page.size();
                for (int i = 0; i < page.size(); i++) {
                    map.put("page_" + String.valueOf(i + 1), page.get(i).select("a").text());
                    if(page.get(i).hasAttr("class") && page.get(i).attr("class").equals("current")){
                        map.put("current_page",i);
                    }
                }
            }

            if (doc.select("div.alert-info.pd10").select("div.meta").select("span").select("a").size() != 3) {
                editable = true;
            }

            Elements icon_e = doc.select("div.side").select("div.box").select("p");
            if (!icon_e.isEmpty()) {
                icon = icon_e.get(0).select("a").select("img").attr("src");
            }

            time = doc.select("div.pd10").select("div.meta").first().select("span").get(1).text();
            replies = doc.select("div.pd10").select("div.meta").first().ownText();
        }

        map.put("type", "header");
        map.put("img_size", img_size);
        map.put("page_size", page_size);
        map.put("content", content);
        map.put("username", username);
        map.put("editable", editable);
        map.put("iframe", iframe);
        map.put("title", title);
        map.put("icon", icon);
        map.put("original", original);
        map.put("time", time);
        map.put("replies", replies);
        return map;
    }

    public static ArrayList<Map<String, Object>> parseArticleGameList(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        Elements c = doc.select("table").select("tbody").select("tr");
        for (Element i : c) {
            Map<String, Object> map = new HashMap<>();
            String game_icon = i.select("img.imgbgnb").attr("src");
            String game_name = i.select("td.pd10").select("p").select("a").text();
            String game_id = i.select("td.pd10").select("p").select("a").attr("href").replace("http://psnine.com/psngame/", "");
            String game_mode = i.select("td.twoge").select("span").text();
            String game_percent = i.select("td.twoge").select("em").text();
            String game_trophy = i.select("td.pd10").select("div.meta").html();
            Elements game_comment = i.select("td.pd10").select("blockquote");

            map.put("type", "game_list");
            map.put("game_icon", game_icon);
            map.put("game_name", game_name);
            map.put("game_mode", game_mode);
            map.put("game_percent", game_percent);
            map.put("game_trophy", game_trophy);
            map.put("trophy_id", game_id);
            map.put("is_comment", game_comment.hasText());
            if (game_comment.hasText()) {
                map.put("comment", game_comment.text());
            }
            listItems.add(map);
        }
        return listItems;
    }


    public static ArrayList<Map<String, Object>> parseAReplies(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        results = results.replace("<img src=\"http://ww4.sinaimg.cn", "<br/><img src=\"http://ww4.sinaimg.cn");
        if (!KEY.PREF_IMAGESQUALITY) {
            results = results.replace("sinaimg.cn/large", "sinaimg.cn/small");
        }
        Document doc = Jsoup.parse(results);
        Elements post = doc.select("div.post");
        for (Element c : post) {
            if (!c.select("a").hasClass("btn")) {
                String content = c.select("div.content.pb10").first().html();
                String username = c.select("div.meta").select("a.psnnode").text();
                String time = c.select("div.meta").first().ownText();
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
                map.put("time", time);
                map.put("type", "reply");
                listItems.add(map);
            }
        }
        return listItems;
    }

    public static ArrayList<Map<String, Object>> parseBReplies(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        results = results.replace("<img src=\"http://ww4.sinaimg.cn", "<br/><img src=\"http://ww4.sinaimg.cn");
        Document doc = Jsoup.parse(results);
        listItems.add(ListInfo(doc));
        Elements post = doc.select("ul.list").select("li");
        for (Element c : post) {
            if (c.attr("id").contains("comment")) {
                String content = c.select("div.content.pb10").html();
                String username = c.select("div.meta").select("a.psnnode").text();
                String icon = c.select("a.l").select("img").attr("src");
                String comment_id = c.select("div.content.pb10").attr("id").replace("comment-content-", "");
                String time = c.select("div.meta").select("span").first().ownText();

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
                map.put("time", time);
                map.put("type", "reply");
                if (!map.toString().equals("{icon=, title=, time=, username=, id=, editable=false}")) {
                    listItems.add(map);
                }
            }
        }
        return listItems;
    }

    private static Map<String, Object> ListInfo(Document doc) {
        Map<String, Object> map = new HashMap<>();
        int max_page = 1;
        Elements div_page = doc.select("div.page").select("ul").select("li");
        int div_page_size = div_page.size();

        if (div_page_size != 0 && div_page.get(div_page_size - 1).attr("class").equals("disabled")) {
            max_page = Integer.valueOf(div_page.get(div_page_size - 2).select("a").text());

        }
        map.put("max_page", max_page);
        return map;
    }

    public static ArrayList<Map<String, Object>> getTopics(String results, int type) {
        UserState.Check(results);
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        listItems.add(ListInfo(doc));
        if (type == KEY.GENE || type == KEY.GENE_FAV) {
            Elements elements = doc.select("ul.list.genelist").select("li");
            for (Element e : elements) {
                String icon = e.select("a.l").select("img").attr("src");
                String content = e.select("div.content.pb10").text();
                String username = e.select("div.meta").select("a").text();
                String id = "";

                if(type == KEY.GENE_FAV){
                    id = e.select("input[name=param]").attr("value");
                }else{
                    Elements a = e.select("a");
                    for (Element i : a) {
                        if (i.attr("href").contains("http://psnine.com/gene/")) {
                            id = i.attr("href").replace("http://psnine.com/gene/", "");
                            break;
                        }
                    }
                }
                String[] tr = e.select("div.meta").text().replace(username, "").replace(" ", "").split("前");
                String time;
                String reply;

                if(tr.length != 2){
                    time = tr[0];
                    reply = "unknown";
                }else{
                    time = tr[0] + "前";
                    reply = tr[1];
                }
                Map<String, Object> map = new HashMap<>();
                map.put("title", content);
                map.put("username", username);
                map.put("id", Integer.valueOf(id));
                map.put("icon", icon);
                map.put("time", time);
                map.put("reply",reply);
                map.put("type", type);
                listItems.add(map);
            }
        } else if (type == KEY.NOTICE) {
            Elements elements = doc.select("ul.list").select("li");
            for (Element c : elements) {
                Elements qa = c.select("div.content.pb10");
                if (!qa.isEmpty()) {
                    Map<String, Object> map = new HashMap<>();
                    String icon = c.select("a").select("img").attr("src");
                    String title = c.select("div.content.pb10").html();

                    String pattern = "<img src=\"http://ww4.sinaimg.cn/.*\">";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(title);

                    while (m.find()) {
                        title = title.replace(m.group(0), "[图片]");
                    }

                    String username = c.select("a.psnnode").text();
                    String time = c.select("div.meta").text().replace(" ", "").replace("查看出处", "").replace(username, "");
                    String url = c.select("a.r").attr("href");


                    map.put("title", title);
                    map.put("username", username);
                    map.put("icon", icon);
                    map.put("time", time);
                    map.put("reply", "");
                    if (url.contains("gene")) {
                        map.put("type", KEY.GENE);
                        map.put("id", Integer.valueOf(url.replace("http://psnine.com/gene/", "")));
                    } else {
                        map.put("type", KEY.TOPIC);
                        map.put("id", Integer.valueOf(url.replace("http://psnine.com/topic/", "")));
                    }

                    listItems.add(map);
                }
            }
        } else if (type == KEY.QA) {
            Elements elements = doc.select("ul.list").select("li");
            for (Element e : elements) {
                String icon = e.select("a.l").select("img").attr("src");
                String content = e.select("p.title").select("a").text();
                String id = e.select("p.title").select("a").attr("href").replace("http://psnine.com/qa/", "");
                String username = e.select("a.psnnode").text();
                String[] tr = e.select("div.meta").first().ownText().replace(" ", "").split("前");
                String reply = e.select("div.meta").select("span.r").text().split("铜")[1].replace(" ", "");

                Map<String, Object> map = new HashMap<>();
                map.put("title", content);
                map.put("username", username);
                map.put("id", Integer.valueOf(id));
                map.put("icon", icon);
                map.put("time", tr[0] + "前");
                map.put("reply", reply);
                map.put("type", type);
                listItems.add(map);
            }
        } else {
            Elements elements = doc.select("ul.list").select("li");
            for (Element e : elements) {
                String icon = e.select("a.l").select("img").attr("src");
                String title = e.select("div.ml64").select("div.title").select("a").text();
                String username = e.select("div.meta").select("a").first().ownText();
                String id;
                String time;
                String reply;
                if(type == KEY.TOPIC_FAV){
                    id = e.select("input[name=param]").attr("value");
                    String[] tr = e.select("div.meta").text().replace(username, "").replace(" ", "").split("前");
                    time = tr[0] + "前";
                    reply = tr[1];
                }else{
                    id = e.select("div.ml64").select("div.title").select("a").attr("href").replace("http://psnine.com/topic/", "");
                    time = e.select("div.meta").first().ownText();
                    reply = e.select("a.rep.r").text()  + "评论";
                }

                Map<String, Object> map = new HashMap<>();
                map.put("title", title);
                map.put("username", username);
                map.put("id", Integer.valueOf(id));
                map.put("icon", icon);
                map.put("time", time);
                map.put("reply", reply);
                map.put("type", type);
                listItems.add(map);
            }
        }
        return listItems;
    }

    public static ArrayList<ArrayList<String>> parseTable(String results) {
        ArrayList<ArrayList<String>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        Elements elements = doc.select("tr");
        for (Element i : elements) {
            ArrayList<String> list = new ArrayList<>();
            Elements c = i.select("td");
            for (Element f : c) {
                list.add(f.html());
            }
            listItems.add(list);
        }

        return listItems;
    }

    public static Map<String, String> parseTropy(String results) {
        Map<String, String> map = new HashMap<>();
        Document doc = Jsoup.parse(results);
        map.put("game_icon_url", doc.select("img.imgbgnb").attr("src"));
        map.put("game_name", doc.select("div.ml64").select("a").first().ownText());
        map.put("game_des", doc.select("div.ml64").select("span").first().ownText());
        map.put("trophy_id", doc.select("a").first().attr("href").replace("http://psnine.com/trophy/", ""));
        Elements root = doc.select("div.box.pd10.mt10");
        if (root.isEmpty()) {
            map.put("has_comment", "false");
        } else {
            map.put("has_comment", "true");
            map.put("user_comment", root.select("div.content.pb10").first().html());
            map.put("user_name", root.select("div.meta").select("a").first().ownText());
            map.put("time", root.select("div.meta").first().ownText());
        }
        return map;
    }

    public static ArrayList<Map<String, Object>> parsePhoto(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        Elements photo = doc.select("div.imgbox");
        for (Element i : photo) {
            Map<String, Object> map = new HashMap<>();
            String img = i.select("img.imgbgnb").attr("src");
            String id = i.select("input[name=delimg]").attr("value");
            map.put("url", img);
            map.put("id", id);
            listItems.add(map);
        }

        return listItems;
    }

    public static List<String> parseElement(String results){
        List<String> elements = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        Elements list = doc.select("ul.list").select("li");
        for(Element i : list){
            elements.add(i.select("a").text());
        }
        return elements;
    }

    public static ArrayList<Map<String, Object>> parsePsnGame(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        listItems.add(ListInfo(doc));
        Elements game_list = doc.select("table.list").select("tbody").select("tr");
        for (Element c : game_list) {
            String game_icon = c.select("img.imgbgnb").attr("src");
            String progress = c.select("div.mb10.progress").text().replace("%", "");
            String game_name = c.select("td").select("p").select("a").text();
            String last_time = c.select("td").get(1).select("small").text();
            String difficulty = c.select("td").get(3).select("span").text();
            String perfection = c.select("td").get(3).select("em").text();
            String trophy = c.select("small.h-p").html();
            String pattern = "/psngame/(\\d+)";
            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            // 现在创建 matcher 对象
            Matcher m = r.matcher(c.select("td").select("p").select("a").attr("href"));

            Map<String, Object> map = new HashMap<>();
            if (m.find()) {
                map.put("trophy_id", m.group(1));
            }
            map.put("game_icon", game_icon);
            map.put("type", "game");
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

    public static ArrayList<Map<String, Object>> parsePSN(String results, int type) {
        if (type == KEY.PSN_GAME) {
            return parsePsnGame(results);
        } else if (type == KEY.PSN_MSG) {
            return parseBReplies(results);
        } else {
            return getTopics(results, type);
        }
    }


    public static Map<String, String> parsePSNINFO(String results) {
        UserState.Check(results);
        Map<String, String> map = new HashMap<>();
        Document doc = Jsoup.parse(results);

        map.put("icon", doc.select("img.avabig").attr("src"));
        map.put("region", doc.select("img.icon-region").attr("src"));
        map.put("auth", doc.select("img.icon-auth").attr("src"));
        map.put("plus", doc.select("img.icon-plus").attr("src"));
        //backgound image
        String pattern = "http://ww4.sinaimg.cn/large/(.*).jpg";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(results);
        if (m.find()) {
            map.put("bg", m.group());
        }
        return map;
    }
}
