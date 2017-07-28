package com.xjgj.mall.components.retrofit;

import android.content.Context;
import android.text.TextUtils;

import com.xjgj.mall.bean.User;


/**
 * Created by we-win on 2017/7/25.
 */

public class UserStorage {

    private Context mContext;

    public UserStorage(Context mContext) {
        this.mContext = mContext;
    }

    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLogin() {
        // TODO 需要增加判断
        return !TextUtils.isEmpty(token);
    }

    private void logout(){
        user = null;
        token = "";
    }
}
