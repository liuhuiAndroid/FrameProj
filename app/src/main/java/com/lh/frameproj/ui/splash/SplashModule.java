package com.lh.frameproj.ui.splash;

import dagger.Module;

/**
 * Created by we-win on 2017/7/20.
 */
@Module
public class SplashModule {

    private SplashActivity mActivity;

    public SplashModule(SplashActivity activity) {
        mActivity = activity;
    }

}
