package com.example.wesense_wearos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.wear.widget.WearableRecyclerView;

import com.example.wesense_wearos.R;
import com.example.wesense_wearos.activities.publish_activities.Activity_publish_basictask_1;
import com.example.wesense_wearos.activities.publish_activities.Activity_publish_sensortask_2;
import com.example.wesense_wearos.adapters.Adapter_ListView_publish;
import com.example.wesense_wearos.beans.Bean_ListView_publish;

import java.util.ArrayList;
import java.util.List;

public class Activity_Publish_Wear extends WearableActivity {
    private WearableRecyclerView mRecyclerView;                                              //为首页显示任务列表的RecyclerView
    private ListView mListView;                                                                     //
    private Adapter_ListView_publish mAdapterListView_publish_;                                     //呈现任务模板的ListView
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        //初始化任务列表数据
        initTaskList();

    }


    private void initTaskList() {
        List<Bean_ListView_publish> beanList = new ArrayList<>();

        //for setting
        beanList.add(new Bean_ListView_publish(getResources().getString(R.string.fragment_publish_template1), getResources().getString(R.string.fragment_publish_template1)));
        beanList.add(new Bean_ListView_publish(getResources().getString(R.string.fragment_publish_template2), getResources().getString(R.string.fragment_publish_template2_minor)));
        //beanList.add(new Bean_ListView_publish("自定义任务发布模板", "选择需要的传感器"));

        mListView = findViewById(R.id.publishpage_modelList);
        mListView.setAdapter(new Adapter_ListView_publish(this, beanList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userName = getSharedPreferences("user", MODE_PRIVATE).getString("userName", "");
                //检查是否登录
                if (userName.equals("")) {
                    Toast.makeText(Activity_Publish_Wear.this, getResources().getString(R.string.notlogin), Toast.LENGTH_SHORT).show();
                    //TODO:logincheck and jump
                    Intent intent = new Intent(Activity_Publish_Wear.this, Activity_login_Wear.class);
                    startActivity(intent);
                } else {
                    //跳转到基础发布页面
                    switch (position) {
                        case 0:
                            Intent intent = new Intent(Activity_Publish_Wear.this, Activity_publish_basictask_1.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Intent intent1 = new Intent(Activity_Publish_Wear.this, Activity_publish_sensortask_2.class);
                            startActivity(intent1);
                            break;
                    }
                }
            }
        });
    }
}
