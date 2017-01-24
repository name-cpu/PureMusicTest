package com.example.kaizhiwei.puremusictest.NetAudio.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 24820 on 2017/1/23.
 */
public class NetGetPlazaIndexRequest extends NetRequest{
    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        map.put("channel", NetConfig.CHANNEL);
        map.put("operator", "2");
        map.put("method", NetConfig.METHOD_GETRECOMMANDINFO);
        map.put("cuid", NetConfig.CUID);
        map.put("focu_num", "8");
        return map;
    }

    @Override
    public String getRequestMethod() {
        return NetConfig.METHOD_GETRECOMMANDINFO;
    }
}
