package com.xjgj.mall.ui.fragment2.order_cancel;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.ToastUtil;
import com.squareup.otto.Bus;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.OrderEntity;
import com.xjgj.mall.injector.PerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/7/20.
 */
@PerActivity
public class OrderCanceledPresenter implements OrderCanceledContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private OrderCanceledContract.View mView;
    private CommonApi mCommonApi;
    private Bus mBus;

    private List<OrderEntity> mOrderEntities = new ArrayList<>();

    private int page = 1;

    @Inject
    public OrderCanceledPresenter(CommonApi commonApi, Bus mBus) {
        this.mCommonApi = commonApi;
        this.mBus = mBus;
    }

    @Override
    public void onOrderWaitingAcceptListReceive() {
        disposables.add(mCommonApi.mallOrderList(page, 4, -1)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<OrderEntity>>, ObservableSource<List<OrderEntity>>>() {
                    @Override
                    public ObservableSource<List<OrderEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<OrderEntity>> listHttpResult) throws Exception {
                        return mCommonApi.flatResponse(listHttpResult);
                    }
                })
                .doOnNext(new Consumer<List<OrderEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<OrderEntity> orderEntities) throws Exception {
                        if (page == 1) {
                            mOrderEntities.clear();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<OrderEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<OrderEntity> orderEntities) throws Exception {
                        if (orderEntities != null) {
                            mOrderEntities.addAll(orderEntities);
                            if (mOrderEntities.size() == 0) {
                                mView.onEmpty();
                            } else {
                                mView.hideLoading();
                                mView.renderOrderList(mOrderEntities);
                                mView.onRefreshCompleted();
                                mView.onLoadCompleted(mOrderEntities.size() == 10 ? true : false);
                            }
                        } else {
                            mView.hideLoading();
                            mView.renderOrderList(mOrderEntities);
                            mView.onRefreshCompleted();
                            mView.onLoadCompleted(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        loadError(throwable);
                    }
                }));
    }


    @Override
    public void onRefresh() {
        page = 1;
        onOrderWaitingAcceptListReceive();
    }

    private void loadError(Throwable throwable) {
        if (mOrderEntities.isEmpty()) {
            mView.onError(throwable);
        } else {
            mView.renderOrderList(mOrderEntities);
            mView.onRefreshCompleted();
            mView.onLoadCompleted(true);
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        onOrderWaitingAcceptListReceive();
    }

    @Override
    public void onOrderClick(int position) {
        OrderEntity orderEntity = mOrderEntities.get(position);
        ToastUtil.showToast("点击条目:" + position + "，数据：");
    }

    @Override
    public void orderCancel(int orderId) {

    }

    @Override
    public void attachView(@NonNull OrderCanceledContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }


}
