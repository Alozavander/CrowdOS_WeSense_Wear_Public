package com.example.wesense_wearos.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.wear.widget.WearableRecyclerView;

import com.example.sense_function.sensorFunction.SenseHelper;
import com.example.sense_function.sensorFunction.SensorService;
import com.example.wesense_wearos.R;
import com.example.wesense_wearos.StringStore;
import com.example.wesense_wearos.adapters.adapter_listview_home;
import com.example.wesense_wearos.beans.Bean_ListView_home;
import com.example.wesense_wearos.beans.User_Task;
import com.example.wesense_wearos.networkClasses.interfacesPack.PostRequest_user_task_add;
import com.example.wesense_wearos.networkClasses.interfacesPack.QueryRequest_task_detail;
import com.example.wesense_wearos.pack_class.Task;
import com.example.wesense_wearos.taskSubmit.Activity_Task_Submit;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//TODO:FIX
public class Activity_Task_Detail extends WearableActivity {

    private final String TAG = "activity_task_detail";
    private Task task;
    private TextView userName_tv;
    private TextView postTime_tv;
    private TextView describe_tv;
    private TextView taskContent_tv;
    private TextView coinsCount_tv;
    private TextView deadline_tv;
    private TextView taskName_tv;
    private TextView taskKind_tv;
    private Button accept_bt;
    private Button submit_bt;
    private Scroller mScroller;
    public Intent mToSensorServiceIntent;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        mScroller = new Scroller(this);
        task = new Task();
        userName_tv = findViewById(R.id.activity_taskDetail_userName);
        taskContent_tv = findViewById(R.id.activity_taskDetail_content);
        coinsCount_tv = findViewById(R.id.activity_taskDetail_coin);
        deadline_tv = findViewById(R.id.activity_taskDetail_deadline);
        postTime_tv = findViewById(R.id.activity_taskDetail_postTime);
        taskName_tv = findViewById(R.id.activity_taskDetail_taskName);
        accept_bt = (Button) findViewById(R.id.activity_taskDetail_accept);
        taskKind_tv = findViewById(R.id.activity_taskDetail_taskKind);
        submit_bt = (Button) findViewById(R.id.activity_taskDetail_submit);
        accept_bt.setVisibility(View.INVISIBLE);
        submit_bt.setVisibility(View.INVISIBLE);


