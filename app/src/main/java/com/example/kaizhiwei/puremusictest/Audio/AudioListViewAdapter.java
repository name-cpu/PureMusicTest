package com.example.kaizhiwei.puremusictest.Audio;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntrty;
import com.example.kaizhiwei.puremusictest.R;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kaizhiwei on 16/11/12.
 */
public class AudioListViewAdapter extends BaseAdapter implements View.OnClickListener {

    public interface IAudioListViewListener{
        public void onItemClick(AudioListViewAdapter adapter, int position);
    }

    public static final int ADAPTER_TYPE_ALLSONG = 1;
    public static final int ADAPTER_TYPE_ARTIST = 2;
    public static final int ADAPTER_TYPE_ALBUM = 3;

    private int mAdapterType;
    private List<MediaEntrty> mListOrigin;
    private List<AudioItemData> mListItemData;
    private Context mContext;
    private Map<IAudioListViewListener, Object> mMapListener;

    public class AudioItemData{
        public static final int TYPE_OPERBAR = 0;
        public static final int TYPE_MEDIA = 1;
        public static final int TYPE_SEPERATOR = 2;
        public static final int TYPE_FOOTER = 3;

        public int mItemType;
        public String mFirstLetterPinYin;
        public String mMainTitle;
        public String mSubTitle;
        public String mSeparatorTitle;
        public List<MediaEntrty> mListMedia;
        public boolean isPlaying;
        public String mFooterInfo;

        AudioItemData(){
            isPlaying = false;
        }
    }

    public AudioListViewAdapter(List<MediaEntrty> list, Context context, int adapterType){
        mListOrigin = list;
        mContext = context;
        mAdapterType = adapterType;
        mListItemData = new ArrayList<>();
        mMapListener = new HashMap<>();
        initData();
    }

    public void initData(){
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

        if(mAdapterType == ADAPTER_TYPE_ALLSONG){
            Map<String, List<Integer>> mapFirstLetter = new TreeMap<String, List<Integer>>(new Comparator<String>() {
                public int compare(String key1, String key2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(key1, key2);
                }});

            for(int i = 0;i < mListOrigin.size();i++){
                MediaEntrty entrty = mListOrigin.get(i);
                if(entrty == null)
                    continue;

                String title = entrty.getFileName();
                char firstChar = title.toUpperCase(Locale.ENGLISH).charAt(0);
                String firstStr = "";
                if(Character.isLetter(firstChar)){
                    String[] vals = new String[0];
                    try {
                        vals = PinyinHelper.toHanyuPinyinStringArray(firstChar, format);
                    } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                        badHanyuPinyinOutputFormatCombination.printStackTrace();
                    }

                    boolean isFindPinYin = false;
                    List<String> listPinYin = null;
                    if(vals != null){
                        listPinYin = Arrays.asList(vals);
                        if(listPinYin != null && listPinYin.size() > 0){
                            isFindPinYin = true;
                        }
                    }

                    if(isFindPinYin){
                        firstStr = listPinYin.get(0).substring(0,1);
                    }
                    else{
                        firstStr = String.valueOf(firstChar);
                    }
                }
                else{
                    firstStr = "#";
                }

                List<Integer> value = mapFirstLetter.get(firstStr);
                if(value == null){
                    value = new ArrayList<>();
                    value.add(i);
                    mapFirstLetter.put(firstStr, value);
                }
                else{
                    value.add(i);
                }
            }

            for(String key : mapFirstLetter.keySet()){
                List<Integer> value = mapFirstLetter.get(key);
                for(int i = 0;i < value.size();i++){
                    MediaEntrty entrty = mListOrigin.get(value.get(i));
                    if(entrty == null)
                        continue ;

                    if(i == 0){
                        AudioItemData itemCategory = new AudioItemData();
                        itemCategory.mItemType = AudioItemData.TYPE_SEPERATOR;
                        itemCategory.mSeparatorTitle = key;
                        mListItemData.add(itemCategory);
                    }

                    AudioItemData itemData = new AudioItemData();
                    itemData.mMainTitle = entrty.getFileName();
                    if(entrty.getArtist() == null || entrty.getArtist().equals("null")){
                        itemData.mSubTitle = "unknown artist";
                    }
                    else{
                        itemData.mSubTitle = entrty.getArtist();
                    }

                    itemData.mSubTitle += " - ";
                    if(entrty.getAlbum() == null || entrty.getAlbum().equals("null")){
                        itemData.mSubTitle += "unknown album";
                    }
                    else{
                        itemData.mSubTitle = entrty.getAlbum();
                    }

                    itemData.mItemType = AudioItemData.TYPE_MEDIA;
                    itemData.mListMedia = new ArrayList<>();
                    itemData.mListMedia.add(entrty);
                    mListItemData.add(itemData);
                }
            }
        }
        else if(mAdapterType == ADAPTER_TYPE_ARTIST){
            Map<String, List<String>> mapFirstLetter = new TreeMap<String, List<String>>(new Comparator<String>() {
                public int compare(String key1, String key2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(key1, key2);
                }});

            //建立歌手和歌曲的对应关系
            Map<String, List<MediaEntrty>> mapArtist = new HashMap<>();
            for(int i = 0;i < mListOrigin.size();i++) {
                MediaEntrty entrty = mListOrigin.get(i);
                if (entrty == null)
                    continue;

                String artist = entrty.getArtist();
                if (artist == null) {
                    artist = "unknown artist";
                }

                List<MediaEntrty> value = mapArtist.get(artist);
                if(value == null){
                    value = new ArrayList<>();
                    value.add(entrty);
                    mapArtist.put(artist, value);
                }
                else{
                    value.add(entrty);
                }
            }

            //建立歌手与其首字符的对应关系
            for(String strArtist : mapArtist.keySet()){
                char firstChar = strArtist.toUpperCase(Locale.ENGLISH).charAt(0);
                String firstStr = "";
                if(Character.isLetter(firstChar)){
                    firstStr = strArtist.substring(0,1);
                }
                else{
                    firstStr = "#";
                }

                List<String> value = mapFirstLetter.get(firstStr);
                if(value == null){
                    value = new ArrayList<>();
                    value.add(strArtist);
                    mapFirstLetter.put(firstStr, value);
                }
                else{
                    value.add(strArtist);
                }
            }

            //建立列表数据
            for(String key : mapFirstLetter.keySet()){
                List<String> value = mapFirstLetter.get(key);
                for(int i = 0;i < value.size();i++){
                    if(i == 0){
                        AudioItemData itemCategory = new AudioItemData();
                        itemCategory.mMainTitle = value.get(i);
                        itemCategory.mSubTitle = "";
                        itemCategory.mItemType = AudioItemData.TYPE_SEPERATOR;
                        itemCategory.mSeparatorTitle = key;
                        mListItemData.add(itemCategory);
                    }

                    List<MediaEntrty> listEntrty = mapArtist.get(value.get(i));
                    if(listEntrty == null || listEntrty.size() == 0)
                        continue ;

                    AudioItemData itemData = new AudioItemData();
                    itemData.mMainTitle = value.get(i);
                    itemData.mSubTitle = String.format("%d song",listEntrty.size());
                    itemData.mListMedia = new ArrayList<>();
                    itemData.mListMedia.addAll(listEntrty);
                    mListItemData.add(itemData);
                }
            }
        }

