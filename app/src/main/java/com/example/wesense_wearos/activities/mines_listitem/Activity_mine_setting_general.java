package com.example.wesense_wearos.activities.mines_listitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.wesense_wearos.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_mine_setting_general extends WearableActivity {

    private ListView generalListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting_general);

        //初始化列表
        initList();

    }



    //通用功能列表
    private void initList() {
        //List<String> generalList = new ArrayList<>();
        List<String> generalList = new ArrayList<>();

        generalList.add(getResources().getString(R.string.setting_general_version));

        generalListView = findViewById(R.id.general_list);
        generalListView.setAdapter(new ArrayAdapter<String>(this,R.layout.listview_item_minepage_minor7,generalList));
        //generalListView.setAdapter(new ArrayAdapter<String>(generalContext,R.layout.fragment_mine_setting_general,generalList));

        //点击通用进入通用功能列表界面
        generalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("------- setting-general -------");
                System.out.println("position = " + position);
                if (position == 0) {
                    Intent intent = new Intent(Activity_mine_setting_general.this,Activity_mine_setting_general_multilan.class);
                    startActivity(intent);
                }
            }
        });
    }
}
