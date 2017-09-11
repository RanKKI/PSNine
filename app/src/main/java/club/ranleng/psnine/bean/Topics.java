package club.ranleng.psnine.bean;

import club.ranleng.psnine.common.KEY;

public class Topics {

    private int type;
    private int max_page = -1;
    private int page;
    private String query;
    private String ele;

    public Topics(){

    }

    public String getTypeName(){
        return KEY.getTypeName(getType());
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEle() {
        return ele;
    }

    public void setEle(String ele) {
        if (ele == null){
            ele = "";
        }
        this.ele = ele;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        if (query == null){
            query = "";
        }
        this.query = query;
    }

    @Override
    public String toString() {
        return "Topics{" +
                "type=" + type +
                ", max_page=" + max_page +
                ", page=" + page +
                ", query='" + query + '\'' +
                ", ele='" + ele + '\'' +
                '}';
    }

    public int getMax_page() {
        return max_page;
    }

    public void setMax_page(int max_page) {
        this.max_page = max_page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
