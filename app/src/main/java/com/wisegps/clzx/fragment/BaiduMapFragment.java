package com.wisegps.clzx.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.baidu.mapapi.map.MapView;
import com.wisegps.clzx.R;
import com.wisegps.clzx.fragment.viewhelper.BaiduMapViewHepler;
import com.wisegps.clzx.utils.SystemTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/14.
 */
public class BaiduMapFragment extends Fragment {

    @Bind(R.id.bmapView)
    MapView mMapView;

    private static BaiduMapFragment mapFragment = null;
    @Bind(R.id.iv_map_type)
    ImageView ivMapType;
    @Bind(R.id.iv_map_triffic)
    ImageView ivMapTriffic;
    @Bind(R.id.iv_map_myloc)
    ImageView ivMapMyloc;
    private Context mContext;

    private BaiduMapViewHepler mBaiduMapViewHepler;

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
        initBaiMap();
        return view;
    }


    private void initBaiMap() {
        ivMapTriffic.setTag("OFF");
        EventBus.getDefault().register(this);
        mBaiduMapViewHepler = new BaiduMapViewHepler(mContext, mMapView);
        mBaiduMapViewHepler.drawline();
        mBaiduMapViewHepler.addMarker();
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




}
