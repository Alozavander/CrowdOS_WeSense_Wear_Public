package com.example.wesense_wearos.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.wear.widget.WearableRecyclerView;

import com.example.wesense_wearos.MCS_RecyclerItemClickListener;
import com.example.wesense_wearos.R;
import com.example.wesense_wearos.adapters.adapter_listview_home;
import com.example.wesense_wearos.beans.Bean_ListView_home;
import com.example.wesense_wearos.networkClasses.interfacesPack.GetNewTen_Request_home_taskList;
import com.example.wesense_wearos.networkClasses.interfacesPack.GetRequest_home_taskList;
import com.example.wesense_wearos.pack_class.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_Home_Wear extends WearableActivity {
    private WearableRecyclerView mRecyclerView;                                              //为首页显示任务列表的RecyclerView
    private List<Bean_ListView_home> mBean_ListView_homes;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private adapter_listview_home mAdapter_listview_home;
    private List<Task> mRequest_TaskList;
    private Context mContext = this;
    int[] photoPath = {R.mipmap.testphoto_1, R.mipmap.testphoto_2, R.mipmap.testphoto_3, R.mipmap.testphoto_4};
    private Set<Integer> mHashSet_TaskID;                                             //用于获取感知任务去重
    private String TAG = "Actvity_Home";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mBean_ListView_homes = new ArrayList<>();
        mHashSet_TaskID = new HashSet<Integer>();
        mRequest_TaskList = new ArrayList<Task>();


        initRecyclerView();
    }

    private void initRecyclerView() {
        //第一次初始化任务
        if (mBean_ListView_homes == null) {
            mBean_ListView_homes = new ArrayList<Bean_ListView_home>();
        }

        //初始化SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.home_swiperefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        mRecyclerView = findViewById(R.id.home_recycler_view);
        //进入页面初始化任务列表
        first_ListRefresh();
        mAdapter_listview_home = new adapter_listview_home(mBean_ListView_homes,this);
        //recyclerView没有跟listView一样封装OnItemClickListener，所以只能手动实现，这里是将监听器绑定在了适配器上
        mAdapter_listview_home.setItemClickListener(new MCS_RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int login_userID = Integer.parseInt(getSharedPreferences("user", MODE_PRIVATE).getString("userID", "-1"));
                //检查是否登录 login_userID == -1
                if (login_userID == -1) {
                    Toast.makeText(Activity_Home_Wear.this, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activity_Home_Wear.this, Activity_login_Wear.class);
                    startActivity(intent);
                }else{
                    Gson gson = new Gson();
                    Intent intent = new Intent(Activity_Home_Wear.this, Activity_Task_Detail.class);
                    intent.putExtra("taskGson", gson.toJson(mBean_ListView_homes.get(position).getTask()));
                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setEdgeItemsCenteringEnabled(true);
        //mRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, WearableRecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter_listview_home);

        initRefreshListener();
    }


    //第一次刷新列表
    private void first_ListRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          getRequest(0);
                                      }

                                  }, 3000
        );
    }

    //初始化刷新监听，包含再请求网络数据传输
    private void initRefreshListener() {
        initPullRefresh();  //上拉刷新
        initLoadMoreListener();  //下拉加载更多
    }

    private void initPullRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  getRequest(1);
                                              }

                                          }, 3000
                );

            }
        });
    }


    private void initLoadMoreListener() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter_listview_home.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getNewTenTaskRequest();
                        }
                    }, 3000);

                }

            }

            //更新lastvisibleItem数值
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }



    public void getRequest(int tag) {
        //根据tag标记不同判定添加到列表开头（1）还是列表尾部（2）,0为初始刷新使用
        final int tempTag = tag;
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder().baseUrl(this.getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        //测试用url
        //Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.60:8889/").addConverterFactory(GsonConverterFactory.create()).build();

        //创建网络接口实例
        GetRequest_home_taskList request_getTaskList = retrofit.create(GetRequest_home_taskList.class);
        //包装发送请求
        Call<ResponseBody> call = request_getTaskList.getCall();

        Log.i("TIMMMMMMMME!","NowTime:" + System.currentTimeMillis());
        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {}.getType();
                    try {
                        //在此附近应该加入内容判定，优化响应逻辑
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        mRequest_TaskList = gson.fromJson(temp, type);
                        //成功获取网络请求内容后，调用内容处理方法
                        //Toast.makeText(mContext,temp,Toast.LENGTH_SHORT).show();
                        Log.i(TAG, mRequest_TaskList.size() + "");
                        List<Bean_ListView_home> tempList = new ArrayList<Bean_ListView_home>();
                        if (mRequest_TaskList.size() > 0) {
                            for (Task task : mRequest_TaskList) {
                                Log.i(TAG,task.toString());
                                if (!mHashSet_TaskID.contains(task.getTaskId())) {
                                    mHashSet_TaskID.add(task.getTaskId());
                                    tempList.add(new Bean_ListView_home(R.mipmap.cat_usericon, task.getUserName(), photoPath[new Random().nextInt(3)], "普通任务", task));
                                }
                            }
                        } else {
                            Toast.makeText(Activity_Home_Wear.this, getResources().getString(R.string.FailToGetData) + mRequest_TaskList.size(), Toast.LENGTH_SHORT).show();
                        }
                        if (tempTag == 0) mAdapter_listview_home.AddHeaderItem(tempList);
                        else if(tempTag == 1)mAdapter_listview_home.AddHeaderItem(tempList);
                        else mAdapter_listview_home.AddFooterItem(tempList);


                        //刷新完成
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Log.i("TIMMMMMMMME!","NowTime:" + System.currentTimeMillis());
                        Toast.makeText(Activity_Home_Wear.this, getResources().getString(R.string.Refresh) + tempList.size() + getResources().getString(R.string.Now) + mBean_ListView_homes.size() + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else Log.i(TAG,"Failed to get request. The response code is :" + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void getNewTenTaskRequest() {

        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //创建网络接口实例
        GetNewTen_Request_home_taskList request_getTaskList = retrofit.create(GetNewTen_Request_home_taskList.class);
        //包装发送请求
        Call<ResponseBody> call = request_getTaskList.query_NewTenTask(minTaskId());


        //异步网络请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd").create();
                    Type type = new TypeToken<List<Task>>() {}.getType();
                    try {
                        //在此附近应该加入内容判定，优化响应逻辑
                        String temp = response.body().string();
                        Log.i(TAG, temp);
                        mRequest_TaskList = gson.fromJson(temp, type);
                        //成功获取网络请求内容后，调用内容处理方法
                        //Toast.makeText(mContext,temp,Toast.LENGTH_SHORT).show();
                        Log.i(TAG, mRequest_TaskList.size() + "");
                        List<Bean_ListView_home> tempList = new ArrayList<Bean_ListView_home>();
                        if (mRequest_TaskList.size() > 0) {
                            for (Task task : mRequest_TaskList) {
                                Log.i(TAG,task.toString());
                                if (!mHashSet_TaskID.contains(task.getTaskId())) {
                                    mHashSet_TaskID.add(task.getTaskId());
                                    tempList.add(new Bean_ListView_home(R.mipmap.cat_usericon, task.getUserName(), photoPath[new Random().nextInt(3)], getResources().getString(R.string.ordinaryTask), task));
                                }
                            }
                        } else {
                            Toast.makeText(Activity_Home_Wear.this, getResources().getString(R.string.taskNoNew), Toast.LENGTH_SHORT).show();
                        }
                        mAdapter_listview_home.AddFooterItem(tempList);


                        //刷新完成
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(Activity_Home_Wear.this, getResources().getString(R.string.Refresh) + " " + tempList.size() + " " + getResources().getString(R.string.Now)  + " " + mBean_ListView_homes.size()  + " " + getResources().getString(R.string.TaskNum), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Activity_Home_Wear.this, getResources().getString(R.string.FailToGetData), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private int minTaskId(){
        int min = Integer.MAX_VALUE;
        for(int i : mHashSet_TaskID){
            if(i < min) min = i;
        }
        return min;
    }
}
