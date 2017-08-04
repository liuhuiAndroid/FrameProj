package com.android.frameproj.library.util.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.android.frameproj.library.util.imageloader.glideprogress.ProgressLoadListener;
import com.android.frameproj.library.util.imageloader.listener.ImageSaveListener;
import com.android.frameproj.library.util.imageloader.listener.SourceReadyListener;


/**
 * Created by soulrelay on 2016/10/11.
 * Class Note:
 * abstract class/interf defined to load image
 * (Strategy Pattern used here)
 * 图片加载的基础接口
 * 当封装的方法参数比较少时可以下面这样进行抽象，如果需要传递的参数较多，可以考虑使用建造者模式
 */
public interface BaseImageLoaderStrategy {
    //正常加载图片 无占位图
    void loadImage(String url, ImageView imageView);

    //这里的context指定为ApplicationContext
    void loadImageWithAppCxt(String url, ImageView imageView);

    void loadImage(String url, int placeholder, ImageView imageView);

    void loadImage(Context context, String url, int placeholder, ImageView imageView);

    //加载圆形图片
    void loadCircleImage(String url, int placeholder, ImageView imageView);

    //加载圆形图片，支持设置边框的宽度和颜色
    void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor);

    void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx);

    // 针对于GIF图片的特殊加载
    void loadGifImage(String url, int placeholder, ImageView imageView);

    // 加载图片的进度回调
    void loadImageWithProgress(String url, ImageView imageView, ProgressLoadListener listener);

    void loadImageWithPrepareCall(String url, ImageView imageView, int placeholder, SourceReadyListener listener);

    void loadGifWithPrepareCall(String url, ImageView imageView, SourceReadyListener listener);

    //清除硬盘缓存
    void clearImageDiskCache(final Context context);

    //清除内存缓存
    void clearImageMemoryCache(Context context);

    //根据不同的内存状态，来响应不同的内存释放策略
    void trimMemory(Context context, int level);

    //获取缓存大小
    String getCacheSize(Context context);

    // 实现图片的本地自定义保存功能
    void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener);

    // TODO 其它特殊需求自己封装，最好不要破坏策略模式的整体结构
}
