package club.ranleng.psnine.model;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.base.BaseTopics;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class TopicsQA extends BaseTopics<TopicsQA.Items> {

    @Pick(value = "ul.list > li")
    private List<TopicsQA.Items> Items = new ArrayList<>();

    @Override
    public List<TopicsQA.Items> items() {
        return Items;
    }

    public static class Items extends BaseTopics.BaseItem {

        @Pick(value = "a.l > img", attr = Attrs.SRC)
        private String avatar;
        @Pick(value = "div.ml64 > div.meta > a.psnnode")
        private String username;
        @Pick(value = "div.ml64 > div.meta", attr = Attrs.OWN_TEXT)
        private String time;
        @Pick(value = " div.ml64 > div.meta > span.r > span:not(.text-bronze)")
        private String reply = "";
        @Pick(value = "div.ml64 > p.title > a")
        private String content;
        @Pick(value = "div.ml64 > p.title > a", attr = Attrs.HREF)
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
            return time.replace(" ", "").split("前")[0] + "前";
        }

        @Override
        public String reply() {
            return reply;
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
