package com.xjgj.mall.ui.personalprofile;

import com.xjgj.mall.bean.User;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by lh on 2017/8/9.
 */

public class PersonalProfileContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void showLoadingContent();

        void hideLoadingContent(int type);

        void mallInfoCompleteSuccess(String s);

        void mallInformationSuccess(User user);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<PersonalProfileContract.View> {

        void mallInfoComplete(String realName, int sex, String address, String companyName,
                              String berth, String headIcon, String birthDay);

        void mallInformation();

    }

}
