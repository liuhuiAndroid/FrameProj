package com.xjgj.mall.api.common;

import com.xjgj.mall.Constants;
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
import com.xjgj.mall.components.retrofit.RequestHelper;
import com.xjgj.mall.components.storage.UserStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${实现接口的调用}
 */
public class CommonApi {

    private CommonService mCommonService;
    private RequestHelper mRequestHelper;
    private UserStorage mUserStorage;

    public CommonApi(OkHttpClient mOkHttpClient, RequestHelper requestHelper, UserStorage userStorage) {
        this.mRequestHelper = requestHelper;
        mUserStorage = userStorage;
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .client(mOkHttpClient)
                        .baseUrl(Constants.BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
        mCommonService = retrofit.create(CommonService.class);
    }

    /**
     * 对网络接口返回的Response进行分割操作
     *
     * @param response
     * @param <T>
     * @return
     */
    public static <T> Observable<T> flatResponse(final HttpResult<T> response) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                if (!e.isDisposed()) {
                    if (response.isSuccess()) {
                        e.onNext(response.getResultValue());
                        e.onComplete();
                    } else {
                        e.onError(new APIException(response.getResultStatus().getCode(),
                                response.getResultStatus().getMessage()));
                    }
                }
            }
        });
    }

    /**
     * 自定义异常，当接口返回的code不为Constants.OK时，需要跑出此异常
     * eg：登陆时验证码错误；参数为传递等
     */
    public static class APIException extends Exception {
        public int code;
        public String message;

        public APIException(int code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    @android.support.annotation.NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @android.support.annotation.NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @android.support.annotation.NonNull
    public static MultipartBody.Part createPartString(String name, String value) {
        //        RequestBody requestFile =
        //                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), value);
        return MultipartBody.Part.createFormData(name, value);
    }

    // =======================================  API

    /**
     * 登录
     */
    public Observable<HttpResult<LoginEntity>> mallLogin(String username, String password) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("username", username);
        params.put("password", password);
        params.put("appType", Constants.APPTYPE);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.mallLogin(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 注册
     */
    public Observable<HttpResult<LoginEntity>> mallRegister(String mobile, String realName, String password,
                                                            String smsCode) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("mobile", mobile);
        params.put("realName", realName);
        params.put("password", password);
        params.put("smsCode", smsCode);
        params.put("source", Constants.APPTYPE); // 用户来源:1-IOS,2-Android,3-Other
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.mallRegister(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 提供从地址到经纬度坐标或者从经纬度坐标到地址的转换服务
     */
    public Observable<ResponseBody> geocoderApi(String latLng) {
        Map<String, Object> params = new HashMap<>();
        // ak:百度地图api key
        params.put("ak", "mFPjNn9HWii1KiKWLTFdgvb3KI7LQVoF");
        params.put("callback", "renderReverse");
        params.put("location", latLng);
        params.put("output", "json");
        params.put("pois", "1");
        params.put("mcode", "CC:DE:0D:85:1D:4A:71:BF:9B:E3:53:F4:7F:37:4D:B3:72:DF:07:D7;com.xjgj.mall");
        return mCommonService.geocoderApi(params).subscribeOn(Schedulers.io());
    }

    /**
     * 用车类型
     */
    public Observable<HttpResult<List<CarTypeEntity>>> carType() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.carType(currentTimeMillis, sign).subscribeOn(Schedulers.io());
    }

    /**
     * 商户-下单
     */
    public Observable<HttpResult<String>> orderSubmit(String serviceTime, String volume, String weight, String serviceType,
                                                      String carType, String remark, String counts, String address, String submitType) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        if(!serviceTime.equals("")) {
            params.put("serviceTime", serviceTime);
        }
        params.put("volume", volume);
        params.put("weight", weight);
        params.put("serviceType", serviceType);
        params.put("carType", carType);
        params.put("remark", remark);
        params.put("counts", counts);
        params.put("address", address);
        params.put("submitType", submitType);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderSubmit(currentTimeMillis, sign, params, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }


    /**
     * 商户-查询个人信息
     */
    public Observable<HttpResult<User>> mallInformation() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.mallInformation(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 商户-个人主页
     */
    public Observable<HttpResult<HomepageEntity>> mallHomepage() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.mallHomepage(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 商户-我的订单
     */
    public Observable<HttpResult<List<OrderEntity>>> mallOrderList(int page, int type) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("page", page);
        params.put("pageSize", 10);
        params.put("type", type);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.mallOrderList(currentTimeMillis, sign, params, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 上传图片
     */
    public Observable<HttpResult<PhotoUploadEntity>> photoUpload(File photo, int type) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        MultipartBody.Part filePart = prepareFilePart("photo", photo);
        MultipartBody.Part typeUpload = createPartString("type", type + "");
        params.put("type", type);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.photoUpload(currentTimeMillis, sign, mUserStorage.getToken(), typeUpload, filePart).subscribeOn(Schedulers.io());
    }

    /**
     * 实名认证
     */
    public Observable<HttpResult<String>> authRealName(String realName, String identityNo, File frontIdentity, File afterIdentity) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        MultipartBody.Part frontIdentityPart = prepareFilePart("frontIdentity", frontIdentity);
        MultipartBody.Part afterIdentityPart = prepareFilePart("afterIdentity", afterIdentity);
        MultipartBody.Part realNamePart = createPartString("realName", realName);
        MultipartBody.Part identityNoPart = createPartString("identityNo", identityNo);
        params.put("realName", realName);
        params.put("identityNo", identityNo);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.authRealName(currentTimeMillis, sign, mUserStorage.getToken(), frontIdentityPart, afterIdentityPart,
                realNamePart, identityNoPart).subscribeOn(Schedulers.io());
    }

    /**
     * 订单取消
     */
    public Observable<HttpResult<String>> orderCancel(int orderId,int reasonType,int cancelType,
                                                      String remark,List<String> pathList) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        List<MultipartBody.Part> partList = new ArrayList<>();
        if(pathList!=null && pathList.size()>0) {
            for (int i = 0; i < pathList.size(); i++) {
                File file  = new File(pathList.get(i));
                MultipartBody.Part filePart = prepareFilePart("photo", file);
                partList.add(filePart);
            }
        }
        MultipartBody.Part orderIdPart = createPartString("orderId", orderId+"");
        MultipartBody.Part reasonTypePart = createPartString("reasonType", reasonType+"");
        MultipartBody.Part cancelTypePart = createPartString("cancelType", cancelType+"");
        MultipartBody.Part remarkPart = createPartString("remark", remark);
        MultipartBody.Part userTypePart = createPartString("userType", 1+"");
        partList.add(orderIdPart);
        partList.add(reasonTypePart);
        partList.add(cancelTypePart);
        partList.add(remarkPart);
        partList.add(userTypePart);

        params.put("orderId", orderId);
        params.put("reasonType", reasonType);
        params.put("cancelType", cancelType);
        params.put("remark", remark);
        params.put("userType", 1);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderCancel(currentTimeMillis, sign, mUserStorage.getToken(),partList).subscribeOn(Schedulers.io());
    }

    /**
     * 商户-订单详情
     */
    public Observable<HttpResult<OrderDetailEntity>> orderDetail(int orderId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("orderId", orderId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderDetail(currentTimeMillis, sign, params, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 发送短信验证码
     */
    public Observable<HttpResult<String>> smsCodeSend(String mobile, int type) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("mobile", mobile);
        params.put("type", type);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.smsCodeSend(currentTimeMillis, sign, params).subscribeOn(Schedulers.io());
    }

    /**
     * 商户-完成订单
     */
    public Observable<HttpResult<String>> orderFinish(int orderId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("orderId", orderId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderFinish(currentTimeMillis, sign, params, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 订单评价
     */
    public Observable<HttpResult<String>> orderComment(int orderId, String level, String content) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("orderId", orderId);
        params.put("type", 1);
        params.put("level", level);
        params.put("content", content);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderComment(currentTimeMillis, sign, params, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 查看实名认证
     */
    public Observable<HttpResult<RealNameEntity>> realNameQuery() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.realNameQuery(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 查看图片
     */
    public Observable<HttpResult<List<PhotoUploadEntity>>> photoQuery(int type) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        MultipartBody.Part typeUpload = createPartString("type", type + "");
        params.put("type", type);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.photoQuery(currentTimeMillis, sign, mUserStorage.getToken(),params).subscribeOn(Schedulers.io());
    }

    /**
     * 查看图片
     */
    public Observable<HttpResult<List<DictionaryEntity>>> dictionaryQuery(int dictionaryType) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("dictionaryType", dictionaryType);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.dictionaryQuery(currentTimeMillis, sign, mUserStorage.getToken(),params).subscribeOn(Schedulers.io());
    }

    /**
     * 订单取消
     */
    public Observable<HttpResult<String>> orderAppeal(int orderId,int complainType,String content,
                                                      List<String> pathList) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        List<MultipartBody.Part> partList = new ArrayList<>();
        if(pathList!=null && pathList.size()>0) {
            for (int i = 0; i < pathList.size(); i++) {
                File file  = new File(pathList.get(i));
                MultipartBody.Part filePart = prepareFilePart("photo", file);
                partList.add(filePart);
            }
        }
        MultipartBody.Part orderIdPart = createPartString("orderId", orderId+"");
        MultipartBody.Part complainTypePart = createPartString("complainType", complainType+"");
        MultipartBody.Part contentPart = createPartString("content", content);
        partList.add(orderIdPart);
        partList.add(complainTypePart);
        partList.add(contentPart);

        params.put("orderId", orderId);
        params.put("complainType", complainType);
        params.put("content", content);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.orderComplain(currentTimeMillis, sign, mUserStorage.getToken(),partList).subscribeOn(Schedulers.io());
    }

}
