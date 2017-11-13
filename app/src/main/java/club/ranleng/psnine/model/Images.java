package club.ranleng.psnine.model;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.utils.Parse;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class Images {

    @Pick(value = "div.box > div.imgbox")
    private List<Items> Items = new ArrayList<>();

    public List<Images.Items> getItems() {
        return Items;
    }

    public static class Items {

        @Pick(value = "img.imgbgnb", attr = Attrs.SRC)
        private String url;
        @Pick(value = "input[name=\"delimg\"]", attr = "value")
        private String id;

        public String getUrl() {
            return url;
        }

        public String getUrlKey() {
            return Parse.parseImageUrl(url, -2);
        }

        public String getId() {
            return id;
        }
    }

}
