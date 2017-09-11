package club.ranleng.psnine.data.moudle;

import com.blankj.utilcode.util.ToastUtils;

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
            ToastUtils.showShort("网络中断，请检查您的网络状态");
        } else if (e instanceof ConnectException) {
            ToastUtils.showShort("网络中断，请检查您的网络状态");
        } else {
            ToastUtils.showShort("error:" + e.getMessage());
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