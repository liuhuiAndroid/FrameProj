package com.xjgj.mall.ui.location;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.log.Logger;
import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.GeoCoderResultEntity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by we-win on 2017/7/25.
 */

public class ChooseLocationPresenter implements com.xjgj.mall.ui.location.ChooseLocationContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private com.xjgj.mall.ui.location.ChooseLocationContract.View mChooseLocationView;

    private CommonApi mCommonApi;
    private Bus mBus;

    @Inject
    public ChooseLocationPresenter(CommonApi commonApi, Bus bus) {
        mCommonApi = commonApi;
        mBus = bus;
    }

    @Override
    public void geocoderApi(String latLng) {
        disposables.add(mCommonApi.geocoderApi(latLng)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        Logger.i("ResponseBody ?");
                        Logger.i("ResponseBody "+responseBody.toString());
                        String geoCoderResultString = responseBody.string().replace("renderReverse&&renderReverse(", "").replace(")", "");
                        GeoCoderResultEntity geoCoderResultEntity = new Gson().fromJson(geoCoderResultString, GeoCoderResultEntity.class);
                        mChooseLocationView.geocoderResultSuccess(geoCoderResultEntity.getResult().getPois());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mChooseLocationView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull com.xjgj.mall.ui.location.ChooseLocationContract.View view) {
        mChooseLocationView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mChooseLocationView = null;
    }

}
