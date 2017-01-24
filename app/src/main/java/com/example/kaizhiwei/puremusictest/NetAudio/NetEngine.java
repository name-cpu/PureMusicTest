package com.example.kaizhiwei.puremusictest.NetAudio;

import android.os.Message;
import android.util.Log;

import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetAlbumInfoData;
import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetConfig;
import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetPlazaIndexData;
import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetRequest;
import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetResponse;
import com.example.kaizhiwei.puremusictest.Util.BaseHandler;
import com.example.kaizhiwei.puremusictest.Util.BusinessCode;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 24820 on 2017/1/23.
 */
public class NetEngine {
    private ExecutorService mThreadPool;
    private static NetEngine mInstance;

    synchronized static public NetEngine getInstance(){
        if(mInstance == null){
            mInstance = new NetEngine();
        }
        return mInstance;
    }

    private NetEngine(){
        mThreadPool = Executors.newFixedThreadPool(4);
    }

    public void asyncGetRecommandInfo(final NetRequest request, final BaseHandler handler){
        if(request == null || handler == null)
            return;

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                String strRequestUrlBase = String.format("http://%s/%s?from=%s&version=%s", NetConfig.HOST, NetConfig.URL, NetConfig.DEVICE_TYPE, NetConfig.APP_VERSION);
                sb.append(strRequestUrlBase);

                Map<String, String> mapParams = request.getParams();
                if(mapParams != null && mapParams.size() > 0){
                    Set<String> setKey = mapParams.keySet();
                    for(String key : setKey){
                        sb.append("&" + key + "=" + mapParams.get(key));
                    }
                }

                NetResponse response = executeRequest(sb.toString(), request.getRequestType());
                Message message = handler.obtainMessage();
                if(response == null || response.getResponseCode() != 200){
                    message.what = BusinessCode.BUSINESS_CODE_ERROR;
                }
                else{
                    message.what = BusinessCode.BUSINESS_CODE_SUCCESS;
                    NetPlazaIndexData data = new NetPlazaIndexData();
                    boolean bRet = data.parser(response.getResponseBody());
                    if(bRet){
                        message.obj = data;
                    }
                }
                handler.sendMessage(message);
            }
        });
    }

    public void asyncGetAlbumInfo(final NetRequest request, final BaseHandler handler){
        if(request == null || handler == null)
            return;

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                String strRequestUrlBase = String.format("http://%s/%s?from=%s&version=%s", NetConfig.HOST, NetConfig.URL, NetConfig.DEVICE_TYPE, NetConfig.APP_VERSION);
                sb.append(strRequestUrlBase);

                Map<String, String> mapParams = request.getParams();
                if(mapParams != null && mapParams.size() > 0){
                    Set<String> setKey = mapParams.keySet();
                    for(String key : setKey){
                        sb.append("&" + key + "=" + mapParams.get(key));
                    }
                }

                NetResponse response = executeRequest(sb.toString(), request.getRequestType());
                Message message = handler.obtainMessage();
                if(response == null || response.getResponseCode() != 200){
                    message.what = BusinessCode.BUSINESS_CODE_ERROR;
                }
                else{
                    message.what = BusinessCode.BUSINESS_CODE_SUCCESS;
                    NetAlbumInfoData data = new NetAlbumInfoData();
                    boolean bRet = data.parser(response.getResponseBody());
                    if(bRet){
                        message.obj = data;
                    }
                }
                handler.sendMessage(message);
            }
        });
    }

    private void addDefaultHeader(HttpURLConnection connection){
        connection.addRequestProperty("Accept-Encoding", NetConfig.ACCEPT_ENCODING);
        connection.addRequestProperty("cuid", NetConfig.CUID);
        connection.addRequestProperty("deviceid", DeviceUtil.getIMEI());
        connection.addRequestProperty("User-Agent", NetConfig.USER_AGENT);
    }

    private NetResponse executeRequest(String strUrl, String strRequestMethod){
        Log.i("weikaizhi", "request url: " + strUrl);
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            addDefaultHeader(connection);
            connection.setRequestMethod(strRequestMethod);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);

            StringBuilder sb = new StringBuilder();
            NetResponse response = new NetResponse();
            response.setResponseCode(connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len, "utf-8"));
                }
                in.close();
                response.setResponseBody(sb.toString());
            }
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
