package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.hp.hpl.sparta.Text;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by kaizhiwei on 16/11/12.
 */
public class AudioListViewAdapter extends BaseAdapter implements View.OnClickListener {

    public interface IAudioListViewListener{
        void onMoreBtnClick(AudioListViewAdapter adapter, int position);
        void onRandomPlayClick(AudioListViewAdapter adapter);
        void onBatchMgrClick(AudioListViewAdapter adapter);
    }

    public static final int ADAPTER_TYPE_ALLSONG = 1;
    public static final int ADAPTER_TYPE_FOLDER = 2;
    public static final int ADAPTER_TYPE_ARTIST = 3;
    public static final int ADAPTER_TYPE_ALBUM = 4;
    public static final int ADAPTER_TYPE_NETWORK = 5;   //显示网络数据

    //操作栏tag
    private static final int OPERATORBAR_RANDOM_PLAY = -1;
    private static final int OPERATORBAR_BATCHMGR = -2;

    private int mAdapterType;
    private List<MusicInfoDao> mListOrigin;
    private List<AudioItemData> mListItemData;
    private List<AudioItemData> mListSearchResultData;
    private String mSearchKey;
    private Context mContext;
    private Map<IAudioListViewListener, Object> mMapListener;
    private boolean mIsShowOperBar = true;
    private boolean mIsShowIndex = true;
    private boolean mIsShowFooter = true;
    private static final int VIEW_TYPE_COUNT = 4;

    private int mListViewItemTypeCount;
    private String mFilterFolder;
    private String mFilterArtist;
    private String mFilterAlbum;

    static public class AudioItemData{
        public static final int TYPE_MEDIA = 0;
        public static final int TYPE_FOOTER = 1;
        public static final int TYPE_OPERBAR = 2;
        public static final int TYPE_INDEX = 3;

        public long id;
        public int mItemType;
        public String mFooterInfo;
        public List<MusicInfoDao> mListMedia;

        AudioItemData(){

        }
    }

    static public class AudioNetWorkItemData extends AudioItemData{
        public String strMain;
        public String strSub;
        public String strKey;

        public AudioNetWorkItemData(){

        }
    }

    static public class AudioSongItemData extends AudioItemData{
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

    static public class AudioFolderItemData extends AudioItemData{
        public String mFolderName;
        public int mFolderSongCount;
        public String mFolderPath;

        AudioFolderItemData(){
            super();
            mFolderSongCount = 0;
        }
    }

    static public class AudioArtistAlbumItemData extends AudioItemData{
        public String mArtistAlbumImagePath;
        public String mArtistAlbumName;
        public int mArtistAlbumSongCount;

        AudioArtistAlbumItemData(){
            super();
            mArtistAlbumSongCount = 0;
        }
    }

    public AudioListViewAdapter(Context context, int adapterType, boolean isShowIndex){
        mContext = context;
        mAdapterType = adapterType;
        mListItemData = new ArrayList<>();
        mMapListener = new HashMap<>();
        mIsShowIndex = isShowIndex;

        mListViewItemTypeCount = VIEW_TYPE_COUNT;
        if(mAdapterType == ADAPTER_TYPE_ALLSONG)
        {
            if(mIsShowIndex == false){
                mListViewItemTypeCount--;
            }
        }
        else
            mListViewItemTypeCount = 2;
    }

    public void setShowOperBar(boolean isShow){
        mIsShowOperBar = isShow;
    }

    public void setShowFooter(boolean isShow){
        mIsShowFooter = isShow;
    }

