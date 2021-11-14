package com.example.wesense_wearos;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.sense_function.sensorFunction.SenseHelper;
import com.example.sense_function.sensorFunction.SensorService;
import com.example.sense_function.sensorFunction.SensorService_Interface;

import static android.content.Context.BIND_AUTO_CREATE;

public class SenseServiceInitHelper {
    private static final String TAG = "SebseServiceInitHelper";
    private Context mContext;
    private SensorService_Interface sensorServiceInterface;
    private boolean isBind;
    private ServiceConnection conn;
    public SenseHelper sh;

    public SenseServiceInitHelper(Context pContext) {
        mContext = pContext;
    }

    public boolean initService() {
        Log.i(TAG, "=======Now Init the sensor Service...===========");
        /** Init the sensor sensing function Service */
        sh = new SenseHelper(mContext);
        Intent intent = new Intent(mContext, SensorService.class);
        if (conn == null) {
            Log.i(TAG, "===========connection creating...============");
            conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    sensorServiceInterface = (SensorService_Interface) service;
                    Log.i(TAG, "sensorService_interface connection is done.");
                    sensorServiceInterface.binder_sensorOn(sh.getSensorList_TypeInt_Integers());
                    Log.i(TAG, "SensorService's sensorOn has been remote.");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.i(TAG, "sensorService disconnected.");
                }
            };
        } else {
            Log.i(TAG, "===============sensorService connection exits.================");
        }
        isBind = mContext.bindService(intent, conn, BIND_AUTO_CREATE);
        Log.i(TAG, "=============SensorService has been bound :" + isBind + "==============");
        return isBind;
    }
}
