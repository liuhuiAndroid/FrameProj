package com.xjgj.mall.ui.orderpay;

import com.xjgj.mall.bean.CouponEntity;
import com.xjgj.mall.bean.PayAlipayEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by lh on 2017/8/25.
 */

public class OrderPayContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading(int type);

        void payOrderResult(PayAlipayEntity payAlipayEntity);

        void payConfirmResult(CouponEntity couponEntity);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<OrderPayContract.View> {

        void payOrder(int orderId, String money, int couponId, boolean b);

        void payConfirm(String outTradeNo);

    }

}
