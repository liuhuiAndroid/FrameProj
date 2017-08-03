package com.xjgj.mall.ui.cancelorder;

import android.support.annotation.NonNull;

import com.xjgj.mall.api.common.CommonApi;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by we-win on 2017/8/3.
 */

public class CancelOrderPresenter implements CancelOrderContract.Presenter{

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;

    private CancelOrderContract.View mView;

    @Inject
    public CancelOrderPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }


    @Override
    public void orderCancel() {

    }

    @Override
    public void attachView(@NonNull CancelOrderContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.dispose();
        mView = null;
    }
}