    public void initData(List<MusicInfoDao> list){
        mListOrigin = list;

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

        mListItemData.clear();
        if(mAdapterType == ADAPTER_TYPE_ALLSONG){
            Map<String, List<Integer>> mapFirstLetter = new TreeMap<>(new Comparator<String>() {
                public int compare(String key1, String key2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(key1, key2);
                }});

            for(int i = 0;i < mListOrigin.size();i++){
                MusicInfoDao entrty = mListOrigin.get(i);
                if(entrty == null)
                    continue;

                String strFirstLetter = "";
                strFirstLetter = entrty.getTitle_letter();

                if(TextUtils.isEmpty(strFirstLetter)) {
                    strFirstLetter = "#";
                }
                else if(strFirstLetter.length() > 1){
                    strFirstLetter = strFirstLetter.substring(0,1);
                }

                List<Integer> value = mapFirstLetter.get(strFirstLetter);
                if(value == null){
                    value = new ArrayList<>();
                    value.add(i);
                    mapFirstLetter.put(strFirstLetter, value);
                }
                else{
                    value.add(i);
                }
            }

            for(String key : mapFirstLetter.keySet()){
                List<Integer> value = mapFirstLetter.get(key);
                for(int i = 0;i < value.size();i++){
                    MusicInfoDao entrty = mListOrigin.get(value.get(i));
                    if(entrty == null)
                        continue ;

                    if(i == 0 && mIsShowIndex){
                        AudioSongItemData itemCategory = new AudioSongItemData();
                        itemCategory.mItemType = AudioItemData.TYPE_INDEX;
                        itemCategory.mSeparatorTitle = key;
                        mListItemData.add(itemCategory);
                    }

                    String filePath = entrty.getSave_path();
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
                    itemData.mMainTitle = TextUtils.isEmpty(entrty.getTitle()) ? "" : entrty.getTitle();
                    itemData.mSubTitle = getArtistAlbum(entrty.getArtist());
                    itemData.mSubTitle += " - ";
                    itemData.mSubTitle += getArtistAlbum(entrty.getAlbum());
                    itemData.id = entrty.get_id();
                    itemData.mItemType = AudioItemData.TYPE_MEDIA;
                    itemData.mListMedia = new ArrayList<>();
                    itemData.mListMedia.add(entrty);
                    mListItemData.add(itemData);
                }
            }

            if(mListItemData.size() > 0){
                if(mIsShowOperBar){
                    //添加操作栏
                    AudioSongItemData operBarData = new AudioSongItemData();
                    operBarData.mItemType = AudioItemData.TYPE_OPERBAR;
                    mListItemData.add(0, operBarData);
                }

                if(mIsShowFooter){
                    //添加footer
                    AudioSongItemData footerData = new AudioSongItemData();
                    footerData.mItemType = AudioItemData.TYPE_FOOTER;
                    mListItemData.add(footerData);
                }
            }
        }
        else if(mAdapterType == ADAPTER_TYPE_FOLDER){
            Map<String, List<MusicInfoDao>> mapFolder = new HashMap<>();
            for(int i = 0;i < mListOrigin.size();i++){
                MusicInfoDao entrty = mListOrigin.get(i);
                if(entrty == null)
                    continue;

                String filePath = "";
                String fileFullPath = entrty.get_data();
                int index = fileFullPath.lastIndexOf(File.separator);
                if(index >= 0){
                    filePath = fileFullPath.substring(0, index);
                }

                if(mapFolder.containsKey(filePath)){
                    List<MusicInfoDao> listFolderMedia = mapFolder.get(filePath);
                    listFolderMedia.add(entrty);
                    mapFolder.put(filePath, listFolderMedia);
                }
                else{
                    List<MusicInfoDao> listFolderMedia = new ArrayList<>();
                    listFolderMedia.add(entrty);
                    mapFolder.put(filePath, listFolderMedia);
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
                folderData.mFolderSongCount = mapFolder.get(key).size();
                folderData.mListMedia = new ArrayList<>();
                folderData.mListMedia.addAll(mapFolder.get(key));
                mListItemData.add(folderData);
            }

            if(mListItemData.size() > 0){
                if(mIsShowFooter){
                    //添加footer
                    AudioFolderItemData footerData = new AudioFolderItemData();
                    footerData.mItemType = AudioItemData.TYPE_FOOTER;
                    mListItemData.add(footerData);
                }
            }
        }
        else if(mAdapterType == ADAPTER_TYPE_ARTIST || mAdapterType == ADAPTER_TYPE_ALBUM){
            Map<String, List<MusicInfoDao>> mapArtist = new TreeMap<String, List<MusicInfoDao>>(new Comparator<String>() {
                public int compare(String key1, String key2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(key1, key2);
                }});

            //建立歌手和歌曲的对应关系
            for(int i = 0;i < mListOrigin.size();i++) {
                MusicInfoDao entrty = mListOrigin.get(i);
                if (entrty == null)
                    continue;

                String strInfo = "";
                if(mAdapterType == ADAPTER_TYPE_ARTIST){
                    strInfo = entrty.getArtist();
                }
                else{
                    strInfo = entrty.getAlbum();
                }

                strInfo = getArtistAlbum(strInfo);
                List<MusicInfoDao> value = mapArtist.get(strInfo);
                if(value != null){

                }
                else{
                    value = new ArrayList<>();
                }
                value.add(entrty);
                mapArtist.put(strInfo, value);
            }

            //建立歌手
            for(String strArtist : mapArtist.keySet()){

                List<MusicInfoDao> value = mapArtist.get(strArtist);
                if(value == null )
                    continue;

                AudioArtistAlbumItemData itemData = new AudioArtistAlbumItemData();
                itemData.mItemType = AudioItemData.TYPE_MEDIA;
                itemData.mArtistAlbumName = strArtist;
                itemData.mArtistAlbumSongCount = value.size();
                itemData.mListMedia = new ArrayList<>();
                itemData.mListMedia.addAll(value);
                mListItemData.add(itemData);
            }

            if(mListItemData.size() > 0){
                if(mIsShowFooter){
                    //添加footer
                    AudioArtistAlbumItemData footerData = new AudioArtistAlbumItemData();
                    footerData.mItemType = AudioItemData.TYPE_FOOTER;
                    mListItemData.add(footerData);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void initNetworkData(List<AudioNetWorkItemData> list){
        if(mListItemData == null){
            mListItemData = new ArrayList<>();
        }
        mListItemData.clear();
        for(int i = 0;i < list.size();i++){
            list.get(i).mItemType = AudioItemData.TYPE_MEDIA;
        }
        mListItemData.addAll(list);
        AudioNetWorkItemData footerItemData = new AudioNetWorkItemData();
        footerItemData.mItemType = AudioItemData.TYPE_FOOTER;
        footerItemData.mFooterInfo = "没有更多了";
        mListItemData.add(footerItemData);
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

    private List<AudioItemData> getCurOperListData(){
        List<AudioItemData> listData = null;
        if(isSearching()){
            listData = mListSearchResultData;
        }
        else{
            listData = mListItemData;
        }
        return listData;
    }

    public AudioItemData getAudioItemData(int index){
        List<AudioItemData> listData = getCurOperListData();

        if(listData == null || index < 0 || index >= listData.size())
            return null;

        return listData.get(index);
    }

    private String getArtistAlbum(String artist){
        String strRet = "";
        if(artist == null || TextUtils.isEmpty(artist) || artist.equals("null"))
            strRet = "unknown";
        else
            strRet = artist;

        return strRet;
    }

    public void setSearchKey(String strSearchKey){
        if(mListSearchResultData == null)
            mListSearchResultData = new ArrayList<>();
        mListSearchResultData.clear();
        mSearchKey = strSearchKey.replace("\n", "");

        List<AudioItemData> listTemp = new ArrayList<>();
        for(int i = 0;i < mListItemData.size();i++){
            AudioItemData itemData = mListItemData.get(i);
            if(itemData == null || itemData.mItemType != AudioItemData.TYPE_MEDIA)
                continue;

            if(mAdapterType == ADAPTER_TYPE_ALLSONG){
                AudioSongItemData songItemData = null;
                if(itemData instanceof AudioSongItemData){
                    songItemData = (AudioSongItemData) itemData;
                }

                if(songItemData == null)
                    continue;

                if(songItemData.mMainTitle.contains(strSearchKey) || songItemData.mSubTitle.contains(strSearchKey)){
                    listTemp.add(songItemData);
                }
            }
            else if(mAdapterType == ADAPTER_TYPE_FOLDER){
                AudioFolderItemData folderItemData = null;
                if(itemData instanceof AudioFolderItemData){
                    folderItemData = (AudioFolderItemData) itemData;
                }

                if(folderItemData == null)
                    continue;

                if(folderItemData.mFolderName.contains(strSearchKey)){
                    mListSearchResultData.add(folderItemData);
                }
            }
            else if(mAdapterType == ADAPTER_TYPE_ARTIST || mAdapterType == ADAPTER_TYPE_ALBUM){
                AudioArtistAlbumItemData aritstAlbumItemData = null;
                if(itemData instanceof AudioArtistAlbumItemData){
                    aritstAlbumItemData = (AudioArtistAlbumItemData) itemData;
                }

                if(aritstAlbumItemData == null)
                    continue;

                if(aritstAlbumItemData.mArtistAlbumName.contains(strSearchKey)){
                    mListSearchResultData.add(aritstAlbumItemData);
                }
            }
        }

        if(mAdapterType == ADAPTER_TYPE_ALLSONG){
            Map<String, List<MusicInfoDao>> mapFirstLetter = new TreeMap<String, List<MusicInfoDao>>(new Comparator<String>() {
                public int compare(String key1, String key2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(key1, key2);
                }});

            for(int i = 0;i < listTemp.size();i++){
                List<MusicInfoDao> list = listTemp.get(i).mListMedia;
                if(list == null)
                    continue;

                for(int j = 0;j < list.size();j++){
                    List<MusicInfoDao> listValue = mapFirstLetter.get(list.get(j).getTitle_letter());
                    if(listValue == null){
                        listValue = new ArrayList<>();
                    }
                    listValue.add(list.get(j));
                    mapFirstLetter.put(list.get(j).getTitle_letter(), listValue);
                }
            }

            for(String key : mapFirstLetter.keySet()) {
                AudioSongItemData itemCategory = new AudioSongItemData();
                itemCategory.mItemType = AudioItemData.TYPE_INDEX;
                itemCategory.mSeparatorTitle = key;
                mListSearchResultData.add(itemCategory);

                List<MusicInfoDao> value = mapFirstLetter.get(key);
                for (int i = 0; i < value.size(); i++) {
                    MusicInfoDao entrty = value.get(i);
                    if (entrty == null)
                        continue;

                    AudioSongItemData itemData = new AudioSongItemData();
                    itemData.mMainTitle = entrty.getTitle();
                    itemData.mSubTitle = getArtistAlbum(entrty.getArtist());
                    itemData.mSubTitle += " - ";
                    itemData.mSubTitle += getArtistAlbum(entrty.getAlbum());
                    itemData.id = entrty.get_id();
                    itemData.mItemType = AudioItemData.TYPE_MEDIA;
                    itemData.mListMedia = new ArrayList<>();
                    itemData.mListMedia.add(entrty);
                    mListSearchResultData.add(itemData);
                }
            }
            //添加footer
            AudioSongItemData footerData = new AudioSongItemData();
            footerData.mItemType = AudioItemData.TYPE_FOOTER;
            mListSearchResultData.add(footerData);
        }
        else if(mAdapterType == ADAPTER_TYPE_FOLDER){
            //添加footer
            AudioFolderItemData footerData = new AudioFolderItemData();
            footerData.mItemType = AudioItemData.TYPE_FOOTER;
            mListSearchResultData.add(footerData);
        }
        else{
            //添加footer
            AudioArtistAlbumItemData footerData = new AudioArtistAlbumItemData();
            footerData.mItemType = AudioItemData.TYPE_FOOTER;
            mListSearchResultData.add(footerData);
        }

        notifyDataSetChanged();
    }

    public void clearSearchkKey(){
        mSearchKey = "";
        notifyDataSetChanged();
    }

    public boolean isSearching(){
        return !TextUtils.isEmpty(mSearchKey);
    }

    @Override
    public int getCount() {
        List<AudioItemData> listData = getCurOperListData();

        if(listData == null)
            return 0;

        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        List<AudioItemData> listData = getCurOperListData();

        if(listData == null)
            return null;

        return listData.get(position);
    }

    private String getHtmlText(String strPlainText){
        String strHtml = "";
        if(isSearching()){
            int index = strPlainText.lastIndexOf(mSearchKey);
            if(index >= 0)
                strHtml = String.format("<font color=black>%s</font><font color=red>%s</font><font color=black>%s</font>", strPlainText.substring(0, index), mSearchKey,
                    strPlainText.substring(index + mSearchKey.length(), strPlainText.length()));
            else{
                strHtml = strPlainText;
            }
        }
        else
        {
            strHtml = strPlainText;
        }
        Log.i("weikaizhi", "strPlainText#" + strPlainText + ",strHtml#" + strHtml);
        return strHtml;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        List<AudioItemData> tempListData = getCurOperListData();

        if(tempListData == null || position < 0 || position >= tempListData.size())
            return null;

        AudioItemData data = null;
        data = tempListData.get(position);
        if(data == null)
            return null;

        AudioSongItemData songData = null;
        AudioFolderItemData folderData = null;
        AudioArtistAlbumItemData artistData = null;
        AudioNetWorkItemData networkData = null;

        switch(mAdapterType){
            case ADAPTER_TYPE_ALLSONG:
                songData = (AudioSongItemData)data;
                break;
            case ADAPTER_TYPE_FOLDER:
                folderData = (AudioFolderItemData)data;
                break;
            case ADAPTER_TYPE_ARTIST:
            case ADAPTER_TYPE_ALBUM:
                artistData = (AudioArtistAlbumItemData)data;
                break;
            case ADAPTER_TYPE_NETWORK:
                networkData = (AudioNetWorkItemData)data;
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
                holder.btnRandom.setTag(OPERATORBAR_RANDOM_PLAY);
                holder.btnBatchMgr.setTag(OPERATORBAR_BATCHMGR);
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
                holder.tvSongMain.setText(Html.fromHtml(getHtmlText(songData.mMainTitle)));
                holder.tvSongSub.setText(Html.fromHtml(getHtmlText(songData.mSubTitle)));
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
                holder.tvFolderName.setText(Html.fromHtml(getHtmlText(folderData.mFolderName)));
                holder.tvFolderSongCount.setText("" + folderData.mFolderSongCount + "首");
                holder.tvFolderPath.setText(folderData.mFolderPath);
                holder.viewSepratorLine.setBackgroundResource(R.color.listviewSeperatorLineColor);
                holder.tvFolderName.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
                holder.tvFolderSongCount.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
                holder.tvFolderPath.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
            }
            else if(mAdapterType == ADAPTER_TYPE_ARTIST || mAdapterType == ADAPTER_TYPE_ALBUM){
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
                holder.tvArtistAlbumMain.setText(Html.fromHtml(getHtmlText(artistData.mArtistAlbumName)));
                holder.tvArtistAlbumSub.setText("" + artistData.mArtistAlbumSongCount + "首");
                holder.viewSepratorLine.setBackgroundResource(R.color.listviewSeperatorLineColor);
                holder.tvArtistAlbumMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
                holder.tvArtistAlbumSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
            }
            else if(mAdapterType == ADAPTER_TYPE_NETWORK){
                if(networkData == null)
                    return null;

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
                holder.tvSongMain.setText(networkData.strMain);
                holder.tvSongSub.setText(networkData.strSub);
                holder.viewSepratorLine.setBackgroundResource(R.color.listviewSeperatorLineColor);
            }
        }
        else if(getItemViewType(position) == AudioItemData.TYPE_INDEX) {
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
            int iCount = 0;
            for(int i = 0;i < tempListData.size();i++){
                AudioItemData tempdata = (AudioItemData)tempListData.get(i);
                if(tempdata == null || tempdata.mItemType != AudioItemData.TYPE_MEDIA)
                    continue;

                iCount++;
            }

            if(convertView == null){
                View view = inflater.inflate(R.layout.audio_footer_item, null);
                view.setBackgroundResource(R.color.backgroundColor);
                holder = new AudioListViewFooterHolder(view);
                view.setTag(holder);
                convertView = view;
            }
            else{
                holder = (AudioListViewFooterHolder) convertView.getTag();
            }
            String strFooterInfo = "";
            if(mAdapterType == ADAPTER_TYPE_ALLSONG){
                strFooterInfo = String.format("%d首歌曲", iCount);
            }
            else if(mAdapterType == ADAPTER_TYPE_FOLDER){
                strFooterInfo = String.format("%d个文件夹", iCount);
            }
            else if(mAdapterType == ADAPTER_TYPE_ARTIST){
                strFooterInfo = String.format("%d位歌手", iCount);
            }
            else if(mAdapterType == ADAPTER_TYPE_ALBUM){
                strFooterInfo = String.format("%d张专辑", iCount);
            }
            else if(mAdapterType == ADAPTER_TYPE_NETWORK){
                strFooterInfo = networkData.mFooterInfo;
            }
            holder.tvFooterInfo.setText(strFooterInfo);
        }

        return convertView;
    }

    public int getItemViewType(int position) {
        List<AudioItemData> listData = getCurOperListData();
        if(listData == null || listData.size() <= 0 ||  position >= listData.size())
            return 0;

        AudioItemData data = listData.get(position);
        //Log.i("weikaizhi", "getItemViewType: " + data.mItemType);
        return data.mItemType;
    }

    public int getViewTypeCount() {
        return mListViewItemTypeCount;
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() == null)
            return;

        int tag = (int)v.getTag();
        for(IAudioListViewListener key : mMapListener.keySet()){
            if(key != null){
                if(tag >= 0){
                    key.onMoreBtnClick(this, (int)v.getTag());
                }
                else if(tag == OPERATORBAR_RANDOM_PLAY){
                    key.onRandomPlayClick(this);
                }
                else if(tag == OPERATORBAR_BATCHMGR){
                    key.onBatchMgrClick(this);
                }
            }
        }
    }

    public void setItemPlayState(MusicInfoDao MusicInfoDao, boolean isPlaying){
        List<AudioItemData> listData = getCurOperListData();
        if(listData == null || listData.size() <= 0 || MusicInfoDao == null)
            return ;

        for(int i = 0;i < listData.size();i++){
            AudioItemData tempdata = listData.get(i);
            if(tempdata == null || tempdata.mItemType != AudioItemData.TYPE_MEDIA)
                continue;

            if((tempdata instanceof AudioSongItemData) == false)
                continue;

            boolean isFind = false;
            for(int j = 0;j < tempdata.mListMedia.size();j++){
                MusicInfoDao tempMedia = tempdata.mListMedia.get(j);
                if(tempMedia == null)
                    continue;

                if(tempMedia.get_id() == MusicInfoDao.get_id()){
                    isFind = true;
                    break;
                }
            }

            AudioSongItemData songData = (AudioSongItemData)tempdata;
            if(isFind) {
                songData.isPlaying = isPlaying;
            }
            else{
                songData.isPlaying = false;
            }
        }
        notifyDataSetChanged();
    }

    public void setItemPlayState(int index, boolean isPlaying){
        List<AudioItemData> listData = getCurOperListData();
        if(listData == null || listData.size() <= 0 ||  index >= listData.size())
            return ;

        for(int i = 0;i < listData.size();i++){
            AudioItemData tempdata = listData.get(i);
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

    public List<AudioItemData> getAdapterAllData(){
        List<AudioItemData> listData = getCurOperListData();
        if(listData == null)
            return null;

        List<AudioItemData> list = new ArrayList<AudioItemData>();
        list.addAll(listData);
        return  list;
    }

    public List<MusicInfoDao> getAdapterOriginData(){
        List<MusicInfoDao> listData = new ArrayList<>();
        for(int i = 0;i < mListItemData.size();i++){
            AudioItemData itemData = mListItemData.get(i);
            if(itemData.mItemType != AudioItemData.TYPE_MEDIA)
                continue;

            if(itemData.mListMedia != null){
                listData.addAll(itemData.mListMedia);
            }
        }
        return listData;
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
