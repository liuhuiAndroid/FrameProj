package com.xjgj.mall.ui.cancelorder.driver_reasons;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.DictionaryEntity;
import com.xjgj.mall.bean.HttpResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/8/3.
 */

public class DriverReasonsPresenter implements DriverReasonsContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private DriverReasonsContract.View mView;
    private CommonApi mCommonApi;

    @Inject
    public DriverReasonsPresenter(CommonApi commonApi) {
        this.mCommonApi = commonApi;
    }

    @Override
    public void dictionaryQuery() {
        disposables.add(mCommonApi.dictionaryQuery(4)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<DictionaryEntity>>, ObservableSource<List<DictionaryEntity>>>() {
                    @Override
                    public ObservableSource<List<DictionaryEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<DictionaryEntity>> listHttpResult) throws Exception {
                        return mCommonApi.flatResponse(listHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DictionaryEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<DictionaryEntity> dictionaryEntities) throws Exception {
                        if (dictionaryEntities != null && dictionaryEntities.size() > 0) {
                            mView.dictionaryQuerySuccess(dictionaryEntities);
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
    public void cancelOrder(int orderId,int reasonType,int cancelType,
                            String remark,List<String> pathList) {
        mView.showLoading();
        disposables.add(mCommonApi.orderCancel(orderId,reasonType,cancelType,remark,pathList)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> stringHttpResult) throws Exception {
                        return mCommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        ToastUtil.showToast(s);
                        mView.cancelOrderSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                }));
    }

    @Override
    public void attachView(@NonNull DriverReasonsContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
