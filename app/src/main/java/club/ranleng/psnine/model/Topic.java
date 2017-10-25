package club.ranleng.psnine.model;

import java.util.ArrayList;
import java.util.List;

import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class Topic {

    @Pick(value = "div.main > div.box > div.pd10 > h1")
    private String title;
    @Pick(value = "div.main > div.box > div.pd10 > div > a.psnnode")
    private String author;
    @Pick(value = "div.page > ul > li")
    private List<Page> pages = new ArrayList<>();
    @Pick(value = "div.content.pd10", attr = Attrs.INNER_HTML)
    private String content;
    @Pick(value = "div.side > div.box > p > a >img", attr = Attrs.SRC)
    private String avatar;
    @Pick(value = "div.main > div.box > div.pd10 > div.meta > span:not(.r)")
    private String time;
    @Pick(value = "div.main > div.box > div.pd10 > div.meta", attr = Attrs.OWN_TEXT)
    private String comments;

    @Override
    public String toString() {
        return "Topic{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", avatar='" + avatar + '\'' +
                ", time='" + time + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<Page> getPages() {
        return pages;
    }

    public String getContent() {
        return content;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTime() {
        return time;
    }

    public String getComments() {
        return comments;
    }

    public static class Page {

        @Pick("a")
        private String title;

        public String getTitle() {
            return title;
        }
    }
}
