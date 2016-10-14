package com.wisegps.clzx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wicare.wistorm.widget.WInputField;
import com.wisegps.clzx.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/9/6.
 */
public class SearchResultActivity extends AppCompatActivity {

    @Bind(R.id.et_search)
    WInputField etSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs

    }


    /**
     * @param context
     */
    public static void startAction(Activity context) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.alpha_enter,R.anim.alpha_exit);
    }

    @OnClick(R.id.btn_back)
    public void onClick() {
        finish();
    }
}