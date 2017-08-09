package com.example.kaizhiwei.puremusictest.bean;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class PlazaIndexBean {
    public List<MixBean> mListMix;
    public List<ModBean> mListMod;
    public DiyBean mDiy;
    public FocusBean mFocus;
    public ShowList mShowList;
    public EntryBean mEntity;
    public RecSong mRecSong;
    public RadioBean mRadio;
    public ModuleBean mModule;
    public KingBean mKing;
    public NewSongBean mNewSong;
    public SceneBean mSceneBean;

    public MixBean findMixDataByModuleKey(String strKey){
        if(mListMix == null || TextUtils.isEmpty(strKey))
            return null;

        for(int i = 0;i < mListMix.size();i++){
            if(mListMix.get(i).moduleKey.equalsIgnoreCase(strKey))
                return mListMix.get(i);
        }

        return null;
    }

    public ModBean findModDataByModuleKey(String strKey){
        if(mListMod == null || TextUtils.isEmpty(strKey))
            return null;

        for(int i = 0;i < mListMod.size();i++){
            if(mListMod.get(i).moduleKey.equalsIgnoreCase(strKey))
                return mListMod.get(i);
        }

        return null;
    }

    public boolean parser(String strJson){
        JSONObject jsonArray = null;
        try {
            jsonArray = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        try{
            JSONObject rootResult = jsonArray.getJSONObject("result");
            int errorCode = jsonArray.getInt("error_code");
            JSONArray jmoduleArray = jsonArray.getJSONArray("module");
            if(jmoduleArray != null){
                mModule = new ModuleBean();
                mModule.parser(jsonArray);
            }

            if(rootResult != null) {
                mListMix = new ArrayList<>();
                for (int i = 0; i < mModule.listModule.size(); i++) {
                    String moduleName = mModule.listModule.get(i).key;
                    if (moduleName.contains("mix_") && rootResult.has(moduleName)) {
                        MixBean mixData = new MixBean();
                        JSONObject jsonObject = (JSONObject) rootResult.get(moduleName);
                        mixData.parser(jsonObject);
                        mixData.moduleKey = moduleName;
                        mListMix.add(mixData);
                    }
                }

                mListMod = new ArrayList<>();
                for (int i = 0; i < mModule.listModule.size(); i++) {
                    String moduleName = mModule.listModule.get(i).key;
                    if (moduleName.contains("mod_") && rootResult.has(moduleName)) {
                        ModBean mixData = new ModBean();
                        JSONObject jsonObject = (JSONObject) rootResult.get(moduleName);
                        mixData.parser(jsonObject);
                        mixData.moduleKey = moduleName;
                        mListMod.add(mixData);
                    }
                }

                if(rootResult.has("diy")){
                    mDiy = new DiyBean();
                    mDiy.parser(rootResult.getJSONObject("diy"));
                }

                if(rootResult.has("focus")){
                    mFocus = new FocusBean();
                    mFocus.parser(rootResult.getJSONObject("focus"));
                }

                if(rootResult.has("show_list")){
                    mShowList = new ShowList();
                    mShowList.parser(rootResult.getJSONObject("show_list"));
                }

                if(rootResult.has("entry")){
                    mEntity = new EntryBean();
                    mEntity.parser(rootResult.getJSONObject("entry"));
                }

                if(rootResult.has("recsong")){
                    mRecSong = new RecSong();
                    mRecSong.parser(rootResult.getJSONObject("recsong"));
                }

                if(rootResult.has("radio")){
                    mRadio = new RadioBean();
                    mRadio.parser(rootResult.getJSONObject("radio"));
                }

                if(rootResult.has("king")){
                    mKing = new KingBean();
                    mKing.parser(rootResult.getJSONObject("king"));
                }

                if(rootResult.has("new_song")){
                    mNewSong = new NewSongBean();
                    mNewSong.parser(rootResult.getJSONObject("new_song"));
                }

                if(rootResult.has("scene")){
                    mSceneBean = new SceneBean();
                    mSceneBean.parser(rootResult.getJSONObject("scene"));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    static public class DiyBean{
        public int error_code;
        public List<DiyItem> listDiyItem;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listDiyItem = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    DiyItem item = new DiyItem();
                    item.parser(jsonObject);
                    listDiyItem.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class DiyItem{
        public int position;
        public String tag;
        public String songidlist;
        public String pic;
        public String title;
        public int collectnum;
        public String type;
        public int listenum;
        public String listid;

        public void parser(JSONObject obj){
            try {
                position = obj.getInt("position");
                tag = obj.getString("tag");
                songidlist = obj.getString("songidlist");
                pic = obj.getString("pic");
                title = obj.getString("title");
                collectnum = obj.getInt("collectnum");
                type = obj.getString("type");
                listenum = obj.getInt("listenum");
                listid = obj.getString("listid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class EntryBean{
        public int error_code;
        public List<EntityItem> listEntityItem;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listEntityItem = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    EntityItem item = new EntityItem();
                    item.parser(jsonObject);
                    listEntityItem.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class EntityItem{
        public String day;
        public String title;
        public String icon;
        public String jump;

        public void parser(JSONObject obj){
            try {
                day = obj.getString("day");
                title = obj.getString("title");
                icon = obj.getString("icon");
                jump = obj.getString("jump");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    static public class FocusBean{
        public int error_code;
        public List<FocusItem> listFocus;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listFocus = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    FocusItem item = new FocusItem();
                    item.parser(jsonObject);
                    listFocus.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class FocusItem{
        public String randpic;
        public String code;
        public int mo_type;
        public int type;
        public String is_publish;
        public String randpic_iphone6;
        public String randpic_desc;

        public void parser(JSONObject obj){
            try {
                randpic = obj.getString("randpic");
                code = obj.getString("code");
                mo_type = obj.getInt("mo_type");
                type = obj.getInt("type");
                is_publish = obj.getString("is_publish");
                randpic_iphone6 = obj.getString("randpic_iphone6");
                randpic_desc = obj.getString("randpic_desc");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class ModBean{
        public int error_code;
        public String moduleKey;
        public List<ModItem> listMod;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listMod = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    ModItem item = new ModItem();
                    item.parser(jsonObject);
                    listMod.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class ModItem{
        public String desc;
        public String pic;
        public String type_id;
        public int type;
        public String title;
        public int tip_type;
        public String author;

        public void parser(JSONObject obj){
            if(obj == null)
                return;

            try {
                desc = obj.getString("desc");
                pic = obj.getString("pic");
                type_id = obj.getString("type_id");
                type = obj.getInt("type");
                title = obj.getString("title");
                tip_type = obj.getInt("tip_type");
                author = obj.getString("author");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class MixBean{
        public int error_code;
        public String moduleKey;
        public List<MixItem> listMix;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listMix = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    MixItem item = new MixItem();
                    item.parser(jsonObject);
                    listMix.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class MixItem{
        public String desc;
        public String pic;
        public String type_id;
        public int type;
        public String title;
        public int tip_type;
        public String author;

        public void parser(JSONObject obj){
            if(obj == null)
                return;

            try {
                desc = obj.getString("desc");
                pic = obj.getString("pic");
                type_id = obj.getString("type_id");
                type = obj.getInt("type");
                title = obj.getString("title");
                tip_type = obj.getInt("tip_type");
                author = obj.getString("author");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class ModuleBean{
        public int error_code;
        public List<ModuleItem> listModule;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("module");
                listModule = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    ModuleItem item = new ModuleItem();
                    item.parser(jsonObject);
                    listModule.add(item);
                }

                Collections.sort(listModule, new Comparator<ModuleItem>() {
                    @Override
                    public int compare(ModuleItem lhs, ModuleItem rhs) {
                        return lhs.pos - rhs.pos;
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public ModuleItem getModuleItemByKey(String strKey){
            if(listModule == null || listModule.size() == 0 || TextUtils.isEmpty(strKey))
                return null;

            for(int i = 0;i < listModule.size();i++){
                if(listModule.get(i).key.equalsIgnoreCase(strKey)){
                    return listModule.get(i);
                }
            }
            return null;
        }
    }

    static public class ModuleItem{
        public String link_url;
        public int pos;
        public String title;
        public String key;
        public String picurl;
        public String title_more;
        public int style;
        public String jump;

        public void parser(JSONObject obj){
            try {
                link_url = obj.getString("link_url");
                pos = obj.getInt("pos");
                title = obj.getString("title");
                key = obj.getString("key");
                picurl = obj.getString("picurl");
                title_more = obj.getString("title_more");
                style = obj.getInt("style");
                jump = obj.getString("jump");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class RadioBean{
        public int error_code;
        public List<RadioItem> listRadioItem;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listRadioItem = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    RadioItem item = new RadioItem();
                    item.parser(jsonObject);
                    listRadioItem.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class RadioItem{
        public String desc;
        public String itemid;
        public String title;
        public String album_id;
        public String type;
        public String channelid;
        public String pic;

        public void parser(JSONObject obj){
            try {
                desc = obj.getString("desc");
                itemid = obj.getString("itemid");
                title = obj.getString("title");
                album_id = obj.getString("album_id");
                type = obj.getString("type");
                channelid = obj.getString("channelid");
                pic = obj.getString("pic");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class RecSong{
        public int error_code;
        public List<RecSongItem> listRecSongItem;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listRecSongItem = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    RecSongItem item = new RecSongItem();
                    item.parser(jsonObject);
                    listRecSongItem.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class RecSongItem{
        public String resource_type_ext;
        public String learn;
        public String del_status;
        public String korean_bb_song;
        public String versions;
        public String title;
        public String bitrate_fee;
        public String song_id;
        public String has_mv_mobile;
        public String pic_premium;
        public String author;

        public void parser(JSONObject obj){
            try {
                resource_type_ext = obj.getString("resource_type_ext");
                learn = obj.getString("learn");
                del_status = obj.getString("del_status");
                korean_bb_song = obj.getString("korean_bb_song");
                versions = obj.getString("versions");
                title = obj.getString("title");
                bitrate_fee = obj.getString("bitrate_fee");
                song_id = obj.getString("song_id");
                has_mv_mobile = obj.getString("has_mv_mobile");
                pic_premium = obj.getString("pic_premium");
                author = obj.getString("author");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class ShowList{
        public int error_code;
        public List<ShowListItem> listShowListItem;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listShowListItem = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    ShowListItem item = new ShowListItem();
                    item.parser(jsonObject);
                    listShowListItem.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class ShowListItem{
        public String type;
        public String picture_iphone6;
        public String picture;
        public String web_url;

        public void parser(JSONObject obj){
            try {
                type = obj.getString("type");
                picture_iphone6 = obj.getString("picture_iphone6");
                picture = obj.getString("picture");
                web_url = obj.getString("web_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class KingBean{
        public int error_code;
        public List<KingItemBean> listKings;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONArray jsonArray = obj.getJSONArray("result");
                listKings = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    KingItemBean item = new KingItemBean();
                    item.parser(jsonObject);
                    listKings.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class KingItemBean{
        public String pic_big;
        public String title;
        public String author;

        public void parser(JSONObject obj){
            try {
                pic_big = obj.getString("pic_big");
                title = obj.getString("title");
                author = obj.getString("author");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class NewSongBean{
        public int error_code;
        public String pic_500;
        public String listid;
        public List<NewSongItemBean> listSongInfos;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                JSONObject jsonResult = obj.getJSONObject("result");
                pic_500 = jsonResult.getString("pic_500");
                listid = jsonResult.getString("listid");

                listSongInfos = new ArrayList<>();
                JSONArray arraySonglist = jsonResult.getJSONArray("song_info");
                for(int i = 0;i < arraySonglist.length();i++){
                    JSONObject jsonObject = (JSONObject)arraySonglist.get(i);
                    NewSongItemBean item = new NewSongItemBean();
                    item.parser(jsonObject);
                    listSongInfos.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class NewSongItemBean{
        public String author;
        public String title;
        public String pic_premium;
        public String song_id;

        public void parser(JSONObject obj){
            try {
                author = obj.getString("author");
                title = obj.getString("title");
                pic_premium = obj.getString("pic_premium");
                song_id = obj.getString("song_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class SceneBean{
        public int error_code;
        public List<SceneItemBean> listScreenItems;
        public List<SceneConfigBean> listConfigs;

        public void parser(JSONObject obj){
            try {
                error_code = obj.getInt("error_code");
                listConfigs = new ArrayList<>();
                JSONArray arrayconfig = obj.getJSONArray("config");
                for(int i = 0;i < arrayconfig.length();i++){
                    JSONObject jsonObject = (JSONObject)arrayconfig.get(i);
                    SceneConfigBean item = new SceneConfigBean();
                    item.parser(jsonObject);
                    listConfigs.add(item);
                }

                JSONObject jsonResult = obj.getJSONObject("result");

                listScreenItems = new ArrayList<>();
                Iterator<String> keys = jsonResult.keys();
                while(keys.hasNext()){
                    String key = keys.next();
                    JSONArray objTemp = jsonResult.getJSONArray(key);
                    for(int i = 0;i < objTemp.length();i++){
                        JSONObject jsonObject = (JSONObject)objTemp.get(i);
                        SceneItemBean item = new SceneItemBean();
                        item.parser(jsonObject);
                        listScreenItems.add(item);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class SceneConfigBean{
        public String play_color;
        public int scene_version;
        public int end_time;
        public String button_color;
        public String bgpic_special;
        public int start_time;
        public String color_other;
        public String bgpic;
        public String desc;
        public String scene_color;

        public void parser(JSONObject obj){
            try {
                play_color = obj.getString("play_color");
                scene_version = obj.getInt("scene_version");
                end_time = obj.getInt("end_time");
                button_color = obj.getString("button_color");
                bgpic_special = obj.getString("bgpic_special");
                start_time = obj.getInt("start_time");
                color_other = obj.getString("color_other");
                bgpic = obj.getString("bgpic");
                desc = obj.getString("desc");
                scene_color = obj.getString("scene_color");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    static public class SceneItemBean{
        public String scene_desc;
        public String bgpic_ios;
        public String icon_android;
        public String bgpic_android;
        public String scene_id;
        public String icon_ios;
        public String scene_model;
        public String scene_name;

        public void parser(JSONObject obj){
            try {
                scene_desc = obj.getString("scene_desc");
                bgpic_ios = obj.getString("bgpic_ios");
                icon_android = obj.getString("icon_android");
                bgpic_android = obj.getString("bgpic_android");
                scene_id = obj.getString("scene_id");
                icon_ios = obj.getString("icon_ios");
                scene_model = obj.getString("scene_model");
                scene_name = obj.getString("scene_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
