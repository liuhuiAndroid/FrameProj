package com.xjgj.mall.ui.fragmentfindcar.on_site;

import com.xjgj.mall.bean.GeoCoderResultEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/8/14.
 */

public class FragmentFindCarOnSiteContract {

    interface View extends BaseView {

        void onError(Throwable throwable);

        void geocoderResultSuccess(List<GeoCoderResultEntity.ResultBean.PoisBean> poisBeanList);
    }

    interface Presenter extends BasePresenter<FragmentFindCarOnSiteContract.View> {

        void geocoderApi(String latLng);

    }

}
