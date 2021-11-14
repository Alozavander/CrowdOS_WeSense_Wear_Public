package com.example.wesense_wearos.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostRequest_gridPage_taskList {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/task/getOrderTaskFromKind/{taskKind}")                                                //Post目标地址
    Call<ResponseBody> getCall(@Path("taskKind") int taskKindTag);
}
