package club.ranleng.psnine.common;

public class KeyGetter {

    public static String getKEY(int key) {
        switch (key) {
            case Key.TOPIC:
                return "topic";
            case Key.GENE:
                return "gene";
            case Key.GUIDE:
                return "guide";
            case Key.OPENBOX:
                return "openbox";
            case Key.PLUS:
                return "plus";
            case Key.QA:
                return "qa";
            case Key.NOTICE:
                return "notice";
        }
        throw new IllegalArgumentException("key is not in the list, plz check it");
    }

    public static String getKEYName(int key) {
        switch (key) {
            case Key.TOPIC:
                return "主页";
            case Key.GENE:
                return "基因";
            case Key.GUIDE:
                return "攻略";
            case Key.OPENBOX:
                return "开箱";
            case Key.PLUS:
                return "PLUS";
            case Key.QA:
                return "问与答";
            case Key.NOTICE:
                return "短消息";
        }
        throw new IllegalArgumentException("key is not in the list, plz check it");
    }

    public static String getGeneOrTopic(int key) {
        if (key == Key.GENE) {
            return getKEY(key);
        } else {
            return getKEY(Key.TOPIC);
        }
    }

    public static String getPath(int key) {
        if (key == Key.GENE || key == Key.QA) {
            return getKEY(key);
        } else {
            return getKEY(Key.TOPIC);
        }
    }
}
