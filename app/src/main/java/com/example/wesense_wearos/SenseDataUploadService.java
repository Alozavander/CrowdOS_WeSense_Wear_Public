package com.example.wesense_wearos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.sense_function.sensorFunction.SenseHelper;
import com.example.sense_function.sensorFunction.SensorSQLiteOpenHelper;
import com.example.wesense_wearos.beans.SensorDetail;
import com.example.wesense_wearos.networkClasses.interfacesPack.PostRequestSensorDetailUploadService;
import com.example.wesense_wearos.utils.FileExportWear;
import com.example.wesense_wearos.utils.SqliteTimeUtil;
import com.google.gson.Gson;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SenseDataUploadService extends Service {
  private static final String TAG = "SenseDataUploadService";
  private Timer mTimer;
  private ExecutorService threadPool;
  private int UserID;

  @Nullable
  @Override
  public IBinder onBind(Intent pIntent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "SenseDataUploadService is on.");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      startForeground(1, initFrontServiceNotification());
    }
    ;
    initTimer();
    threadPool = Executors.newFixedThreadPool(3);
  }


  private Notification initFrontServiceNotification() {
    //获取notification的manager
    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    //创建channel
    NotificationChannel lChannel = new NotificationChannel("channel_sensedataupload", "sensedataupload", NotificationManager.IMPORTANCE_DEFAULT);
    notificationManager.createNotificationChannel(lChannel);
    Intent lNotificationIntent = new Intent(this, MainActivity.class);
    PendingIntent lPendingIntent = PendingIntent.getActivity(this, 0, lNotificationIntent, 0);
    //Use builder to build a notification
    Notification.Builder lBuilder = new Notification.Builder(this, "channel_sensedataupload").setContentIntent(lPendingIntent).setContentTitle("SenseDataUpload").setContentText("collecting data...");
    Notification lNotification = lBuilder.build();
    return lNotification;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return super.onStartCommand(intent, flags, startId);
  }

  private void initTimer() {
    Log.i(TAG, "Upload Timer starts");
    mTimer = new Timer();
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        UploadTask();
      }
    };
    //per hour a collection for a sensing data
    mTimer.schedule(task, 10*60*1000, 60 * 60 * 1000);
    Log.i(TAG, "Upload Timer Task now starts");
  }

  public void UploadTask() {
    String[] nowTime = SqliteTimeUtil.getStartAndEndTime();
    String startTime = nowTime[0];
    String endTime = nowTime[1];
    Log.i(TAG, "StartTime is : " + startTime);
    Log.i(TAG, "EndTime is : " + endTime);
    createAllSensorDataFile(startTime, endTime);
  }

  private void createAllSensorDataFile(String pStartTime, String pEndTime) {
    int[] sensorTypeList = new SenseHelper(this).getSensorList_TypeInt_Integers();
    for (final int i : sensorTypeList) {
      String whereClaus = StringStore.SensorDataTable_SenseType + "=?" + " AND " + StringStore.SensorDataTable_SenseTime
          + " > ? AND " + StringStore.SensorDataTable_SenseTime + " < ?";
      Cursor cursor = new SensorSQLiteOpenHelper(this).getReadableDatabase().query(StringStore.SensorDataTable_Name,
          new String[]{StringStore.SensorDataTable_SenseType,
              StringStore.SensorDataTable_SenseTime,
              StringStore.SensorDataTable_SenseData_1,
              StringStore.SensorDataTable_SenseData_2,
              StringStore.SensorDataTable_SenseData_3},
          whereClaus, new String[]{i + "", pStartTime, pEndTime}, null, null, null);
      if (cursor.getCount() > 0) {
        Log.i(TAG, "There has " + cursor.getCount() + " data for sensor " + i);
        final File saveFile = FileExportWear.exportToTextForEachSensor(cursor, i + "_" + SqliteTimeUtil.getCurrentTimeNoSpaceAndColon() + ".txt", null);
        Log.i(TAG, "The sensor data file size is " + saveFile.length());
        if (saveFile == null) continue;
        //使用线程池中的线程执行uploadFile任务
        threadPool.execute(new Runnable() {
          @Override
          public void run() {
            Log.i(TAG, "Now the " + Thread.currentThread().getName() + " upload the sensor " + i + " data");
            uploadFile(saveFile, i);
            Log.i(TAG, "Now the " + Thread.currentThread().getName() + " upload the sensor " + i + " data task done");
          }
        });
      } else Log.i(TAG, "The sensor " + i + " has no new data");
      cursor.close();
    }
  }

  private void uploadFile(final File pSaveFile, int sensorType) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");// HH:mm:ss
    /** Get the current time */
    Date date = new Date(System.currentTimeMillis());

    SensorDetail lSensorDetail = new SensorDetail(null, UserID, null, null, pSaveFile.getName(), sensorType + "", simpleDateFormat.format(date), null, null, null);
    Gson gson = new Gson();
    String postTask = gson.toJson(lSensorDetail);
    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postTask);
    /** Create the MultiparBody parsing the file */
    MultipartBody.Part body = MultipartBody.Part.createFormData("file", pSaveFile.getName(), RequestBody.create(okhttp3.MediaType.parse("file/*"), pSaveFile));
    Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
    /** Create the network api instance */
    PostRequestSensorDetailUploadService request = retrofit.create(
        PostRequestSensorDetailUploadService.class);
    Call<ResponseBody> call = request.uploadSensorMessage(requestBody, body);
    Log.i(TAG, "The Upload URL is: " + call.request().url() + "\n The content is:" + postTask);

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code() == 200) {
          Log.i(TAG, pSaveFile.getName() + " upload done.");
        } else {
          Log.i(TAG, "The response code is not 200, upload failed.");
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable throwable) {

      }
    });
  }
}
