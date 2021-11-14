package com.example.wesense_wearos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.wesense_wearos.activities.Activity_Home_Wear;
import com.example.wesense_wearos.activities.Activity_Mine_Wear;
import com.example.wesense_wearos.activities.Activity_Publish_Wear;
import com.example.wesense_wearos.activities.Activity_Remind_Wear;
import com.example.wesense_wearos.adapters.adapter_listview_main;
import com.example.wesense_wearos.beans.bean_listview_main;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends WearableActivity {
    public int PERMISSION_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();

        init();
    }

    private void init() {
        Thread lThread = new Thread(new Runnable() {
            @Override
            public void run() {
                initSensorDataUploadService();
                initService();
                //initPermission();
            }
        });
        initWearableRecylerList();
        lThread.run();
    }

    private void initPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.INSTALL_PACKAGES};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            /** Do not have permissions, request them now */
            EasyPermissions.requestPermissions(this, getString(R.string.permission),
                PERMISSION_REQUEST_CODE, perms);
        }
    }

    private void initSensorDataUploadService() {
        Log.i("MainActivity", "ForegroundService");
        /** Open the SensorDataUploadService */
        int userId = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        if(userId == -1){
            Log.i("MainActivity","SenseDataUploadService is not on because of logout.");
        }else{
            Intent lIntent = new Intent(MainActivity.this, SenseDataUploadService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(lIntent);
            } else {
                startService(lIntent);
            }
        }
    }

    private void initService() {
        SenseServiceInitHelper lHelper = new SenseServiceInitHelper(MainActivity.this);
        boolean bind = lHelper.initService();
        Log.i("MainActivity_SenseServiceOpen status: ",bind + "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /** Forward results to EasyPermissions */
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initWearableRecylerList() {
        WearableRecyclerView lWearableRecyclerView = findViewById(R.id.main_recycler_view);
        lWearableRecyclerView.setEdgeItemsCenteringEnabled(true);     //曲线布局
        lWearableRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this));

        /*lWearableRecyclerView.setCircularScrollingGestureEnabled(true);
        lWearableRecyclerView.setBezelFraction(0.5f);
        lWearableRecyclerView.setScrollDegreesPerScreen(90);*/

        //构建列表List
        List<bean_listview_main> lList = new ArrayList<>();
        lList.add(new bean_listview_main(R.mipmap.navi_home,getString(R.string.title_home)));
        lList.add(new bean_listview_main(R.mipmap.navi_map,getString(R.string.title_map)));
        lList.add(new bean_listview_main(R.mipmap.navi_publish,getString(R.string.title_publish)));
        lList.add(new bean_listview_main(R.mipmap.navi_remind,getString(R.string.title_remind)));
        lList.add(new bean_listview_main(R.mipmap.navi_mine,getString(R.string.title_mine)));

        adapter_listview_main lAdapter_listview_main = new adapter_listview_main(lList,this);
        lAdapter_listview_main.setItemClickListener(new MCS_RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO：跳转功能
                switch (position){
                    case 0 :
                        Intent lIntent = new Intent(MainActivity.this, Activity_Home_Wear.class);
                        startActivity(lIntent);
                        break;
                    case 2 :
                        Intent lIntent_publish = new Intent(MainActivity.this, Activity_Publish_Wear.class);
                        startActivity(lIntent_publish);
                        break;
                    case 3 :
                        //int login_userID = Integer.parseInt(getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "-1"));
                        int login_userID = 14;
                        if (login_userID == -1) {
                            //TODO
                        /*Intent intent = new Intent(Activity_Mine_Wear.this, Activity_login.class);
                        startActivity(intent);*/
                        } else {
                            Intent lIntent_remind = new Intent(MainActivity.this, Activity_Remind_Wear.class);
                            startActivity(lIntent_remind);
                        }
                        break;
                    case 4 :
                        Intent lIntent_Mine = new Intent(MainActivity.this, Activity_Mine_Wear.class);
                        startActivity(lIntent_Mine);
                        break;
                    default:
                        break;
                }
            }
        });
        lWearableRecyclerView.setAdapter(lAdapter_listview_main);
    }
}
