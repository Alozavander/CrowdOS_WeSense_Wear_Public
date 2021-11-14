package com.example.wesense_wearos.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.wear.widget.WearableRecyclerView;

import com.example.wesense_wearos.MCS_RecyclerItemClickListener;
import com.example.wesense_wearos.R;
import com.example.wesense_wearos.activities.Activity_EditInfo_Wear;
import com.example.wesense_wearos.activities.Activity_Mine_Wear;
import com.example.wesense_wearos.activities.Activity_login_Wear;
import com.example.wesense_wearos.beans.Bean_ListView_mine;
import com.example.wesense_wearos.beans.Bean_Mine_UserAccount;

import java.util.List;

public class Adapter_ListeView_mine extends WearableRecyclerView.Adapter<WearableRecyclerView.ViewHolder> {
    private List<Bean_ListView_mine> mBeanListView;
    private Bean_Mine_UserAccount mBean_Account;
    private final String TAG = "adapter_listview_mine";
    private LayoutInflater mInflater;//布局装载器对象
    private MCS_RecyclerItemClickListener mListener;
    private Context mContext;
    private Mine_UserAccount_ViewHolder mHolder;

    public Adapter_ListeView_mine(List<Bean_ListView_mine> beanListView, Context context,Bean_Mine_UserAccount pBean_Account) {
        mBeanListView = beanListView;
        mInflater = LayoutInflater.from(context);
        mBean_Account = pBean_Account;
        mContext = context;
    }

    public Mine_UserAccount_ViewHolder getMinewViewHoder(){
        return this.mHolder;
    }

    @NonNull
    @Override
    public WearableRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1) {             //那就是头部登录等出
            view = mInflater.inflate(R.layout.useraccount_minepage,parent,false);
            mHolder =new Mine_UserAccount_ViewHolder(view, mListener);
            return mHolder;
        }else if(viewType == 2){        //那就是列表
            view = mInflater.inflate(R.layout.listview_item_minepage,parent,false);
            return new Mine_List_ViewHolder(view, mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WearableRecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Mine_List_ViewHolder){
            Mine_List_ViewHolder viewHolder = (Mine_List_ViewHolder) holder;
            Bean_ListView_mine bean = mBeanListView.get(position-1); //因为第一个是Account

            viewHolder.icon_iv.setImageResource(bean.getIcon());
            viewHolder.title_tv.setText(bean.getTitle());

            Log.i(TAG,"Mine页面List数据加载完毕.");
        }else if(holder instanceof Mine_UserAccount_ViewHolder){
            Mine_UserAccount_ViewHolder viewHolder = (Mine_UserAccount_ViewHolder) holder;
            viewHolder.usersign_tv.setText(mBean_Account.getUsersign());
            viewHolder.username_tv.setText(mBean_Account.getUsername());
            viewHolder.usericon_iv.setImageResource(mBean_Account.getUsericon());

            if(mBean_Account.isLoginStatue()){
                viewHolder.login_bt.setVisibility(View.GONE);
                viewHolder.edit_bt.setVisibility(View.VISIBLE);
            }else {
                viewHolder.edit_bt.setVisibility(View.GONE);
                viewHolder.login_bt.setVisibility(View.VISIBLE);
            }

            viewHolder.edit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    System.out.println("Edit bt click.");
                    Intent intent1 = new Intent(mContext, Activity_EditInfo_Wear.class);
                    mContext.startActivity(intent1);
                }
            });
            viewHolder.login_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    System.out.println("Login bt click.");
                    Intent intent = new Intent(mContext, Activity_login_Wear.class);
                    mContext.startActivity(intent);
                }
            });

            Log.i(TAG,"Mine页面账户相关数据数据加载完毕.");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }else{
            Log.i(TAG,"instance 错误");
        }
    }

    @Override
    public int getItemCount() {
        return  mBeanListView.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return 1;
        else if(position > 0) return 2;
        else return -1;
    }

    //设置接口
    public void setItemClickListener(MCS_RecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    private class Mine_List_ViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView icon_iv;
        private TextView title_tv;
        private MCS_RecyclerItemClickListener m_MCS_recyclerItemClickListener;

        public Mine_List_ViewHolder(View itemView, MCS_RecyclerItemClickListener pListener) {
            super(itemView);

            icon_iv = itemView.findViewById(R.id.homepage_lvItem_icon);
            title_tv = itemView.findViewById(R.id.homepage_lvItem_title);

            //设置回调接口
            this.m_MCS_recyclerItemClickListener = pListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(m_MCS_recyclerItemClickListener != null){
                m_MCS_recyclerItemClickListener.onItemClick(v,getLayoutPosition());
            }
        }
    }

    private class Mine_UserAccount_ViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView usericon_iv;
        private TextView username_tv;
        private TextView usersign_tv;
        private Button login_bt;
        private Button edit_bt;
        private MCS_RecyclerItemClickListener m_MCS_recyclerItemClickListener;

        public Mine_UserAccount_ViewHolder(View itemView, MCS_RecyclerItemClickListener pListener) {
            super(itemView);

            usericon_iv = itemView.findViewById(R.id.minepage_login_userIcon);
            username_tv = itemView.findViewById(R.id.minepage_login_userName);
            usersign_tv = itemView.findViewById(R.id.minepage_login_userSign);
            login_bt = itemView.findViewById(R.id.minepage_login_bt);
            edit_bt = itemView.findViewById(R.id.minepage_infoEdit_bt);

            //设置回调接口
            this.m_MCS_recyclerItemClickListener = pListener;
            itemView.setOnClickListener(this);
        }

        public ImageView getUsericon_iv() {
            return usericon_iv;
        }

        public void setUsericon_iv(ImageView pUsericon_iv) {
            usericon_iv = pUsericon_iv;
        }

        public TextView getUsername_tv() {
            return username_tv;
        }

        public void setUsername_tv(TextView pUsername_tv) {
            username_tv = pUsername_tv;
        }

        public TextView getUsersign_tv() {
            return usersign_tv;
        }

        public void setUsersign_tv(TextView pUsersign_tv) {
            usersign_tv = pUsersign_tv;
        }

        public Button getLogin_bt() {
            return login_bt;
        }

        public void setLogin_bt(Button pLogin_bt) {
            login_bt = pLogin_bt;
        }

        public Button getEdit_bt() {
            return edit_bt;
        }

        public void setEdit_bt(Button pEdit_bt) {
            edit_bt = pEdit_bt;
        }

        public MCS_RecyclerItemClickListener getM_MCS_recyclerItemClickListener() {
            return m_MCS_recyclerItemClickListener;
        }

        public void setM_MCS_recyclerItemClickListener(MCS_RecyclerItemClickListener pM_MCS_recyclerItemClickListener) {
            m_MCS_recyclerItemClickListener = pM_MCS_recyclerItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if(m_MCS_recyclerItemClickListener != null){
                m_MCS_recyclerItemClickListener.onItemClick(v,getLayoutPosition());
            }

        }
    }

    public void updateUserAccount(Bean_Mine_UserAccount pBean_Account){
        this.mBean_Account = pBean_Account;
        notifyDataSetChanged();
    }


}
