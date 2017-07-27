package com.lh.frameproj.api.common;

import com.lh.frameproj.bean.AccountVersionEntity;
import com.lh.frameproj.bean.CarTypeEntity;
import com.lh.frameproj.bean.HttpResult;
import com.lh.frameproj.bean.LoginEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

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
    @POST("api/mall/login")
    Observable<HttpResult<LoginEntity>> mallLogin(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                 @FieldMap Map<String, Object> params);

    // 提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
    @GET("http://api.map.baidu.com/geocoder/v2/?")
    Observable<ResponseBody> geocoderApi(@QueryMap Map<String, Object> params);

    //用车类型
    @POST("common/car/type")
    Observable<HttpResult<List<CarTypeEntity>>> carType(@Header("timestamp") long timestamp, @Header("sign") String sign);

    //商户-下单
    @FormUrlEncoded
    @Headers("token:4rE6HcLu438nII0stjagjn5mCkZhyXWS4nC9k7fqzsQeY84bqhSlJQA7qGo9Bh0XyePbuSR9aQHJMbZAhTEvCBzai8EQpMtsLm43BaKpqYA=")
    @POST("mall/order/submit ")
    Observable<HttpResult<String>> orderSubmit(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                          @FieldMap Map<String, Object> params);

}
