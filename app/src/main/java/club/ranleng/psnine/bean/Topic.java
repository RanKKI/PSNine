package club.ranleng.psnine.bean;

public class Topic {

    private int topic_id;
    private int type;
    private int page = 1;

    private String original = "";
    private Boolean editable = false;

    public Topic() {

    }

    public Topic(int topic_id, int type) {
        setTopic_id(topic_id);
        setType(type);
    }

    public Topic(int topic_id, int type, int page) {
        setTopic_id(topic_id);
        setType(type);
        setPage(page);
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
