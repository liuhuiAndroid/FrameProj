package com.xjgj.mall.api.common;

import com.xjgj.mall.Constants;
import com.xjgj.mall.bean.AccountVersionEntity;
import com.xjgj.mall.bean.CarTypeEntity;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.LoginEntity;
import com.xjgj.mall.bean.User;
import com.xjgj.mall.components.retrofit.RequestHelper;
import com.xjgj.mall.components.retrofit.UserStorage;

import java.util.HashMap;
import java.util.List;
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
    private UserStorage mUserStorage;

    public CommonApi(OkHttpClient mOkHttpClient, RequestHelper requestHelper, UserStorage userStorage) {
        this.mRequestHelper = requestHelper;
        mUserStorage = userStorage;
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
    public Observable<HttpResult<LoginEntity>> mallLogin(String username, String password) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("username", username);
        params.put("password", password);
        params.put("appType", Constants.APPTYPE);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.mallLogin(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
     */
    public Observable<ResponseBody> geocoderApi(String latLng) {
        Map<String, Object> params = new HashMap<>();
        // ak:百度地图api key
        params.put("ak", "KLOSK99izO93bGjmOKnCxScVy0AOhkGB");
        params.put("callback", "renderReverse");
        params.put("location", latLng);
        params.put("output", "json");
        params.put("pois", "1");
        params.put("mcode", "CC:DE:0D:85:1D:4A:71:BF:9B:E3:53:F4:7F:37:4D:B3:72:DF:07:D7;com.xjgj.mall");
        return mCommonService.geocoderApi(params).subscribeOn(Schedulers.io());
    }

    /**
     * 用车类型
     */
    public Observable<HttpResult<List<CarTypeEntity>>> carType() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.carType(currentTimeMillis, sign).subscribeOn(Schedulers.io());
    }

    /**
     * 商户-下单
     */
    public Observable<HttpResult<String>> orderSubmit(String serviceTime, String volume, String weight, String serviceType,
                                                      String carType, String remark, String counts, String address, String submitType) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("serviceTime", serviceTime);
        params.put("volume", volume);
        params.put("weight", weight);
        params.put("serviceType", serviceType);
        params.put("carType", carType);
        params.put("remark", remark);
        params.put("counts", counts);
        params.put("address", address);
        params.put("submitType", submitType);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderSubmit(currentTimeMillis, sign, params,mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }


    /**
     * 商户-查询个人信息
     */
    public Observable<HttpResult<User>> mallInformation() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.mallInformation(currentTimeMillis, sign,mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

}
