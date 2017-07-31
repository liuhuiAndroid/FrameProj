package com.xjgj.mall.ui.fragment3;

import com.xjgj.mall.bean.HomepageEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/7/31.
 */

public class Fragment3Contract {

    interface  View extends BaseView {
        void onLoadHomepageInfoCompleted(HomepageEntity homepageEntity);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<Fragment3Contract.View> {
        void onLoadHomepageInfo();
    }
}
