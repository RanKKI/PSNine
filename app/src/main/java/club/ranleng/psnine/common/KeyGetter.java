package club.ranleng.psnine.common;

public class KeyGetter {

    public static String getKEY(int key) {
        switch (key) {
            case KEY.TOPIC:
                return "topic";
            case KEY.GENE:
                return "gene";
            case KEY.GUIDE:
                return "guide";
            case KEY.OPENBOX:
                return "openbox";
            case KEY.PLUS:
                return "plus";
            case KEY.QA:
                return "qa";
        }
        throw new IllegalArgumentException("key is not in the list, plz check it");
    }

    public static String getKEYName(int key) {
        switch (key) {
            case KEY.TOPIC:
                return "主页";
            case KEY.GENE:
                return "基因";
            case KEY.GUIDE:
                return "攻略";
            case KEY.OPENBOX:
                return "开箱";
            case KEY.PLUS:
                return "PLUS";
            case KEY.QA:
                return "问与答";
        }
        throw new IllegalArgumentException("key is not in the list, plz check it");
    }
}
