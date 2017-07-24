package com.lh.frameproj;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.log.CrashlyticsTree;
import com.android.frameproj.library.util.log.Logger;
import com.android.frameproj.library.util.log.Settings;
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
        ToastUtil.register(this);

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
        //        if (LeakCanary.isInAnalyzerProcess(this)) {
        //            // This process is dedicated to LeakCanary for heap analysis.
        //            // You should not init your app in this process.
        //            return;
        //        }
        //        LeakCanary.install(this);
    }

    /**
     * 需要保证ApplicationComponent只有一个实例
     */
    private void initComponent() {
        mApplicationComponent =
                DaggerApplicationComponent.builder()
                        .applicationModule(new ApplicationModule(this)) // 如果Module只有有参构造器，则必须显式传入Module实例。
                        .build();
        mApplicationComponent.inject(this);//现在没有需要在MyApplication注入的对象，所以这句代码可写可不写
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getContext() {
        return mContext;
    }
}
