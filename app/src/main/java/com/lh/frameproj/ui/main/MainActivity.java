package com.lh.frameproj.ui.main;

import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
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
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

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
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .addItem(new BottomNavigationItem(R.mipmap.ic_news_24dp, "新闻").setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.mipmap.ic_photo_24dp, "美图").setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.mipmap.ic_video_24dp, "视频").setActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.mipmap.ic_about_me, "关于").setActiveColorResource(R.color.red))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mPresenter.onTabClick(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        getFragmentManager().beginTransaction()
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
        getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }
}
