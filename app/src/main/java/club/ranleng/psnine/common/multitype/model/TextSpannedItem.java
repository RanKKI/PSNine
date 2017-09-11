package club.ranleng.psnine.common.multitype.model;

/**
 * Created by ran on 02/07/2017.
 */

public class TextSpannedItem {

    private String text;
    private Boolean mid = false;
    private int size = 14;
    private int padding = 0;

    public TextSpannedItem(){

    }
    
    public TextSpannedItem(String text) {
        this.text = text;
    }

    public TextSpannedItem(String text, Boolean b) {
        this(text);
        this.mid = b;
    }

    public TextSpannedItem(String text, Boolean b, int size) {
        this(text, b);
        this.size = size;
    }

    public TextSpannedItem(String text, Boolean b, int size, int padding) {
        this(text, b, size);
        this.padding = padding;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getMid() {
        return mid;
    }

    public void setMid(Boolean mid) {
        this.mid = mid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}
