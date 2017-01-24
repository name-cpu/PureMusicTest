package com.example.kaizhiwei.puremusictest.NetAudio.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 24820 on 2017/1/23.
 */
public class NetGetAlbumInfoRequest extends NetRequest{
    private String strAlbumKey;

    @Override
    public String getUrl() {
        return null;
    }

    public void setAlbumKey(String strKey){
        strAlbumKey = strKey;
    }

    public String getAlbumKey(){
        return strAlbumKey;
    }

    @Override
    public Map<String, String> getParams() {

        // channel=1382d&operator=-1&method=baidu.ting.album.getAlbumInfo&format=json&album_id=275347355

        Map<String, String> map = new HashMap<>();
        map.put("channel", NetConfig.CHANNEL);
        map.put("operator", "-1");
        map.put("method", NetConfig.METHOD_GETMLBUMINFO);
        map.put("format", "json");
        map.put("focu_num", "8");
        map.put("album_id", strAlbumKey);
        return map;
    }

    @Override
    public String getRequestMethod() {
        return NetConfig.METHOD_GETMLBUMINFO;
    }
}
