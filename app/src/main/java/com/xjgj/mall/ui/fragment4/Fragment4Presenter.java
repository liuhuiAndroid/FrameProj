package com.xjgj.mall.ui.fragment4;

import android.support.annotation.NonNull;

import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.CarTypeEntity;
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
        disposables.add(mCommonApi.carType()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<CarTypeEntity>>, ObservableSource<List<CarTypeEntity>>>() {
                    @Override
                    public ObservableSource<List<CarTypeEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<CarTypeEntity>> listHttpResult) throws Exception {
                        return CommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CarTypeEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<CarTypeEntity> carTypeEntities) throws Exception {
                        if (carTypeEntities != null && carTypeEntities.size()>0) {
                            mView.renderCarList(carTypeEntities);
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
