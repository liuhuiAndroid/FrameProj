package com.lh.frameproj.ui.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.ToastUtil;
import com.lh.frameproj.api.common.CommonApi;
import com.lh.frameproj.bean.HttpResult;
import com.lh.frameproj.bean.LoginEntity;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/7/21.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;

    private LoginContract.View mLoginView;

    @Inject
    public LoginPresenter(CommonApi commonApi, Bus bus) {
        mCommonApi = commonApi;
        mBus = bus;
    }

    @Override
    public void login(String userName, String password) {
        if (TextUtils.isEmpty(userName)) {
            mLoginView.showUserNameError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mLoginView.showPassWordError("请输入密码");
            return;
        }
        mLoginView.showLoading();
        disposables.add(mCommonApi.mallLogin(userName,password)
                .debounce(800, TimeUnit.MILLISECONDS)
                .map(new Function<HttpResult<LoginEntity>, LoginEntity>() {
                    @Override
                    public LoginEntity apply(@io.reactivex.annotations.NonNull HttpResult<LoginEntity> stringHttpResult) throws Exception {
                        return stringHttpResult.getResultValue();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mLoginView.hideLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull LoginEntity loginEntity) throws Exception {
//                        if (loginEntity != null) {
                            mLoginView.loginSuccess();
//                        } else {
//                            ToastUtil.showToast("登录失败，请检查您的网络");
//                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        mLoginView.loginSuccess();
                        ToastUtil.showToast("登录失败，请检查您的网络");
                    }
                }));

    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
        mLoginView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mLoginView = null;
    }


}
