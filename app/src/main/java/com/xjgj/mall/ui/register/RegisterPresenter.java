package com.xjgj.mall.ui.register;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.frameproj.library.util.PhoneUtil;
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
 * Created by we-win on 2017/7/31.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;
    private Bus mBus;
    private UserStorage mUserStorage;

    private RegisterContract.View mRegisterView;

    @Inject
    public RegisterPresenter(CommonApi commonApi, Bus bus, UserStorage userStorage) {
        mCommonApi = commonApi;
        mBus = bus;
        mUserStorage = userStorage;
    }

    @Override
    public void register(String mobile, String realName, String password, String smsCode) {
        if (TextUtils.isEmpty(mobile)) {
            mRegisterView.showError("请输入手机号");
            return;
        }
        if (!PhoneUtil.isMobile(mobile)) {
            mRegisterView.showError("手机号码格式不正确");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mRegisterView.showError("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(realName)) {
            mRegisterView.showError("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            mRegisterView.showError("请输入验证码");
            return;
        }
        mRegisterView.showLoading();
        disposables.add(mCommonApi.mallRegister(mobile,realName,password,smsCode)
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
                        mRegisterView.hideLoading();
                    }
                }).subscribe(new Consumer<User>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull User user) throws Exception {
                        if (user != null) {
                            mRegisterView.registerSuccess();
                            mUserStorage.setUser(user);
                        } else {
                            ToastUtil.showToast("注册失败，请检查您的网络");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mRegisterView.onError(throwable);
                    }
                }));
    }

    @Override
    public void smsCodeSend(String mobile, int type) {
        if (TextUtils.isEmpty(mobile)) {
            mRegisterView.showError("请输入手机号");
            return;
        }
        if (!PhoneUtil.isMobile(mobile)) {
            mRegisterView.showError("手机号码格式不正确");
            return;
        }
        mRegisterView.refreshSmsCodeUi();
        disposables.add(mCommonApi.smsCodeSend(mobile,type)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> loginEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(loginEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        ToastUtil.showToast(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mRegisterView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull RegisterContract.View view) {
        mRegisterView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mRegisterView = null;
    }

}
