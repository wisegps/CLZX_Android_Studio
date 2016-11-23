package com.wisegps.clzx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.wisegps.clzx.R;
import com.wisegps.clzx.activity.car.CarManagerActivity;
import com.wisegps.clzx.activity.user.AboutActivity;
import com.wisegps.clzx.activity.user.PasswrodActivity;
import com.wisegps.clzx.activity.user.SettingActivity;
import com.wisegps.clzx.adapter.LeftListMenuAdapter;
import com.wisegps.clzx.fragment.BaiduMapEvent;
import com.wisegps.clzx.fragment.BaiduMapFragment;
import com.wisegps.clzx.service.DataService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.lv_left_menu)
    ListView lvLeftMenu;

    @Bind(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;


    private final int REQUEST_SEARCHE_CODE = 0;
    private List<String> menuNames = new ArrayList<>();
    private LeftListMenuAdapter menuAdapter;
    private Activity mContext;

    private BaiduMapFragment baiduMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        startService(new Intent(this, DataService.class));

    }

    /**
     * init view
     */
    private void initView() {
        setSupportActionBar(mToolbar);
        //创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        initMenu();
        switchMapFragment();
    }

    /**
     * @param context
     */
    public static void startAction(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
//        context.overridePendingTransition(R.anim.translate_enter_right, R.anim.translate_out_left);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            SearchResultActivity.startAction(mContext,REQUEST_SEARCHE_CODE);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 切换到车辆列表页面
     */
    private void switchMapFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,BaiduMapFragment.getInstance()).commit();
    }


    /**
     * 初始化侧滑菜单
     */
    private void initMenu(){
        menuNames.add(getResources().getString(R.string.left_menu_car_manager));
        menuNames.add(getResources().getString(R.string.left_menu_password));
        menuNames.add(getResources().getString(R.string.left_menu_setting));
        menuNames.add(getResources().getString(R.string.left_menu_about));
        menuAdapter = new LeftListMenuAdapter(this);
        menuAdapter.setData(menuNames);
        lvLeftMenu.setAdapter(menuAdapter);
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        CarManagerActivity.startAction(mContext);
                        break;
                    case 1:
                        PasswrodActivity.startAction(mContext,1);
                        break;
                    case 2:
                        SettingActivity.startAction(mContext);
                        break;
                    case 3:
                        AboutActivity.startAction(mContext);
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SEARCHE_CODE && resultCode == RESULT_OK){
            EventBus.getDefault().post(new BaiduMapEvent(0));
            Logger.d("发送事件");
        }
    }
}
