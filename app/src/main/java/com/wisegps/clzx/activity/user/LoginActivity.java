package com.wisegps.clzx.activity.user;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.wicare.wistorm.WEncrypt;
import com.wicare.wistorm.api.WUserApi;
import com.wicare.wistorm.http.BaseVolley;
import com.wicare.wistorm.http.OnFailure;
import com.wicare.wistorm.http.OnSuccess;
import com.wisegps.clzx.R;
import com.wisegps.clzx.activity.MainActivity;
import com.wisegps.clzx.app.App;
import com.wisegps.clzx.utils.SystemTools;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";
    @Bind(R.id.et_account)
    EditText etAccount;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.v_vertal_line)
    View vVertalLine;
    @Bind(R.id.tv_password)
    TextView tvPassword;
    @Bind(R.id.tv_register)
    TextView tvRegister;

    private Activity mContext;
    private WUserApi userApi;
    private String account;
    private String password;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private App app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        app = (App)getApplication();
        init();
        initWistorm();
    }

    private void init(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemeber = pref.getBoolean("remenber_password",false);
        if(isRemeber){
            account = pref.getString("account","");
            password = pref.getString("password","");
            etAccount.setText(account);
            etPassword.setText(password);
            checkbox.setChecked(true);
        }else{
            checkbox.setChecked(false);
        }
    }
    /**
     * wistorm api接口网络请求初始化
     */
    private void initWistorm(){
        userApi = new WUserApi(mContext);
        BaseVolley.init(mContext);
    }

    @OnClick({R.id.bt_login, R.id.tv_password, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                if(SystemTools.isNetworkAvailable(mContext)){
                   accountLogin();
                }else{
                    SystemTools.showToast(mContext,"网络不可用，请检查网络");
                }
                break;
            case R.id.tv_password:
                PasswrodActivity.startAction(mContext,0);
                break;
            case R.id.tv_register:
                RegisterActivity.startAction(mContext);
                break;
        }
    }

    /**
     * 账号登录
     */
    private void accountLogin() {
        account = etAccount.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(account)||TextUtils.isEmpty(password)){
            SystemTools.showToast(mContext,"账号或密码不能为空");
            return;
        }else {
            userApi.login(account, WEncrypt.MD5(password), new OnSuccess() {
                @Override
                protected void onSuccess(String response) {
                    Logger.d(TAG,"登录返回信息：" + response);
                    remenberAccountPassword();
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getInt("status_code") == 0){
                            app.token = object.getString("access_token");
                            app.userId = object.getString("uid");
                            LoginActivity.this.finish();
                            MainActivity.startAction(mContext);
                        }else if(object.getInt("status_code") == 1){
                            SystemTools.showToast(mContext,"无效用户");
                        }else if(object.getInt("status_code") == 2){
                            SystemTools.showToast(mContext,"密码错误,请重新输入");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new OnFailure() {
                @Override
                protected void onFailure(VolleyError error) {
                    SystemTools.showToast(mContext,"登录失败");
                }
            });
        }
    }

    /**
     * 保存账号密码
     */
    private void remenberAccountPassword(){
        editor = pref.edit();
        if(checkbox.isChecked()){
            editor.putBoolean("remenber_password",true);
            editor.putString("account",account);
            editor.putString("password",password);
            editor.putString("md5_password",WEncrypt.MD5(password));
        }else{
            editor.clear();
        }
        editor.commit();
    }
}