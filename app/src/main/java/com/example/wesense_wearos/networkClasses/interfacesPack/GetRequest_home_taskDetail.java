package com.example.wesense_wearos.networkClasses.interfacesPack;


import com.example.wesense_wearos.pack_class.Task;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRequest_home_taskDetail {
    //Retrofit 网络请求接口，GET里的为服务器的URL
    @GET("xxxxxx")
    Call<Task> getCall();
}
