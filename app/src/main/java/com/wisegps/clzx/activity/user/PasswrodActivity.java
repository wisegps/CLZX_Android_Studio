package com.wisegps.clzx.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wicare.wistorm.api.WCommApi;
import com.wicare.wistorm.api.WUserApi;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
import com.wisegps.clzx.R;
import com.wisegps.clzx.utils.CountDownTimerUtils;
import com.wisegps.clzx.utils.SystemTools;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/2.
 * 重置密码
 */
public class PasswrodActivity extends AppCompatActivity {

    @Bind(R.id.et_account)
    MaterialEditText etAccount;
    @Bind(R.id.et_valid)
    MaterialEditText etValid;
    @Bind(R.id.et_password)
    MaterialEditText etPassword;
    @Bind(R.id.tv_getvalid)
    TextView tvGetvalid;

    private final String TAG = "PasswrodActivity";

    private Context mContext;
    private WCommApi commApi;
    private WUserApi userApi;
    private String account;
    private String valid;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initWistorm();
    }

    /**
     * 标题和返回箭头
     */
    private void initView() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int type = intent.getIntExtra("type",0);
        if(type == 0){
            mActionBar.setTitle("忘记密码");
        }else{
            mActionBar.setTitle("修改密码");
        }
    }

    private void initWistorm() {
        BaseVolley.init(this);
        commApi = new WCommApi(this);
        userApi = new WUserApi(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return super.onSupportNavigateUp();
    }

    /**
     * @param context
     */
    public static void startAction(Activity context, int type) {
        Intent intent = new Intent(context, PasswrodActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
//        if(type == 1){
//            context.overridePendingTransition(R.anim.alpha_enter,R.anim.alpha_exit);
//        }
    }

    @OnClick({R.id.tv_getvalid, R.id.btn_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getvalid:
                if(SystemTools.isNetworkAvailable(mContext)){
                    getValidCode();
                }else{
                    SystemTools.showToast(mContext,"网络不可用，请检查网络");
                }
                break;
            case R.id.btn_reset:
                if(SystemTools.isNetworkAvailable(mContext)){
                    isValidTrue();
                }else{
                    SystemTools.showToast(mContext,"网络不可用，请检查网络");
                }
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getValidCode(){
        account = etAccount.getText().toString().trim();
        if(TextUtils.isEmpty(account)){
            SystemTools.showToast(this,"账号不能为空");
            return;
        }else{
            commApi.sendSMS(account, 2, new OnSuccess() {
                @Override
                protected void onSuccess(String response) {
                    CountDownTimerUtils timerCount = new CountDownTimerUtils(tvGetvalid, 60000, 1000,0xff067de1,0xffffffff,0xffff0000,0xffffffff);
                    timerCount.start();
                    try {
                        JSONObject object = new JSONObject(response);
                        if("0".equals(object.getString("status_code"))){
                            SystemTools.showToast(mContext,"验证码已发到你的手机" + account + "上");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Logger.d(TAG,"发送验证码返回信息：" + response);
                }
            }, null);
        }
    }

    /**
     * 检查验证码是否正确
     */
    private void isValidTrue() {
        valid = etValid.getText().toString().trim();
        if(TextUtils.isEmpty(valid) || TextUtils.isEmpty(account)){
            SystemTools.showToast(mContext,"账号或者验证码不能为空");
            return;
        }else{
            commApi.validCode(account, valid, new OnSuccess() {
                @Override
                protected void onSuccess(String response) {
                    Logger.d(TAG,"验证码是否正确返回信息：" + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getBoolean("valid")){
                            resetPassword();
                        }else{
                            SystemTools.showToast(mContext,"验证码不对，请重新输入");
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }
    }


    /**
     * 重置密码
     */
    private void resetPassword(){
        password = etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(password)||TextUtils.isEmpty(account)){
            SystemTools.showToast(mContext,"账号或者密码不能为空");
            return;
        }else{
            userApi.passwordReset(account, password, 2, valid, new OnSuccess() {
                @Override
                protected void onSuccess(String response) {
                    Logger.d(TAG,"重置密码返回信息：" + response);
//                    {"status_code":0}
                    try {
                        JSONObject object = new JSONObject(response);
                        if("0".equals(object.getString("status_code"))){
                            SystemTools.showToast(mContext,"修改密码成功");
                        }else{
                            SystemTools.showToast(mContext,"修改密码失败");
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new OnFailure() {
                @Override
                protected void onFailure(VolleyError error) {
                    SystemTools.showToast(mContext,"修改密码失败");
                }
            });
        }
    }

}
