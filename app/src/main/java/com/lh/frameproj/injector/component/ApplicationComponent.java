package com.lh.frameproj.injector.component;

import android.content.Context;

import com.lh.frameproj.MyApplication;
import com.lh.frameproj.api.common.CommonApi;
import com.lh.frameproj.components.okhttp.OkHttpHelper;
import com.lh.frameproj.db.TestDao;
import com.lh.frameproj.injector.module.ApiModule;
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
 * 全局的ApplicationComponent,负责管理整个app的全局类实例，并且它们的生命周期与app的生命周期一样。
 * 若ApplicationComponent和Module的Scope是不一样的，则在编译时报错。
 */
@Singleton
@Component(modules = {ApplicationModule.class,ApiModule.class, DBModule.class})
public interface ApplicationComponent {

    // 被依赖时候，必须显示地把这些A找不到的依赖提供给A，需要添加如下方法

    Context getContext();

    Bus getBus();

    CommonApi getCommonApi();

    TestDao getTestDao();

    // 表示我要提供给谁
    void inject(MyApplication mApplication);

    void inject(BaseActivity mBaseActivity);

    OkHttpHelper getOkHttpHelper();

}
