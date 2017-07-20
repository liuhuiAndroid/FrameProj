package com.lh.frameproj.ui.main;


import android.support.v4.app.Fragment;

import com.lh.frameproj.ui.BasePresenter;
import com.lh.frameproj.ui.BaseView;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface MainContract {

    interface View extends BaseView{

        void setTitle(String title);

        void showFragment(Fragment fragment);
    }

    interface Presenter extends BasePresenter<View>{

        void onTabClick(int position);

    }

}
