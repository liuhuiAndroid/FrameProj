package com.xjgj.mall;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.android.frameproj.library.util.ToastUtil;
import com.android.frameproj.library.util.log.CrashlyticsTree;
import com.android.frameproj.library.util.log.Logger;
import com.android.frameproj.library.util.log.Settings;
import com.baidu.mapapi.SDKInitializer;
import com.xjgj.mall.injector.component.ApplicationComponent;
import com.xjgj.mall.injector.component.DaggerApplicationComponent;
import com.xjgj.mall.injector.module.ApplicationModule;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class MyApplication extends MultiDexApplication {

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

        //初始化百度地图SDK
        SDKInitializer.initialize(getApplicationContext());

        //初始化JPush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
