package club.ranleng.psnine.model.Topics;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.R;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class TopicsDiscount {

    @Pick(value = "ul.dd_ul > li.dd_box")
    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public static class Item {

        @Pick(value = "div.dd_pic > a > img", attr = Attrs.SRC)
        private String icon;
        @Pick(value = "div.dd_info > h4.dd_title.mb10 > a")
        private String name;
        @Pick(value = "div.dd_info > p:nth-child(5)")
        private String time;
        @Pick(value = "div.dd_info > p:nth-child(4)")
        private String des;
        private String discount;
        @Pick(value = "div.dd_info > p:nth-child(2) > a")
        private String activity;

        public String getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }

        public String getDes() {
            return des;
        }

        public String getDiscount() {
            return discount;
        }

        public String getActivity() {
            return activity;
        }
    }
}
