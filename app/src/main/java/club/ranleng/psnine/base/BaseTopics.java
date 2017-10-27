package club.ranleng.psnine.base;

import java.util.List;

public abstract class BaseTopics<T> {

    public abstract List<T> items();

    public List<T> getItems() {
        return items();
    }

    public abstract static class BaseItem {

        public abstract String avatar();

        public abstract String username();

        public abstract String time();

        public abstract String reply();

        public abstract String content();

        public abstract String url();

        public String getAvatar() {
            return avatar();
        }

        public String getUsername() {
            return username();
        }

        public String getTime() {
            return time();
        }

        public String getReply() {
            return reply();
        }

        public String getContent() {
            return content();
        }

        public String getUrl() {
            return url();
        }

    }

}
