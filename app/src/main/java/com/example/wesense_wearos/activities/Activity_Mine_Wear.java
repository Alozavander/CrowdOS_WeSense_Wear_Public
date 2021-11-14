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
import androidx.wear.widget.WearableRecyclerView;

import com.example.wesense_wearos.MCS_RecyclerItemClickListener;
import com.example.wesense_wearos.R;
import com.example.wesense_wearos.activities.mines_listitem.Activity_mine_minor1_publish;
import com.example.wesense_wearos.activities.mines_listitem.Activity_mine_minor2_accepted;
import com.example.wesense_wearos.activities.mines_listitem.Activity_mine_minor5_sensorData;
import com.example.wesense_wearos.activities.mines_listitem.Activity_mine_minor7_setting;
import com.example.wesense_wearos.adapters.Adapter_ListeView_mine;
import com.example.wesense_wearos.beans.Bean_ListView_mine;
import com.example.wesense_wearos.beans.Bean_Mine_UserAccount;
import com.example.wesense_wearos.taskSubmit.SelectDialog;

import java.util.ArrayList;
import java.util.List;

public class Activity_Mine_Wear extends WearableActivity {
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
        setContentView(R.layout.activity_mine);

        user_Name_tv = findViewById(R.id.minepage_login_userName);
        edit_bt = findViewById(R.id.minepage_infoEdit_bt);
        login_bt = findViewById(R.id.minepage_login_bt);   //通过MCSlistener去实现
        user_Icon = findViewById(R.id.minepage_login_userIcon);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("action_Fragment_mine_userInfo_login")) {
                    userInfo_Refresh();
                }
                if (action.equals("action_Fragment_mine_userInfo_quit")) {
                    userInfo_Recover();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("action_Fragment_mine_userInfo_login");
        filter.addAction("action_Fragment_mine_userInfo_quit");
        //注册了跳转页面的广播接收器，记得要解绑
        registerReceiver(receiver, filter);



        //初始化按钮，判定是否自动登录
        //initButton();

        //初始化列表数据
        initBean_LV_Mine();

    }


    //暂时弹出框用不上
    private void initUserIcon() {
        //绑定点击事件
        user_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> choiceList = new ArrayList<String>();
                choiceList.add(getString(R.string.checkUserIcon));
                choiceList.add(getString(R.string.updateUserIcon));
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {

                        }
                        if (position == 1) {

                        }
                    }
                }, choiceList);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //每次start时检查用户登录情况
        checkUserLoginStatue();
    }

    private void checkUserLoginStatue() {
        String userName = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userName", "");
        Bean_Mine_UserAccount lBean_mine_userAccount;
        if (userName.equals(""))
            lBean_mine_userAccount = new Bean_Mine_UserAccount(R.mipmap.haimian_usericon, "未登录", "请登录", false);
        else
            lBean_mine_userAccount = new Bean_Mine_UserAccount(R.mipmap.haimian_usericon, userName, "暂无签名", true);
        mAdapter_listeView_mine.updateUserAccount(lBean_mine_userAccount);
    }

    private void initButton() {
        //“登陆”按钮绑定相应事件——点击进入登陆页面，暂定他的int参数为用不到的0
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Mine_Wear.this, Activity_login_Wear.class);
                startActivity(intent);
            }
        });


        //编辑资料的button
        edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //11为此处不会用到的参数
                Intent intent = new Intent(Activity_Mine_Wear.this, Activity_EditInfo_Wear.class);
                        startActivity(intent);
            }
        });

        //根据当前app中的用户信息判定是否登录
        String userName = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userName", "");
        if (userName.equals("")) userInfo_Recover();
        else userInfo_Refresh();
    }


    private void initBean_LV_Mine() {
        List<Bean_ListView_mine> listView_mine = new ArrayList<>();

        //填充数据,有几行填充几行
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_yifabu, getResources().getString(R.string.fragment_mine_funclist_published)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_yijieshou, getResources().getString(R.string.fragment_mine_funclist_received)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_star, getResources().getString(R.string.fragment_mine_funclist_favorite)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_wallet, getResources().getString(R.string.fragment_mine_funclist_wallet)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_promotion, getResources().getString(R.string.setting_sensorfunction)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_message, getResources().getString(R.string.fragment_mine_funclist_notificaiton)));
        listView_mine.add(new Bean_ListView_mine(R.drawable.icon_setting, getResources().getString(R.string.fragment_mine_funclist_setting)));

        //关联到布局文件中的listview
        mRecyclerView = findViewById(R.id.minepage_login_funciton_lv);
        //创建用户登录信息的内容
        String userName = getSharedPreferences("user", Context.MODE_PRIVATE).getString("userName", "");
        Bean_Mine_UserAccount lBean_mine_userAccount;
        if (userName.equals(""))
            lBean_mine_userAccount = new Bean_Mine_UserAccount(R.mipmap.haimian_usericon, "未登录", "请登录", false);
        else
            lBean_mine_userAccount = new Bean_Mine_UserAccount(R.mipmap.haimian_usericon, userName, "暂无签名", true);

        //给adapter添加事件点击监听器
        mAdapter_listeView_mine = new Adapter_ListeView_mine(listView_mine, this, lBean_mine_userAccount);

        //mRecyclerView.setEdgeItemsCenteringEnabled(true);   //曲线布局用的WearableLayout与RecyclerView的多type貌似冲突
        //mRecyclerView.setLayoutManager(new WearableLinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, WearableRecyclerView.VERTICAL, false));
        mAdapter_listeView_mine.setItemClickListener(new MCS_RecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int login_userID = Integer.parseInt(getSharedPreferences("user", Context.MODE_PRIVATE).getString("userID", "-1"));
                if (login_userID == -1) {
                    //TODO
                        Intent intent = new Intent(Activity_Mine_Wear.this, Activity_login_Wear.class);
                        startActivity(intent);
                } else {
                    switch (position) {
                        case 1:        //0被改成登录adapter
                            //System.out.println("You click the first 1");
                            Intent intent_1 = new Intent(Activity_Mine_Wear.this, Activity_mine_minor1_publish.class);
                            startActivity(intent_1);
                            // Test Code:Toast.makeText(Activity_Mine_Wear.this, "You clicc B", Toast.LENGTH_LONG).show();
                            break;
                        case 2:
                            Intent intent_2 = new Intent(Activity_Mine_Wear.this, Activity_mine_minor2_accepted.class);
                            startActivity(intent_2);
                            break;
                       /* case 4:
                            Intent intent_4 = new Intent(Activity_Mine_Wear.this, Activity_mine_minor4_wallet.class);
                            startActivity(intent_4);
                            break;*/
                        case 5:
                            Intent intent_5 = new Intent(Activity_Mine_Wear.this, Activity_mine_minor5_sensorData.class);
                            startActivity(intent_5);
                            break;
                        case 7:
                            Intent intent_7 = new Intent(Activity_Mine_Wear.this, Activity_mine_minor7_setting.class);
                            startActivity(intent_7);
                            break;
                        default:
                            Toast.makeText(Activity_Mine_Wear.this, getResources().getString(R.string.notYetOpen), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter_listeView_mine);

    }

    //刷新用户页用户信息的方法
    public void userInfo_Refresh() {
        //这里的sharedPreferences用法暂时保存，其实也可以在login页面使用intent传送对应信息
        String userName = getSharedPreferences("user", MODE_PRIVATE).getString("userName", getResources().getString(R.string.fail_to_get_username));
        //这里的参数是一个Bean_Mine_UserAccount
        mAdapter_listeView_mine.updateUserAccount(new Bean_Mine_UserAccount(R.mipmap.haimian_usericon,userName,"暂无签名",true));
    }

    //刷新用户页用户信息的方法
    public void userInfo_Recover() {
        //这里的参数是一个Bean_Mine_UserAccount
        mAdapter_listeView_mine.updateUserAccount(new Bean_Mine_UserAccount(R.mipmap.haimian_usericon,getText(R.string.not_login).toString(),"请先登录",false));
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(Activity_Mine_Wear.this, R.style.transparentFrameWindowStyle, listener, names);
        //从activity的isFinishing转换而来，测试点
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑广播接收器
        unregisterReceiver(receiver);
    }
}
