package com.example.kaizhiwei.puremusictest.domin;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;

/**
 * Created by 24820 on 2017/6/27.
 */
public class RetrofitClientBanFen  extends BaseRetrofitClient {
    private static RetrofitClientBanFen ourInstance = new RetrofitClientBanFen();

    public static RetrofitClientBanFen getInstance() {
        return ourInstance;
    }

    private RetrofitClientBanFen() {
    }

    @Override
    protected String getBaseUrl() {
        return String.format("http://%s/%s", PureMusicContant.HOST_BAIFEN, "");
    }
}
