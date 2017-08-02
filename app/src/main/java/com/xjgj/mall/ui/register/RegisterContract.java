package com.xjgj.mall.ui.register;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/7/31.
 */

public class RegisterContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void showError(String error);

        void registerSuccess();

        void onError(Throwable throwable);

        void refreshSmsCodeUi();
    }

    interface Presenter extends BasePresenter<RegisterContract.View> {

        void register(String mobile, String realName,String password,String smsCode);

        void smsCodeSend(String mobile, int type);

    }

}
