package com.xjgj.mall.ui.fragmentfindcar.on_site;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.GeoCoderResultEntity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by lh on 2017/8/14.
 */

public class FragmentFindCarOnSitePresenter implements FragmentFindCarOnSiteContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private FragmentFindCarOnSiteContract.View mView;
    private CommonApi mCommonApi;

    @Inject
    public FragmentFindCarOnSitePresenter(CommonApi commonApi) {
        this.mCommonApi = commonApi;
    }

    @Override
    public void geocoderApi(String latLng) {
        disposables.add(mCommonApi.geocoderApi(latLng)
                .debounce(800, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        String geoCoderResultString = responseBody.string().replace("renderReverse&&renderReverse(", "").replace(")", "");
                        GeoCoderResultEntity geoCoderResultEntity = new Gson().fromJson(geoCoderResultString, GeoCoderResultEntity.class);
                        mView.geocoderResultSuccess(geoCoderResultEntity.getResult().getPois());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull FragmentFindCarOnSiteContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }


}
