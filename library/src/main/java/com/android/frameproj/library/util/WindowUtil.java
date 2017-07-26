package com.android.frameproj.library.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by we-win on 2017/7/26.
 */

public class WindowUtil {

    private static int windowWidth;
    private static int windowHeight;

    public static void init(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        windowWidth = point.x;
        windowHeight = point.y;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }


    /**
     * 获得屏幕宽度分辨率
     * @param ctx
     * @return
     */
    public static final int getWidth(Context ctx){
        if(nWidth == 0){
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            nWidth = Math.min(dm.widthPixels, dm.heightPixels);
        }
        return nWidth;
    }
    private static int nWidth;


    /**
     * 获得屏幕高度分辨率
     * @param ctx
     * @return
     */
    public static final int getHeight(Context ctx){
        if(nHeight == 0){
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            nHeight = Math.max(dm.widthPixels, dm.heightPixels);
        }
        return nHeight;
    }
    private static int nHeight;

    /**
     * 获得屏幕密度，一般值为0.75、1、1.5。相当于getDensityDip()/160
     * @param ctx
     * @return
     */
    public static final float getDensity(Context ctx){
        if(nDensity == 0){
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            nDensity = dm.density;
        }
        return nDensity;
    }
    private static float nDensity;

    /**
     * 获得屏幕密度dpi，一般值为120、160、240。
     * dpi：dots per inch。
     * drawable与hdpi,mdpi,ldpi的关系：densityDpi=120：ldpi、densityDpi=160：mdpi、densityDpi=240：hdpi
     * @param ctx
     * @return
     */
    public static final int getDensityDpi(Context ctx) {
        if(nDpi == 0){
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            nDpi = dm.densityDpi;
        }
        return nDpi;
    }
    private static int nDpi;//密度DPI


    /**
     * 获得状态栏高度，需UI初始化结束后调用
     * @return
     */
    public static final int getStatusBarHeight(Activity act){
        Rect rect= new Rect();
        act.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 设置为无标题栏，必须在setContentView之前调用
     */
    public static final void setNoTitle(Activity act){
        act.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 设置为全屏模式
     */
    public static final void setFullScreen(Activity act){
        act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置屏幕保持唤醒状态
     */
    public static final void setScreenKeepOn(Activity act){
        act.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 单位dip转px，当densityDpi=160时，1dip=1px。
     * dip:device independent pixels(设备独立像素)、sp: scaled pixels(放大像素)。
     * @param ctx
     * @param dip
     * @return
     */
    public static int dip2px(Context ctx, float dip){
        return (int)(dip * getDensity(ctx) + 0.5f);
    }

    /**
     * 截取屏幕，只限于APP自身
     * @param act
     * @return
     */
    public static Bitmap captureScreen(Activity act) {
        View decorview = act.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        //decorview.buildDrawingCache();
        return decorview.getDrawingCache();
    }

    /**
     * 将View转换为Bitmap
     * @param view
     * @return
     */
    public static Bitmap captureView(View view) {
        view.setDrawingCacheEnabled(true);
        //decorview.buildDrawingCache();
        return view.getDrawingCache();
    }

}