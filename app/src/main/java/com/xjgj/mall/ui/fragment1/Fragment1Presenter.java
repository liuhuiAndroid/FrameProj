package com.xjgj.mall.ui.fragment1;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.OrderEntity;

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
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment1Presenter implements Fragment1Contract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Fragment1Contract.View mView;
    private CommonApi mCommonApi;
    private int page = 1;

    private List<OrderEntity> mOrderListPublicEntities = new ArrayList<>();

    @Inject
    public Fragment1Presenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void onThreadReceive() {
        disposables.add(mCommonApi.mallOrderList(page,currentType)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<OrderEntity>>, ObservableSource<List<OrderEntity>>>() {
                    @Override
                    public ObservableSource<List<OrderEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<OrderEntity>> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .doOnNext(new Consumer<List<OrderEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<OrderEntity> orderListPublicEntities) throws Exception {
                        if (page == 1) {
                            mOrderListPublicEntities.clear();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<OrderEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<OrderEntity> orderListEntities) throws Exception {
                        mOrderListPublicEntities.addAll(orderListEntities);
                        if (mOrderListPublicEntities.size() == 0) {
                            mView.onEmpty(currentType);
                        } else {
                            mView.onRefreshCompleted(mOrderListPublicEntities);
                            mView.onLoadCompleted(orderListEntities.size() == 10 ? true : false);
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void orderConfirm(int orderId) {
        disposables.add(mCommonApi.orderFinish(orderId)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> stringHttpResult) throws Exception {
                        return mCommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        ToastUtil.showToast(s);
                        mView.onRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    private int currentType = -1;
    @Override
    public void onRefresh(int type) {
        page = 1;
        currentType = type;
        onThreadReceive();
    }

    @Override
    public void onLoadMore() {
        page++;
        onThreadReceive();
    }

    @Override
    public void attachView(@NonNull Fragment1Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        disposables.clear();
    }

}
