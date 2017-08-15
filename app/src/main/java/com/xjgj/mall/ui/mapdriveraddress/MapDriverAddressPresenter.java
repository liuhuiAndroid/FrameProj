package com.xjgj.mall.ui.mapdriveraddress;

import android.support.annotation.NonNull;

import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.DriverAddressEntity;
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
 * Created by lh on 2017/8/15.
 */

public class MapDriverAddressPresenter implements MapDriverAddressContract.Presenter{

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;

    private MapDriverAddressContract.View mView;

    @Inject
    public MapDriverAddressPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }


    @Override
    public void driverAddress(double longtitude,double latitude) {
        mView.showLoading();
        disposables.add(mCommonApi.driverAddress(longtitude,latitude)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<DriverAddressEntity>>, ObservableSource<List<DriverAddressEntity>>>() {
                    @Override
                    public ObservableSource<List<DriverAddressEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<DriverAddressEntity>> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DriverAddressEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<DriverAddressEntity> driverAddressEntities) throws Exception {
                        if (driverAddressEntities != null) {
                            mView.hideLoading(0);
                            mView.driverAddressResult(driverAddressEntities);
                        } else {
                            mView.hideLoading(-1);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.hideLoading(-1);
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull MapDriverAddressContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
