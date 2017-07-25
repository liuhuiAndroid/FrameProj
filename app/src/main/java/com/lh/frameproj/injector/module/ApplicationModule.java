package com.lh.frameproj.injector.module;

import android.content.Context;

import com.android.frameproj.library.util.log.Logger;
import com.lh.frameproj.components.okhttp.OkHttpHelper;
import com.lh.frameproj.components.retrofit.RequestHelper;
import com.lh.frameproj.components.retrofit.UserStorage;
import com.lh.frameproj.injector.PerApp;
import com.lh.frameproj.util.SPUtil;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 * singleton是application级别的 我们要放在application里面去初始化
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
    @PerApp
    public Context provideApplicationContext() {
        return context.getApplicationContext();
    }

    /**
     * Provides最终解决第三方类库依赖注入问题
     * Module中的创建类实例方法用Provides进行标注，Component在搜索到目标类中用Inject注解标注的属性后，
     * Component就会去Module中去查找用Provides标注的对应的创建类实例方法，
     * 这样就可以解决第三方类库用dagger2实现依赖注入了
     * @return
     */
    @Provides @PerApp public Bus provideBusEvent() {
        return new Bus();
    }

    /**
     * 获取OkHttpClient
     * @return
     */
    @Provides @PerApp
    @Named("api") // 区分返回类型相同的@Provides 方法
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
        return builder.build();
    }

    @Provides
    @PerApp
    OkHttpClient provideOkHttpClient(@Named("api") OkHttpClient mOkHttpClient) {
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        builder.interceptors().clear();
        return builder.build();
    }


    /**
     * Module中@Provides方法可以带输入参数，其参数由Module集合中的其他@Provides方法提供，或者自动调用构造方法
     * @param mOkHttpClient
     * @return
     */
    @Provides
    @PerApp
    OkHttpHelper provideOkHttpHelper(OkHttpClient mOkHttpClient) {
        return new OkHttpHelper(mOkHttpClient);
    }


    @Provides
    @PerApp
    SPUtil provideSPUtil(Context context) {
        return new SPUtil(context);
    }


    @Provides
    @PerApp
    RequestHelper provideRequestHelper(Context mContext,
                                       UserStorage mUserStorage) {
        return new RequestHelper(mContext, mUserStorage);
    }

    @Provides
    @PerApp
    UserStorage provideUserStorage(Context mContext) {
        return new UserStorage(mContext);
    }


}
