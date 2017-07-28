package com.xjgj.mall.injector.module;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.android.frameproj.library.util.log.Logger;
import com.squareup.otto.Bus;
import com.xjgj.mall.Constants;
import com.xjgj.mall.components.okhttp.OkHttpHelper;
import com.xjgj.mall.components.retrofit.RequestHelper;
import com.xjgj.mall.components.retrofit.UserStorage;
import com.xjgj.mall.injector.PerApp;
import com.xjgj.mall.service.LocationService;
import com.xjgj.mall.util.SPUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static android.provider.MediaStore.getVersion;

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
     *
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
     *
     * @return
     */
    @Provides
    @PerApp
    public Bus provideBusEvent() {
        return new Bus();
    }

    /**
     * 获取OkHttpClient
     *
     * @return
     */
    @Provides
    @PerApp
    @Named("api")
    // 区分返回类型相同的@Provides 方法
    OkHttpClient provideApiOkHttpClient(final Context context) {
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
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder1 = request.newBuilder();
                        builder1.addHeader("key", Constants.app_key);
                        builder1.addHeader("app_type", "2");
                        builder1.addHeader("OS_type", "os_type");
                        String deviceId = ((TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE))).getDeviceId();
                        builder1.addHeader("device_id", deviceId);
                        builder1.addHeader("app_version", getVersion(context));
                        Request build = builder1.build();
                        return chain.proceed(build);
                    }
                });
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
     *
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
    RequestHelper provideRequestHelper(Context mContext, UserStorage mUserStorage) {
        return new RequestHelper(mContext, mUserStorage);
    }

    @Provides
    @PerApp
    UserStorage provideUserStorage(Context mContext) {
        return new UserStorage(mContext);
    }

    @Provides
    @PerApp
    LocationService provideLocationService(Context mContext) {
        return new LocationService(mContext);
    }


}
