package com.xjgj.mall.ui.custommap;

import android.support.annotation.NonNull;

import com.squareup.otto.Bus;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.AddressEntity;
import com.xjgj.mall.bean.HttpResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/7/25.
 */

public class CustomMapPresenter implements CustomMapContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CustomMapContract.View mView;

    private CommonApi mCommonApi;
    private Bus mBus;

    @Inject
    public CustomMapPresenter(CommonApi commonApi, Bus bus) {
        mCommonApi = commonApi;
        mBus = bus;
    }

    @Override
    public void attachView(@NonNull CustomMapContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

    @Override
    public void addressList(String s) {
        disposables.add(mCommonApi.addressList(s)
                .debounce(800, TimeUnit.MILLISECONDS)
                .switchMap(new Function<HttpResult<List<AddressEntity>>, ObservableSource<List<AddressEntity>>>() {
                    @Override
                    public ObservableSource<List<AddressEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<AddressEntity>> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AddressEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<AddressEntity> addressEntities) throws Exception {
                        mView.addressListResultSuccess(addressEntities);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

}
