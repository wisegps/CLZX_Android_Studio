package com.wisegps.clzx.fragment.maphelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.orhanobut.logger.Logger;
import com.wisegps.clzx.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class BaiduMapHepler {

    private Activity mContext;
    private BaiduMap mBaiduMap;

    public static final int MAP_TYPE_NORMAL    = 1;//普通街道地图
    public static final int MAP_TYPE_SATELLITE = 2;//卫星视图

    public BaiduMapHepler(Activity context, BaiduMap baiduMap){
        this.mContext = context;
        this.mBaiduMap = baiduMap;
    }

    public void setTrafficEnabled(boolean enabled){
        mBaiduMap.setTrafficEnabled(enabled);
    }

    /**
     * @param mapType 1:普通街道视图 2:卫星视图 （默认是normal）
     */
    public void setMapType(int mapType){
        switch (mapType) {
            case 1:
                mBaiduMap.setMapType(MAP_TYPE_NORMAL);
                break;
            case 2:
                mBaiduMap.setMapType(MAP_TYPE_SATELLITE);
                break;
            default:
                mBaiduMap.setMapType(MAP_TYPE_NORMAL);
                break;
        }
    }



    /**
     * @param target 设置要移动镜头的目标（移动这个点会在地图中心）
     * @param zoom 设置缩放级别
     */
    public void animateCamera(LatLng target, int zoom){
//        int targetScreenX = mBaiduMap.getMapStatus().targetScreen.x;// 地图操作中心点在屏幕中的坐标x
//        int targetScreenY = mBaiduMap.getMapStatus().targetScreen.y;// 地图操作中心点在屏幕中的坐标y
//        Logger.d("dm : " + screenWidth + " --" + screenHeigh + "\n"
//        + "tar : " + targetScreenX + " --" + targetScreenY);
//        Point point = new Point(targetScreenX,targetScreenY+100);
        MapStatus.Builder builer = new MapStatus.Builder();
        MapStatus mMapStatus = builer
                .target(target)
                .zoom(zoom)
//                .targetScreen(point)
                .build();// 定义地图状态
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        //mBaiduMap.setMapStatus(mMapStatusUpdate);// 改变地图状态
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
    }

    /**
     * @param target 坐标
     * @param drawable marker 图标
     * @return
     */
    public Marker addMarker(LatLng target, int drawable){
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(drawable);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions().position(target).icon(bitmap);
        //在地图上添加Marker，并显示
        return (Marker) (mBaiduMap.addOverlay(options));
    }

    /**
     * @param target 坐标
     * @return
     */
    public Marker addMarker(LatLng target){
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_default_marker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions().position(target).icon(bitmap);
        //在地图上添加Marker，并显示
        return (Marker) (mBaiduMap.addOverlay(options));
    }

    /**
     * @param points
     * @param color
     * @param width
     */
    public void drawLine(List<LatLng> points,int color,int width){
        OverlayOptions polyline = new PolylineOptions()
                .color(color)
                .width(width)
                .points(points);
        mBaiduMap.addOverlay(polyline);
    }

    /**
     * 清除地图上面的东西
     */
    public void clear(){
        mBaiduMap.clear();
    }


    /**
     * @param marker 移除marker
     */
    public void removeMarker(Marker marker){
        marker.remove();
    }


}
