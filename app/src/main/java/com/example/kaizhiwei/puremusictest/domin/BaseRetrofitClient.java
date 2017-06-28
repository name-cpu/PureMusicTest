package com.example.kaizhiwei.puremusictest.domin;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 24820 on 2017/6/27.
 */
public abstract class BaseRetrofitClient {
    public static final long DEFAULT_CONNECT_TIMEOUT = 10*1000;
    public static final long DEFAULT_READ_TIMEOUT = 10*1000;
    public static final long DEFAULT_WRITE_TIMEOUT = 10*1000;

    private Retrofit retrofit;

    protected BaseRetrofitClient(){
        retrofit = createRetrofit();
    }

    private OkHttpClient createOkHttp(){
        HttpHeadersInterceptor headersInterceptor = new HttpHeadersInterceptor();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(headersInterceptor)
                .build();
        return okHttpClient;
    }

    protected Retrofit createRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .client(createOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl())
                .build();
        return retrofit;
    }

    protected abstract String getBaseUrl();

    public <T> T create(Class<?> clazz){
        return (T) retrofit.create(clazz);
    }
}
