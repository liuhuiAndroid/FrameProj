package com.lh.frameproj.bean;

/**
 * Created by we-win on 2017/7/21.
 */

public class LoginEntity {

    private long validTime;
    private String token;

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
