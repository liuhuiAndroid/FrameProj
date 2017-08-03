package com.xjgj.mall.components.storage;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xjgj.mall.bean.User;
import com.xjgj.mall.util.SPUtil;


/**
 * Created by we-win on 2017/7/25.
 */

public class UserStorage {

    private Context mContext;
    private SPUtil mSPUtil;

    public UserStorage(Context context, SPUtil spUtil) {
        this.mContext = context;
        this.mSPUtil = spUtil;
    }

    private String token;
    private User user;

    public String getToken() {
        if(token!=null && !TextUtils.isEmpty(token)) {
            return token;
        }else{
            token = mSPUtil.getTOKNE();
            return mSPUtil.getTOKNE();
        }
    }

    public void setToken(String token) {
        this.token = token;
        mSPUtil.setTOKNE(token);
    }

    public User getUser() {
        if(user!=null) {
            return user;
        }else{
            String userString = mSPUtil.getUSER();
            this.user = new Gson().fromJson(userString,User.class);
            return this.user;
        }
    }

    public void setUser(User user) {
        this.user = user;
        String userJson = new Gson().toJson(user);
        mSPUtil.setUSER(userJson);
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(token) || (!TextUtils.isEmpty(mSPUtil.getTOKNE()));
    }

    public void logout(){
        user = null;
        token = "";
        mSPUtil.setTOKNE("");
    }
}
