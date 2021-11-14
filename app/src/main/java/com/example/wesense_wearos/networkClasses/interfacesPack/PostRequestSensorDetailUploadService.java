package com.example.wesense_wearos.networkClasses.interfacesPack;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/** Upload File */
public interface PostRequestSensorDetailUploadService {
    @Multipart
    @POST("/sensordetail/uploadSensorFileMessageDetail")
    Call<ResponseBody> uploadSensorMessage(@Part("sensor_detail") RequestBody sensorDetail, @Part MultipartBody.Part file);
}
