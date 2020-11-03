package br.com.sicredi.core.extension.rx

import br.com.sicredi.rx.DefaultCompletableObserver
import br.com.sicredi.rx.DefaultMaybeObserver
import br.com.sicredi.rx.DefaultObservable
import br.com.sicredi.rx.DefaultSingleObserver
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

//Observable
fun <T> Observable<T>.observe(observer: DefaultObservable<T>): Any =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(observer)

fun <T> Observable<T>.observe(
    onNext: (T) -> Unit, onError: (Throwable) -> Unit,
    onComplete: () -> Unit = {}, onSubscribe: (Disposable) -> Unit = {}
): Disposable = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    .subscribe(onNext, onError, onComplete, onSubscribe)

//Completable
fun Completable.observe(completableObserver: DefaultCompletableObserver): DefaultCompletableObserver =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(completableObserver)

fun Completable.observe(
    onComplete: () -> Unit, onError: (Throwable) -> Unit
): Disposable = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    .subscribe(onComplete, onError)


//Single
fun <T> Single<T>.observe(singleObserver: DefaultSingleObserver<T>): Any =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(singleObserver)

fun <T> Single<T>.observe(
    onSuccess: (T) -> Unit, onError: (Throwable) -> Unit
): Disposable = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    .subscribe(onSuccess, onError)

//Maybe
fun <T> Maybe<T>.observe(maybeObserver: DefaultMaybeObserver<T>): Any =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(maybeObserver)

fun <T> Maybe<T>.observe(
    onSuccess: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit
): Disposable = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    .subscribe(onSuccess, onError, onComplete)