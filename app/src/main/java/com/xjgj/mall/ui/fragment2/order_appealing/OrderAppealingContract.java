package com.xjgj.mall.ui.fragment2.order_appealing;

import com.xjgj.mall.bean.OrderEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/7/20.
 */

public class OrderAppealingContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void renderOrderList(List<OrderEntity> orders);

        void onRefreshCompleted();

        void onLoadCompleted(boolean isLoadAll);

        void onEmpty();

        void onError(Throwable throwable);

        //刷新页面
        void onRefresh();

    }

    interface Presenter extends BasePresenter<View> {

        void onOrderWaitingAcceptListReceive();

        void onRefresh();

        void onLoadMore();

        void onOrderClick(int position);

        void orderCancel(int orderId);

    }

}
