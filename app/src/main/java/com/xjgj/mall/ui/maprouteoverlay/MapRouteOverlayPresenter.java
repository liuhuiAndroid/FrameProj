package com.xjgj.mall.ui.maprouteoverlay;

import android.support.annotation.NonNull;

import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.CarAddressEntity;
import com.xjgj.mall.bean.HttpResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lh on 2017/8/16.
 */

public class MapRouteOverlayPresenter implements MapRouteOverlayContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;

    private MapRouteOverlayContract.View mView;

    @Inject
    public MapRouteOverlayPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }


    @Override
    public void carAddress(int orderId, String collectTime, final boolean isFirst) {
        if (isFirst) {
            mView.showLoading();
        }
        disposables.add(mCommonApi.carAddress(orderId, collectTime)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<CarAddressEntity>, ObservableSource<CarAddressEntity>>() {
                    @Override
                    public ObservableSource<CarAddressEntity> apply(@io.reactivex.annotations.NonNull HttpResult<CarAddressEntity> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CarAddressEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CarAddressEntity driverAddressEntities) throws Exception {
                        if (driverAddressEntities != null) {
                            mView.carAddressResult(driverAddressEntities);
                            if (isFirst) {
                                mView.hideLoading(0);
                            }
                        } else {
                            if (isFirst) {
                                mView.hideLoading(-1);
                            }
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
    public void attachView(@NonNull MapRouteOverlayContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
