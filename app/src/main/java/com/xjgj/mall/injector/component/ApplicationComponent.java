package com.xjgj.mall.injector.component;

import android.content.Context;

import com.squareup.otto.Bus;
import com.xjgj.mall.MyApplication;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.components.okhttp.OkHttpHelper;
import com.xjgj.mall.components.retrofit.UserStorage;
import com.xjgj.mall.db.TestDao;
import com.xjgj.mall.injector.PerApp;
import com.xjgj.mall.injector.module.ApiModule;
import com.xjgj.mall.injector.module.ApplicationModule;
import com.xjgj.mall.injector.module.DBModule;
import com.xjgj.mall.service.LocationService;
import com.xjgj.mall.ui.BaseActivity;

import dagger.Component;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 * 全局的ApplicationComponent,负责管理整个app的全局类实例，并且它们的生命周期与app的生命周期一样。
 * 若ApplicationComponent和Module的Scope是不一样的，则在编译时报错。
 *
 * 一个Component可以包含多个Module，这样Component获取依赖时候会自动从多个Module中查找获取，Module间不能有重复方法
 */
@PerApp
@Component(modules = {ApplicationModule.class,ApiModule.class, DBModule.class}) //3 指明Component在哪些Module中查找依赖
public interface ApplicationComponent { //4 接口，自动生成实现

    // 被依赖时候，必须显示地把这些A找不到的依赖提供给A，需要添加如下方法
    // Component的方法可以没有输入参数，但是就必须有返回值
    Context getContext();

    Bus getBus();

    CommonApi getCommonApi();

    TestDao getTestDao();

    // 表示我要提供给谁
    // 有输入参数返回值类型就是void
    void inject(MyApplication mApplication);  //5  注入方法，在Container中调用

    void inject(BaseActivity mBaseActivity);

    OkHttpHelper getOkHttpHelper();

    LocationService getLocationService();

    UserStorage getUserStorage();

}
