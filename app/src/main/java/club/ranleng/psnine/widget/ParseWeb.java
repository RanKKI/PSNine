package club.ranleng.psnine.widget;

import com.blankj.utilcode.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ran .
 */

public class ParseWeb {
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

    public static ArrayList<Map<String, Object>> parseArticleList(String results, int type) {
        UserStatus.Check(results);
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        listItems.add(ListInfo(doc));
        if (type == KEY.TYPE_GENE) {
            Elements elements = doc.select("ul.list.genelist").select("li");
            for (Element e : elements) {
                String icon = e.select("a.l").select("img").attr("src");
                String content = e.select("div.content.pb10").text();
                String username = e.select("div.meta").select("a").text();
                String id = "";
                Elements a = e.select("a");
                for (Element i : a) {
                    if (i.attr("href").contains("http://psnine.com/gene/")) {
                        id = i.attr("href").replace("http://psnine.com/gene/", "");
                    }
                }
                String[] tr = e.select("div.meta").text().replace(username, "").replace(" ", "").split("前");

                Map<String, Object> map = new HashMap<>();
                map.put("title", content);
                map.put("username", username);
                map.put("id", Integer.valueOf(id));
                map.put("icon", icon);
                map.put("time", tr[0] + "前");
                map.put("reply", tr[1]);
                map.put("type", type);
                listItems.add(map);
            }
        } else if (type == KEY.TYPE_NOTICE) {
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
                        map.put("type", KEY.TYPE_GENE);
                        map.put("id", Integer.valueOf(url.replace("http://psnine.com/gene/", "")));
                    } else {
                        map.put("type", KEY.TYPE_TOPIC);
                        map.put("id", Integer.valueOf(url.replace("http://psnine.com/topic/", "")));
                    }

                    listItems.add(map);
                }
            }
        } else {
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
                map.put("id", Integer.valueOf(id));
                map.put("icon", icon);
                map.put("time", time);
                map.put("reply", reply + "评论");
                map.put("type", type);
                listItems.add(map);
            }
        }
        return listItems;
    }

    public static Map<String, Object> parseArticleHeader(String results, int type) {
        Map<String, Object> map = new HashMap<>();
        if(!KEY.PREF_IMAGESQUALITY){
            results = results.replace("sinaimg.cn/large","sinaimg.cn/small");
        }
        Document doc = Jsoup.parse(results);

        String username;
        String icon = "https://static-resource.np.community.playstation.net/avatar/3RD/UP10631304010_B041E9E7D6C08ADC46E7_L.png";
        String time;
        String replies;
        String content;
        String original = null;
        String video;
        Boolean editable = false;
        int img_size = 0;
        int page_size = 1;

        video = doc.select("embed").attr("src");
        if (video.isEmpty()) {
            video = null;
        }

        video = doc.select("iframe").attr("src");
        if (video.isEmpty()) {
            video = null;
        }

        if (type == KEY.TYPE_GENE) {
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
            if (doc.select("div.meta").get(1).select("span").select("a").size() != 2) {
                editable = true;
            }

            icon = doc.select("div.side").select("div.box").select("p").select("a").select("img").attr("src");
            String[] temp = doc.select("div.meta").first().ownText().replace(" ", "").split("前");
            time = temp[0] + "前";
            replies = temp[1];

        } else {
            content = doc.select("div.content.pd10").html();
            username = doc.select("a.title2").text();
            Elements page = doc.select("div.page").select("ul").select("li");
            if (!page.isEmpty()) {
                page_size = page.size();
                for (int i = 0; i < page.size(); i++) {
                    map.put("page_" + String.valueOf(i + 1), page.get(i).select("a").text());
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
        map.put("video", video);

        map.put("icon", icon);
        map.put("original", original);
        map.put("time", time);
        map.put("replies", replies);
        return map;
    }

    public static ArrayList<Map<String, Object>> parseAReplies(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        results = results.replace("<img src=\"http://ww4.sinaimg.cn", "<br/><img src=\"http://ww4.sinaimg.cn");
        if(!KEY.PREF_IMAGESQUALITY){
            results = results.replace("sinaimg.cn/large","sinaimg.cn/small");
        }
        Document doc = Jsoup.parse(results);
        Elements post = doc.select("div.post");
        for (Element c : post) {
            if (!c.select("a").hasClass("btn")) {
                String content = c.select("div.content.pb10").first().html();
                String username = c.select("div.meta").select("a.psnnode").text();
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
            String content = c.select("div.content.pb10").html();
            String username = c.select("div.meta").select("a.psnnode").text();
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
            if (!map.toString().equals("{icon=, title=, time=, username=, id=, editable=false}")) {
                listItems.add(map);
            }

        }
        return listItems;
    }

    public static ArrayList<Map<String, Object>> parseArticleGameList(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        Elements c = doc.select("table>tbody>tr");
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

    public static ArrayList<Map<String, Object>> parseArticle(String results, int type) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        listItems.add(parseArticleHeader(results, type));

        for (Map<String, Object> i : parseArticleGameList(results)) listItems.add(i);
        for (Map<String, Object> i : parseAReplies(results)) listItems.add(i);

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
        if (root.isEmpty() || root == null) {
            map.put("has_comment", "false");
        } else {
            map.put("has_comment", "true");
            map.put("user_comment", root.select("div.content.pb10").first().html());
            map.put("user_name", root.select("div.meta").select("a").first().ownText());
            map.put("time", root.select("div.meta").first().ownText());
        }
        return map;
    }

    public static ArrayList<Map<String, Object>> parsePSNGameTrophy(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        Elements header = doc.select("div.box.pd10");
        Boolean is_user = false;
        int total = doc.select("div.box").size();
        Elements user_info = doc.select("div.box").get(1).select("table").select("tbody").select("tr").select("td");
        String user_href = user_info.get(0).select("p").select("a").attr("href");
        if (user_href != null && user_href.contains("psnid")) {
            is_user = true;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("header_icon", header.select("img").attr("src"));
        map.put("header_name", header.select("h1").first().ownText());
        map.put("type", "header");
        listItems.add(map);

        if (is_user) {
            map = new HashMap<>();
            map.put("type", "user");
            map.put("username", user_info.get(0).select("p").select("a").text());
            map.put("percentage", user_info.get(0).select("em").text());
            if (user_info.size() != 1) {
                map.put("first_trophy", user_info.get(1).ownText());
                map.put("last_trophy", user_info.get(2).ownText());
                if (user_info.size() > 3) {
                    map.put("total_time", user_info.get(3).ownText());
                }

            }
            listItems.add(map);
        }

        int trophy_num;
        if (is_user) {
            trophy_num = 2;
        } else {
            trophy_num = 1;
        }
        for (int c = trophy_num; c < total; c++) {
            Elements trophy = doc.select("div.box").get(c).select("table").select("tr");

            for (Element i : trophy) {
                if (!i.attr("id").isEmpty()) {
                    map = new HashMap<>();
                    map.put("type", "item");
                    map.put("trophy_icon", i.select("td").get(0).select("img").attr("src"));
                    map.put("trophy_name", i.select("td").get(1).select("p").text());
                    map.put("trophy_id", i.select("td").get(1).select("p").select("a").first().attr("href").replace("http://psnine.com/trophy/", ""));
                    map.put("trophy_des", i.select("td").get(1).select("em").get(i.select("td").get(1).select("em").size() - 1).text());
                    map.put("trophy_tips", i.select("td").get(1).select("p").select("em.alert-success.pd5").text());
                    if (is_user) {
                        map.put("trophy_date", i.select("td").get(2).select("em").html());
                    } else {
                        map.put("trophy_date", "");
                    }
                    map.put("trophy_percent", i.select("td.twoge").first().ownText());
                    listItems.add(map);
                }
            }
        }

        return listItems;
    }


    public static ArrayList<Map<String, Object>> parsePSNGameTrophyTips(String results) {
        ArrayList<Map<String, Object>> listItems = new ArrayList<>();

        Document doc = Jsoup.parse(results);
        Elements header = doc.select("div.box.pd5");
        Map<String, Object> map = new HashMap<>();
        map.put("trophy_icon", header.select("img").attr("src"));
        map.put("trophy_id", "");
        map.put("trophy_name", header.select("div.ml80.pd10").select("h1").first().text());
        map.put("trophy_des", header.select("div.ml80.pd10").select("em").first().text());
        map.put("trophy_date", "");
        map.put("trophy_percent", "");
        map.put("trophy_tips", "");
        listItems.add(map);

        for (Map<String, Object> i : parseAReplies(results)) {
            listItems.add(i);
        }

        for (Map<String, Object> i : parseBReplies(results)) {
            listItems.add(i);
        }


        return listItems;
    }

    public static Map<String, String> parseGeneEdit(String results) {
        Map<String, String> map = new HashMap<>();
        Document doc = Jsoup.parse(results);
        map.put("content", doc.select("textarea[name=content]").text());
        map.put("element", doc.select("input[name=element]").attr("value"));
        map.put("photo", doc.select("input[name=photo]").attr("value"));
        map.put("video", doc.select("input[name=video]").attr("value"));
        map.put("muid", doc.select("input[name=muid]").attr("value"));
        map.put("url", doc.select("input[name=url]").attr("value"));
        return map;
    }

    public static Map<String, String> parseTopicEdit(String results) {
        Map<String, String> map = new HashMap<>();
        Document doc = Jsoup.parse(results);
        map.put("title", doc.select("input[name=title]").attr("value"));
        map.put("content", doc.select("textarea[name=content]").text());
        map.put("open", doc.select("select[name=open]").select("option[selected=selected]").attr("value"));
        return map;
    }

    public static ArrayList<Map<String, String>> parsePhoto(String results) {
        ArrayList<Map<String, String>> listItems = new ArrayList<>();
        Document doc = Jsoup.parse(results);
        Elements photo = doc.select("div.imgbox");
        for (Element i : photo) {
            Map<String, String> map = new HashMap<>();
            String img = i.select("img.imgbgnb").attr("src");
            String id = i.select("input[name=delimg]").attr("value");
            map.put("url", img);
            map.put("id", id);
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
            return parseArticleList(results, type);
        }
    }

    public static Map<String, String> parsePeronalHeader(String results) {
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

        String upgame = doc.select("div.psnbtn.psnbtnright").select("a").first().text();
        upgame = String.valueOf(!upgame.contains("冷却"));
        String upbase = String.valueOf(doc.select("div.psnbtn.psnbtnright").select("a").size() != 1);


        Map<String, String> map = new HashMap<>();
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

        map.put("upbase", upbase);
        map.put("upgame", upgame);
        map.put("type", "header");

        String pattern = "http://ww4.sinaimg.cn/large/(.*).jpg";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(results);
        if (m.find()) {
            map.put("bgurl", m.group());
        }
        return map;
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
}
