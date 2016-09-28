package com.lh.frameproj;

import android.app.Application;
import android.content.Context;

import com.lh.frameproj.injector.component.ApplicationComponent;
import com.lh.frameproj.injector.component.DaggerApplicationComponent;
import com.lh.frameproj.injector.module.ApplicationModule;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class MyApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
        mContext = getApplicationContext();
        new AppError().initUncaught();
    }

    private void initComponent() {
        mApplicationComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getContext() {
        return mContext;
    }
}
