package br.com.sicredi.rx

import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

abstract class DefaultMaybeObserver<T>  : MaybeObserver<T> {

    override fun onSuccess(t: T) {}

    override fun onComplete() {}

    override fun onSubscribe(d: Disposable) {}

    override fun onError(e: Throwable) {}
}