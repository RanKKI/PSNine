package xyz.rankki.psnine

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject


class RxBus private constructor() {

    private val bus: PublishSubject<Any> = PublishSubject.create()

    companion object {
        fun get(): RxBus = Inner.instance
    }

    private object Inner {
        val instance: RxBus = RxBus()
    }

    fun send(any: Any) = bus.onNext(any)

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return bus.ofType(eventType).observeOn(AndroidSchedulers.mainThread())
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }

}