package com.lh.frameproj.ui.comfirmorder;

import com.lh.frameproj.ui.BasePresenter;
import com.lh.frameproj.ui.BaseView;

/**
 * Created by we-win on 2017/7/21.
 */

public interface ComfirmOrderContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void submitSuccess();
    }

    interface Presenter extends BasePresenter<View> {

        void orderSubmit(String serviceTime, String volume, String weight, String serviceType,
                         String carType, String remark, String counts, String address, String submitType);

    }

}
