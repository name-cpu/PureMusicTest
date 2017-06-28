package com.example.kaizhiwei.puremusictest.domin;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;

/**
 * Created by 24820 on 2017/6/27.
 */
public class RetrofitClient extends BaseRetrofitClient {
    private static RetrofitClient ourInstance = new RetrofitClient();

    public static RetrofitClient getInstance() {
        return ourInstance;
    }

    private RetrofitClient() {
    }

    @Override
    protected String getBaseUrl() {
        return String.format("http://%s", PureMusicContant.HOST);
    }
}
