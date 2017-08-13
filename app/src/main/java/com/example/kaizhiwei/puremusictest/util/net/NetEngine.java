package com.example.kaizhiwei.puremusictest.util.net;

import android.os.Message;

import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import com.example.kaizhiwei.puremusictest.util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by 24820 on 2017/1/23.
 */
public class NetEngine {
    private static NetEngine mInstance;

    synchronized static public NetEngine getInstance(){
        if(mInstance == null){
            mInstance = new NetEngine();
        }
        return mInstance;
    }

    private NetEngine(){
    }

    public void netGet(final String host, final String url, final Map<String, String> mapQuery, final BaseHandler handler){
        BaseRunnable runnable = new BaseRunnable(handler) {
            @Override
            public void doBusiness() throws Exception {
                StringBuilder sb = new StringBuilder();
                String strRequestUrlBase = String.format("http://%s/%s?from=%s&version=%s", host, url, PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION);
                sb.append(strRequestUrlBase);

                if(mapQuery != null && mapQuery.size() > 0){
                    Set<String> setKey = mapQuery.keySet();
                    for(String key : setKey){
                        sb.append("&" + key + "=" + mapQuery.get(key));
                    }
                }

                NetResponse response = executeRequest(sb.toString(), "GET");
                Message message = new Message();
                if(response == null || response.getResponseCode() != 200){
                    message.what = BusinessCode.BUSINESS_CODE_ERROR;
                }
                else{
                    message.what = BusinessCode.BUSINESS_CODE_SUCCESS;
                    message.obj = response.getResponseBody();
                }
                if(handler != null){
                    handler.sendMessage(message);
                }
            }
        };
    }

    private void addDefaultHeader(HttpURLConnection connection){
        connection.addRequestProperty("Accept-Encoding", PureMusicContant.ACCEPT_ENCODING);
        connection.addRequestProperty("cuid", PureMusicContant.CUID);
        connection.addRequestProperty("deviceid", DeviceUtil.getIMEI());
        connection.addRequestProperty("User-Agent", PureMusicContant.USER_AGENT);
    }

    private NetResponse executeRequest(String strUrl, String strRequestMethod){
        try {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            addDefaultHeader(connection);
            connection.setRequestMethod(strRequestMethod);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);

            NetResponse response = new NetResponse();
            response.setResponseCode(connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                InputStream in = connection.getInputStream();
                int len = 0;
                byte[] buf = new byte[1024];
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                while ((len = in.read(buf)) != -1) {
                    buffer.write(buf, 0, len);
                }
                in.close();

                URLDecoder ud = new URLDecoder();
                String str = ud.decode(new String(buffer.toByteArray()), "utf-8");
                response.setResponseBody(str);
            }

            return response;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
