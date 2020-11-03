package br.com.sicredi.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class DefaultObservable<T> : Observer<T> {

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
    }

    override fun onError(e: Throwable) {
    }
}