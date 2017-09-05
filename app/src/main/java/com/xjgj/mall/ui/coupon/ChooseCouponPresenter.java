package com.xjgj.mall.ui.coupon;

import android.support.annotation.NonNull;

import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.CouponEntity;
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
 * Created by lh on 2017/9/5.
 */

public class ChooseCouponPresenter implements ChooseCouponContract.Presenter {

    private ChooseCouponContract.View mView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private CommonApi mCommonApi;

    @Inject
    public ChooseCouponPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void couponList() {
        mView.showLoading();
        disposables.add(mCommonApi.couponList()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<CouponEntity>>, ObservableSource<List<CouponEntity>>>() {
                    @Override
                    public ObservableSource<List<CouponEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<CouponEntity>> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CouponEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<CouponEntity> couponEntityList) throws Exception {
                        if (couponEntityList != null && couponEntityList.size() > 0) {
                            mView.hideLoading(1);
                            mView.couponListSuccess(couponEntityList);
                        } else {
                            mView.hideLoading(0);
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
    public void attachView(@NonNull ChooseCouponContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
