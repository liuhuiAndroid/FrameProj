package com.lh.frameproj.api.common;

import com.lh.frameproj.Constants;
import com.lh.frameproj.bean.AccountVersionEntity;
import com.lh.frameproj.bean.HttpResult;
import com.lh.frameproj.bean.LoginEntity;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${实现接口的调用}
 */
public class CommonApi {

    private CommonService mCommonService;

    public CommonApi(OkHttpClient mOkHttpClient) {
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .client(mOkHttpClient)
                        .baseUrl(Constants.BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
        mCommonService = retrofit.create(CommonService.class);
    }

    /**
     * 版本更新
     */
    public Observable<HttpResult<AccountVersionEntity>> accountVersion() {
        return mCommonService.accountVersion().subscribeOn(Schedulers.io());
    }

    /**
     * 登录
     */
    public Observable<HttpResult<LoginEntity>> loginSms() {
        return mCommonService.loginSms(null,null,null,null,null,null).subscribeOn(Schedulers.io());
    }

    /**
     * 登录
     */
    public Observable<ResponseBody> geocoderApi(String latLng) {
        // ak:百度地图api key
        return mCommonService.geocoderApi("KLOSK99izO93bGjmOKnCxScVy0AOhkGB","renderReverse",latLng,"json",1,"CC:DE:0D:85:1D:4A:71:BF:9B:E3:53:F4:7F:37:4D:B3:72:DF:07:D7;com.lh.frameproj").subscribeOn(Schedulers.io());
    }

}
