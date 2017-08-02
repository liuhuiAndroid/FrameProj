package com.xjgj.mall.ui.orderevaluate;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/8/2.
 */

public class OrderEvaluateContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void orderCommentSuccess();

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<OrderEvaluateContract.View> {

        void orderComment(int orderId,String level,String content);

    }

}
