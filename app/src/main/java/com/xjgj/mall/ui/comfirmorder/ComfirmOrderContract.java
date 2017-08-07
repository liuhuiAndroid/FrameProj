package com.xjgj.mall.ui.comfirmorder;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/7/21.
 */

public interface ComfirmOrderContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void submitSuccess();

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {

        void orderSubmit(String serviceTime, String volume, String weight, String serviceType,
                         String carType, String remark, String counts, String address,
                         String submitType,int flgTogether);

    }

}
