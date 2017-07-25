package com.lh.frameproj.ui.fragment4;

import android.support.annotation.NonNull;

import com.lh.frameproj.api.common.CommonApi;
import com.lh.frameproj.bean.AccountVersionEntity;
import com.lh.frameproj.bean.HttpResult;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/7/25.
 */

public class Fragment4Presenter implements Fragment4Contract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Fragment4Contract.View mView;
    private CommonApi mCommonApi;

    @Inject
    public Fragment4Presenter(CommonApi commonApi) {
        this.mCommonApi = commonApi;
    }

    @Override
    public void onCarListReceive() {
        disposables.add(mCommonApi.accountVersion()
                .debounce(800, TimeUnit.MILLISECONDS)
                .map(new Function<HttpResult<AccountVersionEntity>, AccountVersionEntity>() {
                    @Override
                    public AccountVersionEntity apply(@io.reactivex.annotations.NonNull HttpResult<AccountVersionEntity> stringHttpResult) throws Exception {
                        return stringHttpResult.getResultValue();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AccountVersionEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull AccountVersionEntity accountVersionEntity) throws Exception {
                        if (accountVersionEntity != null) {
                            mView.renderCarList(new ArrayList<String>());
                        } else {
                            loadError();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        loadError();
                        throwable.printStackTrace();
                    }
                }));
    }

    private void loadError() {
        mView.onError();
    }

    @Override
    public void attachView(@NonNull Fragment4Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
