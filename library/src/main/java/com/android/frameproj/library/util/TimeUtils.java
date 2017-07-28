package com.android.frameproj.library.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * @author andyxuan
 * @time 2014-12-26
 * @copyright 10yearslater
 * @description 获取时间的帮助类
 */
public class TimeUtils {



    //将时间戳转换为正常时间，即格式化时间戳
    public static String getLocalTimeSecond(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long cc_time = Long.parseLong(str);
        return df.format(new Date(cc_time * 1000L));
    }

    public static String getLocalTimeSecondDynasty(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long cc_time = Long.parseLong(str);
        return df.format(new Date(cc_time * 1000L));
    }

    /**
     * 将时间戳转换为 2014年07月08日 这种
     * @param str
     * @return
     */
    public static String getLocalTime(String str) {

        String time = getOriginalTime(str);
        String year = time.substring(0, 4);
        String month = time.substring(5, 7);
        String day = time.substring(8, 10);
        return year + "年" + month + "月" + day + "日";
    }

    public static String getMemoryTime(String str) {

        String time = getOriginalTime(str);
        String year = time.substring(0, 4);
        String month = time.substring(5, 7);
        String day = time.substring(8, 10);
        return year + "." + month + "." + day;
    }


    /**
     * 将时间戳转换为 2014年07月08日 这种
     * @param str
     * @return
     */
    public static String getExchangeTime(String str) {

        String time = getLocalTimeSecond(str);
        String year = time.substring(0, 4);
        String month = time.substring(5, 7);
        String day = time.substring(8, 10);
        return year + "年" + month + "月" + day + "日"+" "+time.substring(11);
    }


    /**
     * 将时间戳转换为 2014-07-08 这种
     * @param str
     * @return
     */
    public static String getOriginalTime(String str) {
        String time = "";
        if (!TextUtils.isEmpty(str)){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            long cc_time = Long.parseLong(str);
            time = df.format(new Date(cc_time * 1000L));
        }

        return time;
    }



    public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

    /**
     * 获取本地时间戳 2015-08-21 15:58:27
     * @return
     */
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd HH:mm:ss");
	}
    //获取当前的时间(timeStamp型)
	public static String getCurrentTimeMillis(){
		return (System.currentTimeMillis()/1000)+"";
	}


    /**
     * 将 yyyy-mm-dd hh:mm 转换成时间戳
     * @param time
     * @return
     */
    public static String getAnyTime(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format.parse(time).getTime()/1000+"";

        } catch (ParseException e3) {

            e3.printStackTrace();
        }
        return "";
    }


    public static String getDayTime(String str) {

        String time = getLocalTimeSecond(str);
        String month = time.substring(5, 7);
        String day = time.substring(8, 10);
        return  month + "月" + day + "日";
    }

    public static String getHourTime(String str) {

        String time = getLocalTimeSecond(str);
        String hour = time.substring(11, 13);
        return  hour+"时";
    }

    public static String getMinuTime(String str) {

        String time = getLocalTimeSecond(str);
        String minu = time.substring(14, 16);
        return  minu;
    }

    public static String getYearAndDay(String str) {

        String time = getLocalTimeSecond(str);
        String year = time.substring(0,4);
        String month = time.substring(5, 7);
        String day = time.substring(8, 10);
        return  year+"-"+month+"-"+day;
    }

    public static String getYear(String str) {

        String time = getLocalTimeSecond(str);
        String year = time.substring(0,4);
        return  year;
    }


//    /**
//     * 获得星期几
//     * @param context
//     * @return
//     */
//    public static String getWeek(Context context){
//        String week = "";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//也可将此值当参数传进来
//        Date  curDate = new Date(System.currentTimeMillis());
//        String pTime = format.format(curDate);
//        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(format.parse(pTime));
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        switch(c.get(Calendar.DAY_OF_WEEK)){
//            case 1:
//                week  = context.getResources().getString(R.string.sunday);
//                break;
//            case 2:
//                week  = context.getResources().getString(R.string.mondy);
//                break;
//            case 3:
//                week  = context.getResources().getString(R.string.tuesday);
//                break;
//            case 4:
//                week  = context.getResources().getString(R.string.wednesday);
//                break;
//            case 5:
//                week  = context.getResources().getString(R.string.thursday);
//                break;
//            case 6:
//                week  = context.getResources().getString(R.string.friday);
//                break;
//            case 7:
//                week  = context.getResources().getString(R.string.saturday);
//                break;
//            default:
//                break;
//        }
//        return week;
//    }


//
//    public static String getWeek(Context context,String timeStamp){
//        String week = "";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//也可将此值当参数传进来
//
//        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(format.parse(timeStamp));
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        switch(c.get(Calendar.DAY_OF_WEEK)){
//            case 1:
//                week  = context.getResources().getString(R.string.sunday);
//                break;
//            case 2:
//                week  = context.getResources().getString(R.string.mondy);
//                break;
//            case 3:
//                week  = context.getResources().getString(R.string.tuesday);
//                break;
//            case 4:
//                week  = context.getResources().getString(R.string.wednesday);
//                break;
//            case 5:
//                week  = context.getResources().getString(R.string.thursday);
//                break;
//            case 6:
//                week  = context.getResources().getString(R.string.friday);
//                break;
//            case 7:
//                week  = context.getResources().getString(R.string.saturday);
//                break;
//            default:
//                break;
//        }
//        return week;
//    }

    /**
     * 计算 距离发布几分、几小时、几天
     * @param timeStamp
     * @param context
     * @return
     */
//    public static String getPublish(Context context,String timeStamp){
//        String current = getCurrentTimeMillis();
//        long localTime = Long.parseLong(current);
//        if (TextUtils.isEmpty(timeStamp)){
//            return "";
//        }
//        long preTime = Long.parseLong(timeStamp);
//        long tempTime = localTime-preTime;
//        int tempMinute =  (int)tempTime/60;
//        int tempHour =  (int)tempTime/(60*60);
//        int tempDay = (int)tempTime/(60*60*24);
//
//        if ((tempTime/60)<10){
//            return context.getResources().getString(R.string.some_time_before);
//        }else{
//            if(10<=tempMinute&& tempMinute<60){
//                return  tempMinute+context.getResources().getString(R.string.some_times_before);
//            }else{
//                if(1<=tempHour&& tempHour<24){
//                    return  tempHour+context.getResources().getString(R.string.some_hour_before);
//                }else{
//                    if (1<=tempDay && tempDay<10){
//                        return  tempDay+context.getResources().getString(R.string.some_day_before);
//                    }else{
//                        return getOriginalTime(timeStamp).substring(0,10);
//                    }
//                }
//
//            }
//        }
//
//    }

}
