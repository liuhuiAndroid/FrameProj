package com.lh.frameproj;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lh.frameproj.injector.component.ApplicationComponent;
import com.lh.frameproj.injector.component.DaggerApplicationComponent;
import com.lh.frameproj.injector.module.ApplicationModule;
import com.lh.frameproj.util.log.CrashlyticsTree;
import com.lh.frameproj.util.log.Logger;
import com.lh.frameproj.util.log.Settings;
import com.squareup.leakcanary.LeakCanary;

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

        // 初始化日志功能
        Logger.initialize(
                new Settings()
                        .isShowMethodLink(true)
                        .isShowThreadInfo(false)
                        .setMethodOffset(0)
                        .setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT)
        );
        if (!BuildConfig.DEBUG) {
            // for release
            Logger.plant(new CrashlyticsTree());
        }

        // 初始化LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
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
