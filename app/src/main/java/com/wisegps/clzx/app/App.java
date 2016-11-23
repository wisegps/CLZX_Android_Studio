package com.wisegps.clzx.app;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Administrator on 2016/9/6.
 */
public class App extends Application {

    public String userId;
    public String token;

    public LatLng mlatLng;//我的位置


    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
    }
}
