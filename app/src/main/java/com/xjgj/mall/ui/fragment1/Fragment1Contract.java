package com.xjgj.mall.ui.fragment1;

import com.xjgj.mall.ui.BasePresenter;
import com.xjgj.mall.ui.BaseView;

import java.util.List;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface Fragment1Contract {

    interface  View extends BaseView{

        void onRefreshCompleted(List<String> data);

    }

    interface Presenter extends BasePresenter<View>{
        void onThreadReceive();

        void onRefresh();

        void onReload();
    }

}
