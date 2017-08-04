package com.xjgj.mall.ui.certification;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.RealNameEntity;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/8/1.
 */

public class CertificationPresenter implements CertificationContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;

    private CertificationContract.View mView;

    @Inject
    public CertificationPresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void authRealName(String realName, String identityNo, String frontIdentity, String afterIdentity) {
        if(TextUtils.isEmpty(realName)){
            ToastUtil.showToast("请填写真实姓名");
            return;
        }
        if(TextUtils.isEmpty(identityNo)){
            ToastUtil.showToast("请填写身份证号");
            return;
        }
        if (!identityNo.matches("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$")) {
            ToastUtil.showToast("身份证号格式错误");
            return;
        }
        if(frontIdentity == null || frontIdentity.equals("")){
            ToastUtil.showToast("请选择身份证正面");
            return;
        }
        if(afterIdentity == null || afterIdentity.equals("")){
            ToastUtil.showToast("请选择身份证反面");
            return;
        }
        mView.showLoading();
        disposables.add(mCommonApi.authRealName(realName,identityNo,new File(frontIdentity),new File(afterIdentity))
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> stringHttpResult) throws Exception {
                        return CommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        mView.authRealNameSuccess(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void realNameQuery() {
        disposables.add(mCommonApi.realNameQuery()
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<RealNameEntity>, ObservableSource<RealNameEntity>>() {
                    @Override
                    public ObservableSource<RealNameEntity> apply(@io.reactivex.annotations.NonNull HttpResult<RealNameEntity> RealNameEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(RealNameEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RealNameEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull RealNameEntity realNameEntity) throws Exception {
                        if (realNameEntity != null) {
                            mView.realNameQuerySuccess(realNameEntity);
                        } else {

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
    public void attachView(@NonNull CertificationContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }
}
