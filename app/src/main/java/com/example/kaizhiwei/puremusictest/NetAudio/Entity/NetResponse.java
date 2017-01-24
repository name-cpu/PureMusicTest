package com.example.kaizhiwei.puremusictest.NetAudio.Entity;

/**
 * Created by 24820 on 2017/1/23.
 */
public class NetResponse {
    private int responseCode;
    private String responseBody;
    public NetRequest mRequest;

    public int getResponseCode(){
        return responseCode;
    }

    public void setResponseCode(int code){
        responseCode = code;
    }

    public String getResponseBody(){
        return responseBody;
    }

    public void setResponseBody(String str){
        responseBody = str;
    }
}
