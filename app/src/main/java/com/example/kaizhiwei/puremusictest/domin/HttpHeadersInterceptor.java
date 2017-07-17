package com.example.kaizhiwei.puremusictest.domin;

import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetConfig;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 24820 on 2017/6/27.
 */
public class HttpHeadersInterceptor implements Interceptor {
    public static final String DEVICE_TYPE = "android";
    public static final String APP_VERSION = "5.9.8.1";
    public static final String CHANNEL = "1426d";
    public static final String CUID = "1234567890";
    public static final String IMEI = DeviceUtil.getIMEI();
    public static final String ACCEPT_ENCODING = "gzip";
    public static final String USER_AGENT = "android_5.9.8.1;baiduyinyue";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orginRequest = chain.request();
        Request newRequest = addHeads(orginRequest);
        return chain.proceed(newRequest);
    }

    private Request addHeads(Request originRequest){
        Request.Builder requestBuilder = originRequest.newBuilder();
        requestBuilder.addHeader("Accept-Encoding", ACCEPT_ENCODING);
        requestBuilder.addHeader("cuid", CUID);
        requestBuilder.addHeader("deviceid", IMEI);
        requestBuilder.addHeader("User-Agent", USER_AGENT);

        if(originRequest.body() instanceof FormBody){
            FormBody.Builder newFormBOdy = new FormBody.Builder();
            FormBody oldFormBody = (FormBody) originRequest.body();
            for(int i = 0;i < oldFormBody.size();i++){
                newFormBOdy.add(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
            }
            requestBuilder.method(originRequest.method(), newFormBOdy.build());
        }
        else{
            requestBuilder.method(originRequest.method(), originRequest.body())
                    .url(originRequest.url());
        }

        return requestBuilder.build();
    }
}
