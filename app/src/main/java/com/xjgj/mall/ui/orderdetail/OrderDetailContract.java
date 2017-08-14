package com.xjgj.mall.ui.orderdetail;

import com.xjgj.mall.bean.OrderDetailEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/8/1.
 */

public class OrderDetailContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading(int type);

        void orderDetailResult(OrderDetailEntity orderDetailEntity);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<OrderDetailContract.View> {

        void orderDetail(int orderId);

    }

}
