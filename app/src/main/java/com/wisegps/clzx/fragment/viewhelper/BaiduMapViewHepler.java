package com.wisegps.clzx.fragment.viewhelper;

import android.content.Context;
import android.graphics.Color;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.wisegps.clzx.fragment.maphelper.BaiduMapHepler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class BaiduMapViewHepler {

    private final String TAG = "BaiduMapViewHepler";
    private Context mContext;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduMapHepler mBaiduMapHepler;


    public BaiduMapViewHepler(Context context, MapView mapView){
        this.mContext = context;
        this.mMapView = mapView;
        this.mBaiduMap = mapView.getMap();
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
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        mBaiduMapHepler.addMarker(pt1);
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


}
