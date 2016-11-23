package com.wisegps.clzx.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.orhanobut.logger.Logger;
import com.wisegps.clzx.R;
import com.wisegps.clzx.app.App;
import com.wisegps.clzx.fragment.maphelper.BaiduMapHepler;
import com.wisegps.clzx.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆追踪页面
 * Created by Administrator on 2016/11/22.
 */
public class TrackingActivity extends AppCompatActivity {



    @Bind(R.id.bmapView)
    MapView mMapView;
    @Bind(R.id.iv_map_type)
    ImageView ivMapType;
    @Bind(R.id.iv_map_triffic)
    ImageView ivMapTriffic;
    @Bind(R.id.iv_map_myloc)
    ImageView ivMapMyloc;

    private App app;
    private Activity mContext;
    private BaiduMapHepler mBaiduMapHepler;
    private BaiduMap mBaiduMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_baidu_map);
        mContext = this;
        app = (App)getApplication();
        ButterKnife.bind(this);
        initBaiMap();
    }

    private void initBaiMap() {
        View child;// 隐藏logo
        for(int i=0;i<mMapView.getChildCount();i++){
            child = mMapView.getChildAt(i);
            if (child != null && child instanceof ImageView){
                child.setVisibility(View.INVISIBLE);
            }
        }
        ivMapMyloc.setVisibility(View.GONE);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.getUiSettings().setCompassEnabled(false);
        mBaiduMap.getUiSettings().setCompassEnabled(true);
        mBaiduMapHepler = new BaiduMapHepler(mContext,mBaiduMap);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        ivMapTriffic.setTag("OFF");
    }
    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }

    /**
     * @param context
     */
    public static void startAction(Activity context, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, TrackingActivity.class);
        if (bundle!=null)
            intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
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
                    break;
        }
    }


    /**
     * 地图实时路况切换
     */
    private void setMapTriffic(){
        if(ivMapTriffic.getTag().equals("ON")){
            ivMapTriffic.setTag("OFF");
            mBaiduMapHepler.setTrafficEnabled(false);
            ivMapTriffic.setImageResource(R.drawable.ic_triffic_off);
            SystemTools.showToast(mContext,getResources().getString(R.string.map_triffic_off));
        }else{
            ivMapTriffic.setTag("ON");
            mBaiduMapHepler.setTrafficEnabled(true);
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
                    mBaiduMapHepler.setMapType(BaiduMapHepler.MAP_TYPE_SATELLITE);
                    iv_satellite.setChecked(true);
                    iv_normal.setChecked(false);
                    tvSate.setTextColor(getResources().getColor(R.color.blue_theme));
                }else {
                    mBaiduMapHepler.setMapType(BaiduMapHepler.MAP_TYPE_NORMAL);
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
                    mBaiduMapHepler.setMapType(BaiduMapHepler.MAP_TYPE_NORMAL);
                    iv_satellite.setChecked(false);
                    iv_normal.setChecked(true);
                    tvNarmal.setTextColor(getResources().getColor(R.color.blue_theme));
                }else {
                    mBaiduMapHepler.setMapType(BaiduMapHepler.MAP_TYPE_SATELLITE);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
