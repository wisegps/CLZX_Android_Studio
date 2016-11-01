package com.wisegps.clzx.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 系统设置参数
 * Created by Administrator on 2016/10/20.
 */
public class SharePreferenceData {
    private Context mContext;
    private final String SETTING = "Setting";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String IS_AUTO = "Auto_Update_Time";


    public SharePreferenceData(Context context){
        this.mContext = context;
        this.sp = context.getSharedPreferences(SETTING,Context.MODE_PRIVATE);
        this.editor = sp.edit();
    }
    public void setAutoUpdateTime(int s){
        editor.putInt(IS_AUTO,s);
        editor.commit();
    }

    public int getAutoUpdateTime(){
        int time = sp.getInt(IS_AUTO,10);
        return time;
    }






}
