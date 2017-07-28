package com.xjgj.mall.ui.location;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/7/25.
 */

public class ChooseLocationContract {
    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void geocoderResultSuccess(String geoCoderResultEntity);
    }

    interface Presenter extends BasePresenter<ChooseLocationContract.View> {

        void geocoderApi(String latLng);

    }
}
