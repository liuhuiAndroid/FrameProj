package com.lh.frameproj.components.retrofit;

import android.content.Context;

import com.lh.frameproj.Constants;
import com.lh.frameproj.util.SecurityUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by we-win on 2017/7/25.
 */

public class RequestHelper {

    private Context mContext;
    private UserStorage mUserStorage;

    public RequestHelper(Context mContext, UserStorage mUserStorage) {
        this.mContext = mContext;
        this.mUserStorage = mUserStorage;
    }

    /**
     * 签名
     *
     * @param map
     * @return
     */
    public String getRequestSign(Map<String, Object> map, long currentTimeMillis) {
        String generateDigest = SecurityUtil.getMd5(map, currentTimeMillis);
        return generateDigest;
    }

    /**
     * 公共参数
     *
     * @return
     */
    public Map<String, Object> getHttpRequestMap(long currentTimeMillis) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("timestamp", currentTimeMillis);
        map.put("key", Constants.app_key);
        //        if (mUserStorage.isLogin()) {
        //            try {
        //                map.put("token", URLEncoder.encode(mUserStorage.getToken(), "UTF-8"));
        //            } catch (Exception e) {
        //                e.printStackTrace();
        //            }
        //        }
        return map;
    }

}
