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

        void onLoadCompleted(boolean haMore);

        void onError();

        void onEmpty();

        void showContentUi(String tid, String pid, int page);

    }

    interface Presenter extends BasePresenter<View> {
        void onMessageListReceive();

        void onRefresh();

        void onReload();

        void onLoadMore();

        void onMessageClick(int position);
    }

}
