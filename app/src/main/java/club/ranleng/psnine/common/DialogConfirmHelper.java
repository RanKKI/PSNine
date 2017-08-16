package club.ranleng.psnine.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import club.ranleng.psnine.R;

public class DialogConfirmHelper extends DialogFragment {

    private Callback callback;
    private String message;

    public DialogConfirmHelper() {

    }

    public void edit(Callback callback, String message) {
        this.callback = callback;
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext())
                .setMessage(message)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.confirm();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.cancel();
                    }
                });
        return builder.create();
    }

    public interface Callback {
        void confirm();

        void cancel();
    }
}
