package com.xjgj.mall.ui.businesslicence;

import com.xjgj.mall.bean.PhotoUploadEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

/**
 * Created by we-win on 2017/7/31.
 */

public class BusinessLicenceContract {

    interface View extends BaseView {
        void showLoading();

        void hideLoading();

        void photoUploadSuccess();

        void photoQuerySuccess(PhotoUploadEntity photoUploadEntity);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<BusinessLicenceContract.View> {

        void photoUpload(String photoPath,int type);

        void photoQuery();

    }

}
