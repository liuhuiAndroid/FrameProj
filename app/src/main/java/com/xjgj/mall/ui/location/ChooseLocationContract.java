package com.xjgj.mall.ui.location;

import com.xjgj.mall.bean.GeoCoderResultEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/7/25.
 */

public class ChooseLocationContract {
    interface View extends BaseView {

        void geocoderResultSuccess(List<GeoCoderResultEntity.ResultBean.PoisBean> poisBeanList);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<ChooseLocationContract.View> {

        void geocoderApi(String latLng);

    }
}
