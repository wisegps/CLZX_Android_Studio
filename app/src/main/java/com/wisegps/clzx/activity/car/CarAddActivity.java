package com.wisegps.clzx.activity.car;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wicare.wistorm.api.WDepartmentApi;
import com.wicare.wistorm.api.WVehicleApi;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
import com.wicare.wistorm.widget.pickerview.WTimePopupWindow;
import com.wisegps.clzx.R;
import com.wisegps.clzx.app.App;
import com.wisegps.clzx.utils.SystemTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/14.
 */
public class CarAddActivity extends AppCompatActivity {

    @Bind(R.id.et_car_name)
    MaterialEditText etCarName;
    @Bind(R.id.et_car_desc)
    MaterialEditText etCarDesc;
    @Bind(R.id.et_car_frameno)
    MaterialEditText etCarFrameno;
    @Bind(R.id.et_car_engineno)
    MaterialEditText etCarEngineno;
    @Bind(R.id.et_car_buy_date)
    MaterialEditText etCarBuyDate;

    @Bind(R.id.et_car_mileage)
    MaterialEditText etCarMileage;

    @Bind(R.id.et_car_maintainmileage)
    MaterialEditText etCarMaintainmileage;
    @Bind(R.id.et_car_insuranceExpireln)
    MaterialEditText etCarInsuranceExpireln;
    @Bind(R.id.et_car_inspectExpireln)
    MaterialEditText etCarInspectExpireln;
    @Bind(R.id.et_car_department)
    MaterialEditText etCarDepartment;

    private final int REQUEST_ADD_DEPARTMENT = 0;
    private App app;
    private Activity mContext;
    private WVehicleApi vehicleApi;
    private String departmentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cars);
        ButterKnife.bind(this);
        initView();
        initWistorm();
        app = (App) getApplication();
    }

    /**
     * 标题和返回箭头
     */
    private void initView() {
        mContext = this;
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        etCarBuyDate.setOnTouchListener(onTouchListener);
        etCarInspectExpireln.setOnTouchListener(onTouchListener);
        etCarInsuranceExpireln.setOnTouchListener(onTouchListener);
        etCarDepartment.setOnTouchListener(onTouchListener);
    }

    private void initWistorm() {
        BaseVolley.init(this);
        vehicleApi = new WVehicleApi(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }

    /**
     * @param context
     */
    public static void startAction(Activity context, int requestCode) {
        Intent intent = new Intent(context, CarAddActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 编辑框点击事件
     */
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.et_car_buy_date:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        WTimePopupWindow pwDate = new WTimePopupWindow(mContext, WTimePopupWindow.Type.YEAR_MONTH_DAY);
                        pwDate.setOnTimeSelectListener(new WTimePopupWindow.OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date) {
                                etCarBuyDate.setText(SystemTools.getDate(date));
                            }
                        });
                        if(!pwDate.isShowing()){
                            pwDate.showAtLocation(view, Gravity.BOTTOM, 0, 0,new Date());
                        }
                    }
                    break;
                case R.id.et_car_inspectExpireln:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        WTimePopupWindow pwDate = new WTimePopupWindow(mContext, WTimePopupWindow.Type.YEAR_MONTH_DAY);
                        pwDate.setOnTimeSelectListener(new WTimePopupWindow.OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date) {
                                etCarInspectExpireln.setText(SystemTools.getDate(date));
                            }
                        });
                        if(!pwDate.isShowing()){
                            pwDate.showAtLocation(view, Gravity.BOTTOM, 0, 0,new Date());
                        }
                    }
                    break;
                case R.id.et_car_insuranceExpireln:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        WTimePopupWindow pwDate = new WTimePopupWindow(mContext, WTimePopupWindow.Type.YEAR_MONTH_DAY);
                        pwDate.setOnTimeSelectListener(new WTimePopupWindow.OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date) {
                                etCarInsuranceExpireln.setText(SystemTools.getDate(date));
                            }
                        });
                        if(!pwDate.isShowing()){
                            pwDate.showAtLocation(view, Gravity.BOTTOM, 0, 0,new Date());
                        }
                    }
                    break;
                case R.id.et_car_department:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        DepartmentActivity.startAction(mContext,REQUEST_ADD_DEPARTMENT);
                    }
                    break;
            }
            return false;
        }
    };

    @OnClick({R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                carAdd();
                break;
        }
    }

    /**
     * 添加车辆
     */
    private void carAdd() {
        String name = etCarName.getText().toString();
        String desc = etCarDesc.getText().toString();
        String frameNo = etCarFrameno.getText().toString();
        String engineNo = etCarEngineno.getText().toString();
        String buyDate = etCarBuyDate.getText().toString();
        String mileage = etCarMileage.getText().toString();
        String maintainMileage = etCarMaintainmileage.getText().toString();
        String insuranceExpireln = etCarInsuranceExpireln.getText().toString();
        String inspectExpireln = etCarInspectExpireln.getText().toString();
        if(TextUtils.isEmpty(departmentId)){
            departmentId = "0";
        }
        if(TextUtils.isEmpty(mileage)){
            mileage = "0";
        }
        if(TextUtils.isEmpty(maintainMileage)){
            maintainMileage = "0";
        }
        if(!SystemTools.isNumeric(mileage)||!SystemTools.isNumeric(maintainMileage)){
            SystemTools.showToast(mContext,getResources().getString(R.string.mileage_must_number));
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", app.token);
        params.put("uid", app.userId);
        params.put("name", name);//车牌号
        params.put("desc", desc);//车辆描述
        params.put("frameNo", frameNo);//车架号
        params.put("engineNo", engineNo);//发动机号
        params.put("buyDate", buyDate);//购置日期
        params.put("mileage", mileage);//行驶里程
        params.put("maintainMileage",maintainMileage);//下次保养里程
        params.put("insuranceExpireln", insuranceExpireln);//保养日期
        params.put("inspectExpireln", inspectExpireln);//年检日期
        params.put("departId", departmentId);//部门id
        vehicleApi.create(params, new OnSuccess() {
            @Override
            protected void onSuccess(String response) {
                Logger.d("创建车辆返回信息 ：" + response);
//                创建车辆返回信息 ：{"status_code":0,"objectId":768391204697149400}
                try {
                    JSONObject object = new JSONObject(response);
                    if("0".equals(object.getString("status_code"))){
                        SystemTools.showToast(mContext,getResources().getString(R.string.add_success));
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }else{
                        SystemTools.showToast(mContext,getResources().getString(R.string.add_fail));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new OnFailure() {
            @Override
            protected void onFailure(VolleyError error) {
                SystemTools.showToast(mContext,getResources().getString(R.string.net_error));
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ADD_DEPARTMENT && resultCode == RESULT_OK ){
            departmentId = data.getStringExtra("department_id");
            etCarDepartment.setText(data.getStringExtra("department_name"));
            Logger.d("返回----------------" + departmentId);
        }
    }
}
