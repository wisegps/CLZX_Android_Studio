package com.wisegps.clzx.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.map.A;
import com.orhanobut.logger.Logger;
import com.wisegps.clzx.R;
import com.wisegps.clzx.app.App;
import com.wisegps.clzx.fragment.viewhelper.BaiduMapViewHepler;
import com.wisegps.clzx.utils.SystemTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2016/10/14.
 */
public class BaiduMapFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

    @Bind(R.id.bmapView)
    MapView mMapView;

    private static BaiduMapFragment mapFragment = null;

    @Bind(R.id.iv_map_type)
    ImageView ivMapType;
    @Bind(R.id.iv_map_triffic)
    ImageView ivMapTriffic;
    @Bind(R.id.iv_map_myloc)
    ImageView ivMapMyloc;

    private Activity mContext;
    private App app;
    private static final int RC_LOCATION_PERM = 124;
    private static final int RC_SETTINGS_SCREEN = 125;
    private BaiduMapViewHepler mBaiduMapViewHepler;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    /**
     * 实例化
     *
     * @return
     */
    public static BaiduMapFragment getInstance() {
        if (mapFragment == null) {
            mapFragment = new BaiduMapFragment();
        }
        return mapFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baidu_map, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        app = (App)getActivity().getApplication();
        initBaiMap();
        return view;
    }


    private void initBaiMap() {
        View child;// 隐藏logo
        for(int i=0;i<mMapView.getChildCount();i++){
            child = mMapView.getChildAt(i);
            Logger.d(" VIEW : " + child);
            if (child != null && child instanceof ImageView){
                child.setVisibility(View.INVISIBLE);
            }
        }
        ivMapTriffic.setTag("OFF");
        EventBus.getDefault().register(this);
        mBaiduMapViewHepler = new BaiduMapViewHepler(mContext, mMapView);
        mLocationClient = new LocationClient(mContext);     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            locationTask();
        }else{
            initLocation();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void helloEventBus(BaiduMapEvent event) {
        SystemTools.showToast(mContext, "收到事件");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mLocationClient.unRegisterLocationListener(myListener);
    }

    @OnClick({R.id.iv_map_type, R.id.iv_map_triffic, R.id.iv_map_myloc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_map_type:
                showPopMapLayers();
                break;
            case R.id.iv_map_triffic:
                setMapTriffic();
                break;
            case R.id.iv_map_myloc:
                if(app.mlatLng!=null)
                mBaiduMapViewHepler.setMyLocation(app.mlatLng,15);
                break;
        }
    }

    /**
     * 地图实时路况切换
     */
    private void setMapTriffic(){
        if(ivMapTriffic.getTag().equals("ON")){
            ivMapTriffic.setTag("OFF");
            mBaiduMapViewHepler.setMapTriffic(false);
            ivMapTriffic.setImageResource(R.drawable.ic_triffic_off);
            SystemTools.showToast(mContext,getResources().getString(R.string.map_triffic_off));
        }else{
            ivMapTriffic.setTag("ON");
            mBaiduMapViewHepler.setMapTriffic(true);
            ivMapTriffic.setImageResource(R.drawable.ic_triffic_on);
            SystemTools.showToast(mContext,getResources().getString(R.string.map_triffic_on));
        }
    }

    TextView tvSate;
    TextView tvNarmal;
    /**
     * 设置地图类型
     */
    private void showPopMapLayers() {
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View popunwindwow = mLayoutInflater.inflate(R.layout.item_map_type_popwindow, null);
        final PopupWindow mPopupWindow = new PopupWindow(popunwindwow, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(ivMapMyloc, 0, 0);
        ImageView ivClose = (ImageView)popunwindwow.findViewById(R.id.iv_close);
        tvNarmal = (TextView) popunwindwow.findViewById(R.id.tv_map_normal);
        tvSate = (TextView) popunwindwow.findViewById(R.id.tv_map_sate);
        final CheckBox iv_satellite = (CheckBox) popunwindwow.findViewById(R.id.iv_satellite);
        final CheckBox iv_normal = (CheckBox) popunwindwow.findViewById(R.id.iv_noamal);
        int mapType = mMapView.getMap().getMapType();
        if(mapType == 1){
            iv_normal.setChecked(true);
            tvNarmal.setTextColor(getResources().getColor(R.color.blue_theme));
            tvSate.setTextColor(getResources().getColor(R.color.color_666666));
        }else{
            iv_satellite.setChecked(true);
            tvNarmal.setTextColor(getResources().getColor(R.color.color_666666));
            tvSate.setTextColor(getResources().getColor(R.color.blue_theme));
        }
        iv_satellite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mBaiduMapViewHepler.setMapSatelliteType();
                    iv_satellite.setChecked(true);
                    iv_normal.setChecked(false);
                    tvSate.setTextColor(getResources().getColor(R.color.blue_theme));
                }else {
                    mBaiduMapViewHepler.setMapNormalType();
                    iv_normal.setChecked(true);
                    iv_satellite.setChecked(false);
                    tvSate.setTextColor(getResources().getColor(R.color.color_666666));
                }
            }
        });
        iv_normal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mBaiduMapViewHepler.setMapNormalType();
                    iv_satellite.setChecked(false);
                    iv_normal.setChecked(true);
                    tvNarmal.setTextColor(getResources().getColor(R.color.blue_theme));
                }else {
                    mBaiduMapViewHepler.setMapSatelliteType();
                    iv_normal.setChecked(false);
                    iv_satellite.setChecked(true);
                    tvNarmal.setTextColor(getResources().getColor(R.color.color_666666));
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }


    @AfterPermissionGranted(RC_LOCATION_PERM)
    public void locationTask() {
        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION ,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            // Have permissions, do the thing!
            initLocation();
        } else {
            // Ask for both permissions
            Logger.d( "Ask for both permissions ----------");
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location_contacts),
                    RC_LOCATION_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        Logger.d( "onRequestPermissionsResult---------");
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.d("onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Logger.d( "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_SETTINGS_SCREEN).build().show();
        }
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(50000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location!=null){
                Logger.d("LAT : " + location.getLatitude()+"\n" +
                        "LON : " + location.getLongitude() );
                app.mlatLng = new LatLng(location.getLatitude(),location.getLongitude());
            }
        }
    }
}