        initData();
        check_user_task();
    }


    private void initData() {
        String taskGson = getIntent().getStringExtra("taskGson");
        Gson gson = new Gson();
        System.out.println(taskGson);
        task = gson.fromJson(taskGson, Task.class);
        userName_tv.setText(task.getUserName());
        taskContent_tv.setText(task.getDescribe_task());
        coinsCount_tv.setText(task.getCoin().toString());
        deadline_tv.setText(new SimpleDateFormat("yyyy.MM.dd").format(task.getDeadLine()));
        postTime_tv.setText(new SimpleDateFormat("yyyy.MM.dd").format(task.getPostTime()));
        taskName_tv.setText(task.getTaskName());
        if(task.getTaskKind() == null) taskKind_tv.setText(R.string.ordinaryTask);
        else {
            switch (task.getTaskKind()){
                case 0:taskKind_tv.setText(getString(R.string.home_grid_0));break;
                case 1:taskKind_tv.setText(getString(R.string.home_grid_1));break;
                case 2:taskKind_tv.setText(getString(R.string.home_grid_2));break;
                case 3:taskKind_tv.setText(getString(R.string.home_grid_3));break;
                case 4:taskKind_tv.setText(getString(R.string.home_grid_4));break;
            }
        }
    }

    private void check_user_task() {
        int login_userID = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
        //检查是否登录
        if (login_userID == -1) {
            Toast.makeText(Activity_Task_Detail.this, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT);
            //Intent intent = new Intent(Activity_Task_Detail.this, Activity_login.class);
            //startActivity(intent);
        } else {
            User_Task user_task = new User_Task(null, login_userID, task.getTaskId(), 0, null, null,0);
            Gson gson = new Gson();
            String content = gson.toJson(user_task);
            queryRequest(content);
        }
    }


    public void queryRequest(final String content) {
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();

        //创建网络接口实例
        QueryRequest_task_detail request = retrofit.create(QueryRequest_task_detail.class);

        //创建RequestBody
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), content);

        //包装发送请求
        Call<ResponseBody> call = request.check_user_task(requestBody);

        final Context context = this;

        System.out.println("The task_user status request begin");

        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //System.out.println("The task_user status response: " + response.code());
                //在此处做未接受任务、已接受任务的两种情况处理，并加入用户登录跳转页
                if (response.code() == 200) {
                    //根据返回内容初始化按钮
                    String status = null;
                    try {
                        status = response.body().string() + "";
                        //System.out.println("The task_user status: " + status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i(TAG, "Status: " + status);

                    //这里根据返回的字符判定
                    switch (status) {
                        case "-1":
                            accept_bt.setVisibility(View.VISIBLE);
                            accept_bt.setText(getResources().getString(R.string.taskAccept));
                            accept_bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //判定是否含有要求的传感器，检测即中止
                                    /** The test is aborted when the required sensor is determined */
                                    String sensorTypesString = task.getSensorTypes();
                                    boolean canAccept = true;
                                    if(sensorTypesString != null){
                                        String[] tempStrings = sensorTypesString.split(StringStore.Divider_1);
                                        int[] types = new int[tempStrings.length];
                                        for(int temp = 0; temp < tempStrings.length; temp++) types[temp] = Integer.parseInt(
                                            tempStrings[temp]);
                                        if(types.length <= 0) canAccept = true;
                                        else if (types.length == 1 && types[0] == 1) canAccept = true;
                                        else{
                                            SenseHelper lSenseHelper = new SenseHelper(
                                                Activity_Task_Detail.this);
                                            for(int i : types){
                                                /** -1 :all sensors; -2:senseuploadServiceTag*/
                                                if(i<0) continue;
                                                if(!lSenseHelper.containSensor(i)) {
                                                    canAccept = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if(sensorTypesString == null || sensorTypesString.equals("null")) canAccept = true;
                                    if(canAccept) {
                                        add_User_Task_Request(content);
                                        mToSensorServiceIntent = new Intent(Activity_Task_Detail.this, SensorService.class);

                                        String taskSensor = task.getSensorTypes();
                                        mToSensorServiceIntent.putExtra("task_sensor_need",taskSensor);
                                        startService(mToSensorServiceIntent);
                                    }
                                    else {
                                        //TODO:转换成功string.xml文件中的字符
                                        new AlertDialog.Builder(Activity_Task_Detail.this).setTitle(getString(R.string.failToAcceptTask)).setMessage(getString(R.string.taskSensorNeedRemind))
                                            .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).setCancelable(false).show();
                                    }
                                }
                            });
                            break;
                        case "0":
                            accept_bt.setVisibility(View.INVISIBLE);
                            submit_bt.setVisibility(View.VISIBLE);
                            if(!submit_bt.hasOnClickListeners()){
                                submit_bt.setText(getResources().getString(R.string.submitData));
                                submit_bt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Activity_Task_Detail.this, Activity_Task_Submit.class);
                                        //将Task需求的传感器类型转换成字符传递给Task_Submit
                                        if(task.getSensorTypes() == null)   intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name), StringStore.SP_StringError); //添加空提示
                                        else {
                                            String sensorTypes = task.getSensorTypes();
                                            if (sensorTypes != null || !sensorTypes.equals("null")) {
                                                intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name), sensorTypes);
                                            } else {
                                                //添加错误提示字符串
                                                intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name), StringStore.SP_StringError);
                                            }
                                        }
                                        intent.putExtra(getResources().getString(R.string.intent_taskID_name), task.getTaskId());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                            break;
                        case "1":
                            accept_bt.setVisibility(View.VISIBLE);
                            submit_bt.setVisibility(View.INVISIBLE);
                            accept_bt.setText(getResources().getString(R.string.taskCompleted));
                            accept_bt.setEnabled(false);
                            break;
                        default:
                            Log.e(TAG,"user_task返回状态码错误");
                            break;
                    }
                }else{
                    Log.e(TAG,"user_task查询状态码失败.  The request body content is : " + content);
                    Toast.makeText(context,getResources().getString(R.string.QueryStatusCodeFailed),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void add_User_Task_Request(final String content) {
        final Context context = this;
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();

        //实例化网络接口
        PostRequest_user_task_add request = retrofit.create(PostRequest_user_task_add.class);

        //初始化requestbody
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), content);
        Log.i(TAG,"The request body is :" + content);
        //初始化call
        Call<ResponseBody> call = request.add_user_task(requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, getResources().getString(R.string.AcceptTaskSuccessful), Toast.LENGTH_SHORT).show();
                    accept_bt.setVisibility(View.INVISIBLE);
                    submit_bt.setVisibility(View.VISIBLE);
                    submit_bt.setText(getResources().getString(R.string.submitData));
                    submit_bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Activity_Task_Detail.this, Activity_Task_Submit.class);
                            intent.putExtra(getResources().getString(R.string.intent_taskID_name), task.getTaskId());
                            String sensorType = task.getSensorTypes();
                            //有效化判定
                            if(sensorType != null){
                                intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name),sensorType);
                            }else {
                                //TODO：无效化代码
                                //这里直接给予了默认的GPS传感器-1
                                intent.putExtra(getResources().getString(R.string.intent_taskSensorTypes_name),"-1");
                            }
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Log.i(TAG,getResources().getString(R.string.AcceptTaskFailed));
                    Toast.makeText(context, getResources().getString(R.string.AcceptTaskFailed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mToSensorServiceIntent != null) stopService(mToSensorServiceIntent);
    }

}
