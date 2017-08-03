package com.xjgj.mall.ui.cancelorder.my_reasons;

import com.xjgj.mall.bean.DictionaryEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/8/3.
 */

public class MyReasonsContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void dictionaryQuerySuccess(List<DictionaryEntity> dictionaryEntitieList);

        void cancelOrderSuccess();

        void onError(Throwable throwable);

    }

    interface Presenter extends BasePresenter<MyReasonsContract.View> {

        void dictionaryQuery();

        void cancelOrder(int orderId,int reasonType,int cancelType,
                         String remark,List<String> pathList);

    }

}
