package com.xjgj.mall.ui.orderpay;

import android.support.annotation.NonNull;

import com.squareup.otto.Bus;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.CouponEntity;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.PayAlipayEntity;
import com.xjgj.mall.components.storage.UserStorage;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lh on 2017/8/25.
 */

public class OrderPayPresenter implements OrderPayContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    private OrderPayContract.View mView;

    @Inject
    public OrderPayPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void payOrder(int orderId, String money, int couponId, boolean b) {
        mView.showLoading();
        disposables.add(mCommonApi.payAlipay(orderId, money,couponId,b)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<PayAlipayEntity>, ObservableSource<PayAlipayEntity>>() {
                    @Override
                    public ObservableSource<PayAlipayEntity> apply(@io.reactivex.annotations.NonNull HttpResult<PayAlipayEntity> payAlipayEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(payAlipayEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading(0);
                    }
                }).subscribe(new Consumer<PayAlipayEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull PayAlipayEntity payAlipayEntity) throws Exception {
                        mView.payOrderResult(payAlipayEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading(0);
                    }
                }));
    }

    @Override
    public void payConfirm(String outTradeNo) {
        mView.showLoading();
        disposables.add(mCommonApi.payConfirm(outTradeNo)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<CouponEntity>, ObservableSource<CouponEntity>>() {
                    @Override
                    public ObservableSource<CouponEntity> apply(@io.reactivex.annotations.NonNull HttpResult<CouponEntity> couponEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(couponEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading(0);
                    }
                }).subscribe(new Consumer<CouponEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CouponEntity couponEntity) throws Exception {
                        mView.payConfirmResult(couponEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading(0);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull OrderPayContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
