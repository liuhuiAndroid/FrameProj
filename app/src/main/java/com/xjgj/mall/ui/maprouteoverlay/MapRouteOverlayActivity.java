package com.xjgj.mall.ui.maprouteoverlay;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.frameproj.library.statefullayout.StatefulLayout;
import com.android.frameproj.library.statefullayout.StatusfulConfig;
import com.android.frameproj.library.util.log.Logger;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xjgj.mall.R;
import com.xjgj.mall.bean.CarAddressEntity;
import com.xjgj.mall.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by lh on 2017/8/14.
 * 车辆行驶轨迹
 */

public class MapRouteOverlayActivity extends BaseActivity implements MapRouteOverlayContract.View {

    private static final String TAG = "MapRouteOverlayActivity";
    @BindView(R.id.baiduMapView)
    MapView mBaiduMapView;
    @BindView(R.id.image_back)
    ImageView mImageBack;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.statefulLayout)
    StatefulLayout mStatefulLayout;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    MapRouteOverlayPresenter mPresenter;

    private BaiduMap mBaiduMap;
    private int mOrderId;
    private String mCollectTime = "";

    private boolean isFirst = true;
    @Override
    public int initContentView() {
        return R.layout.activity_map_route_overlay;
    }

    @Override
    public void initInjector() {
        DaggerMapRouteOverlayComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        mPresenter.attachView(this);
        int type = getIntent().getIntExtra("type", -1);
        mOrderId = getIntent().getIntExtra("orderId", -1);
        if (type == 1) {
            mTextTitle.setText("车辆行驶轨迹");
        } else if (type == 2) {
            mTextTitle.setText("车辆当前位置");
        }
        mImageBack.setImageResource(R.drawable.btn_back);
        mImageBack.setVisibility(View.VISIBLE);
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBaiduMap = mBaiduMapView.getMap();
        mBaiduMap.clear();

        disposables.add(Observable.interval(0, 15, TimeUnit.SECONDS)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long aLong) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.carAddress(mOrderId, mCollectTime,isFirst);
                                isFirst = false;
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    List<LatLng> latLngPolygon = new ArrayList<>();

    /**
     * 绘制折线图
     *
     * @param carAddressBeen
     * @param isSingle
     */
    private void drawLineChart(List<CarAddressEntity.CarAddressBean> carAddressBeen, boolean isSingle) {

        if (isSingle) { //单点
            if (carAddressBeen != null && carAddressBeen.size() == 1) {

                double latstart = carAddressBeen.get(0).getLatitude();
                double lngstart = carAddressBeen.get(0).getLongitude();
                // 终点位置
                LatLng endPosition = new LatLng(latstart, lngstart);
                addOverLayout(endPosition);
                mapZoomStatus(endPosition);
                return;
            }
        } else {
            // 只有一个点的时候直接放一个覆盖物
            if (carAddressBeen.size() == 1) {

                double latstart = carAddressBeen.get(0).getLatitude();
                double lngstart = carAddressBeen.get(0).getLongitude();
                // 终点位置
                LatLng endPosition = new LatLng(latstart, lngstart);
                addOverLayout(endPosition);
                mapZoomStatus(endPosition);
                return;
            }

            double sumLat = 0;
            double sumLng = 0;
            double minLat = 1000;
            double minLng = 1000;
            double maxLat = 0;
            double maxLng = 0;
            // 多个点的时候绘制路径
            //向latLngPolygon中添加获取到的所有坐标点
            for (int i = 0; i < carAddressBeen.size(); i++) {
                double latstart = carAddressBeen.get(i).getLatitude();
                double lngstart = carAddressBeen.get(i).getLongitude();
                LatLng latLng = new LatLng(latstart, lngstart);
                latLngPolygon.add(latLng);
                double latitude = carAddressBeen.get(i).getLatitude();
                double longitude = carAddressBeen.get(i).getLongitude();
                sumLat += latitude;
                sumLng += longitude;
                if (latitude >= maxLat) {
                    maxLat = latitude;
                }
                if (latitude <= minLat) {
                    minLat = latitude;
                }

                if (longitude >= maxLng) {
                    maxLng = longitude;
                }
                if (longitude <= minLng) {
                    minLng = longitude;
                }
            }

            //获取起点和终点以及计算中心点
            double latstart = carAddressBeen.get(0).getLatitude();
            double lngstart = carAddressBeen.get(0).getLongitude();
            double latend = carAddressBeen.get(carAddressBeen.size() - 1).getLatitude();
            double lngend = carAddressBeen.get(carAddressBeen.size() - 1).getLongitude();
            final double midlat = sumLat / carAddressBeen.size();
            final double midlon = sumLng / carAddressBeen.size();
            LatLng pointMid = new LatLng(midlat, midlon);// 中点
            LatLng pointStart = new LatLng(latstart, lngstart);// 最小点
            LatLng pointEnd = new LatLng(latend, lngend);// 最大点

            LatLng pointMin = new LatLng(minLat, minLng);// 最小点
            LatLng pointMax = new LatLng(maxLat, maxLng);// 最大点

            //设置地图缩放等级和中心点
            mapZoomStatus(pointMid, pointMin, pointMax);

            // 添加覆盖物
            addOverLayout(pointStart, pointEnd);

            //开始绘制路线
            drawMyRoute(latLngPolygon);
        }
    }

    /**
     * 设置地图缩放等级和中心点
     *
     * @param pointLng
     */
    private void mapZoomStatus(LatLng pointLng) {
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(pointLng, 15.0f);// 设置缩放比例
        mBaiduMap.animateMapStatus(u);
    }

    /**
     * 设置地图缩放等级和中心点
     *
     * @param pointMid
     * @param pointStart
     * @param pointEnd
     */
    private void mapZoomStatus(LatLng pointMid, LatLng pointStart, LatLng pointEnd) {
        //地图缩放等级
        int zoomLevel[] = {2000000, 1000000, 500000, 200000, 100000, 50000,
                25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0};
        // 计算两点之间的距离，重新设定缩放值，让全部marker显示在屏幕中。
        int distance = (int) DistanceUtil.getDistance(pointStart, pointEnd);
        int i;
        for (i = 0; i < 17; i++) {
            if (zoomLevel[i] < distance) {
                break;
            }
        }
        //根据起点和终点的坐标点计算出距离来对比缩放等级获取最佳的缩放值，用来得到最佳的显示折线图的缩放等级
        float zoom = i + 4;
        if (zoom > 18) {
            zoom = 18;
        }
        Logger.i("zoom = " + zoom);
        // 设置当前位置显示在地图中心
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(pointMid, zoom);// 设置缩放比例
        mBaiduMap.animateMapStatus(u);
    }


    /**
     * 添加覆盖物
     *
     * @param endPosition
     */
    private void addOverLayout(LatLng endPosition) {

        // 构建Marker图标
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.mipmap.bike_icon);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions optionEnd = new MarkerOptions().position(endPosition)
                .icon(bitmap1).zIndex(8).draggable(true);
        mBaiduMap.addOverlay(optionEnd);

    }

    /**
     * 添加覆盖物
     *
     * @param startPosition
     * @param endPosition
     */
    private void addOverLayout(LatLng startPosition, LatLng endPosition) {
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_st);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions optionStart = new MarkerOptions().position(startPosition)
                .icon(bitmap).zIndex(8).draggable(true);

        // 构建Marker图标
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.mipmap.bike_icon);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions optionEnd = new MarkerOptions().position(endPosition)
                .icon(bitmap1).zIndex(8).draggable(true);

        mOptionEnd = optionEnd;
        // 在地图上添加Marker，并显示
        List<OverlayOptions> overlay = new ArrayList<OverlayOptions>();
        overlay.add(optionStart);
        overlay.add(optionEnd);
        mBaiduMap.addOverlays(overlay);

    }

    private OverlayOptions mOptionEnd;

    /**
     * 根据数据绘制轨迹
     *
     * @param points2
     */
    protected void drawMyRoute(List<LatLng> points2) {
        OverlayOptions options = new PolylineOptions().color(0xAAFF0000)
                .width(10).points(points2);
        mBaiduMap.addOverlay(options);
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
                            mPresenter.carAddress(mOrderId, "", true);
                        }
                    }).build();
            mStatefulLayout.showError(statusfulConfig);
        } else if (type == 0) {
            mStatefulLayout.showContent();
        }
    }

    private List<CarAddressEntity.CarAddressBean> mCarAddressBeen = new ArrayList<>();

    @Override
    public void carAddressResult(CarAddressEntity carAddressEntity) {
        int status = carAddressEntity.getStatus();
        mCollectTime = carAddressEntity.getCollectTime();
        mBaiduMap.clear();
        if (status == 1) {//单点
            List<CarAddressEntity.CarAddressBean> data = carAddressEntity.getData();
            drawLineChart(data, true);
        } else {//多点连线
            if (carAddressEntity.getData() != null && carAddressEntity.getData().size() > 0) {
                mCarAddressBeen.clear();
                mCarAddressBeen.addAll(carAddressEntity.getData());
                drawLineChart(mCarAddressBeen, false);
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        disposables.clear();
    }

}