package com.lh.frameproj.ui;

import android.support.annotation.NonNull;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface BasePresenter<T extends BaseView> {

    void attachView(@NonNull T view);

    void detachView();

}
