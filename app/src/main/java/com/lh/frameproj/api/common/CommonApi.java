package com.lh.frameproj.api.common;

import com.lh.frameproj.Constants;
import com.lh.frameproj.bean.AccountVersionEntity;
import com.lh.frameproj.bean.HttpResult;
import com.lh.frameproj.bean.LoginEntity;
import com.lh.frameproj.components.retrofit.RequestHelper;

import java.util.HashMap;
import java.util.Map;

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
    private RequestHelper mRequestHelper;

    public CommonApi(OkHttpClient mOkHttpClient,RequestHelper requestHelper) {
        this.mRequestHelper = requestHelper;
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
    public Observable<HttpResult<LoginEntity>> mallLogin(String username,String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params.put("appType",Constants.APPTYPE);
        long currentTimeMillis = System.currentTimeMillis();
        String sign = mRequestHelper.getRequestSign(params,currentTimeMillis);
        return mCommonService.mallLogin(currentTimeMillis,sign,params).subscribeOn(Schedulers.io());
    }

    /**
     * 提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
     */
    public Observable<ResponseBody> geocoderApi(String latLng) {
        Map<String, Object> params = new HashMap<>();
        // ak:百度地图api key
        params.put("ak", "KLOSK99izO93bGjmOKnCxScVy0AOhkGB");
        params.put("callback","renderReverse");
        params.put("location",latLng);
        params.put("output","json");
        params.put("pois","1");
        params.put("mcode","CC:DE:0D:85:1D:4A:71:BF:9B:E3:53:F4:7F:37:4D:B3:72:DF:07:D7;com.lh.frameproj");
        return mCommonService.geocoderApi(params).subscribeOn(Schedulers.io());
    }

}
