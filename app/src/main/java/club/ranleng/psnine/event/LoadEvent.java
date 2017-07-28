package club.ranleng.psnine.event;

/**
 * Created by ran .
 */

public class LoadEvent {

    private Boolean f = false;

    public LoadEvent(){

    }

    public LoadEvent(Boolean f){
        this.f = f;
    }

    public boolean getF(){
        return f;
    }

    public void setF(){
        this.f = false;
    }
}
