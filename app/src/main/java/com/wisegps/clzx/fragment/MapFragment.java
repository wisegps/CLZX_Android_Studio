package com.wisegps.clzx.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.wisegps.clzx.R;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/14.
 */
public class MapFragment extends Fragment {

    @Bind(R.id.bmapView)
    MapView mMapView;

    private static MapFragment mapFragment = null;
    private Context mContext;
    private BaiduMap baiduMap;
    /**
     * 实例化
     * @return
     */
    public static MapFragment getInstance() {
        if (mapFragment == null) {
            mapFragment = new MapFragment();
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
        baiduMap = mMapView.getMap();

    }

}
