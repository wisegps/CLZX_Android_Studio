package com.wisegps.clzx.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisegps.clzx.R;
import com.wisegps.clzx.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 关于
 * Created by Administrator on 2016/9/3.
 */
public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.iv_app_icon)
    ImageView ivAppIcon;
    @Bind(R.id.tv_version)
    TextView tvVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 标题和返回箭头
     */
    private void initView() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        tvVersion.setText(SystemTools.getVersion(this));
        ivAppIcon.setBackgroundResource(R.drawable.ic_app_icon);
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
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
    }


}
