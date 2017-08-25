package com.android.frameproj.library.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.frameproj.library.R;

/**
 * Created by lh on 2017/8/25.
 */

public class DialogManager {

    private volatile static DialogManager instance = null;

    private DialogManager(){

    }

    //使用双重检查锁定实现的懒汉式单例类
    public static DialogManager getInstance() {
        //第一重判断
        if (instance == null) {
            //锁定代码块
            synchronized (DialogManager.class){
                // 第二重判断
                if(instance == null){
                    instance = new DialogManager(); // 创建单例实例
                }
            }
        }
        return instance;
    }

    public Dialog createLoadingDialog(Context paramContext) {
        View localView = LayoutInflater.from(paramContext).inflate(R.layout.dialog_loading, null);
        RelativeLayout localRelativeLayout = (RelativeLayout) localView.findViewById(R.id.dialog_view);
        ImageView localImageView = (ImageView) localView.findViewById(R.id.img_round);
        Animation localAnimation = AnimationUtils.loadAnimation(paramContext, R.anim.rorate_loading);
        localAnimation.setInterpolator(new LinearInterpolator());
        localImageView.startAnimation(localAnimation);
        Dialog localDialog = new Dialog(paramContext, R.style.LoadingDialog);
        localDialog.setCanceledOnTouchOutside(false);
        localDialog.setContentView(localRelativeLayout, new LinearLayout.LayoutParams(-2, -2));
        return localDialog;
    }

}
