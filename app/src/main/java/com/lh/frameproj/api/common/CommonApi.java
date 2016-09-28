package com.lh.frameproj.api.common;

import com.lh.frameproj.Constants;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class CommonApi {

    private CommonService mCookieService;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();


    public CommonApi(OkHttpClient mOkHttpClient) {
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(gsonConverterFactory)
                        .client(mOkHttpClient)
                        .baseUrl(Constants.BASE_URL)
                        .addCallAdapterFactory(rxJavaCallAdapterFactory)
                        .build();
        mCookieService = retrofit.create(CommonService.class);
    }

    public CommonService getCookieService() {
        return mCookieService;
    }

    //    public Observable<Void> messageGetAllNew() {
//        return mCookieService.messageGetAllNew().subscribeOn(Schedulers.io());
//    }

}
