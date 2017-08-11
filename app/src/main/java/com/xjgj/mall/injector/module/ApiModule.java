package com.xjgj.mall.injector.module;

import android.content.Context;

import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.components.retrofit.RequestHelper;
import com.xjgj.mall.components.storage.UserStorage;
import com.xjgj.mall.injector.PerApp;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@Module
public class ApiModule {

    @Provides
    @PerApp
    public CommonApi providesCookieApi(Context context, @Named("api") OkHttpClient okHttpClient, RequestHelper requestHelper,
                                       UserStorage userStorage) {
        return new CommonApi(context,okHttpClient,requestHelper,userStorage);
    }

}
