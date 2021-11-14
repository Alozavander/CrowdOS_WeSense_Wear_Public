package com.example.wesense_wearos.networkClasses.interfacesPack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

//本接口是为了获取用户积分
public interface PostRequest_User_Coins {
    //Retrofit网络请求接口，GET里面为服务器的URL
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/user/getUser/{userId}")                                                //Post目标地址
    Call<ResponseBody> User_Coins(@Path("userId") Integer userId);
}
