package com.xjgj.mall.ui.personalprofile;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.User;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lh on 2017/8/9.
 */

public class PersonalProfilePresenter implements PersonalProfileContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;

    private PersonalProfileContract.View mView;

    @Inject
    public PersonalProfilePresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }


    @Override
    public void mallInfoComplete(String realName, int sex, String address, String companyName,
                                 String berth, String headIcon, String birthDay) {
        mView.showLoading();
        disposables.add(mCommonApi.mallInfoComplete(realName, sex, address, companyName, berth,
                headIcon, birthDay)
                .flatMap(new Function<HttpResult<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@io.reactivex.annotations.NonNull HttpResult<String> stringHttpResult) throws Exception {
                        return CommonApi.flatResponse(stringHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        mView.hideLoading();
                        mView.mallInfoCompleteSuccess(s);
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
    public void mallInformation() {
        mView.showLoadingContent();
        disposables.add(mCommonApi.mallInformation()
                .flatMap(new Function<HttpResult<User>, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(@io.reactivex.annotations.NonNull HttpResult<User> userHttpResult) throws Exception {
                        return CommonApi.flatResponse(userHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull User user) throws Exception {
                        if (user != null) {
                            mView.mallInformationSuccess(user);
                            mView.hideLoadingContent(0);
                        } else {
                            ToastUtil.showToast("获取用户失败，请检查您的网络");
                            mView.hideLoadingContent(-1);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoadingContent(-1);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull PersonalProfileContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }
}
