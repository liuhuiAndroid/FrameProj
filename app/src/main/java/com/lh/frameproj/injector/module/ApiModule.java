package com.lh.frameproj.injector.module;

import com.lh.frameproj.api.common.CommonApi;
import com.lh.frameproj.components.retrofit.RequestHelper;
import com.lh.frameproj.injector.PerApp;

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
    public CommonApi providesCookieApi(@Named("api") OkHttpClient okHttpClient,RequestHelper requestHelper) {
        return new CommonApi(okHttpClient,requestHelper);
    }

}
