package com.xjgj.mall.api.common;


import com.xjgj.mall.bean.CarTypeEntity;
import com.xjgj.mall.bean.DictionaryEntity;
import com.xjgj.mall.bean.HomepageEntity;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.LoginEntity;
import com.xjgj.mall.bean.OrderDetailEntity;
import com.xjgj.mall.bean.OrderEntity;
import com.xjgj.mall.bean.PhotoUploadEntity;
import com.xjgj.mall.bean.RealNameEntity;
import com.xjgj.mall.bean.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${写接口}
 */
public interface CommonService {

    //登录
    @FormUrlEncoded
    @POST("mall/login")
    Observable<HttpResult<LoginEntity>> mallLogin(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                  @FieldMap Map<String, Object> params);

    //注册
    @FormUrlEncoded
    @POST("mall/register")
    Observable<HttpResult<LoginEntity>> mallRegister(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                     @FieldMap Map<String, Object> params);

    // 提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
    @GET("http://api.map.baidu.com/geocoder/v2/?")
    Observable<ResponseBody> geocoderApi(@QueryMap Map<String, Object> params);

    //用车类型
    @POST("common/car/type")
    Observable<HttpResult<List<CarTypeEntity>>> carType(@Header("timestamp") long timestamp, @Header("sign") String sign);

    //商户-下单
    @FormUrlEncoded
    @POST("mall/order/submit ")
    Observable<HttpResult<String>> orderSubmit(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                               @FieldMap Map<String, Object> params, @Header("token") String token);

    //商户-我的订单
    @FormUrlEncoded
    @POST("mall/order/list")
    Observable<HttpResult<List<OrderEntity>>> mallOrderList(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                            @FieldMap Map<String, Object> params, @Header("token") String token);

    //商户-查询个人信息
    @POST("mall/info/my")
    Observable<HttpResult<User>> mallInformation(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                 @Header("token") String token);

    //商户-个人主页
    @POST("mall/homepage")
    Observable<HttpResult<HomepageEntity>> mallHomepage(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                        @Header("token") String token);

    //上传图片
    @Multipart
    @POST("common/photo/upload")
    Observable<HttpResult<PhotoUploadEntity>> photoUpload(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                          @Header("token") String token, @Part MultipartBody.Part... file);

    //实名认证
    @Multipart
    @POST("user/auth/realName")
    Observable<HttpResult<String>> authRealName(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                @Header("token") String token, @Part MultipartBody.Part... file);

    //订单取消
    @Multipart
    @POST("order/cancel")
    Observable<HttpResult<String>> orderCancel(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                               @Header("token") String token, @Part List<MultipartBody.Part> partList);

    //商户-订单详情
    @FormUrlEncoded
    @POST("mall/order/detail")
    Observable<HttpResult<OrderDetailEntity>> orderDetail(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                          @FieldMap Map<String, Object> params, @Header("token") String token);

    //发送短信验证码
    @FormUrlEncoded
    @POST("common/smsCode/send")
    Observable<HttpResult<String>> smsCodeSend(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                          @FieldMap Map<String, Object> params);

    //商户-完成订单
    @FormUrlEncoded
    @POST("mall/order/finish")
    Observable<HttpResult<String>> orderFinish(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                               @FieldMap Map<String, Object> params, @Header("token") String token);

    //订单评价
    @FormUrlEncoded
    @POST("order/comment")
    Observable<HttpResult<String>> orderComment(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                               @FieldMap Map<String, Object> params, @Header("token") String token);

    //查看图片
    @FormUrlEncoded
    @POST("common/photo/query")
    Observable<HttpResult<List<PhotoUploadEntity>>> photoQuery(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                               @Header("token") String token,@FieldMap Map<String, Object> params);

    //查看实名认证
    @POST("user/auth/realName/query")
    Observable<HttpResult<RealNameEntity>> realNameQuery(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                         @Header("token") String token);

    //查询字典
    @FormUrlEncoded
    @POST("common/dictionary/query")
    Observable<HttpResult<List<DictionaryEntity>>> dictionaryQuery(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                                   @Header("token") String token, @FieldMap Map<String, Object> params);

    //订单申诉
    @Multipart
    @POST("order/complain")
    Observable<HttpResult<String>> orderComplain(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                               @Header("token") String token, @Part List<MultipartBody.Part> partList);
}
