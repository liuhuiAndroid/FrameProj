package com.xjgj.mall.ui.orderdetail;

import android.support.annotation.NonNull;

import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.OrderDetailEntity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/8/1.
 */

public class OrderDetailPresenter implements OrderDetailContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;

    private OrderDetailContract.View mView;

    @Inject
    public OrderDetailPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void orderDetail(int orderId) {
        mView.showLoading();
        disposables.add(mCommonApi.orderDetail(orderId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<OrderDetailEntity>, ObservableSource<OrderDetailEntity>>() {
                    @Override
                    public ObservableSource<OrderDetailEntity> apply(@io.reactivex.annotations.NonNull HttpResult<OrderDetailEntity> orderDetailEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(orderDetailEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                }).subscribe(new Consumer<OrderDetailEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull OrderDetailEntity orderDetailEntity) throws Exception {
                        mView.orderDetailResult(orderDetailEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.hideLoading();
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull OrderDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}