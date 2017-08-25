package com.xjgj.mall.ui.orderpay;

import com.xjgj.mall.bean.PayOrderEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by lh on 2017/8/25.
 */

public class OrderPayContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading(int type);

        void payOrderResult(PayOrderEntity payOrderEntity);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<OrderPayContract.View> {

        void payOrder(int orderId,double money);

    }

}