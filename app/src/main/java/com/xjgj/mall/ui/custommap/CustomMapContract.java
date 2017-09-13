package com.xjgj.mall.ui.custommap;

import com.xjgj.mall.bean.AddressEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/7/25.
 */

public class CustomMapContract {
    interface View extends BaseView {

        void addressListResultSuccess(List<AddressEntity> addressEntities);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<CustomMapContract.View> {

        void addressList(String latLng);

    }
}
