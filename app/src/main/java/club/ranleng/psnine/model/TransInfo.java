package club.ranleng.psnine.model;

import java.util.ArrayList;
import java.util.List;

import me.ghui.fruit.annotations.Pick;

public class TransInfo {

    @Pick(value = "table.list.mt10 > * > tr")
    private List<Item> items = new ArrayList<>();
    @Pick(value = "body > div:nth-child(2) > div:nth-child(2) > div > table > tbody > tr > td > div")
    private String NBamount;
    @Pick(value = "body > div:nth-child(2) > div:nth-child(3) > div > table:nth-child(3) > tbody > tr > td:nth-child(1) > div")
    private String ZBamount;
    @Pick(value = "body > div:nth-child(2) > div:nth-child(3) > div > table:nth-child(3) > tbody > tr > td:nth-child(2) > div")
    private String VIPlevel;

    public List<Item> getItems() {
        return items;
    }

    public String getNBamount() {
        return NBamount;
    }

    public String getZBamount() {
        return ZBamount;
    }

    public String getVIPlevel() {
        return VIPlevel;
    }

    public static class Item {

        @Pick(value = "td:nth-child(1)")
        private String amount;
        @Pick(value = "td:nth-child(2)")
        private String type;
        @Pick(value = "td:nth-child(3)")
        private String transNum;
        @Pick(value = "td:nth-child(4)")
        private String date;
        @Pick(value = "td:nth-child(5)")
        private String status;

        public String getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }

        public String getTransNum() {
            return transNum;
        }

        public String getDate() {
            return date;
        }

        public String getStatus() {
            return status;
        }
    }
}
