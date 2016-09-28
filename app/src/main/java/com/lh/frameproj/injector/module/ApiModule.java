package com.lh.frameproj.injector.module;

import com.lh.frameproj.api.common.CommonApi;

import javax.inject.Named;
import javax.inject.Singleton;

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
    @Singleton
    public CommonApi providesCookieApi(@Named("api") OkHttpClient okHttpClient) {
        return new CommonApi(okHttpClient);
    }

}
