package club.ranleng.psnine.common.event;

public class TabsChange {

    private Boolean change;

    public TabsChange(Boolean b){
        this.change = b;
    }

    public Boolean getChange() {
        return change;
    }

    public void setChange(Boolean change) {
        this.change = change;
    }
}
