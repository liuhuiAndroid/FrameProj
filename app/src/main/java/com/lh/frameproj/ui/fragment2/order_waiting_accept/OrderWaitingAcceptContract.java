package com.lh.frameproj.ui.fragment2.order_waiting_accept;

import com.lh.frameproj.ui.BasePresenter;
import com.lh.frameproj.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/7/20.
 */

public class OrderWaitingAcceptContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void renderOrderList(List<String> orders);

        void onRefreshCompleted();

        void onLoadCompleted(boolean isLoadAll);

        void onError();

        void onEmpty();

    }

    interface Presenter extends BasePresenter<View> {

        void onOrderWaitingAcceptListReceive();

        void onRefresh();

        void onLoadMore();

        void onOrderClick(int position);

    }

}
