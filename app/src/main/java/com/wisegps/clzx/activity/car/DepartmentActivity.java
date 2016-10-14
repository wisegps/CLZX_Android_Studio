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
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wicare.wistorm.api.WDepartmentApi;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
import com.wisegps.clzx.R;
import com.wisegps.clzx.activity.car.adapter.DepartmentAdapter;
import com.wisegps.clzx.activity.car.model.DepartmentInfo;
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
 * Created by Administrator on 2016/9/21.
 */
public class DepartmentActivity extends AppCompatActivity {

    @Bind(R.id.lv_department)
    ListView lvDepartment;
    @Bind(R.id.tv_no_department)
    TextView tvNoDepartment;
    @Bind(R.id.f_add_department)
    FloatingActionButton fAddDepartment;

    private Activity mContext;
    private App app;
    private WDepartmentApi departmentApi;//部门接口
    private List<DepartmentInfo.DataBean> departmentInfoList;
    private DepartmentAdapter departmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        ButterKnife.bind(this);
        initView();
        initWistorm();
        getDepartmentList();
    }

    /**
     * 标题和返回箭头
     */
    private void initView() {
        mContext = this;
        app = (App) getApplication();
        departmentInfoList = new ArrayList<>();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        fAddDepartment.setBackgroundTintList(ColorStateList.valueOf(0xff067de1));
        departmentAdapter = new DepartmentAdapter(mContext);
        departmentAdapter.setData(departmentInfoList);
        lvDepartment.setAdapter(departmentAdapter);
        lvDepartment.setOnItemClickListener(onItemClickListener);
        lvDepartment.setOnItemLongClickListener(onItemLongClickListener);
    }

    /**
     * init wistorm
     */
    private void initWistorm() {
        BaseVolley.init(this);
        departmentApi = new WDepartmentApi(this);
    }

    /**
     * @param context
     */
    public static void startAction(Activity context, int requestCode) {
        Intent intent = new Intent(context, DepartmentActivity.class);
        context.startActivityForResult(intent, requestCode);
    }


    /**
     * onitem click
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Intent intent = new Intent();
            intent.putExtra("department_id", departmentInfoList.get(position).getObjectId() + "");
            intent.putExtra("department_name", departmentInfoList.get(position).getName());
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    /**
     * item long on click
     */
    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
            final String[] items={getResources().getString(R.string.delete_department),getResources().getString(R.string.edit_department_name)};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.edit);
            builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if(i == 0){
                        deleteDepartment(position);
                    }else{
                        showChangeDepartmentNameDialog(position);
                    }
                }
            });
            builder.show();
            return true;
        }
    };


    /**
     * 删除部门
     * @param postion
     */
    private void deleteDepartment(int postion){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", app.token);
        params.put("objectId", departmentInfoList.get(postion).getObjectId() + "");//删除相应的部门
        departmentApi.delete(params, new OnSuccess() {
            @Override
            protected void onSuccess(String response) {
                Logger.d("删除部门返回信息 ：" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if("0".equals(object.getString("status_code"))){
                        SystemTools.showToast(mContext,getResources().getString(R.string.delete_success));
                        getDepartmentList();
                    }else{
                        SystemTools.showToast(mContext,getResources().getString(R.string.delete_failed));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },null);
    }

    /**
     * 修改部门名字
     * @param position
     */
    private void showChangeDepartmentNameDialog(final int position){
        final EditText etName = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_department_name);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(etName.getText().toString())){
                    SystemTools.showToast(mContext,getResources().getString(R.string.no_content));
                }else{
                    dialog.dismiss();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("access_token", app.token);
                    params.put("_objectId", departmentInfoList.get(position).getObjectId() + "");//部门名字
                    params.put("name", etName.getText().toString());//部门名字
                    departmentApi.update(params, new OnSuccess() {
                        @Override
                        protected void onSuccess(String response) {
                            Logger.d("更新部门返回信息 ：" + response);
//                更新部门返回信息 ：{"status_code":0,"n":1,"nModified":1}
                            try {
                                JSONObject object = new JSONObject(response);
                                if("0".equals(object.getString("status_code"))){
                                    SystemTools.showToast(mContext,getResources().getString(R.string.edit_success));
                                    getDepartmentList();
                                }else{
                                    SystemTools.showToast(mContext,getResources().getString(R.string.edit_fail));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },null);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(etName,50,20,50,20);
        builder.show();
    }

    @OnClick(R.id.f_add_department)
    public void onClick() {
        showAddDepartment();
    }

    /**
     * 添加部门
     */
    private void showAddDepartment() {
        final EditText etName = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_department);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(etName.getText().toString())){
                    SystemTools.showToast(mContext,getResources().getString(R.string.no_content));
                }else{
                    dialog.dismiss();
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("access_token", app.token);
                    params.put("uid", app.userId);
                    params.put("name", etName.getText().toString());//部门名字
                    departmentApi.create(params, new OnSuccess() {
                        @Override
                        protected void onSuccess(String response) {
                            Logger.d("创建部门返回信息 ：" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                if("0".equals(object.getString("status_code"))){
                                    SystemTools.showToast(mContext,getResources().getString(R.string.add_success));
                                    getDepartmentList();
                                }else{
                                    SystemTools.showToast(mContext,getResources().getString(R.string.add_fail));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },null);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(etName,50,20,50,20);
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 获取部门列表
     */
    private void getDepartmentList(){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", app.token);
        params.put("uid",app.userId);
        params.put("sort", "objectId");
        params.put("min_id", "0");
        params.put("max_id", "0");
        params.put("limit", "-1");
        String fields = "name,objectId,uid";
        departmentApi.list(params, fields, new OnSuccess() {
            @Override
            protected void onSuccess(String response) {
                Logger.d("获取部门列表返回信息 ：" + response);
                Gson gson = new Gson();
                DepartmentInfo departmentInfo = gson.fromJson(response, DepartmentInfo.class);
                departmentInfoList = departmentInfo.getData();
                if (departmentInfoList.size() == 0) {
                    showNoInfo(true);
                } else {
                    showNoInfo(false);
                }
                departmentAdapter.setData(departmentInfoList);
                departmentAdapter.notifyDataSetChanged();
            }
        }, new OnFailure() {
            @Override
            protected void onFailure(VolleyError error) {
                SystemTools.showToast(mContext,getResources().getString(R.string.net_error));
            }
        });
    }


    /**
     * @param isHad
     */
    private void showNoInfo(boolean isHad){
        if(!isHad){
            tvNoDepartment.setVisibility(View.GONE);
        }else{
            tvNoDepartment.setVisibility(View.VISIBLE);
        }
    }

}
