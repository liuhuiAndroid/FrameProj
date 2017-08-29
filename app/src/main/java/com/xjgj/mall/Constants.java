package com.xjgj.mall;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Constants {

    public static final int OK = 0;

    public static String LH_LOG_PATH = "/LH/Log/";// 日志默认保存目录

    public static final String BASE_URL = "http://192.168.1.130:8080/";
    //    public static final String BASE_URL = "http://we-win.3322.org/nhy2/";
    //    public static final String BASE_URL = "http://121.41.103.166/nhy/";

    // app类型：1:ios,2:andriod
    public static final int APPTYPE = 2;

    //服务端自定义API key、value
    public static String app_key = "B272F43387B8504C";
    public static String app_value = "70BAE8B491362AB39042B77C7653199D";

    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;


    public static final int REQUEST_CHOOSE_LOCATION_CODE = 100;
    public static final int RESULT_CHOOSE_LOCATION_CODE = 101;
    public static final int REQUEST_EXTRA_SERVICE_CODE = 102;
    public static final int RESULT_EXTRA_SERVICE_CODE = 103;
    public static final int REQUEST_WRITE_SENTENCE_CODE = 104;
    public static final int RESULT_WRITE_SENTENCE_CODE = 105;
    public static final int REQUEST_DELIVERY_INFO_CODE = 106;
    public static final int RESULT_DELIVERY_INFO_CODE = 107;
    public static final int REQUEST_REGISTER_CODE = 108;
    public static final int RESULT_REGISTER_CODE = 109;

    public static final int REQUEST_IMPROVE_ORDER_CODE = 110;
    public static final int RESULT_IMPROVE_ORDER_CODE = 111;
    public static final int REQUEST_CONFIRM_ORDER_CODE = 112;
    public static final int RESULT_CONFIRM_ORDER_CODE = 113;
    public static final int REQUEST_BUSINESS_LICENCE_CODE = 114;
    public static final int RESULT_BUSINESS_LICENCE_CODE = 115;
    public static final int REQUEST_CERTIFICATION_CODE = 116;
    public static final int RESULT_CERTIFICATION_CODE = 117;
    public static final int REQUEST_IMPROVE_ORDER_CODE_FROM_DETAIL = 118;
    public static final int RESULT_IMPROVE_ORDER_CODE_FROM_DETAIL = 119;

}
