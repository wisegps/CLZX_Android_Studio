package com.wisegps.clzx.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.wisegps.clzx.R;

import butterknife.ButterKnife;

/**
 * 设置参数页面
 * Created by Administrator on 2016/9/3.
 */
public class SettingActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    /**
     * 标题和返回箭头
     */
    private void initView() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
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
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
    }


}
