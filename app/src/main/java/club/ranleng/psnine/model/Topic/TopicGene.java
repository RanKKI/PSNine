package club.ranleng.psnine.model.Topic;

import club.ranleng.psnine.base.BaseTopic;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class TopicGene extends BaseTopic {

    @Pick(value = "div.main > div.box:not(.mt20):not(.oh) > div.pd10 > div.content.pb10", attr = Attrs.INNER_HTML)
    private String title;
    @Pick(value = "a.title2")
    private String author;
    @Pick(value = "div.pd10.content", attr = Attrs.INNER_HTML)
    private String content;
    @Pick(value = "div.side > div > p > a > img", attr = Attrs.SRC)
    private String avatar;
    @Pick(value = "div.pd10 > div.meta", attr = Attrs.OWN_TEXT)
    private String time;
//    @Pick(value = "div.main > div.box > div.pd10 > div.meta", attr = Attrs.OWN_TEXT)
//    private String comments;

    @Override
    public String title() {
        return title;
    }

    @Override
    public String author() {
        return author;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public String avatar() {
        return avatar;
    }

    @Override
    public String time() {
        return time.replace(" ", "").split("前")[0] + "前";
    }

    @Override
    public String comments() {
        return time.replace(" ", "").split("前")[1];
    }
}
