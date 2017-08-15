package com.xjgj.mall.ui.mapdriveraddress;

import com.xjgj.mall.bean.DriverAddressEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/8/15.
 */

public class MapDriverAddressContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading(int type);

        void driverAddressResult(List<DriverAddressEntity> driverAddressEntities);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<MapDriverAddressContract.View> {

        void driverAddress(double longtitude,double latitude);

    }

}
