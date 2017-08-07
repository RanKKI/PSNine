package club.ranleng.psnine.common.multitype.model;

import java.util.Map;

public class TopicsBean {

    private String icon;
    private String title;
    private String username;
    private String time;
    private String reply;
    private int id;
    private int type;

    public TopicsBean() {

    }

    public TopicsBean(Map<String, Object> map) {
        this.icon = (String) map.get("icon");
        this.title = (String) map.get("title");
        this.time = (String) map.get("time");
        this.id = (int) map.get("id");
        this.username = (String) map.get("username");
        this.reply = (String) map.get("reply");
        this.type = (int) map.get("type");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
