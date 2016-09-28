package com.lh.frameproj.ui.fragment1;

import com.lh.frameproj.ui.BasePresenter;
import com.lh.frameproj.ui.BaseView;

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
