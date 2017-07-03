package club.ranleng.psnine.Listener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ran on 22/06/2017.
 */

public interface RequestPhotoListener {
    void onPrepare();

    void onSuccess(ArrayList<String> result);

}
