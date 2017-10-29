package club.ranleng.psnine.base;

public abstract class BaseTopic {

    public abstract String title();
    public abstract String author();
    public abstract String content();
    public abstract String avatar();
    public abstract String time();
    public abstract String comments();

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

}
