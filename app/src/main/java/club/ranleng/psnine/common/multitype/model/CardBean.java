package club.ranleng.psnine.common.multitype.model;

import android.support.annotation.NonNull;

public class CardBean {

    private String content;


    public CardBean(){

    }
    public CardBean(@NonNull String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
