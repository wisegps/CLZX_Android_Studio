package com.wisegps.clzx.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wisegps.clzx.R;
import com.wisegps.clzx.app.SharePreferenceData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置参数页面
 * Created by Administrator on 2016/9/3.
 */
public class SettingActivity extends AppCompatActivity {

    @Bind(R.id.tv_auto_updata_time)
    TextView tvAutoUpdataTime;

    private Context mContext;
    private SharePreferenceData spd;
    private int autoUpdateTime;

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
        spd = new SharePreferenceData(this);
        autoUpdateTime = spd.getAutoUpdateTime();
        tvAutoUpdataTime.setText(autoUpdateTime + "s");
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

    /**
     * 显示刷新时间
     */
    private void showAutoUpdateTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.edit);
        builder.setIcon(R.drawable.ic_warmming);
        builder.setSingleChoiceItems(R.array.update_time, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                autoUpdateTime = getTime(i);
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tvAutoUpdataTime.setText(autoUpdateTime + "s");
                spd.setAutoUpdateTime(autoUpdateTime);
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

    @OnClick(R.id.rl_auto_update_time)
    public void onClick() {
        showAutoUpdateTimeDialog();
    }

    private int getTime(int i){
        int time = 0;
        switch(i){
            case 0:
                time = 10;
                break;
            case 1:
                time = 30;
                break;
            case 2:
                time = 60;
                break;
            case 3:
                time = 180;
                break;
            case 4:
                time = 300;
                break;
            case 5:
                time = 600;
                break;
        }
        return time;
    }
}
