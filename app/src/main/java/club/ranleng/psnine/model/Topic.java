package club.ranleng.psnine.model;

import club.ranleng.psnine.base.BaseTopic;
import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class Topic extends BaseTopic {

    @Pick(value = "div.main > div.box > div.pd10 > h1")
    private String title;
    @Pick(value = "div.main > div.box > div.pd10 > div > a.psnnode")
    private String author;
    @Pick(value = "div.content.pd10", attr = Attrs.INNER_HTML)
    private String content;
    @Pick(value = "div.side > div.box > p > a >img", attr = Attrs.SRC)
    private String avatar;
    @Pick(value = "div.main > div.box > div.pd10 > div.meta > span:not(.r)")
    private String time;
    @Pick(value = "div.main > div.box > div.pd10 > div.meta", attr = Attrs.OWN_TEXT)
    private String comments;

    //    @Pick(value = "div.page > ul > li")
//    private List<Page> pages = new ArrayList<>();

    @Override
    public String toString() {
        return "ApiTopic{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", avatar='" + avatar + '\'' +
                ", time='" + time + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

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


//    public List<Page> getPages() {
//        return pages;
//    }
//
//    public static class Page {
//
//        @Pick("a")
//        private String title;
//
//        public String getTitle() {
//            return title;
//        }
//    }

}
