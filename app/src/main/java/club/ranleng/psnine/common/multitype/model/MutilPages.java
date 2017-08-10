package club.ranleng.psnine.common.multitype.model;

import java.util.ArrayList;


public class MutilPages {

    private ArrayList<String> pages;
    private int current_page;

    public MutilPages(ArrayList<String> list, int current_page) {
        this.pages = list;
        this.current_page = current_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public ArrayList<String> getPages() {
        return pages;
    }
}
