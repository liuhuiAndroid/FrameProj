package com.lh.frameproj.ui.splash;

import dagger.Module;

/**
 * Created by we-win on 2017/7/20.
 * 可以把封装第三方类库的代码放入Module中
 * Module其实是一个简单工厂模式，Module里面的方法基本都是创建类实例的方法
 */
@Module
public class SplashModule {

    private SplashActivity mActivity;

    public SplashModule(SplashActivity activity) {
        mActivity = activity;
    }

}
