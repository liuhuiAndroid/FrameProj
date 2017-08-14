package com.xjgj.mall.ui.comfirmorder;

import android.support.annotation.NonNull;

import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/7/27.
 */

public class ComfirmOrderPresenter implements ComfirmOrderContract.Presenter {

    private ComfirmOrderContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private CommonApi mCommonApi;

    @Inject
    public ComfirmOrderPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void orderSubmit(String serviceTime, String volume, String weight, String serviceType,
                            String carType, String remark, String counts, String address,
                            String submitType, int flgTogether,int flgSite) {
        mView.showLoading();
        disposables.add(mCommonApi.orderSubmit(serviceTime, volume, weight, serviceType, carType, remark,
                counts, address, submitType, flgTogether,flgSite)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> stringHttpResult) throws Exception {
                        return CommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        mView.submitSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull ComfirmOrderContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
