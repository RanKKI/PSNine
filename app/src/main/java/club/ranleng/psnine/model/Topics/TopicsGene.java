package club.ranleng.psnine.model.Topics;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.base.model.BaseTopics;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class TopicsGene extends BaseTopics<TopicsGene.Items> {

    @Pick(value = "ul.list.genelist > li")
    private List<TopicsGene.Items> Items = new ArrayList<>();

    @Override
    public List<TopicsGene.Items> items() {
        return Items;
    }

    public static class Items extends BaseTopics.BaseItem {

        @Pick(value = "a.l > img", attr = Attrs.SRC)
        private String avatar;
        @Pick(value = "div.ml64 > div.meta > a.psnnode")
        private String username;
        @Pick(value = "div.ml64 > div.meta", attr = Attrs.OWN_TEXT)
        private String time;
        private String reply = "0";
        @Pick(value = "div.ml64 > a > div.content")
        private String content;
        @Pick(value = "div.ml64 > a.touch", attr = Attrs.HREF)
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
            return time.replace(" ", "").split("前")[1];
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
