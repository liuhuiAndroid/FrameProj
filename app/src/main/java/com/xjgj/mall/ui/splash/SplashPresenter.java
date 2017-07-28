package com.xjgj.mall.ui.splash;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.frameproj.library.util.log.Logger;
import com.xjgj.mall.components.okhttp.OkHttpHelper;
import com.xjgj.mall.injector.PerActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by we-win on 2017/7/20.
 */

@PerActivity
public class SplashPresenter implements SplashContract.Presenter{

    private SplashContract.View mSplashView;

    private Context mContext;
    private OkHttpHelper mOkHttpHelper;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public SplashPresenter(Context mContext, OkHttpHelper mOkHttpHelper) {
        this.mContext = mContext;
        this.mOkHttpHelper = mOkHttpHelper;
    }

    @Override
    public void attachView(@NonNull SplashContract.View view) {
        mSplashView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mSplashView = null;
    }

    @Override
    public void initData() {
        disposables.add(getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver()));
    }

    private Observable<? extends Long> getObservable() {
        return Observable.timer(1, TimeUnit.SECONDS);
    }

    private DisposableObserver<Long> getObserver() {
        return new DisposableObserver<Long>() {

            @Override
            public void onNext(Long value) {
                Logger.i("onNext:");
            }

            @Override
            public void onError(Throwable e) {
                Logger.i("onError:"+e.getMessage());
            }

            @Override
            public void onComplete() {
                Logger.i("onComplete:");
                mSplashView.showMainUi();
            }
        };
    }

}
