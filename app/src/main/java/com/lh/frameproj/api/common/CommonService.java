package com.lh.frameproj.api.common;

import com.lh.frameproj.bean.AccountVersionEntity;
import com.lh.frameproj.bean.HttpResult;

import io.reactivex.Observable;
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



}
