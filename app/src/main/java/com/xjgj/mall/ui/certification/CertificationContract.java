package com.xjgj.mall.ui.certification;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/8/1.
 */

public class CertificationContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void authRealNameSuccess(String string);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<CertificationContract.View> {

        void authRealName(String realName, String identityNo, String frontIdentity, String afterIdentity);

    }

}
