package com.xjgj.mall.ui.orderappeal;

import com.xjgj.mall.bean.DictionaryEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/8/3.
 */

public class OrderAppealContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void orderAppealSuccess();

        void dictionaryQuerySuccess(List<DictionaryEntity> dictionaryEntitieList);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<OrderAppealContract.View> {

        void orderAppeal(int orderId,int complainType,String content,List<String> pathList);

        void dictionaryQuery();

    }

}
