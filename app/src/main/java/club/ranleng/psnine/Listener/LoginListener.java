package club.ranleng.psnine.Listener;

import android.widget.EditText;

/**
 * Created by ran on 23/06/2017.
 */

public interface LoginListener {
    /**
     * 准备中
     **/
    void onPrepare();

    /**
     * 空值
     **/
    void isEmpty(EditText editText);

    /**
     * 登陆成功
     **/
    void OnSuccess();

    /**
     * 登陆失败
     **/
    void OnFailed();

}
