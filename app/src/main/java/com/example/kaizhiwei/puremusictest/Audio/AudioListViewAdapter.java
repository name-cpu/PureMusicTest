package com.example.kaizhiwei.puremusictest.Audio;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.R;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by kaizhiwei on 16/11/12.
 */
public class AudioListViewAdapter extends BaseAdapter implements View.OnClickListener {

    public interface IAudioListViewListener{
        public void onItemClick(AudioListViewAdapter adapter, int position);
    }

    public static final int ADAPTER_TYPE_ALLSONG = 1;
    public static final int ADAPTER_TYPE_FOLDER = 2;
    public static final int ADAPTER_TYPE_ARTIST = 3;
    public static final int ADAPTER_TYPE_ALBUM = 4;

    private int mAdapterType;
    private List<MediaEntity> mListOrigin;
    private List<AudioItemData> mListItemData;
    private Context mContext;
    private Map<IAudioListViewListener, Object> mMapListener;
    private boolean mIsShowHeader;
    private int mListViewItemTypeCount;
    private String mFilterFolder;
    private String mFilterArtist;
    private String mFilterAlbum;

    public class AudioItemData{
        public static final int TYPE_OPERBAR = 0;
        public static final int TYPE_MEDIA = 1;
        public static final int TYPE_HEADER = 2;
        public static final int TYPE_FOOTER = 3;

        public int mItemType;
        public String mFooterInfo;
        public List<MediaEntity> mListMedia;

        AudioItemData(){

        }
    }

    public class AudioSongItemData extends AudioItemData{
        public String mFirstLetterPinYin;
        public String mMainTitle;
        public String mSubTitle;
        public String mSeparatorTitle;
        public boolean isPlaying;

        AudioSongItemData(){
            super();
            isPlaying = false;
        }
    }

    public class AudioFolderItemData extends AudioItemData{
        public String mFolderName;
        public int mFolderSongCount;
        public String mFolderPath;

        AudioFolderItemData(){
            super();
            mFolderSongCount = 0;
        }
    }

    public class AudioArtistItemData extends AudioItemData{
        public String mArtistImagePath;
        public String mArtistName;
        public int mArtistSongCount;

        AudioArtistItemData(){
            super();
            mArtistSongCount = 0;
        }
    }

    public class AudioAlbumItemData extends AudioItemData{
        public String mAlbumImagePath;
        public String mAlbumName;
        public int mAlbumSongCount;

        AudioAlbumItemData(){
            super();
            mAlbumSongCount = 0;
        }
    }

    public AudioListViewAdapter(Context context, int adapterType, boolean isShowHeader){

        mContext = context;
        mAdapterType = adapterType;
        mListItemData = new ArrayList<>();
        mMapListener = new HashMap<>();
        mIsShowHeader = isShowHeader;

        if(mAdapterType == ADAPTER_TYPE_ALLSONG && mIsShowHeader)
            mListViewItemTypeCount = 4;
        else if(mAdapterType == ADAPTER_TYPE_ALLSONG && !mIsShowHeader)
            mListViewItemTypeCount = 3;
        else
            mListViewItemTypeCount = 2;
    }

