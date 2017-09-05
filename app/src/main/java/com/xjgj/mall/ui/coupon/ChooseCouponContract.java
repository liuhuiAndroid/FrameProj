package com.xjgj.mall.ui.coupon;

import com.xjgj.mall.bean.CouponEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by lh on 2017/9/5.
 */

public class ChooseCouponContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading(int type);

        void couponListSuccess(List<CouponEntity> couponEntities);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<ChooseCouponContract.View> {

        void couponList();

    }

}
