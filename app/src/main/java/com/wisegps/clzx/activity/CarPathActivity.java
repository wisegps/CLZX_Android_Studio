package com.wisegps.clzx.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ZoomControls;

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
 * 轨迹回放页面
 * Created by Administrator on 2016/11/22.
 */
public class CarPathActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{


    @Bind(R.id.bmapView)
    MapView mMapView;

    @Bind(R.id.iv_map_triffic)
    ImageView ivMapTriffic;
    @Bind(R.id.iv_map_myloc)
    ImageView ivMapMyloc;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.p_play_pressbar)
    SeekBar pPlayPressbar;
    @Bind(R.id.ll_player)
    LinearLayout llPlayer;

    private App app;
    private Activity mContext;
    private BaiduMapHepler mBaiduMapHepler;
    private BaiduMap mBaiduMap;
    private final int HALF_SECOND = 500;
    private final int UPDATE_SEEKBAR = 0;
    private final int SEEKBAR_FINISH = 1;
    private int maxProgress = 10;
    private int currentProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_baidu_map);
        mContext = this;
        app = (App) getApplication();
        ButterKnife.bind(this);
        initView();
        initBaiMap();
    }

    /**
     * initial view
     */
    private void initView(){
        View child;// 隐藏logo
        for(int i=0;i<mMapView.getChildCount();i++){
            child = mMapView.getChildAt(i);
            Logger.d(" VIEW : " + child);
            if (child != null && child instanceof ImageView){
                child.setVisibility(View.INVISIBLE);
            }
        }
        // 不显示地图上比例尺
        mMapView.showScaleControl(false);
        llPlayer.setVisibility(View.VISIBLE);
        ivMapMyloc.setVisibility(View.GONE);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        ivMapTriffic.setTag("OFF");
        pPlayPressbar.setOnSeekBarChangeListener(this);
    }

    /**
     * initial map
     */
    private void initBaiMap() {
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        mBaiduMap.getUiSettings().setCompassEnabled(false);
        mBaiduMap.getUiSettings().setCompassEnabled(true);
        mBaiduMapHepler = new BaiduMapHepler(mContext, mBaiduMap);
    }

    /**
     * @param context
     */
    public static void startAction(Activity context, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, CarPathActivity.class);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 控制播放轨迹
     */
    private void playContrl(){
        if(isFinish){
            isFinish = false;
            pPlayPressbar.setProgress(0);
            if(isPlay){
                pPlayPressbar.setMax(maxProgress);
                ivPlay.setImageResource(R.drawable.ic_car_path_stop);
                mHandler.postDelayed(mTasks, 0);
            }else{
                ivPlay.setImageResource(R.drawable.ic_car_path_play);
                mHandler.removeCallbacks(mTasks);
            }
        }else {
            if(isPlay){
                pPlayPressbar.setMax(maxProgress);
                ivPlay.setImageResource(R.drawable.ic_car_path_stop);
                mHandler.postDelayed(mTasks, 0);
            }else{
                ivPlay.setImageResource(R.drawable.ic_car_path_play);
                mHandler.removeCallbacks(mTasks);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b){
            currentProgress = i;
            mHandler.sendEmptyMessage(UPDATE_SEEKBAR);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    boolean isFinish = false;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_SEEKBAR:
                    if(currentProgress < maxProgress){
                        isFinish=false;
                        pPlayPressbar.setProgress(currentProgress);
                    }else if(currentProgress == maxProgress){
                        pPlayPressbar.setProgress(currentProgress);
                        currentProgress = 0;
                        mHandler.removeCallbacks(mTasks);
                        mHandler.sendEmptyMessage(SEEKBAR_FINISH);
                    }
                    break;
                case SEEKBAR_FINISH:
                    isFinish = true;
                    isPlay = false;
                    ivPlay.setImageResource(R.drawable.ic_car_path_play);
                    break;
            }
        }
    };


    int time = 0;
    /**
     * 定时提交数据
     */
    private Runnable mTasks = new Runnable(){
        public void run(){
            time ++;
            if(time == 2){
                time = 0;
                currentProgress ++;
                mHandler.sendEmptyMessage(UPDATE_SEEKBAR);
            }
            mHandler.postDelayed(mTasks, HALF_SECOND);
        }
    };

    boolean isPlay = false;
    @OnClick({R.id.iv_map_type, R.id.iv_map_triffic, R.id.iv_map_myloc,R.id.iv_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_map_type:
                showPopMapLayers();
                break;
            case R.id.iv_map_triffic:
                setMapTriffic();
                break;
            case R.id.iv_play:
                isPlay = !isPlay;
                playContrl();
                break;
        }
    }
    /**
     * 地图实时路况切换
     */
    private void setMapTriffic() {
        if (ivMapTriffic.getTag().equals("ON")) {
            ivMapTriffic.setTag("OFF");
            mBaiduMapHepler.setTrafficEnabled(false);
            ivMapTriffic.setImageResource(R.drawable.ic_triffic_off);
            SystemTools.showToast(mContext, getResources().getString(R.string.map_triffic_off));
        } else {
            ivMapTriffic.setTag("ON");
            mBaiduMapHepler.setTrafficEnabled(true);
            ivMapTriffic.setImageResource(R.drawable.ic_triffic_on);
            SystemTools.showToast(mContext, getResources().getString(R.string.map_triffic_on));
        }
    }

    TextView tvSate;
    TextView tvNarmal;

    /**
     * 设置地图类型
     */
    private void showPopMapLayers() {/**/
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View popunwindwow = mLayoutInflater.inflate(R.layout.item_map_type_popwindow, null);
        final PopupWindow mPopupWindow = new PopupWindow(popunwindwow, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAsDropDown(ivMapMyloc, 0, 0);
        ImageView ivClose = (ImageView) popunwindwow.findViewById(R.id.iv_close);
        tvNarmal = (TextView) popunwindwow.findViewById(R.id.tv_map_normal);
        tvSate = (TextView) popunwindwow.findViewById(R.id.tv_map_sate);
        final CheckBox iv_satellite = (CheckBox) popunwindwow.findViewById(R.id.iv_satellite);
        final CheckBox iv_normal = (CheckBox) popunwindwow.findViewById(R.id.iv_noamal);
        int mapType = mMapView.getMap().getMapType();
        if (mapType == 1) {
            iv_normal.setChecked(true);
            tvNarmal.setTextColor(getResources().getColor(R.color.blue_theme));
            tvSate.setTextColor(getResources().getColor(R.color.color_666666));
        } else {
            iv_satellite.setChecked(true);
            tvNarmal.setTextColor(getResources().getColor(R.color.color_666666));
            tvSate.setTextColor(getResources().getColor(R.color.blue_theme));
        }
        iv_satellite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mBaiduMapHepler.setMapType(BaiduMapHepler.MAP_TYPE_SATELLITE);
                    iv_satellite.setChecked(true);
                    iv_normal.setChecked(false);
                    tvSate.setTextColor(getResources().getColor(R.color.blue_theme));
                } else {
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
                if (b) {
                    mBaiduMapHepler.setMapType(BaiduMapHepler.MAP_TYPE_NORMAL);
                    iv_satellite.setChecked(false);
                    iv_normal.setChecked(true);
                    tvNarmal.setTextColor(getResources().getColor(R.color.blue_theme));
                } else {
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
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
