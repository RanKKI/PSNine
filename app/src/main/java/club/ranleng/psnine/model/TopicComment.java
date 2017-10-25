package club.ranleng.psnine.model;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import me.ghui.fruit.Attrs;
import me.ghui.fruit.annotations.Pick;

public class TopicComment {

    @Pick(value = "div.post")
    private List<Comment> comments = new ArrayList<>();

    @Pick(value = "div.box > div.page:first-child > ul > li:last-child > a")
    private String maxPage;

    public int getMaxPage() {
        maxPage = maxPage.replace(" ","");
        maxPage = maxPage.replace("条","");
        int max = Integer.valueOf(maxPage);
        int total = max / 40;
        int m = total % 40;
        if(m != 0){
            total ++;
        }
        return total;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public static class Comment {
        @Pick(value = "a.l > img", attr = Attrs.SRC)
        private String avatar;
        @Pick(value = "div > div.meta > a.psnnode")
        private String username;
        @Pick(value = "div.post > div > div.meta", attr = Attrs.OWN_TEXT)
        private String time;
        @Pick(value = "div > div.content.pb10", attr = Attrs.HTML)
        private String content;

        public String getAvatar() {
            return avatar;
        }

        public String getUsername() {
            return username;
        }

        public String getTime() {
            return time;
        }

        public String getContent() {
            return content;
        }
    }
}
