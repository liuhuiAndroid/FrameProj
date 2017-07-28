package com.xjgj.mall.bean;


import com.google.gson.annotations.SerializedName;
import com.xjgj.mall.Constants;

/**
 * Created by WE-WIN-027 on 2016/6/29.
 *
 *
 * @des ${TODO}
 */
public class HttpResult<T> {

    private ResultStatusBean resultStatus;

    private @SerializedName("resultValue") T resultValue;

    public static class ResultStatusBean {
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public boolean isSuccess() {
        return resultStatus.getCode() == Constants.OK;
    }

    public ResultStatusBean getResultStatus() {
        return resultStatus;
    }

    public T getResultValue() {
        return resultValue;
    }

}
