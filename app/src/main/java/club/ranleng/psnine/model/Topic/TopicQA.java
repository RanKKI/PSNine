package club.ranleng.psnine.model.Topic;

import club.ranleng.psnine.base.model.BaseTopic;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class TopicQA extends BaseTopic {

    @Pick(value = "div.main > div:nth-child(1) > div:nth-child(2) > h1")
    private String title;
    @Pick(value = "div.main > div:nth-child(1) > div:nth-child(2) > div > a")
    private String author;
    @Pick(value = "div.main > div:nth-child(1) > div.content.pd10", attr = Attrs.INNER_HTML)
    private String content;
    @Pick(value = "div.side > div > p:nth-child(1) > a > img", attr = Attrs.SRC)
    private String avatar;
    @Pick(value = "div.main > div:nth-child(1) > div:nth-child(2) > div > span:nth-child(4)")
    private String time;
    @Pick(value = "div.main > div:nth-child(1) > div.alert-warning.pd10.font12 > span:last-child")
    private String comments;

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
        return time;
    }

    @Override
    public String comments() {
        return comments;
    }
}
