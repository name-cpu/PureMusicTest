package com.example.kaizhiwei.puremusictest.NetAudio.Entity;

import com.example.kaizhiwei.puremusictest.Util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 24820 on 2017/1/23.
 */
public class NetAlbumInfoData {
    public String album_id;
    public String author;
    public String title;
    public String publishcompany;
    public String prodcompany;
    public String country;
    public String language;
    public String songs_total;

    public String info;
    public String styles;
    public String style_id;
    public String publishtime;
    public String artist_ting_uid;
    public String all_artist_ting_uid;
    public String gender;
    public String area;
    public String pic_small;
    public String pic_big;
    public String hot;
    public int favorites_num;
    public int recommend_num;
    public int collect_num;
    public int share_num;
    public int comment_num;
    public String artist_id;
    public String all_artist_id;
    public String pic_radio;
    public String pic_s500;
    public String pic_s1000;
    public String ai_presale_flag;
    public String resource_type_ext;
    public String listen_num;
    public String buy_url;
    public List<NetSongInfo> listSongInfo;

    public boolean parser(String strJson){
        JSONObject jsonArray = null;
        try {
            jsonArray = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        try{
            JSONObject rootResult = jsonArray.getJSONObject("albumInfo");
            if(rootResult == null)
                return false;

            album_id = rootResult.getString("album_id");
            author = rootResult.getString("author");
            title = rootResult.getString("title");
            publishcompany = rootResult.getString("publishcompany");
            prodcompany = rootResult.getString("prodcompany");
            country = rootResult.getString("country");
            language = rootResult.getString("language");
            songs_total = rootResult.getString("songs_total");
            info = rootResult.getString("info");
            info = StringUtil.unicode2hanzi(info);
            styles = rootResult.getString("styles");
            style_id = rootResult.getString("style_id");
            publishtime = rootResult.getString("publishtime");
            artist_ting_uid = rootResult.getString("artist_ting_uid");
            all_artist_ting_uid = rootResult.getString("all_artist_ting_uid");
            gender = rootResult.getString("gender");
            area = rootResult.getString("area");
            gender = rootResult.getString("gender");
            pic_small = rootResult.getString("pic_small");
            pic_big = rootResult.getString("pic_big");
            hot = rootResult.getString("hot");
            favorites_num = rootResult.getInt("favorites_num");
            recommend_num = rootResult.getInt("recommend_num");
            collect_num = rootResult.getInt("collect_num");
            share_num = rootResult.getInt("share_num");
            comment_num = rootResult.getInt("comment_num");
            artist_id = rootResult.getString("artist_id");
            all_artist_id = rootResult.getString("all_artist_id");
            pic_radio = rootResult.getString("pic_radio");
            pic_s500 = rootResult.getString("pic_s500");
            pic_s1000 = rootResult.getString("pic_s1000");
            ai_presale_flag = rootResult.getString("ai_presale_flag");
            resource_type_ext = rootResult.getString("resource_type_ext");
            listen_num = rootResult.getString("listen_num");
            buy_url = rootResult.getString("buy_url");

            JSONArray jsonSongList = jsonArray.getJSONArray("songlist");
            if(jsonSongList != null){
                listSongInfo = new ArrayList<>();
                for(int i = 0;i < jsonSongList.length();i++){
                    NetSongInfo songInfo = new NetSongInfo();
                    songInfo.parser((JSONObject) jsonSongList.get(i));
                    listSongInfo.add(songInfo);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    static public class NetSongInfo{
        public String artist_id;
        public String all_artist_id;
        public String all_artist_ting_uid;
        public String language;
        public String publishtime;
        public String album_no;
        public String versions;
        public String pic_big;
        public String pic_small;
        public String hot;
        public String file_duration;
        public String del_status;
        public String resource_type;
        public String copy_type;
        public String has_mv_mobile;
        public String all_rate;
        public String toneid;
        public String country;
        public String area;
        public String lrclink;
        public String bitrate_fee;
        public String si_presale_flag;
        public String song_id;
        public String title;
        public String ting_uid;
        public String author;
        public String album_id;
        public String album_title;
        public String is_first_publish;
        public String havehigh;
        public String charge;
        public String has_mv;
        public String learn;
        public String song_source;
        public String piao_id;
        public String korean_bb_song;
        public String resource_type_ext;
        public String mv_provider;

        public void parser(JSONObject obj) {
            if (obj == null)
                return;

            try {
                artist_id = obj.getString("artist_id");
                all_artist_id = obj.getString("all_artist_id");
                all_artist_ting_uid = obj.getString("all_artist_ting_uid");
                language = obj.getString("language");
                publishtime = obj.getString("publishtime");
                album_no = obj.getString("album_no");
                versions = obj.getString("versions");
                pic_big = obj.getString("pic_big");
                pic_small = obj.getString("pic_small");
                hot = obj.getString("hot");
                file_duration = obj.getString("file_duration");
                del_status = obj.getString("del_status");
                resource_type = obj.getString("resource_type");
                copy_type = obj.getString("copy_type");
                has_mv_mobile = obj.getString("has_mv_mobile");
                all_rate = obj.getString("all_rate");
                toneid = obj.getString("toneid");
                country = obj.getString("country");
                area = obj.getString("area");
                lrclink = obj.getString("lrclink");
                bitrate_fee = obj.getString("bitrate_fee");
                si_presale_flag = obj.getString("si_presale_flag");
                song_id = obj.getString("song_id");
                title = obj.getString("title");
                ting_uid = obj.getString("ting_uid");
                author = obj.getString("author");
                album_id = obj.getString("album_id");
                album_title = obj.getString("album_title");
                is_first_publish = obj.getString("is_first_publish");
                havehigh = obj.getString("havehigh");
                charge = obj.getString("charge");
                has_mv = obj.getString("has_mv");
                learn = obj.getString("learn");
                song_source = obj.getString("song_source");
                piao_id = obj.getString("piao_id");
                korean_bb_song = obj.getString("korean_bb_song");
                resource_type_ext = obj.getString("resource_type_ext");
                mv_provider = obj.getString("mv_provider");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
