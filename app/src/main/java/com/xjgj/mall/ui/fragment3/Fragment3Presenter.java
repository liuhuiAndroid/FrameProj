package com.xjgj.mall.ui.fragment3;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HomepageEntity;
import com.xjgj.mall.bean.HttpResult;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/7/31.
 */

public class Fragment3Presenter implements Fragment3Contract.Presenter{

    private final CompositeDisposable disposables = new CompositeDisposable();
    private Fragment3Contract.View mView;
    private CommonApi mCommonApi;

    @Inject
    public Fragment3Presenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void onLoadHomepageInfo() {
        disposables.add(mCommonApi.mallHomepage()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<HomepageEntity>, ObservableSource<HomepageEntity>>() {
                    @Override
                    public ObservableSource<HomepageEntity> apply(@io.reactivex.annotations.NonNull HttpResult<HomepageEntity> homepageEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(homepageEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HomepageEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull HomepageEntity homepageEntity) throws Exception {
                        if (homepageEntity != null) {
                            mView.onLoadHomepageInfoCompleted(homepageEntity);
                        } else {
                            ToastUtil.showToast("没有查询到信息");
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
    public void attachView(@NonNull Fragment3Contract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }
}
