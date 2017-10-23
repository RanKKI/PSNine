package club.ranleng.psnine.model;

import java.util.ArrayList;
import java.util.List;

import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class TopicsNormal {

    @Pick(value = "ul.list > li")
    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public static class Item {

        @Pick(value = "a.l > img", attr = Attrs.SRC)
        private String avatar;
        @Pick(value = "a.psnnode")
        private String username;
        @Pick(value = "div.ml64 > div.meta", attr = Attrs.OWN_TEXT)
        private String time;
        @Pick(value = "a.rep.r")
        private String reply;
        @Pick(value = "div.ml64 > div.title > a")
        private String content;
        @Pick(value = "div.ml64 > div.title > a", attr = Attrs.HREF)
        private String url;

        public String getAvatar() {
            return avatar;
        }

        public String getUsername() {
            return username;
        }

        public String getTime() {
            return time;
        }

        public String getReply() {
            return reply;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }
    }
}
