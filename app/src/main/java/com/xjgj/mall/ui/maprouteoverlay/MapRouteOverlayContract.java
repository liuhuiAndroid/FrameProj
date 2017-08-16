package com.xjgj.mall.ui.maprouteoverlay;

import com.xjgj.mall.bean.CarAddressEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by lh on 2017/8/16.
 */

public class MapRouteOverlayContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading(int type);

        void carAddressResult(CarAddressEntity carAddressEntity);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<MapRouteOverlayContract.View> {

        void carAddress(int orderId, String collectTime, boolean isFirst);

    }

}
