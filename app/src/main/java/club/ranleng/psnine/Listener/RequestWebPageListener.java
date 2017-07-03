package club.ranleng.psnine.Listener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ran on 22/06/2017.
 */

public interface RequestWebPageListener {
    void onPrepare();

    void on404();

    void onSuccess(ArrayList<Map<String, Object>> result);

}
