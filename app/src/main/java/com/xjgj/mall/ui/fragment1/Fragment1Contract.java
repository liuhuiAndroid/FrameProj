package com.xjgj.mall.ui.fragment1;


import com.xjgj.mall.bean.OrderEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface Fragment1Contract {

    interface  View extends BaseView {

        void onRefreshCompleted(List<OrderEntity> orderListEntities);

        void onLoadCompleted(boolean isLoadAll);

        void onError(Throwable throwable);

        void onRefresh();

        void onEmpty(int currentType);

    }

    interface Presenter extends BasePresenter<View> {

        void onThreadReceive();

        void onRefresh(int type,int addrType);

        void onLoadMore();

        void orderConfirm(int orderId);


    }

}
