package com.lh.frameproj.ui.splash;

import com.lh.frameproj.ui.BasePresenter;
import com.lh.frameproj.ui.BaseView;

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
