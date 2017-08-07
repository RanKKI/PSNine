package club.ranleng.psnine.data.moudle;

import com.blankj.utilcode.util.LogUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class SimpleSubscriber<T> implements Observer<T> {

    private SimpleSubCallBack<T> simpleSubCallback;

    public SimpleSubscriber(SimpleSubCallBack<T> simpleSubCallback) {
        this.simpleSubCallback = simpleSubCallback;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (simpleSubCallback != null) {
            simpleSubCallback.onStart();
        }
    }

    @Override
    public void onNext(T t) {
        if (simpleSubCallback != null) {
            simpleSubCallback.onNext(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof SocketTimeoutException) {
//            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
//            Toast.makeText(application, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(application, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (simpleSubCallback != null) {
            simpleSubCallback.onComplete();
        }
    }

    @Override
    public void onComplete() {
        if (simpleSubCallback != null) {
            simpleSubCallback.onComplete();
        }
    }

}