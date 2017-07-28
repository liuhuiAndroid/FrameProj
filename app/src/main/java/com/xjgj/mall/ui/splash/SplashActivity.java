package com.xjgj.mall.ui.splash;

import android.Manifest;
import android.widget.Toast;

import com.android.frameproj.library.util.log.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xjgj.mall.R;
import com.xjgj.mall.components.retrofit.UserStorage;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.login.LoginActivity;
import com.xjgj.mall.ui.main.MainActivity;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by we-win on 2017/7/20.
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {

    @Inject
    SplashPresenter mPresenter;
    @Inject
    UserStorage mUserStorage;

    private RxPermissions mRxPermissions;

    @Override
    public int initContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void initInjector() {
        DaggerSplashComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        //初始化权限管理
        mRxPermissions = new RxPermissions(this);
        mPresenter.attachView(this);
        //请求读写SDcard权限和定位权限
        mRxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {
                        if (granted) { // Always true pre-M
                        } else {
                            Toast.makeText(SplashActivity.this, "没有权限部分功能不能正常运行!", Toast.LENGTH_SHORT).show();
                        }
                        Logger.i("granted = " + granted);
                        mPresenter.initData();
                    }
                });
    }

    @Override
    public void showMainUi() {
        if(mUserStorage.isLogin()) {
            openActivity(MainActivity.class);
        }else{
            openActivity(LoginActivity.class);
        }
        finish();
    }

}
