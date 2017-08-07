package club.ranleng.psnine.data.moudle;

public interface SimpleSubCallBack<T> {

    void onStart();

    void onNext(T t);

    void onComplete();
}
