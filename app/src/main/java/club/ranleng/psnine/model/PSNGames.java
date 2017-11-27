package club.ranleng.psnine.model;

import android.text.Html;
import android.text.Spanned;

import java.util.ArrayList;
import java.util.List;

import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class PSNGames {

    @Pick(value = "table.list > tbody > tr")
    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public static class Item {

        @Pick(value = "td:nth-child(2) > p > a")
        private String name;
        @Pick(value = "td:nth-child(2) > small")
        private String time;
        @Pick(value = "td.pd15 > a > img", attr = Attrs.SRC)
        private String icon;
        @Pick(value = "td:nth-child(3)", attr = Attrs.OWN_TEXT)
        private String totalTime;
        @Pick(value = "td:nth-child(4) > span")
        private String mode;
        @Pick(value = "td:nth-child(4) > em")
        private String perfectPercent;
        @Pick(value = "td.pd10 > div > div")
        private String progress;
        @Pick(value = "td.pd10 > small", attr = Attrs.INNER_HTML)
        private String trophy;

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }

        public String getIcon() {
            return icon;
        }

        public String getTotalTime() {
            return totalTime;
        }

        public String getMode() {
            return mode;
        }

        public String getPerfectPercent() {
            return perfectPercent;
        }

        public int getProgress() {
            return Integer.valueOf(progress.replace(" ", "").replace("%", ""));
        }

        public Spanned getTrophy() {
            return Html.fromHtml(trophy);
        }

    }

}
