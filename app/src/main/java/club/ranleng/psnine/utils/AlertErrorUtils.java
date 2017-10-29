package club.ranleng.psnine.utils;

import android.app.AlertDialog;
import android.content.Context;

import club.ranleng.psnine.R;

public class AlertErrorUtils {

    public static void show(Context context, String error) {
        new AlertDialog.Builder(context).setTitle(R.string.error)
                .setMessage(error)
                .setPositiveButton(R.string.confirm, null)
                .create()
                .show();
    }

    public static void show(Context context, Exception e) {
        show(context, e.getMessage());
    }

}
