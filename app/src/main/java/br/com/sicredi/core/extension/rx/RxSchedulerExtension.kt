package br.com.sicredi.core.extension.rx

import br.com.sicredi.provider.scheduler.BaseSchedulerProvider
import br.com.sicredi.rx.DefaultCompletableObserver
import br.com.sicredi.rx.DefaultMaybeObserver
import br.com.sicredi.rx.DefaultObservable
import br.com.sicredi.rx.DefaultSingleObserver
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//Observable
fun <T> Observable<T>.observe(baseSchedulerProvider: BaseSchedulerProvider, observer: DefaultObservable<T>): Any =
    this.subscribeOn(baseSchedulerProvider.io())
        .observeOn(baseSchedulerProvider.ui())
        .subscribeWith(observer)

//Completable
fun Completable.observe(baseSchedulerProvider: BaseSchedulerProvider, completableObserver: DefaultCompletableObserver): DefaultCompletableObserver =
    this.subscribeOn(baseSchedulerProvider.io())
        .observeOn(baseSchedulerProvider.ui())
        .subscribeWith(completableObserver)

//Single
fun <T> Single<T>.observe(baseSchedulerProvider: BaseSchedulerProvider, singleObserver: DefaultSingleObserver<T>): Any =
    this.subscribeOn(baseSchedulerProvider.io())
        .observeOn(baseSchedulerProvider.ui())
        .subscribeWith(singleObserver)

//Maybe
fun <T> Maybe<T>.observe(baseSchedulerProvider: BaseSchedulerProvider, maybeObserver: DefaultMaybeObserver<T>): Any =
    this.subscribeOn(baseSchedulerProvider.io())
        .observeOn(baseSchedulerProvider.ui())
        .subscribeWith(maybeObserver)
