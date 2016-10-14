package com.wisegps.clzx.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/2.
 */
public class SystemTools {

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return "版本：CLZX_V" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param context
     * @param msg
     */
    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    /**
     * int 数组转换成字符串
     * @param status
     * @return
     */
    public static String array2String(int [] status){
        if(status!=null && status.length>0){
            StringBuffer buffer = new StringBuffer();
            for(int i=0;i<status.length;i++){
                buffer.append(status[i]);
                buffer.append(",");
            }
            return buffer.substring(0, buffer.length()-1);
        }
        return "";

    }

    /**
     * 字符串转换成int数组
     * @param str
     * @return
     */
    public static int[] string2Array(String str){
        if(str == null || str.length() ==0){
            return null;
        }
        String[] strArray =  str.split(",");
        int[] ary = null;
        if(strArray!=null& strArray.length>0){
            ary = new int[strArray.length];
            for(int i=0;i<strArray.length;i++){
                ary[i] = Integer.parseInt(strArray[i]);
            }
        }
        return ary;
    }


    public static String getStartTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String str = sdf.format(date);
        return str;
    }

    public static String getStopTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:00");
        String str = sdf.format(date);
        return str;
    }


    public static String getEncodeTime(long current){
        Date date = new Date(current);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(date);
        return URLEncoder.encode(str);
    }

    public static String getStringTime(long current){
        Date date = new Date(current);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(date);
        return str;
    }

    public static String getCurrentStringTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String str = sdf.format(date);
        return str;
    }

    /**
     * @param date
     * @return
     */
    public static String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 正则表达式判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
