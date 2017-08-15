package com.xjgj.mall.ui.mapdriveraddress;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.frameproj.library.statefullayout.StatefulLayout;
import com.android.frameproj.library.statefullayout.StatusfulConfig;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
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
    private BaiduMap mBaiduMap;

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
        mBaiduMap = mBaiduMapView.getMap();

        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextTitle.setText("附近车辆");

        mLongitude = getIntent().getDoubleExtra("longitude", 0.0);
        mLatitude = getIntent().getDoubleExtra("latitude", 0.0);
        mPresenter.driverAddress(mLongitude, mLatitude);
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
                            mPresenter.driverAddress(mLongitude, mLatitude);
                        }
                    }).build();
            mStatefulLayout.showError(statusfulConfig);
        } else if (type == 0) {
            mStatefulLayout.showContent();
        }
    }

    @Override
    public void driverAddressResult(List<DriverAddressEntity> driverAddressEntities) {
        double sumLat = 0;
        double sumLng = 0;
        if (driverAddressEntities != null && driverAddressEntities.size() > 0) {
            for (int i = 0; i < driverAddressEntities.size(); i++) {
                DriverAddressEntity driverAddressEntity = driverAddressEntities.get(i);
                sumLat += driverAddressEntity.getLatitude();
                sumLng += driverAddressEntity.getLongitude();

                //添加覆盖物
                addOverLayout(new LatLng(driverAddressEntity.getLatitude(),driverAddressEntity.getLongitude()));
            }
        }
        final double midlat = sumLat / driverAddressEntities.size();
        final double midlon = sumLng / driverAddressEntities.size();

        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng latLng = new LatLng(midlat, midlon);
        float zoom = 15.0f; // 默认 15级
        builder.target(latLng).zoom(zoom);
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        //改变地图状态
        mBaiduMap.animateMapStatus(mMapStatusUpdate);

    }

    /**
     * 添加覆盖物
     * @param endPosition
     */
    private void addOverLayout(LatLng endPosition) {

        // 构建Marker图标
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.mipmap.bike_icon);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions optionEnd = new MarkerOptions().position(endPosition)
                .icon(bitmap1)
                .zIndex(8) //设置marker所在层级
                .draggable(true);
        mBaiduMap.addOverlay(optionEnd);

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