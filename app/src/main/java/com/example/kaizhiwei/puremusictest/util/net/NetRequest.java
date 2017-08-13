package com.example.kaizhiwei.puremusictest.util.net;

import java.util.Map;

/**
 * Created by 24820 on 2017/1/23.
 */
public abstract class NetRequest {
    public abstract String getUrl();
    public abstract Map<String, String> getParams();
    public String getRequestType(){
        return "GET";
    }
    public abstract String getRequestMethod();
}
