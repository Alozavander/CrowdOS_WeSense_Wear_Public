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

import java.util.List;

import com.example.wesense_wearos.beans.bean_listview_main;

public class adapter_listview_main extends WearableRecyclerView.Adapter<WearableRecyclerView.ViewHolder> {
    private final String TAG = "adapter_listview_main";
    private List<bean_listview_main> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private MCS_RecyclerItemClickListener mListener;

    public adapter_listview_main(List<bean_listview_main> pList, Context pContext) {
        mList = pList;
        mContext = pContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public WearableRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1) {
            view = mInflater.inflate(R.layout.adapter_main_listview_layout,parent,false);
            return new Main_ViewHolder(view, mListener);
        }else{
            Log.i(TAG,"viewType返回值出错");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mList == null){
            Log.i(TAG,"List<bean_listview_main> 为空");
            //Toast.makeText(mConetxt,"viewType返回值出错",Toast.LENGTH_SHORT).show();
            return -1;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WearableRecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Main_ViewHolder){
            Main_ViewHolder lHolder = (Main_ViewHolder) holder;
            bean_listview_main lbean = mList.get(position);

            lHolder.navigationIcon_iv.setImageResource(lbean.getImage());
            lHolder.navigationName_tv.setText(lbean.getName());
        }else{
            Log.i(TAG,"instance 错误");
            //Toast.makeText(mConetxt,"instance 错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //设置接口
    public void setItemClickListener(MCS_RecyclerItemClickListener listener) {
        this.mListener = listener;
    }

    class Main_ViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView navigationIcon_iv;
        //private ImageView categoryIcon_iv;
        // private ImageView starIcon_iv;
        private TextView navigationName_tv;
        private MCS_RecyclerItemClickListener m_MCS_recyclerItemClickListener;


        public Main_ViewHolder(@NonNull View itemView, MCS_RecyclerItemClickListener pListener) {
            super(itemView);

            navigationIcon_iv = itemView.findViewById(R.id.navigation_icon);
            navigationName_tv = itemView.findViewById(R.id.navigation_tv);

            this.m_MCS_recyclerItemClickListener = mListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View pView) {
            if(m_MCS_recyclerItemClickListener != null){
                m_MCS_recyclerItemClickListener.onItemClick(pView,getLayoutPosition());
            }
        }
    }
}
