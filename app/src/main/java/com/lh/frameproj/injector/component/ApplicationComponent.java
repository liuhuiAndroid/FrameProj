package com.lh.frameproj.injector.component;

import android.content.Context;

import com.lh.frameproj.MyApplication;
import com.lh.frameproj.components.okhttp.OkHttpHelper;
import com.lh.frameproj.db.TestDao;
import com.lh.frameproj.injector.module.ApplicationModule;
import com.lh.frameproj.injector.module.DBModule;
import com.lh.frameproj.ui.BaseActivity;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@Singleton
@Component(modules = {ApplicationModule.class, DBModule.class})
public interface ApplicationComponent {

    // 被依赖时候，必须显示地把这些A找不到的依赖提供给A，需要添加如下方法

    Context getContext();

    Bus getBus();

    TestDao getTestDao();

    void inject(MyApplication mApplication);

    void inject(BaseActivity mBaseActivity);

    OkHttpHelper getOkHttpHelper();

}
