package com.mainsm.appwritePlusAndroid.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mainsm.appwritePlusAndroid.utils.Constants;
import com.mainsm.appwritePlusAndroid.utils.PersistentCookieJar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private Context context;
    private static Retrofit retrofit = null;
    private static RetrofitClient mInstance = null;
    private static final String BASE_URL = "http://192.168.1.3/v1/";
    private RetrofitClient(Context context) {
        this.context = context;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()

                .addInterceptor(
                        chain -> {
                            Request request ;
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder()

                                    .addHeader("X-Appwrite-Project", Constants.PROJECT_ID)
                                    .addHeader("origin", "http://localhost")
                                    .addHeader("Content-Type", "application/json")


                                    .method(original.method(), original.body());
                            request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                )

                .addInterceptor(interceptor)
                .cookieJar(new PersistentCookieJar(context))
                .build();


        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new RetrofitClient(context);
        }
        return mInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }

}
