package club.ranleng.psnine.event;

/**
 * Created by ran .
 */

public class LoadEvent {

    private Boolean Load_Finish = false;

    public LoadEvent(Boolean t){
        Load_Finish = t;
    }

    public Boolean Load_Finish(){
        return Load_Finish;
    }

}
