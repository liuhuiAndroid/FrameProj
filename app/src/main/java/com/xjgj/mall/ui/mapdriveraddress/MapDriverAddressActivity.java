package com.xjgj.mall.ui.mapdriveraddress;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.statefullayout.StatefulLayout;
import com.android.frameproj.library.statefullayout.StatusfulConfig;
import com.baidu.mapapi.map.MapView;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.DriverAddressEntity;
import com.xjgj.mall.ui.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lh on 2017/8/15.
 * 附近司机位置
 */

public class MapDriverAddressActivity extends BaseActivity implements MapDriverAddressContract.View {
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
    @BindView(R.id.baiduMapView)
    MapView mBaiduMapView;
    @BindView(R.id.statefulLayout)
    StatefulLayout mStatefulLayout;

    @Inject
    MapDriverAddressPresenter mPresenter;
    private double mLongitude;
    private double mLatitude;

    @Override
    public int initContentView() {
        return R.layout.activity_map_driver_address;
    }

    @Override
    public void initInjector() {
        DaggerMapDriverAddressComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);

        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle.setText("附近司机");

        mLongitude = getIntent().getDoubleExtra("longitude", 0.0);
        mLatitude = getIntent().getDoubleExtra("latitude", 0.0);
        mPresenter.driverAddress(mLongitude,mLatitude);
    }

    @Override
    public void showLoading() {
        mStatefulLayout.showLoading();
    }

    @Override
    public void hideLoading(int type) {
        if (type == -1) {
            StatusfulConfig statusfulConfig = new StatusfulConfig.Builder()
                    .setOnErrorStateButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.driverAddress(mLongitude,mLatitude);
                        }
                    }).build();
            mStatefulLayout.showError(statusfulConfig);
        } else if (type == 0) {
            mStatefulLayout.showContent();
        }
    }

    @Override
    public void driverAddressResult(List<DriverAddressEntity> driverAddressEntities) {

    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
