package com.lh.frameproj.api.common;

import com.lh.frameproj.bean.AccountVersionEntity;
import com.lh.frameproj.bean.HttpResult;
import com.lh.frameproj.bean.LoginEntity;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${写接口}
 */
public interface CommonService {

    // 版本控制
    @POST("api/version/control")
    Observable<HttpResult<AccountVersionEntity>> accountVersion();

    //登录
    @FormUrlEncoded
    @POST("api/account/login/sms")
    Observable<HttpResult<LoginEntity>> loginSms(@Header("timestamp") String timestamp, @Header("sign") String sign,
                                                 @Field("username") String username, @Field("authCode") String authCode,
                                                 @Field("device") String device, @Field("appType") String appType);

    // 提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
    @GET("http://api.map.baidu.com/geocoder/v2/?")
    Observable<ResponseBody> geocoderApi(@Query("ak") String ak, @Query("callback") String callback, @Query("location") String location,
                                         @Query("output") String output, @Query("pois") int pois, @Query("mcode") String mcode);

}
