package com.lh.frameproj.injector.module;

import android.content.Context;

import com.lh.frameproj.components.okhttp.AddCookiesInterceptor;
import com.lh.frameproj.components.okhttp.ReceivedCookiesInterceptor;
import com.lh.frameproj.util.log.Logger;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@Module
public class ApplicationModule {
    private static final String TAG = "ApplicationModule";

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    /**
     * 提供context
     * @return
     */
    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return context.getApplicationContext();
    }

    @Provides @Singleton public Bus provideBusEvent() {
        return new Bus();
    }

    /**
     * 获取OkHttpClient
     * @return
     */
    @Provides @Singleton @Named("api")
    OkHttpClient provideApiOkHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logger.i(message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(logging);
        builder.addInterceptor(new AddCookiesInterceptor(context));
        builder.addInterceptor(new ReceivedCookiesInterceptor(context));
        return builder.build();
    }
}
