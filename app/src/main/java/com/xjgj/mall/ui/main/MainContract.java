package com.xjgj.mall.ui.main;


import android.support.v4.app.Fragment;

import com.xjgj.mall.bean.DictionaryEntity;
import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface MainContract {

    interface View extends BaseView{

        void setTitle(int position,String title);

        void addFragment(Fragment fragment);

        void showFragment(Fragment fragment);

        void hideFragment(Fragment fragment);

        void dictionaryQuerySuccess(List<DictionaryEntity> dictionaryEntitieList);

        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View>{

        void onTabClick(int position);

        void initFragment();

        void dictionaryQuery();
    }

}
