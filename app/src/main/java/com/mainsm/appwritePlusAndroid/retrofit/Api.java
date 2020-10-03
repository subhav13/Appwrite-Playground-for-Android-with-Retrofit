package com.mainsm.appwritePlusAndroid.retrofit;

import com.mainsm.appwritePlusAndroid.model.requestModel.RegisterUserRequestBody;
import com.mainsm.appwritePlusAndroid.model.requestModel.SignInRequestBody;
import com.mainsm.appwritePlusAndroid.model.responseModel.RegisterUserResponse;
import com.mainsm.appwritePlusAndroid.model.responseModel.SignInResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Api {
    @POST("account/sessions")
    Call<SignInResponse> loginUser(@Body SignInRequestBody requestBody);

    @POST("account")
    Call<RegisterUserResponse> registerUser(@Body RegisterUserRequestBody userRequestBody);

    @DELETE
    Call<ResponseBody> deleteUserSession(@Url String url);
}
