package com.lh.frameproj.ui.fragment4;

import com.lh.frameproj.ui.BasePresenter;
import com.lh.frameproj.ui.BaseView;

import java.util.List;

/**
 * Created by we-win on 2017/7/25.
 */

public class Fragment4Contract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void renderCarList(List<String> orders);

        void onError();

    }

    interface Presenter extends BasePresenter<Fragment4Contract.View> {

        void onCarListReceive();

    }

}
