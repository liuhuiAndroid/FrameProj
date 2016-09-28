package com.lh.frameproj.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.lh.frameproj.AppManager;
import com.lh.frameproj.MyApplication;
import com.lh.frameproj.injector.component.ApplicationComponent;
import com.lh.frameproj.injector.module.ActivityModule;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //注入数据库
        getApplicationComponent().inject(this);
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        initInjector();
        initUiAndListener();
        AppManager.getAppManager().addActivity(this);
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((MyApplication) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    private void initTheme() {

    }

    /**
     * 设置view
     */
    public abstract int initContentView();

    /**
     * 注入Injector
     */
    public abstract void initInjector();

    /**
     * init UI && Listener
     */
    public abstract void initUiAndListener();

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

}
