package club.ranleng.psnine.ui.newTopic;

import android.content.DialogInterface;

import club.ranleng.psnine.R;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.utils.AlertUtils;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.FormBody;
import okhttp3.ResponseBody;

public class newTopicPresenter implements newTopicContact.Presenter {

    private newTopicContact.View view;

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
        final Disposable disposable = ApiManager.getDefault()
                .newGene(body)
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        view.finishPosted();
                    }
                });
        AlertUtils.LoadingDialog(view.getCtx(),
                R.string.sending, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        disposable.dispose();
                    }
                });
    }
}
