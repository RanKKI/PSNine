package club.ranleng.psnine.model.Topics;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.base.model.BaseTopics;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class Topics extends BaseTopics<Topics.Items> {

    @Pick(value = "ul.list > li")
    private List<Items> Items = new ArrayList<>();

    @Override
    public List<Items> items() {
        return Items;
    }

    public static class Items extends BaseTopics.BaseItem {

        @Pick(value = "a.l > img", attr = Attrs.SRC)
        private String avatar;
        @Pick(value = "a.psnnode")
        private String username;
        @Pick(value = "div.ml64 > div.meta", attr = Attrs.OWN_TEXT)
        private String time;
        @Pick(value = "a.rep.r")
        private String reply = "0";
        @Pick(value = "div.ml64 > div.title > a")
        private String content;
        @Pick(value = "div.ml64 > div.title > a", attr = Attrs.HREF)
        private String url;

        @Override
        public String avatar() {
            return avatar;
        }

        @Override
        public String username() {
            return username;
        }

        @Override
        public String time() {
            return time;
        }

        @Override
        public String reply() {
            return reply + "评论";
        }

        @Override
        public String content() {
            return content;
        }

        @Override
        public String url() {
            return url;
        }
    }

}
