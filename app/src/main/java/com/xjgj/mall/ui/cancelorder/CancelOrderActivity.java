package com.xjgj.mall.ui.cancelorder;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.adapter.SmartFragmentStatePagerAdapter;
import com.luck.picture.lib.config.PictureConfig;
import com.squareup.otto.Bus;
import com.xjgj.mall.R;
import com.xjgj.mall.injector.HasComponent;
import com.xjgj.mall.ui.BaseActivity;
import com.xjgj.mall.ui.cancelorder.driver_reasons.DriverReasonsFragment;
import com.xjgj.mall.ui.cancelorder.my_reasons.MyReasonsFragment;
import com.xjgj.mall.util.CommonEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 取消订单
 */

public class CancelOrderActivity extends BaseActivity implements HasComponent<CancelOrderComponent> {

    @BindView(R.id.image_back)
    ImageView mImageBack;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.image_handle)
    ImageView mImageHandle;
    @BindView(R.id.text_handle)
    TextView mTextHandle;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.textCancleOrder)
    TextView mTextCancleOrder;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private CancelOrderComponent mCancelOrderComponent;

    @Inject
    Bus mBus;
    private ReasonsFragmentPagerAdapter mMyMainFragmentPagerAdapter;

    @Override
    public int initContentView() {
        return R.layout.activity_cancel_order;
    }

    @Override
    public void initInjector() {
        mCancelOrderComponent = DaggerCancelOrderComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        mCancelOrderComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle.setText(getResources().getString(R.string.cancle_reason));
        int orderId = getIntent().getIntExtra("orderId", -1);

        mMyMainFragmentPagerAdapter = new ReasonsFragmentPagerAdapter(getSupportFragmentManager());
        mMyMainFragmentPagerAdapter.addFragment(MyReasonsFragment.newInstance(orderId), "本人原因");
        mMyMainFragmentPagerAdapter.addFragment(DriverReasonsFragment.newInstance(orderId), "司机原因");
        mViewPager.setAdapter(mMyMainFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //防止频繁的销毁视图
        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public CancelOrderComponent getComponent() {
        return mCancelOrderComponent;
    }

    public static class ReasonsFragmentPagerAdapter extends SmartFragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public ReasonsFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    /**
     * 取消订单
     */
    @OnClick(R.id.textCancleOrder)
    public void mTextCancleOrder(){
        int currentItem = mViewPager.getCurrentItem();
        mBus.post(new CommonEvent().new CancleOrderEvent(currentItem));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    if(mMyMainFragmentPagerAdapter!=null) {
                        MyReasonsFragment myReasonsFragment = (MyReasonsFragment) mMyMainFragmentPagerAdapter.getRegisteredFragment(0);
                        if(myReasonsFragment!=null) {
                            myReasonsFragment.onActivityResult(requestCode, resultCode, data);
                        }

                        DriverReasonsFragment driverReasonsFragment = (DriverReasonsFragment) mMyMainFragmentPagerAdapter.getRegisteredFragment(1);
                        if(driverReasonsFragment!=null) {
                            driverReasonsFragment.onActivityResult(requestCode, resultCode, data);
                        }
                    }
                    break;
            }
        }
    }

}
