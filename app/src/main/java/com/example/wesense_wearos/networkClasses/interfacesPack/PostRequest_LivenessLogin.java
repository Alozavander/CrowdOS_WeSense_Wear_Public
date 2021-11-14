package com.example.wesense_wearos.networkClasses.interfacesPack;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostRequest_LivenessLogin {


    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("liveness/enterLiveness")                                                //Post目标地址
    Call<ResponseBody> livenessLogin(@Body RequestBody livenessInfo);
}
