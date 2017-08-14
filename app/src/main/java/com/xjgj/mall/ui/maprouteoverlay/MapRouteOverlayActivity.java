package com.xjgj.mall.ui.maprouteoverlay;

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
import com.xjgj.mall.bean.OrderDetailEntity;
import com.xjgj.mall.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lh on 2017/8/14.
 * 司机行驶轨迹
 */

public class MapRouteOverlayActivity extends BaseActivity {


    @BindView(R.id.baiduMapView)
    MapView mBaiduMapView;

    private BaiduMap mBaiduMap;

    @Override
    public int initContentView() {
        return R.layout.activity_map_route_overlay;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initUiAndListener() {
        mBaiduMap = mBaiduMapView.getMap();
        mBaiduMap.clear();

        List<OrderDetailEntity.CarAddressBean> addressList = (List<OrderDetailEntity.CarAddressBean>) getIntent().getSerializableExtra("addressList");
        if (addressList != null && addressList.size() > 0) {
            drawLineChart(addressList);
        }
    }

    List<LatLng> latLngPolygon = new ArrayList<>();

    /**
     * 绘制折线图
     * @param carAddressBeen
     */
    private void drawLineChart(List<OrderDetailEntity.CarAddressBean> carAddressBeen) {
        //向latLngPolygon中添加获取到的所有坐标点
        for (int i = 0; i < carAddressBeen.size(); i++) {
            double latstart = carAddressBeen.get(i).getLatitude();
            double lngstart = carAddressBeen.get(i).getLongitude();
            LatLng pt1 = new LatLng(latstart, lngstart);
            Logger.i("lat:"+latstart+",lng:"+lngstart);
            latLngPolygon.add(pt1);
        }
        //获取起点和终点以及计算中心点
        double latstart = carAddressBeen.get(0).getLatitude();
        double lngstart = carAddressBeen.get(0).getLongitude();
        double latend = carAddressBeen.get(carAddressBeen.size() - 1).getLatitude();
        double lngend = carAddressBeen.get(carAddressBeen.size() - 1).getLongitude();

        final double midlat = (latstart + latend) / 2;
        final double midlon = (lngstart + lngend) / 2;
        LatLng point = new LatLng(midlat, midlon);// 中点
        LatLng point1 = new LatLng(latstart, lngstart);// 起点
        LatLng point2 = new LatLng(latend, lngend);// 终点

        //地图缩放等级
        int zoomLevel[] = {2000000, 1000000, 500000, 200000, 100000, 50000,
                25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0};
        // 计算两点之间的距离，重新设定缩放值，让全部marker显示在屏幕中。
        int jl = (int) DistanceUtil.getDistance(point1, point2);

        int i;
        for (i = 0; i < 17; i++) {
            if (zoomLevel[i] < jl) {
                break;
            }
        }
        //根据起点和终点的坐标点计算出距离来对比缩放等级获取最佳的缩放值，用来得到最佳的显示折线图的缩放等级
        float zoom = i + 2;
        // 设置当前位置显示在地图中心
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(point, zoom);// 设置缩放比例
        mBaiduMap.animateMapStatus(u);

        /**
         * 创建自定义overlay
         */
        // 起点位置
        LatLng geoPoint = new LatLng(latstart, lngstart);
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_st);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(geoPoint)
                .icon(bitmap).zIndex(8).draggable(true);

        // 终点位置
        LatLng geoPoint1 = new LatLng(latend, lngend);
        // 构建Marker图标
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_en);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option1 = new MarkerOptions().position(geoPoint1)
                .icon(bitmap1).zIndex(8).draggable(true);
        // 在地图上添加Marker，并显示

        List<OverlayOptions> overlay = new ArrayList<OverlayOptions>();
        overlay.add(option);
        overlay.add(option1);
        mBaiduMap.addOverlays(overlay);

        //开始绘制
        drawMyRoute(latLngPolygon);
    }

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

}
