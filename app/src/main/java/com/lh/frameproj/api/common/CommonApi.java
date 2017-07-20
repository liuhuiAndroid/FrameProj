package com.lh.frameproj.api.common;

import com.lh.frameproj.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class CommonApi {

    private CommonService mCookieService;

    public CommonApi(OkHttpClient mOkHttpClient) {
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .client(mOkHttpClient)
                        .baseUrl(Constants.BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
