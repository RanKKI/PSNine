package club.ranleng.psnine.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ProgressBar;

import club.ranleng.psnine.R;

public class AlertUtils {

    public static void ErrorDialog(Context context, String error) {
        new AlertDialog.Builder(context).setTitle(R.string.error)
                .setMessage(error)
                .setPositiveButton(R.string.confirm, null)
                .create()
                .show();
    }

    public static void ErrorDialog(Context context, Exception e) {
        ErrorDialog(context, e.getMessage());
    }

    public static void LoadingDialog(Context context, int title, DialogInterface.OnClickListener cancelListener) {
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(progressBar);
        if (cancelListener != null) {
            builder.setNegativeButton(R.string.cancel, cancelListener);
        }
        builder.show();
    }
}