        if(mListItemData.size() > 0){
            //添加操作栏
            AudioItemData operBarData = new AudioItemData();
            operBarData.mItemType = AudioItemData.TYPE_OPERBAR;
            mListItemData.add(0, operBarData);

            //添加footer
            AudioItemData footerData = new AudioItemData();
            footerData.mItemType = AudioItemData.TYPE_FOOTER;
            mListItemData.add(footerData);
        }
    }

    public void registerListener(IAudioListViewListener listener){
        if(mMapListener == null){
            mMapListener = new HashMap<>();
        }

        mMapListener.put(listener,listener);
    }

    public void unregisterListener(IAudioListViewListener listener){
        if(mMapListener == null){
            mMapListener = new HashMap<>();
        }

        if(mMapListener.containsKey(listener)){
            mMapListener.remove(listener);
        }
    }

    public int getAdapterType(){
        return mAdapterType;
    }

    public AudioItemData getAudioItemData(int index){
        if(index < 0 || index >= mListItemData.size())
            return null;

        return mListItemData.get(index);
    }

    @Override
    public int getCount() {
        if(mListItemData == null)
            return 0;

        return mListItemData.size();
    }

    @Override
    public Object getItem(int position) {
        if(mListItemData == null)
            return null;

        return mListItemData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mListItemData == null || position < 0 || position >= mListItemData.size())
            return null;

        AudioItemData data = mListItemData.get(position);
        if(data == null)
            return null;

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(getItemViewType(position) == AudioItemData.TYPE_OPERBAR){
            AudioOperatioinBarHolder holder = null;
            if(convertView == null){
                View view = inflater.inflate(R.layout.audio_operationbar, null);
                view.setBackgroundResource(R.color.backgroundColor);
                holder = new AudioOperatioinBarHolder(view);
                view.setTag(holder);
                convertView = view;
                holder.btnRandom.setOnClickListener(this);
                holder.btnBatchMgr.setOnClickListener(this);
            }
            else{
                holder = (AudioOperatioinBarHolder) convertView.getTag();
            }
        }
        else if(getItemViewType(position) == AudioItemData.TYPE_MEDIA){
            AudioListViewAdapterHolder holder = null;
            if(convertView == null){
                View view = inflater.inflate(R.layout.audio_item, null);
                view.setBackgroundResource(R.color.backgroundColor);
                holder = new AudioListViewAdapterHolder(view);
                view.setTag(holder);
                convertView = view;
                holder.ibBtnMore.setOnClickListener(this);
            }
            else{
                holder = (AudioListViewAdapterHolder) convertView.getTag();
            }
            holder.ibBtnMore.setTag(position);
            holder.tvSongMain.setText(data.mMainTitle);
            holder.tvSongSub.setText(data.mSubTitle);
            holder.viewSepratorLine.setBackgroundResource(R.color.listviewSeperatorLineColor);

            if(data.isPlaying){
                holder.tvSongMain.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.tvSongSub.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
            else{
                holder.tvSongMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
                holder.tvSongSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
            }
        }
        else if(getItemViewType(position) == AudioItemData.TYPE_SEPERATOR) {
            AudioListViewAdapterSeperatorHolder holder = null;
            if (convertView == null) {
                View view = inflater.inflate(R.layout.audio_seperator_item, null);
                holder = new AudioListViewAdapterSeperatorHolder(view);
                view.setTag(holder);
                convertView = view;
                view.setEnabled(false);
                view.setClickable(false);
                view.setBackgroundResource(R.color.backgroundColor);
                TextPaint tp = holder.tvCategaryName.getPaint();
                tp.setFakeBoldText(true);
                holder.tvCategaryName.setTextSize((float) 16.0);
                holder.tvCategaryName.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                holder = (AudioListViewAdapterSeperatorHolder) convertView.getTag();
            }
            holder.tvCategaryName.setText(data.mSeparatorTitle);
        }
        else if(getItemViewType(position) == AudioItemData.TYPE_FOOTER){
            AudioListViewFooterHolder holder = null;
            if(convertView == null){
                View view = inflater.inflate(R.layout.audio_footer_item, null);
                view.setBackgroundResource(R.color.backgroundColor);
                holder = new AudioListViewFooterHolder(view);

                int iCount = 0;
                for(int i = 0;i < mListItemData.size();i++){
                    AudioItemData tempdata = (AudioItemData)mListItemData.get(i);
                    if(tempdata == null || tempdata.mItemType != AudioItemData.TYPE_MEDIA)
                        continue;

                    iCount++;
                }
                holder.tvFooterInfo.setText(String.format("%d首歌曲", iCount));
                view.setTag(holder);
                convertView = view;
            }
            else{
                holder = (AudioListViewFooterHolder) convertView.getTag();
            }
        }

        return convertView;
    }

    public int getItemViewType(int position) {
        if(mListItemData == null || mListItemData.size() <= 0 ||  position >= mListItemData.size())
            return 0;

        AudioItemData data = mListItemData.get(position);
        return data.mItemType;
    }

    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public void onClick(View v) {
        for(IAudioListViewListener key : mMapListener.keySet()){
            if(key != null){
                //key.onItemClick(this, (int)v.getTag());
            }
        }
    }

    public void setItemPlayState(int index, boolean isPlaying){
        for(int i = 0;i < mListItemData.size();i++){
            AudioItemData tempdata = mListItemData.get(i);
            if(tempdata == null || tempdata.mItemType != AudioItemData.TYPE_MEDIA)
                continue;

            if(i == index){
                tempdata.isPlaying = isPlaying;
            }
            else{
                tempdata.isPlaying = false;
            }

        }
        notifyDataSetChanged();
    }

    private class AudioOperatioinBarHolder{
        public TextView btnRandom;
        public TextView btnBatchMgr;

        public AudioOperatioinBarHolder(View view){
            btnRandom = (TextView)view.findViewById(R.id.btnRandom);
            btnBatchMgr = (TextView)view.findViewById(R.id.btnBatchMgr);
        }
    }

    private class AudioListViewAdapterHolder{
        public TextView tvTopLine;
        public ImageView ivSongImage;
        public TextView tvSongMain;
        public TextView tvSongSub;
        public ImageView ibBtnMore;
        public View viewSepratorLine;

        public AudioListViewAdapterHolder(View view){
            tvTopLine = (TextView)view.findViewById(R.id.tvTopLine);
            ivSongImage = (ImageView)view.findViewById(R.id.ivSongImage);
            tvSongMain = (TextView)view.findViewById(R.id.tvSongMain);
            tvSongSub = (TextView)view.findViewById(R.id.tvSongSub);
            ibBtnMore = (ImageView)view.findViewById(R.id.ibBtnMore);
            viewSepratorLine = (View)view.findViewById(R.id.viewSepratorLine);
        }
    }

    private class AudioListViewAdapterSeperatorHolder{
        public TextView tvCategaryName;

        public AudioListViewAdapterSeperatorHolder(View view){
            tvCategaryName = (TextView)view.findViewById(R.id.tvCategaryName);

        }
    }

    private class AudioListViewFooterHolder{
        public TextView tvFooterInfo;

        public AudioListViewFooterHolder(View view){
            tvFooterInfo = (TextView)view.findViewById(R.id.tvFooterInfo);

        }
    }
}
