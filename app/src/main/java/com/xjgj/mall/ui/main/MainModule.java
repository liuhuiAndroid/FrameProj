package com.xjgj.mall.ui.main;

import dagger.Module;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@Module
public class MainModule {

    private MainActivity mActivity;

    public MainModule(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

}
