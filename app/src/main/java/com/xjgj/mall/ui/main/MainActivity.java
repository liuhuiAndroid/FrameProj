package com.xjgj.mall.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.xjgj.mall.R;
import com.xjgj.mall.injector.HasComponent;
import com.xjgj.mall.ui.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainContract.View
        , HasComponent<MainComponent> {

    @BindView(R.id.tv_title)
    TextView mToolbarTitle;
    @BindView(R.id.frame_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.bnve)
    BottomNavigationViewEx mBottomNavigationViewEx;

    @Inject
    MainPresenter mPresenter;

    private MainComponent mMainComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportFragmentManager().getFragments() != null
                && getSupportFragmentManager().getFragments().size() > 0) {
            getSupportFragmentManager().getFragments().clear();
        }
    }

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .mainModule(new MainModule(this))
                .build();
        mMainComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        // 禁止所有动画效果
        mBottomNavigationViewEx.enableAnimation(false);
        mBottomNavigationViewEx.enableShiftingMode(false);
        mBottomNavigationViewEx.enableItemShiftingMode(false);
        // 自定义图标和文本大小
        //        mBottomNavigationViewEx.setIconSize(widthDp, heightDp);
        //        mBottomNavigationViewEx.setTextSize(sp);
        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.i_choose:
                        mPresenter.onTabClick(0);
                        break;
                    case R.id.i_order:
                        mPresenter.onTabClick(1);
                        break;
                    case R.id.i_mine:
                        mPresenter.onTabClick(2);
                        break;
                }
                return true;
            }
        });

        mPresenter.attachView(this);
        mPresenter.initFragment();
    }


    @Override
    public void setTitle(String title) {
        mToolbarTitle.setText(title);
    }

    @Override
    public void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).commit();
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    @Override
    public void hideFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }

}
