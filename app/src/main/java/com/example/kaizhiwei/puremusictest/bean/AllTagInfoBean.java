package com.example.kaizhiwei.puremusictest.bean;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/7/15.
 */

public class AllTagInfoBean {
    private List<List<SongTagInfo>> taglist = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    public AllTagInfoBean(String info){
        parse(info);
    }

    private void parse(String info){
        if(TextUtils.isEmpty(info))
            return;

        try {
            JSONObject jsonObject = new JSONObject(info);
            JSONArray jsonTags = jsonObject.getJSONArray("tags");
            JSONObject jsonObjTagList = jsonObject.getJSONObject("taglist");

            for(int i = 0;i < jsonTags.length();i++){
                String str = jsonTags.getString(i);
                tags.add(str);
            }

            for(int i = 0;i < tags.size();i++){
                JSONArray jsonArray = jsonObjTagList.getJSONArray(tags.get(i));

                List<SongTagInfo> list = new ArrayList<>();
                for(int j = 0;j < jsonArray.length();j++){
                    SongTagInfo tagInfo = new SongTagInfo();
                    JSONObject jsonTag = jsonArray.getJSONObject(j);
                    tagInfo.setHot(jsonTag.getInt("hot"));
                    tagInfo.setTag_from(jsonTag.getInt("tag_from"));
                    tagInfo.setTitle(jsonTag.getString("title"));
                    list.add(tagInfo);
                }
                taglist.add(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<List<SongTagInfo>> getTaglist() {
        return taglist;
    }

    public void setTaglist(List<List<SongTagInfo>> taglist) {
        this.taglist = taglist;
    }

    public static class SongTagInfo{
        private String title;
        private int hot;
        private int tag_from;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getHot() {
            return hot;
        }

        public void setHot(int hot) {
            this.hot = hot;
        }

        public int getTag_from() {
            return tag_from;
        }

        public void setTag_from(int tag_from) {
            this.tag_from = tag_from;
        }
    }
}
