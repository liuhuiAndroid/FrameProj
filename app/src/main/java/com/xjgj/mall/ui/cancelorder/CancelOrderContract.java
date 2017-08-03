package com.xjgj.mall.ui.cancelorder;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/8/3.
 */

public class CancelOrderContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void orderCancelSuccess();

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<CancelOrderContract.View> {

        void orderCancel();

    }

}
