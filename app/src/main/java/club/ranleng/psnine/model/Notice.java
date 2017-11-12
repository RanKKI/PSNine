package club.ranleng.psnine.model;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.base.BaseTopics;
import club.ranleng.psnine.utils.Parse;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class Notice extends BaseTopics<Notice.Items> {

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
        @Pick(value = "div.ml64 > div.content")
        private String content;
        @Pick(value = "div.ml64 > div.meta > a.r", attr = Attrs.HREF)
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
            return "";
        }

        @Override
        public String content() {
            return Parse.parseNoticeHtml(content);
        }

        @Override
        public String url() {
            return url;
        }
    }
}
