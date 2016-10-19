package com.wisegps.clzx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.SearchView;

import com.wisegps.clzx.R;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/9/6.
 */
public class SearchResultActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{


    @Bind(R.id.searchView)
    SearchView searchView;
    @Bind(R.id.lv_car)
    ListView lvCar;

    private Activity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 标题和返回箭头
     */
    private void initView() {
        mContext = this;
        //        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        searchView.setOnQueryTextListener(this);
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
        Intent intent = new Intent(context, SearchResultActivity.class);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.alpha_enter,R.anim.alpha_exit);
    }


    @Override
    public boolean onQueryTextChange(String s) {
        if (!TextUtils.isEmpty(s)){
//            mListView.setFilterText(newText);
        }else{
//            mListView.clearTextFilter();
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
}