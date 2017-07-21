package com.lh.frameproj.ui.splash;

import com.lh.frameproj.R;
import com.lh.frameproj.ui.BaseActivity;
import com.lh.frameproj.ui.login.LoginActivity;

import javax.inject.Inject;


/**
 * Created by we-win on 2017/7/20.
 */

public class SplashActivity extends BaseActivity implements SplashContract.View{

    @Inject
    SplashPresenter mPresenter;

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
        mPresenter.attachView(this);
        mPresenter.initData();
    }

    @Override
    public void showMainUi() {
        openActivity(LoginActivity.class);
        finish();
    }
}
