package club.ranleng.psnine.bean;

import club.ranleng.psnine.common.KEY;

public class Topics {

    private int type;
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
        return String.format("Type: %s, Ele: %s, Query: %s",type,ele,query);
    }
}
