package com.wisegps.clzx.activity.car;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wicare.wistorm.api.WVehicleApi;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
import com.wisegps.clzx.R;
import com.wisegps.clzx.activity.car.adapter.CarListAdapter;
import com.wisegps.clzx.activity.car.model.CarInfo;
import com.wisegps.clzx.app.App;
import com.wisegps.clzx.utils.SystemTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆管理
 * Created by Administrator on 2016/9/3.
 */
public class CarManagerActivity extends AppCompatActivity {

    @Bind(R.id.lv_car)
    ListView lvCar;
    @Bind(R.id.f_add_car)
    FloatingActionButton addcar;
    @Bind(R.id.tv_no_car)
    TextView tvNoCar;

    private final int REQUEST_ADD_CAR = 0;
    private final int REQUEST_BIND_DEVICE = 1;
    private final int REQUEST_CAR_DETAIL  = 2;
    private Activity mContext;
    private App app;
    private WVehicleApi vehicleApi;
    private CarListAdapter carsListAdapter;
    private List<CarInfo.DataBean> carListInfo = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        ButterKnife.bind(this);
        initView();
        initWistorm();
        getCarListInfo();
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
        addcar.setBackgroundTintList(ColorStateList.valueOf(0xff067de1));
        carsListAdapter = new CarListAdapter(mContext);
        carsListAdapter.setData(carListInfo);
        lvCar.setAdapter(carsListAdapter);
        lvCar.setOnScrollListener(onScrollListener);
        carsListAdapter.setOnCarEditClickListener(onCarEditClickListener);
        carsListAdapter.setOnBindDeviceClickListener(onBindDeviceClickListener);
        carsListAdapter.setOnCarDetailClickListener(onCarDetailClickListener);
        carsListAdapter.setOnDeviceClickListener(onDeviceClickListener);
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
    public static void startAction(Activity context) {
        Intent intent = new Intent(context, CarManagerActivity.class);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
    }


    /**
     * 获取车辆列表信息
     */
    private void getCarListInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("access_token", app.token);
        params.put("uid", app.userId);
        params.put("sort", "objectId");
        params.put("min_id", "0");
        params.put("max_id", "0");
        params.put("limit", "-1");
        String fields = "name,desc,frameNo,engineNo,buyDate,mileage,maintainMileage,insuranceExpireln,inspectExpireln,departId,did,objectId,uid";
        vehicleApi.list(params, fields, new OnSuccess() {
            @Override
            protected void onSuccess(String response) {
                Logger.d("获取车辆列表返回信息 ：" + response);
                Gson gson = new Gson();
                CarInfo carInfos = gson.fromJson(response, CarInfo.class);
                carListInfo = carInfos.getData();
                if (carListInfo.size() == 0) {
                    showNoInfo(true);
                } else {
                    showNoInfo(false);
                }
                carsListAdapter.setData(carListInfo);
                carsListAdapter.notifyDataSetChanged();
            }
        }, new OnFailure() {
            @Override
            protected void onFailure(VolleyError error) {

            }
        });
    }

    /**
     * 绑定设备监听
     */
    private CarListAdapter.OnBindDeviceClickListener onBindDeviceClickListener = new CarListAdapter.OnBindDeviceClickListener() {
        @Override
        public void onClick(View v,int position) {
            DeviceBindActivity.startAction(mContext,REQUEST_BIND_DEVICE,carListInfo.get(position).getObjectId() + "");
        }
    };

    /**
     * 设备图标点击事件
     */
    private CarListAdapter.OnDeviceClickListener onDeviceClickListener = new CarListAdapter.OnDeviceClickListener() {
        @Override
        public void onClick(View v, int position) {
            SystemTools.showToast(mContext,"设备  " + position);
        }
    };

    /**
     * 车辆编辑点击事件
     */
    private CarListAdapter.OnCarEditClickListener onCarEditClickListener = new CarListAdapter.OnCarEditClickListener() {
        @Override
        public void onClick(View v, final int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.edit);
            builder.setMessage(R.string.delete_car);
            builder.setIcon(R.drawable.ic_warmming);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    deleteCar(position);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
    };

    /**
     * 车辆详情点击事件
     */
    private CarListAdapter.OnCarDetailClickListener onCarDetailClickListener = new CarListAdapter.OnCarDetailClickListener() {
        @Override
        public void onClick(View v,int position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("carInfo",carListInfo.get(position));
            CarDetailActivity.startAction(mContext,REQUEST_CAR_DETAIL,bundle);
        }
    };

    @OnClick(R.id.f_add_car)
    public void onClick() {
        CarAddActivity.startAction(mContext, REQUEST_ADD_CAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_CAR && resultCode == RESULT_OK) {
            SystemTools.showToast(mContext,getResources().getString(R.string.bind_device_success));
            getCarListInfo();
        }else if(requestCode == REQUEST_BIND_DEVICE && resultCode == RESULT_OK){
            getCarListInfo();
        }else if(requestCode == REQUEST_CAR_DETAIL && resultCode == RESULT_OK){
            getCarListInfo();
        }
    }

    /**
     * @param isHad
     */
    private void showNoInfo(boolean isHad) {
        if (!isHad) {
            tvNoCar.setVisibility(View.GONE);
        } else {
            tvNoCar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param position 删除车辆
     */
    private void deleteCar(final int  position){
        HashMap<String,String> params = new HashMap<>();
        params.put("access_token", app.token);
        params.put("objectId",carListInfo.get(position).getObjectId()+"");
        vehicleApi.delete(params, new OnSuccess() {
            @Override
            protected void onSuccess(String response) {
                Logger.d("删除车辆返回信息 ：" + response);
//                删除车辆返回信息 ：{"status_code":0} status_code 为 0 表示删除车辆成功
                try {
                    JSONObject object = new JSONObject(response);
                    if("0".equals(object.getString("status_code"))){
                        SystemTools.showToast(mContext,getResources().getString(R.string.edit_success));
                        carListInfo.remove(position);
                        carsListAdapter.setData(carListInfo);
                        carsListAdapter.notifyDataSetChanged();
                    }else{
                        SystemTools.showToast(mContext,getResources().getString(R.string.edit_fail));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new OnFailure() {
            @Override
            protected void onFailure(VolleyError error) {
                SystemTools.showToast(mContext,getResources().getString(R.string.edit_fail));
            }
        });
    }

    /**
     * listview 滑动监听
     */
    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            switch (scrollState) {

            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
                // 判断滚动到底部
                if (lvCar.getFirstVisiblePosition() == 0) {
                    addcar.setVisibility(View.VISIBLE);
                }else {
                    addcar.setVisibility(View.GONE);
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                // 判断滚动到底部
                if (lvCar.getFirstVisiblePosition() == 0) {
                    addcar.setVisibility(View.VISIBLE);
                }else {
                    addcar.setVisibility(View.GONE);
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                // 判断滚动到底部
                if (lvCar.getFirstVisiblePosition() == 0) {
                    addcar.setVisibility(View.VISIBLE);
                }else {
                    addcar.setVisibility(View.GONE);
                }
                break;
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
        }
    };
    /**
     objectId: Number //对象ID
     uid: Number //所属用户ID
     name: String //部门名称
     adminId: Number //管理用户ID
     parentId: Number //上级部门id
     treePath: String //节点路径树
     ACL：Array //用户或者角色
     createdAt: Date 创建日期
     updatedAt: Date 更新日期
     */
}
