package com.example.wesense_wearos.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.wear.widget.WearableRecyclerView;

import com.example.wesense_wearos.MCS_RecyclerItemClickListener;
import com.example.wesense_wearos.R;
import com.example.wesense_wearos.beans.Bean_ListView_home;

import java.util.List;

public class adapter_listview_home extends WearableRecyclerView.Adapter<WearableRecyclerView.ViewHolder> {
    private final String TAG = "adapter_listview_home";
    private List<Bean_ListView_home> mBean_ListView_homes;
    private Context mContext;
    private LayoutInflater mInflater;
    private MCS_RecyclerItemClickListener mListener;

    public adapter_listview_home(List<Bean_ListView_home> pList, Context pContext) {
        mBean_ListView_homes = pList;
        mContext = pContext;
        mInflater = LayoutInflater.from(mContext);
    }

    //索引对应数据项的ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public WearableRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1) {
            view = mInflater.inflate(R.layout.adapter_home_listview_layout,parent,false);
            return new Home_ViewHolder(view, mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mBean_ListView_homes.size() <= 0){
            Log.i(TAG,"List<bean_listview_home> 为空");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return -1;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WearableRecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof Home_ViewHolder){
            Home_ViewHolder holder = (Home_ViewHolder) viewHolder;
            Bean_ListView_home bean = mBean_ListView_homes.get(position);
            //Log.i(TAG,"mBean_ListView_homes size : " + mBean_ListView_homes.size());
            //Log.i(TAG,"holder is null? : " + (holder == null));
            //Log.i(TAG,"holderView is null? : " + (holder.deadline_tv == null));          //测试holder内部组件是否为null
            holder.deadline_tv.setText(mContext.getString(R.string.Task_Detail_deadline) + "  " + bean.getDeadline());
            holder.coinsCount_tv.setText(bean.getCoinsCount());
            holder.taskName_tv.setText(bean.getTask().getTaskName());
            if(bean.getTask().getTaskKind() == null) holder.taskKind_tv.setText(mContext.getString(R.string.ordinaryTask));
            else {
                switch (bean.getTask().getTaskKind()){
                    case 0: holder.taskKind_tv.setText(mContext.getString(R.string.home_grid_0));break;
                    case 1:holder.taskKind_tv.setText(mContext.getString(R.string.home_grid_1));break;
                    case 2:holder.taskKind_tv.setText(mContext.getString(R.string.home_grid_2));break;
                    case 3:holder.taskKind_tv.setText(mContext.getString(R.string.home_grid_3));break;
                    case 4:holder.taskKind_tv.setText(mContext.getString(R.string.ordinaryTask));break;
                }
            }
            holder.taskContent_tv.setText(bean.getTaskContent());
            holder.time_tv.setText(bean.getPostTime());
            holder.photo_iv.setImageResource(bean.getPhoto());
            //测试使用，实际应使用网络加载路径
            holder.userIcon_iv.setImageResource(bean.getUserIcon());
            holder.userID_tv.setText(bean.getUserID());
            holder.taskCount_tv.setText(bean.getTaskCount() + "");
            Log.i(TAG,"数据加载完毕.");
        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mBean_ListView_homes.size();
    }

    //设置接口
    public void setItemClickListener(MCS_RecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    class Home_ViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView userIcon_iv;
        //private ImageView categoryIcon_iv;
        // private ImageView starIcon_iv;
        private TextView taskName_tv;
        private ImageView photo_iv;
        private TextView userID_tv;
        private TextView time_tv;
        private TextView taskKind_tv;
        private TextView taskContent_tv;
        private TextView taskCount_tv;
        private TextView coinsCount_tv;
        private TextView deadline_tv;
        private MCS_RecyclerItemClickListener m_MCS_recyclerItemClickListener;


        public Home_ViewHolder(@NonNull View itemView, MCS_RecyclerItemClickListener listener) {
            super(itemView);
            //对viewHolder的属性进行赋值
            taskName_tv = itemView.findViewById(R.id.listview_TaskName);
            //Log.i(TAG,"taskName_tv is null? " + (taskName_tv == null));
            //Log.i(TAG,"itemView is null? " + (itemView == null));
            userIcon_iv = itemView.findViewById(R.id.listview_userIcon);
            time_tv = itemView.findViewById(R.id.listview_Time);
            taskKind_tv = itemView.findViewById(R.id.listview_taskKind);
            taskContent_tv = itemView.findViewById(R.id.listview_TaskContent);
            taskCount_tv = itemView.findViewById(R.id.listview_TaskCount);
            coinsCount_tv = itemView.findViewById(R.id.listview_CoinsCount);
            deadline_tv = itemView.findViewById(R.id.listview_DeadlineText);
            userID_tv = itemView.findViewById(R.id.listview_userID);
            photo_iv = itemView.findViewById(R.id.listview_Photo);

            //设置回调接口
            this.m_MCS_recyclerItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(m_MCS_recyclerItemClickListener != null){
                m_MCS_recyclerItemClickListener.onItemClick(v,getLayoutPosition());
            }
        }
    }



    public void AddHeaderItem(List<Bean_ListView_home> items){
        mBean_ListView_homes.addAll(0,items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<Bean_ListView_home> items){
        mBean_ListView_homes.addAll(items);
        notifyDataSetChanged();
    }
}
