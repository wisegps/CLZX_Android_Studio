package com.wisegps.clzx.activity.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wicare.wistorm.api.WVehicleApi;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
import com.wicare.wistorm.zbar.CaptureActivity;
import com.wisegps.clzx.R;
import com.wisegps.clzx.activity.SearchResultActivity;
import com.wisegps.clzx.app.App;
import com.wisegps.clzx.utils.SystemTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绑定设备
 * Created by Administrator on 2016/9/22.
 */
public class DeviceBindActivity extends AppCompatActivity {


    @Bind(R.id.et_device_id)
    MaterialEditText etDeviceId;

    private Activity mContext;
    private App app;
    private WVehicleApi vehicleApi;

    private String objectId;//车辆id
    private String did;//设备id
    private final int REQUEST_BING_DEVICE_CODE=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device);
        ButterKnife.bind(this);
        initView();
        initWistorm();
        CaptureActivity.startAction(mContext,REQUEST_BING_DEVICE_CODE);
    }

    private void initWistorm() {
        BaseVolley.init(this);
        vehicleApi = new WVehicleApi(this);
    }
    /**
     * 标题和返回箭头
     */
    private void initView() {
        ButterKnife.bind(this);
        mContext = this;
        app = (App) getApplication();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        objectId = getIntent().getStringExtra("objectId");
        Logger.d("车辆id : " + objectId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }

    /**
     * @param context
     */
    public static void startAction(Activity context, int requestCode,String objectId) {
        Intent intent = new Intent(context, DeviceBindActivity.class);
        intent.putExtra("objectId",objectId);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bind_device_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_scan) {
            CaptureActivity.startAction(mContext,REQUEST_BING_DEVICE_CODE);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 取得返回信息
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            etDeviceId.setText(scanResult);
        }
    }


    @OnClick(R.id.btn_confirm)
    public void onClick() {
        did = etDeviceId.getText().toString().trim();
        if(TextUtils.isEmpty(did)){
            SystemTools.showToast(mContext,getResources().getString(R.string.no_content));
            return;
        }else{
            bindDevice(did);
        }
    }

    /**
     * @param did
     */
    private void bindDevice(String did){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", app.token);
        params.put("_objectId", objectId);
        params.put("did", did);//更新设备id
        vehicleApi.update(params, new OnSuccess() {
            @Override
            protected void onSuccess(String response) {
                Logger.d("更新车辆返回信息 ：" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if("0".equals(object.getString("status_code"))){
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        SystemTools.showToast(mContext,getResources().getString(R.string.bind_device_failed));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                更新车辆返回信息 ：{"status_code":0,"n":1,"nModified":1}
            }
        }, new OnFailure() {
            @Override
            protected void onFailure(VolleyError error) {

            }
        });
    }
}
