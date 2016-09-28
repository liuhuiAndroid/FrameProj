package com.lh.frameproj.injector.module;

import android.app.Activity;

import com.lh.frameproj.injector.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }

}
