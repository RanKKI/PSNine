package club.ranleng.psnine.base.model;

import java.util.List;

import club.ranleng.psnine.model.Topic.TopicGame;

public abstract class BaseTopic {

    public abstract String title();

    public abstract String author();

    public abstract String content();

    public abstract String avatar();

    public abstract String time();

    public abstract String comments();

    public abstract List<TopicGame> games();

    public String getTitle() {
        return title();
    }

    public String getAuthor() {
        return author();
    }

    public String getContent() {
        return content();
    }

    public String getAvatar() {
        return avatar();
    }

    public String getTime() {
        return time();
    }

    public String getComments() {
        return comments();
    }

    public String getFormattedContent() {
        String title = getTitle();
        String content = getContent();
        String result = "";
        if (title != null && content != null) {
            result = String.format("<p>%s</p><br/>%s", title, content);
        } else if (title != null) {
            result = title;
        } else if (content != null) {
            result = content;
        }
        return result;
    }

    public List<TopicGame> getGames() {
        return games();
    }

}
