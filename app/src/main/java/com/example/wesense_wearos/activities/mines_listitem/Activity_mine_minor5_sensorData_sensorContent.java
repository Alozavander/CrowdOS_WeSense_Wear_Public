package com.example.wesense_wearos.activities.mines_listitem;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sense_function.sensorFunction.SenseHelper;
import com.example.sense_function.sensorFunction.SensorSQLiteOpenHelper;
import com.example.wesense_wearos.MCS_RecyclerItemClickListener;
import com.example.wesense_wearos.R;
import com.example.wesense_wearos.StringStore;
import com.example.wesense_wearos.activities.SenseDataDisplay.SQLiteDataDisplay;
import com.example.wesense_wearos.adapters.Adapter_RecyclerView_setting_sensorData;

import java.util.ArrayList;
import java.util.List;

public class Activity_mine_minor5_sensorData_sensorContent extends WearableActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private Adapter_RecyclerView_setting_sensorData mAdapter;
    private List<String[]> mList;
    public String[] mSensorS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_minor5_sensor_data_sensor_content);

        initData();
        initViews();
    }

    private void initData() {
        //使用senseHelper获取传感器列表
        mSensorS = new SenseHelper(this).getSensorList_TypeInt_Strings();
        mList = new ArrayList<>();
        for(String s : mSensorS) {
            int sensorType = SenseHelper.sensorXmlName2Type(this,s);
            Cursor lCursor = new SensorSQLiteOpenHelper(this).getReadableDatabase().query(StringStore.SensorDataTable_Name,
                    new String[]{StringStore.SensorDataTable_SenseType,
                            StringStore.SensorDataTable_SenseTime,
                            StringStore.SensorDataTable_SenseData_1,
                            StringStore.SensorDataTable_SenseData_2,
                            StringStore.SensorDataTable_SenseData_3},
                    StringStore.SensorDataTable_SenseType + "=?", new String[]{sensorType+""}, null, null, null);
            String lnumb =  getString(R.string.setting_sensorData_dangqianshujushuliang) + "  " + lCursor.getCount();
            mList.add(new String[]{s,lnumb});
            lCursor.close();
        }
    }

    private void initViews() {
        findViewById(R.id.setting_sensorData_sensorContent_back).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.setting_sensorData_sensorContent_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        mAdapter = new Adapter_RecyclerView_setting_sensorData(this,mList);
        mAdapter.setRecyclerItemClickListener(new MCS_RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //添加跳转事件
                Intent lIntent = new Intent(Activity_mine_minor5_sensorData_sensorContent.this, SQLiteDataDisplay.class);
                lIntent.putExtra("sensorName",mSensorS[position]);
                startActivity(lIntent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //mSwipeRefreshLayout = findViewById(R.id.setting_sensorData_sensorContent_swiperefreshLayout);
        //mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE, Color.GREEN);
        //绑定列表事件点击
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_sensorData_sensorContent_back:
                finish();
                break;
        }
    }

}
