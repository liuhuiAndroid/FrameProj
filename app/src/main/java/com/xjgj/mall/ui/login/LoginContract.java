package com.xjgj.mall.ui.login;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/7/21.
 */

public interface LoginContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void showUserNameError(String error);

        void showPassWordError(String error);

        void loginSuccess();

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {

        void login(String userName, String identifyingCode);

    }

}
