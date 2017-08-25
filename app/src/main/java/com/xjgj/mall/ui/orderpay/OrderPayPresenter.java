package com.xjgj.mall.ui.orderpay;

import android.support.annotation.NonNull;

import com.squareup.otto.Bus;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.components.storage.UserStorage;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

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
    public void payOrder(int orderId, double money) {
        mView.showLoading();
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
