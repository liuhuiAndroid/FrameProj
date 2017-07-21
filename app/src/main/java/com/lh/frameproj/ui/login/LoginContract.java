package com.lh.frameproj.ui.login;

import com.lh.frameproj.ui.BasePresenter;
import com.lh.frameproj.ui.BaseView;

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
    }

    interface Presenter extends BasePresenter<View> {

        void login(String userName, String identifyingCode);

    }

}
