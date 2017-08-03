package com.xjgj.mall.ui.fragment4;

import com.xjgj.mall.bean.CarTypeEntity;
import com.xjgj.mall.bean.GeoCoderResultEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/7/25.
 */

public class Fragment4Contract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void renderCarList(List<CarTypeEntity> orders);

        void onError(Throwable throwable);

        void geocoderResultSuccess(List<GeoCoderResultEntity.ResultBean.PoisBean> poisBeanList);
    }

    interface Presenter extends BasePresenter<Fragment4Contract.View> {

        void onCarListReceive();

        void geocoderApi(String latLng);

    }

}
