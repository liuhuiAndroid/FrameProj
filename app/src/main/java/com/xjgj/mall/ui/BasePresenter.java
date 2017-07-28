package com.xjgj.mall.ui;

import android.support.annotation.NonNull;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface BasePresenter<T extends BaseView> {

    //绑定view，这个方法将会在activity中调用
    void attachView(@NonNull T view);

    //解绑
    void detachView();

}
