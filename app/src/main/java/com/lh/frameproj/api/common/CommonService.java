package com.lh.frameproj.api.common;

import com.lh.frameproj.bean.AccountVersionEntity;
import com.lh.frameproj.bean.HttpResult;
import com.lh.frameproj.bean.LoginEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
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

}
