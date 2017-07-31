package com.xjgj.mall.ui.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.ToastUtil;
import com.squareup.otto.Bus;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.LoginEntity;
import com.xjgj.mall.bean.User;
import com.xjgj.mall.components.storage.UserStorage;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
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
    private UserStorage mUserStorage;

    private LoginContract.View mLoginView;

    @Inject
    public LoginPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
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
        disposables.add(mCommonApi.mallLogin(userName, password)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<LoginEntity>, ObservableSource<LoginEntity>>() {
                    @Override
                    public ObservableSource<LoginEntity> apply(@io.reactivex.annotations.NonNull HttpResult<LoginEntity> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<LoginEntity, Observable<HttpResult<User>>>() {
                    @Override
                    public Observable<HttpResult<User>> apply(@io.reactivex.annotations.NonNull LoginEntity loginEntity) throws Exception {
                        //保存token
                        mUserStorage.setToken(loginEntity.getToken());
                        return mCommonApi.mallInformation();
                    }
                }).flatMap(new Function<HttpResult<User>, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(@io.reactivex.annotations.NonNull HttpResult<User> userHttpResult) throws Exception {
                        return CommonApi.flatResponse(userHttpResult);
                    }
                }).doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mLoginView.hideLoading();
                    }
                }).subscribe(new Consumer<User>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull User user) throws Exception {
                        if (user != null) {
                            mLoginView.loginSuccess();
                            mUserStorage.setUser(user);
                        } else {
                            ToastUtil.showToast("登录失败，请检查您的网络");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mLoginView.onError(throwable);
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
