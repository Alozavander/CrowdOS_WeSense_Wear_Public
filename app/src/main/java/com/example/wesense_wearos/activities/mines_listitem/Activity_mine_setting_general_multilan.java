package com.example.wesense_wearos.activities.mines_listitem;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.wesense_wearos.R;
import com.example.wesense_wearos.adapters.Adapter_Rb_Tv_multiLanguage;

import java.util.ArrayList;
import java.util.List;

public class Activity_mine_setting_general_multilan extends WearableActivity {
    private ListView languageListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting_general_multilan);

        //初始化列表
        initList();
        initBackBT();
    }

    private void initBackBT() {
/*        ImageView back_im = findViewById(R.id.general_multilan_backarrow);
        back_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    //语言选择列表 简体中文 - > *
    private void initList() {
        List<String> languageList = new ArrayList<>();

        languageList.add("简体中文");
        languageList.add("English");

        languageListView = findViewById(R.id.general_multilan_list);
        languageListView.setAdapter(new Adapter_Rb_Tv_multiLanguage(languageList,Activity_mine_setting_general_multilan.this));

    }
}
