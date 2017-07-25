package com.lh.frameproj.ui.location;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.log.Logger;
import com.lh.frameproj.api.common.CommonApi;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by we-win on 2017/7/25.
 */

public class ChooseLocationPresenter implements ChooseLocationContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private ChooseLocationContract.View mChooseLocationView;

    private CommonApi mCommonApi;
    private Bus mBus;

    @Inject
    public ChooseLocationPresenter(CommonApi commonApi, Bus bus) {
        mCommonApi = commonApi;
        mBus = bus;
    }

    @Override
    public void geocoderApi(String latLng) {
        disposables.add(mCommonApi.geocoderApi(latLng)
                .debounce(800, TimeUnit.MILLISECONDS)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mChooseLocationView.hideLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        mChooseLocationView.geocoderResultSuccess(responseBody.string());
                        Logger.i("jsonObject = "+responseBody.string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        ToastUtil.showToast("登录失败，请检查您的网络");
                    }
                }));
    }

    @Override
    public void attachView(@NonNull ChooseLocationContract.View view) {
        mChooseLocationView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mChooseLocationView = null;
    }

}
