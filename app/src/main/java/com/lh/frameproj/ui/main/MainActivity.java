package com.lh.frameproj.ui.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lh.frameproj.R;
import com.lh.frameproj.injector.HasComponent;
import com.lh.frameproj.ui.BaseActivity;
import com.lh.frameproj.ui.fragment1.Fragment1;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainContract.View
        , HasComponent<MainComponent> {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frame_layout)
    FrameLayout mFrameLayout;
    @BindView(R.id.bnve)
    BottomNavigationViewEx mBottomNavigationViewEx;

    @Inject
    MainPresenter mPresenter;

    private MainComponent mMainComponent;

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
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,
                        new Fragment1())
                .commit();

        mToolbarTitle.setText(R.string.MainActivity_title_news);
        mPresenter.attachView(this);
    }


    @Override
    public void setTitle(String title) {
        mToolbarTitle.setText(title);
    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }

}
