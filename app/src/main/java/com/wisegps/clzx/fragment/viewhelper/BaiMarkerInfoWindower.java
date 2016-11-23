package com.wisegps.clzx.fragment.viewhelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wisegps.clzx.R;
import com.wisegps.clzx.activity.CarPathActivity;
import com.wisegps.clzx.activity.TrackingActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/21.
 */
public class BaiMarkerInfoWindower {

    TextView tvCarId;
    TextView tvTime;
    TextView tvCarState;
    TextView tvMiliage;
    TextView tvCarTrack;
    TextView tvCarPath;

    private Activity mContext;
    private View infoView;

    public BaiMarkerInfoWindower(Activity context) {
        this.mContext = context;
    }

    public View getView() {
        if (infoView == null) {
            infoView = LayoutInflater.from(mContext).inflate(R.layout.item_mark_info_window, null);
        }
        initView(infoView);
        return infoView;
    }


    private void initView(View view){
        tvCarId = (TextView)view.findViewById(R.id.tv_car_id);
        tvTime = (TextView)view.findViewById(R.id.tv_time);
        tvCarState = (TextView)view.findViewById(R.id.tv_car_state);
        tvMiliage = (TextView)view.findViewById(R.id.tv_miliage);
        tvCarTrack = (TextView)view.findViewById(R.id.tv_car_track);
        tvCarPath = (TextView)view.findViewById(R.id.tv_car_path);
        tvCarPath.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvCarPath.getPaint().setAntiAlias(true);//抗锯齿
        tvCarTrack.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvCarTrack.getPaint().setAntiAlias(true);//抗锯齿
        tvCarPath.setOnClickListener(onClickListener);
        tvCarTrack.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_car_track:
                    TrackingActivity.startAction(mContext,0,null);
                    break;
                case R.id.tv_car_path:
                    CarPathActivity.startAction(mContext,1,null);
                    break;
            }
        }
    };


}
