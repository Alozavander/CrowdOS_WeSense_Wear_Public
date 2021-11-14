package com.example.wesense_wearos.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.wesense_wearos.MCS_RecyclerItemClickListener;
import com.example.wesense_wearos.MainActivity;
import com.example.wesense_wearos.R;
import com.example.wesense_wearos.activities.mines_listitem.Activity_mine_minor1_publish;
import com.example.wesense_wearos.activities.mines_listitem.Activity_mine_minor2_accepted;
import com.example.wesense_wearos.activities.mines_listitem.Activity_mine_minor5_sensorData;
import com.example.wesense_wearos.activities.mines_listitem.Activity_mine_minor7_setting;
import com.example.wesense_wearos.activities.remind_activities.Activity_mine_remind_doing;
import com.example.wesense_wearos.activities.remind_activities.Activity_mine_remind_done;
import com.example.wesense_wearos.activities.remind_activities.Activity_mine_remind_recomment;
import com.example.wesense_wearos.adapters.Adapter_ListeView_mine;
import com.example.wesense_wearos.adapters.adapter_listview_main;
import com.example.wesense_wearos.beans.Bean_ListView_mine;
import com.example.wesense_wearos.beans.Bean_Mine_UserAccount;
import com.example.wesense_wearos.beans.bean_listview_main;
import com.example.wesense_wearos.taskSubmit.SelectDialog;

import java.util.ArrayList;
import java.util.List;

public class Activity_Remind_Wear extends WearableActivity {
    private WearableRecyclerView mRecyclerView;
    private TextView user_Name_tv;
    private BroadcastReceiver receiver;
    private Button login_bt;
    private Button edit_bt;
    private ImageView user_Icon;
    private Adapter_ListeView_mine mAdapter_listeView_mine;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);


        //初始化列表数据
        initWearableRecylerList();

    }

    private void initWearableRecylerList() {
        WearableRecyclerView lWearableRecyclerView = findViewById(R.id.remind_recycler_view);
        lWearableRecyclerView.setEdgeItemsCenteringEnabled(true);     //曲线布局
        lWearableRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this));

        /*lWearableRecyclerView.setCircularScrollingGestureEnabled(true);
        lWearableRecyclerView.setBezelFraction(0.5f);
        lWearableRecyclerView.setScrollDegreesPerScreen(90);*/

        //构建列表List
        List<bean_listview_main> lList = new ArrayList<>();
        lList.add(new bean_listview_main(R.mipmap.navi_home,getString(R.string.mine_minor2_accepted_processing)));
        lList.add(new bean_listview_main(R.mipmap.navi_map,getString(R.string.mine_minor2_accepted_done)));
        lList.add(new bean_listview_main(R.mipmap.navi_publish,getString(R.string.recommend)));




        adapter_listview_main lAdapter_listview_main = new adapter_listview_main(lList,this);
        lAdapter_listview_main.setItemClickListener(new MCS_RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO：跳转功能
                switch (position){
                    case 0 :
                        Intent lIntent = new Intent(Activity_Remind_Wear.this, Activity_mine_remind_doing.class);
                        startActivity(lIntent);
                        break;
                    case 1 :
                        Intent lIntent_publish = new Intent(Activity_Remind_Wear.this, Activity_mine_remind_done.class);
                        startActivity(lIntent_publish);
                        break;
                    case 2 :
                        Intent lIntent_remind = new Intent(Activity_Remind_Wear.this, Activity_mine_remind_recomment.class);
                        startActivity(lIntent_remind);
                        break;
                }
            }
        });
        lWearableRecyclerView.setAdapter(lAdapter_listview_main);

        //检查是否登录
        int login_userID = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        if(login_userID == -1){
            Intent lIntent = new Intent(Activity_Remind_Wear.this,Activity_login_Wear.class);
            startActivity(lIntent);
        }
    }
}
