package club.ranleng.psnine.ui.newTopic;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import club.ranleng.psnine.R;
import club.ranleng.psnine.data.remote.ApiManager;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.FormBody;
import okhttp3.ResponseBody;

public class newTopicPresenter implements newTopicContact.Presenter {

    private newTopicContact.View view;
    private Disposable disposable;

    private newTopicPresenter(newTopicContact.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    public static void newInstance(newTopicContact.View view) {
        new newTopicPresenter(view);
    }

    @Override
    public void start() {
        view.setup();
    }

    @Override
    public void post(FormBody body) {
        final ProgressDialog dialog = new ProgressDialog(view.getCtx());
        dialog.setMessage(view.getCtx().getString(R.string.sending));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (disposable != null) {
                    disposable.dispose();
                }
            }
        });
        dialog.show();
        disposable = ApiManager.getDefault()
                .newGene(body)
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        dialog.dismiss();
                        view.finishPosted();
                    }
                });
    }
}