    public void initData(List<MediaEntity> list){
        mListOrigin = list;

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

        mListItemData.clear();
        if(mAdapterType == ADAPTER_TYPE_ALLSONG){
            Map<String, List<Integer>> mapFirstLetter = new TreeMap<String, List<Integer>>(new Comparator<String>() {
                public int compare(String key1, String key2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(key1, key2);
                }});

            for(int i = 0;i < mListOrigin.size();i++){
                MediaEntity entrty = mListOrigin.get(i);
                if(entrty == null)
                    continue;

                List<Integer> value = mapFirstLetter.get(entrty.title_letter);
                if(value == null){
                    value = new ArrayList<>();
                    value.add(i);
                    mapFirstLetter.put(entrty.title_letter, value);
                }
                else{
                    value.add(i);
                }
            }

            for(String key : mapFirstLetter.keySet()){
                List<Integer> value = mapFirstLetter.get(key);
                for(int i = 0;i < value.size();i++){
                    MediaEntity entrty = mListOrigin.get(value.get(i));
                    if(entrty == null)
                        continue ;

                    if(i == 0 && mIsShowHeader){
                        AudioSongItemData itemCategory = new AudioSongItemData();
                        itemCategory.mItemType = AudioItemData.TYPE_HEADER;
                        itemCategory.mSeparatorTitle = key;
                        mListItemData.add(itemCategory);
                    }

                    String filePath = entrty.getFilePath();
                    String strArtist = entrty.getArtist();
                    String strAlbum = entrty.getAlbum();

                    if(!TextUtils.isEmpty(mFilterFolder)){
                        if(!filePath.contains(mFilterFolder))
                            continue ;
                    }

                    if(!TextUtils.isEmpty(mFilterArtist)){
                        if(!strArtist.contains(mFilterArtist))
                            continue ;
                    }

                    if(!TextUtils.isEmpty(mFilterAlbum)){
                        if(!strAlbum.contains(mFilterAlbum))
                            continue ;
                    }

                    AudioSongItemData itemData = new AudioSongItemData();
                    itemData.mMainTitle = entrty.getTitle();
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

            if(mListItemData.size() > 0){
                //添加操作栏
                AudioSongItemData operBarData = new AudioSongItemData();
                operBarData.mItemType = AudioItemData.TYPE_OPERBAR;
                mListItemData.add(0, operBarData);

                //添加footer
                AudioSongItemData footerData = new AudioSongItemData();
                footerData.mItemType = AudioItemData.TYPE_FOOTER;
                mListItemData.add(footerData);
            }
        }
        else if(mAdapterType == ADAPTER_TYPE_FOLDER){
            Map<String, Integer> mapFolder = new HashMap<>();
            for(int i = 0;i < mListOrigin.size();i++){
                MediaEntity entrty = mListOrigin.get(i);
                if(entrty == null)
                    continue;

                String filePath = "";
                String fileFullPath = entrty.getFilePath();
                int index = fileFullPath.lastIndexOf(File.separator);
                if(index >= 0){
                    filePath = fileFullPath.substring(0, index);
                }

                if(mapFolder.containsKey(filePath)){
                    int value = mapFolder.get(filePath);
                    mapFolder.put(filePath, value + 1);
                }
                else{
                    mapFolder.put(filePath, 1);
                }
            }

            Set<String> setKey = mapFolder.keySet();
            for(String key : setKey){
                AudioFolderItemData folderData = new AudioFolderItemData();
                folderData.mItemType = AudioItemData.TYPE_MEDIA;
                folderData.mFolderPath = key;
                int index = key.lastIndexOf(File.separator);
                if(index >= 0){
                    folderData.mFolderName = key.substring(index+1, key.length());
                }
                folderData.mFolderSongCount = mapFolder.get(key);
                mListItemData.add(folderData);
            }

            if(mListItemData.size() > 0){
                //添加footer
                AudioFolderItemData footerData = new AudioFolderItemData();
                footerData.mItemType = AudioItemData.TYPE_FOOTER;
                mListItemData.add(footerData);
            }
        }
        else if(mAdapterType == ADAPTER_TYPE_ARTIST){
            Map<String, Integer> mapArtist = new TreeMap<String, Integer>(new Comparator<String>() {
                public int compare(String key1, String key2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(key1, key2);
                }});

            //建立歌手和歌曲的对应关系
            for(int i = 0;i < mListOrigin.size();i++) {
                MediaEntity entrty = mListOrigin.get(i);
                if (entrty == null)
                    continue;

                String artist = entrty.getArtist();
                if (artist == null) {
                    artist = "unknown artist";
                }

                Integer value = mapArtist.get(artist);
                if(value == null){
                    mapArtist.put(artist, 1);
                }
                else{
                    mapArtist.put(artist, ++value);
                }
            }

            //建立歌手
            for(String strArtist : mapArtist.keySet()){

                int iCount = mapArtist.get(strArtist);
                AudioArtistItemData itemData = new AudioArtistItemData();
                itemData.mItemType = AudioItemData.TYPE_MEDIA;
                itemData.mArtistName = strArtist;
                itemData.mArtistSongCount = iCount;
                itemData.mListMedia = new ArrayList<>();
                mListItemData.add(itemData);
            }
        }
        notifyDataSetChanged();
    }

    public void setFilterFolder(String strFolderName){
        mFilterFolder = strFolderName;
    }

    public void setFilterArtist(String strArtistrName){
        mFilterArtist = strArtistrName;
    }

    public void setFilterAlbum(String strAlbumName){
        mFilterAlbum = strAlbumName;
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

        AudioSongItemData songData = null;
        AudioFolderItemData folderData = null;
        AudioArtistItemData artistData = null;
        AudioAlbumItemData albumData = null;

        switch(mAdapterType){
            case ADAPTER_TYPE_ALLSONG:
                songData = (AudioSongItemData)data;
                break;
            case ADAPTER_TYPE_FOLDER:
                folderData = (AudioFolderItemData)data;
                break;
            case ADAPTER_TYPE_ARTIST:
                artistData = (AudioArtistItemData)data;
                break;
            case ADAPTER_TYPE_ALBUM:
                albumData = (AudioAlbumItemData)data;
                break;
        }


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
            if(mAdapterType == ADAPTER_TYPE_ALLSONG){
                AudioListViewAdapterHolder holder = null;
                if(convertView == null){
                    View view = inflater.inflate(R.layout.audio_allsong_item, null);
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
                holder.tvSongMain.setText(songData.mMainTitle);
                holder.tvSongSub.setText(songData.mSubTitle);
                holder.viewSepratorLine.setBackgroundResource(R.color.listviewSeperatorLineColor);

                if(songData.isPlaying){
                    holder.tvSongMain.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                    holder.tvSongSub.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                }
                else{
                    holder.tvSongMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
                    holder.tvSongSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
                }
            }
            else if(mAdapterType == ADAPTER_TYPE_FOLDER){
                if(folderData == null)
                    return null;

                AudioListViewFolderHolder holder = null;
                if(convertView == null){
                    View view = inflater.inflate(R.layout.audio_folder_item, null);
                    view.setBackgroundResource(R.color.backgroundColor);
                    holder = new AudioListViewFolderHolder(view);
                    view.setTag(holder);
                    convertView = view;
                    holder.ibBtnMore.setOnClickListener(this);
                }
                else{
                    holder = (AudioListViewFolderHolder) convertView.getTag();
                }
                holder.ibBtnMore.setTag(position);
                holder.tvFolderName.setText(folderData.mFolderName);
                holder.tvFolderSongCount.setText("" + folderData.mFolderSongCount + "首");
                holder.tvFolderPath.setText(folderData.mFolderPath);
                holder.viewSepratorLine.setBackgroundResource(R.color.listviewSeperatorLineColor);
                holder.tvFolderName.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
                holder.tvFolderSongCount.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
                holder.tvFolderPath.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
            }
            else if(mAdapterType == ADAPTER_TYPE_ARTIST){
                if(artistData == null)
                    return null;

                AudioListViewAristAlbumHolder holder = null;
                if(convertView == null){
                    View view = inflater.inflate(R.layout.audio_artist_album_item, null);
                    view.setBackgroundResource(R.color.backgroundColor);
                    holder = new AudioListViewAristAlbumHolder(view);
                    view.setTag(holder);
                    convertView = view;
                    holder.ibBtnMore.setOnClickListener(this);
                }
                else{
                    holder = (AudioListViewAristAlbumHolder) convertView.getTag();
                }
                holder.ibBtnMore.setTag(position);
                holder.tvArtistAlbumMain.setText(artistData.mArtistName);
                holder.tvArtistAlbumSub.setText("" + artistData.mArtistSongCount + "首");
                holder.viewSepratorLine.setBackgroundResource(R.color.listviewSeperatorLineColor);
                holder.tvArtistAlbumMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
                holder.tvArtistAlbumSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
            }
            else if(mAdapterType == ADAPTER_TYPE_ALBUM){

            }
        }
        else if(getItemViewType(position) == AudioItemData.TYPE_HEADER) {
            AudioListViewAdapterHeaderHolder holder = null;
            if (convertView == null) {
                View view = inflater.inflate(R.layout.audio_seperator_item, null);
                holder = new AudioListViewAdapterHeaderHolder(view);
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
                holder = (AudioListViewAdapterHeaderHolder) convertView.getTag();
            }
            holder.tvCategaryName.setText(songData.mSeparatorTitle);
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

                String strFooterInfo = "";
                if(mAdapterType == ADAPTER_TYPE_ALLSONG){
                    strFooterInfo = String.format("%d首歌曲", iCount);
                }
                else if(mAdapterType == ADAPTER_TYPE_FOLDER){
                    strFooterInfo = String.format("%d个文件夹", iCount);
                }
                holder.tvFooterInfo.setText(strFooterInfo);
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
        return mListViewItemTypeCount;
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

            if((tempdata instanceof AudioSongItemData) == false)
                continue;

            AudioSongItemData songData = (AudioSongItemData)tempdata;
            if(i == index){
                songData.isPlaying = isPlaying;
            }
            else{
                songData.isPlaying = false;
            }

        }
        notifyDataSetChanged();
    }

    //所有歌曲操作栏视图
    private class AudioOperatioinBarHolder{
        public TextView btnRandom;
        public TextView btnBatchMgr;

        public AudioOperatioinBarHolder(View view){
            btnRandom = (TextView)view.findViewById(R.id.btnRandom);
            btnBatchMgr = (TextView)view.findViewById(R.id.btnBatchMgr);
        }
    }

    //所有歌曲item holder
    private class AudioListViewAdapterHolder{
        public ImageView ivSongImage;
        public TextView tvSongMain;
        public TextView tvSongSub;
        public ImageView ibBtnMore;
        public View viewSepratorLine;

        public AudioListViewAdapterHolder(View view){
            ivSongImage = (ImageView)view.findViewById(R.id.ivSongImage);
            tvSongMain = (TextView)view.findViewById(R.id.tvSongMain);
            tvSongSub = (TextView)view.findViewById(R.id.tvSongSub);
            ibBtnMore = (ImageView)view.findViewById(R.id.ibBtnMore);
            viewSepratorLine = (View)view.findViewById(R.id.viewSepratorLine);
        }
    }

    //所有歌曲item的头视图的holder
    private class AudioListViewAdapterHeaderHolder{
        public TextView tvCategaryName;

        public AudioListViewAdapterHeaderHolder(View view){
            tvCategaryName = (TextView)view.findViewById(R.id.tvCategaryName);

        }
    }

    //所有歌曲item的脚视图的holder
    private class AudioListViewFooterHolder{
        public TextView tvFooterInfo;

        public AudioListViewFooterHolder(View view){
            tvFooterInfo = (TextView)view.findViewById(R.id.tvFooterInfo);

        }
    }

    //文件夹列表item的holder
    private class AudioListViewFolderHolder{
        public TextView tvFolderName;
        public TextView tvFolderSongCount;
        public TextView tvFolderPath;
        public ImageView ibBtnMore;
        public View viewSepratorLine;

        public AudioListViewFolderHolder(View view){
            tvFolderName = (TextView)view.findViewById(R.id.tvFolderName);
            tvFolderSongCount = (TextView)view.findViewById(R.id.tvFolderSongCount);
            tvFolderPath = (TextView)view.findViewById(R.id.tvFolderPath);
            ibBtnMore = (ImageView)view.findViewById(R.id.ibBtnMore);
            viewSepratorLine = (View)view.findViewById(R.id.viewSepratorLine);
        }
    }

    //歌手和专辑列表item的holder
    private class AudioListViewAristAlbumHolder{
        public ImageView ivArtistAlbumImage;
        public TextView tvArtistAlbumMain;
        public TextView tvArtistAlbumSub;
        public ImageView ibBtnMore;
        public View viewSepratorLine;

        public AudioListViewAristAlbumHolder(View view){
            ivArtistAlbumImage = (ImageView)view.findViewById(R.id.ivArtistAlbumImage);
            tvArtistAlbumMain = (TextView)view.findViewById(R.id.tvArtistAlbumMain);
            tvArtistAlbumSub = (TextView)view.findViewById(R.id.tvArtistAlbumSub);
            ibBtnMore = (ImageView)view.findViewById(R.id.ibBtnMore);
            viewSepratorLine = (View)view.findViewById(R.id.viewSepratorLine);
        }
    }
}
