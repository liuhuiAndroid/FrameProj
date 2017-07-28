package com.xjgj.mall.ui.splash;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/7/20.
 */

public interface SplashContract {

    interface View extends BaseView{
        void showMainUi();
    }

    interface Presenter extends BasePresenter<View> {
        void initData();
    }

}
