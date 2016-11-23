package com.wisegps.clzx.fragment.viewhelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

import com.wisegps.clzx.fragment.maphelper.BaiduMapHepler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class BaiduMapViewHepler{

    private final String TAG = "BaiduMapViewHepler";
    private Activity mContext;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduMapHepler mBaiduMapHepler;
    private BaiMarkerInfoWindower infoWindower;

    public BaiduMapViewHepler(Activity context, MapView mapView){
        this.mContext = context;
        this.mMapView = mapView;
        this.mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.getUiSettings().setCompassEnabled(false);
        mBaiduMap.getUiSettings().setCompassEnabled(true);
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
        infoWindower = new BaiMarkerInfoWindower(mContext);
        mBaiduMapHepler = new BaiduMapHepler(context,mBaiduMap);

    }


    public void drawline(){
        //定义多边形的五个顶点
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        mBaiduMapHepler.drawLine(pts, Color.BLUE,5);
    }

    public void addMarker(){
//        LatLng pt1 = new LatLng(22.583268, 113.918371);//高德定位
//        LatLng pt2 = new LatLng(22.586011, 113.913421);//原生定位

//        mBaiduMapHepler.addMarker(pt1);
//        mBaiduMapHepler.addMarker(pt2);


        //tencent lat: 22.58318lon :113.918321

        LatLng pt1 = new LatLng(22.5830891927, 113.91820176866);//高德定位
        LatLng pt2 = new LatLng(22.585851666666, 113.912975);//原生定位
        LatLng pt3 = new LatLng(22.58318, 113.918321);//腾讯
        // 总结 腾讯和高德定位出来的基本一样
        mBaiduMapHepler.addMarker(pt1);
        mBaiduMapHepler.addMarker(pt2);
        mBaiduMapHepler.addMarker(pt3);
    }

    public void setMapTriffic(boolean enable){
        mBaiduMapHepler.setTrafficEnabled(enable);
    }
    public void setMapNormalType(){
        mBaiduMapHepler.setMapType(BaiduMapHepler.MAP_TYPE_NORMAL);
    }
    public void setMapSatelliteType(){
        mBaiduMapHepler.setMapType(BaiduMapHepler.MAP_TYPE_SATELLITE);
    }

    public void setTargetCenter(LatLng target, int zoom){
        mBaiduMapHepler.animateCamera(target,zoom);
        mBaiduMapHepler.addMarker(target);
    }


    Marker marker;
    public void setMyLocation(LatLng target, int zoom){
        if(marker!=null)
            marker.remove();
        mBaiduMapHepler.animateCamera(target,zoom);
        marker = mBaiduMapHepler.addMarker(target);
    }


    /**
     * 汽车标注点击事件
     */
    private BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        public boolean onMarkerClick(Marker marker) {
            showMarkerInfoWindow(marker);
            return false;
        }
    };



    View markerWindow;
    private void showMarkerInfoWindow(Marker marker){
        markerWindow = infoWindower.getView();
        // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow carInfoWindow = new InfoWindow(markerWindow, marker.getPosition(), 0);
        // 显示InfoWindow
        mBaiduMap.showInfoWindow(carInfoWindow);
    }

}
