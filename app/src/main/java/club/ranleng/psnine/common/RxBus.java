package club.ranleng.psnine.common;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class RxBus {
    private static volatile RxBus defaultInstance;
    private final PublishSubject<Object> bus = PublishSubject.create();

    public static RxBus getDefault() {
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance ;
    }

    public void send(final Object event) {
        bus.onNext(event);
    }

    public  <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType).observeOn(AndroidSchedulers.mainThread());
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }

}
