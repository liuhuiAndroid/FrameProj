package com.lh.frameproj.api.common;

import retrofit2.http.POST;
        import rx.Observable;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public interface CommonService {

    //获取最新消息
    @POST("Message/GetAllNew")
    Observable<Void> messageGetAllNew();

}
