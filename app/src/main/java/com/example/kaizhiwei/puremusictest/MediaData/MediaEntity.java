package com.example.kaizhiwei.puremusictest.MediaData;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.videolan.libvlc.Media;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by kaizhiwei on 16/11/12.
 */



public class MediaEntity implements Serializable, Parcelable {

    public long _id;
    public String _data;
    public long _size;
    public String _display_name;
    public String title;
    public String title_key;
    public String title_letter;
    public long date_added;
    public long date_modified;
    public String mime_type;
    public long duration;
    public long bookmark;
    public String artist;
    public String artist_key;
    public String composer;
    public String album;
    public String album_key;
    public String album_art;
    public long track;
    public long year;
    public long mediastore_id;
    public String lyric_path;
    public long is_lossless;
    public String artist_image;
    public String album_image;
    public long last_playtime;
    public long play_times;
    public long data_from;
    public String save_path;
    public long is_played;
    public long song_id;
    public long equalizer_level;
    public long replay_gain_level;
    public long is_offline_cache;
    public long is_faved;
    public long have_high;
    public long bitrate;
    public long has_original;
    public long flag;
    public String original_rate;
    public String all_rates;
    public long is_deleted;
    public long skip_auto_scan;
    public long is_offline;
    public long has_pay_status;
    public String version;
    public String cache_path;
    public long play_type;
    public String file_url;
    public String file_hash;
    private static HanyuPinyinOutputFormat mPinYinFormat;


    private Media mMedia;

    public MediaEntity(){

    }

    public MediaEntity(Media media) {
        if (media.getType() == Media.Track.Type.Audio) {

        }
        _data = media.getUri().getPath();
        File file = new File(_data);
        if (file.exists()) {
            _size = file.length();
        }

        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        _display_name = fileName.substring(0, index);
        title = _display_name;
        String strExtension = fileName.substring(index+1, fileName.length());

        if (mPinYinFormat == null) {
            mPinYinFormat = new HanyuPinyinOutputFormat();
            mPinYinFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            mPinYinFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        }

        ArrayList<String> arrayPinYin =  getPinYin(title);
        if(arrayPinYin.size() >= 2){
            title_key = arrayPinYin.get(0);
            title_letter = arrayPinYin.get(1);
        }

        date_modified = file.lastModified();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        strExtension = strExtension.toLowerCase();

        if(mimeTypeMap.hasExtension(strExtension)){
            mime_type = mimeTypeMap.getMimeTypeFromExtension(strExtension);
        }
        duration = media.getDuration();
        String strArtist = media.getMeta(Media.Meta.Artist);
        if(!TextUtils.isEmpty(strArtist)){
            artist = strArtist;
        }
        else
        {
            artist = "<unknown>";
        }

        arrayPinYin =  getPinYin(artist);
        if(arrayPinYin.size() >= 2){
            artist_key = arrayPinYin.get(0);
        }


        String strAlbum = media.getMeta(Media.Meta.Album);
        if(!TextUtils.isEmpty(strAlbum)){
            album = strAlbum;
        }
        else
        {
            album = "<unknown>";
        }

        arrayPinYin =  getPinYin(album);
        if(arrayPinYin.size() >= 2){
            album_key = arrayPinYin.get(0);
        }

        track = media.getTrackCount();

        data_from = 0;  //0 从磁盘 1 从网络
        save_path = file.getParent();
        is_faved = 0;   //0 未收藏 1已收藏
        bitrate = 128;
        String strRating = media.getMeta(Media.Meta.Rating);

        mMedia = media;
    }

    protected MediaEntity(Parcel in) {
        _id = in.readLong();
        _data = in.readString();
        _size = in.readLong();
        _display_name = in.readString();
        title = in.readString();
        title_key = in.readString();
        title_letter = in.readString();
        date_added = in.readLong();
        date_modified = in.readLong();
        mime_type = in.readString();
        duration = in.readLong();
        bookmark = in.readLong();
        artist = in.readString();
        artist_key = in.readString();
        composer = in.readString();
        album = in.readString();
        album_key = in.readString();
        album_art = in.readString();
        track = in.readLong();
        year = in.readLong();
        mediastore_id = in.readInt();
        lyric_path = in.readString();
        is_lossless = in.readLong();
        artist_image = in.readString();
        album_image = in.readString();
        last_playtime = in.readLong();
        play_times = in.readLong();
        data_from = in.readLong();
        save_path = in.readString();
        is_played = in.readLong();
        song_id = in.readLong();
        equalizer_level = in.readLong();
        replay_gain_level = in.readLong();
        is_offline_cache = in.readLong();
        is_faved = in.readLong();
        have_high = in.readLong();
        bitrate = in.readLong();
        has_original = in.readLong();
        flag = in.readLong();
        original_rate = in.readString();
        all_rates = in.readString();
        is_deleted = in.readLong();
        skip_auto_scan = in.readLong();
        is_offline = in.readLong();
        has_pay_status = in.readLong();
        version = in.readString();
        cache_path = in.readString();
        play_type = in.readLong();
        file_url = in.readString();
        file_hash = in.readString();
    }

    public static final Creator<MediaEntity> CREATOR = new Creator<MediaEntity>() {
        @Override
        public MediaEntity createFromParcel(Parcel in) {
            return new MediaEntity(in);
        }

        @Override
        public MediaEntity[] newArray(int size) {
            return new MediaEntity[size];
        }
    };

    public ArrayList<String> getPinYin(String strData){
        String str = strData.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\][1-9].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        ArrayList<String> arrayPinYin = new ArrayList<>();
        String strPinYin = "";
        String strFirstLetter = "";

        for(int i = 0;i < str.length();i++){
            char firstChar = str.toUpperCase(Locale.ENGLISH).charAt(i);
            if (Character.isLetter(firstChar)) {
                String[] vals = new String[0];
                try {
                    vals = PinyinHelper.toHanyuPinyinStringArray(firstChar, mPinYinFormat);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                    continue ;
                }

                boolean isFindPinYin = false;
                List<String> listPinYin = null;
                if (vals != null) {
                    listPinYin = Arrays.asList(vals);
                    if (listPinYin != null && listPinYin.size() > 0) {
                        isFindPinYin = true;
                    }
                }

                if (isFindPinYin) {
                    //去掉声平声降
                    String temp = listPinYin.get(0).substring(0, listPinYin.get(0).length() - 1);
                    strPinYin += temp;

                    if(i == 0){
                        strFirstLetter = strPinYin.substring(0,1);
                    }
                } else {
                    strPinYin += firstChar;
                    if(i == 0){
                        strFirstLetter += firstChar;
                    }
                }
            }
            else {
                strPinYin += "#";
            }
        }

        arrayPinYin.add(strPinYin);
        arrayPinYin.add(strFirstLetter);
        return arrayPinYin;
    }


    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return artist;
    }

    public String getAlbum(){
        return album;
    }

    public String getFilePath(){
        return _data;
    }

    public long getDuration(){
        return duration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(_data);
        dest.writeLong(_size);
        dest.writeString(_display_name);
        dest.writeString(title);
        dest.writeString(title_key);
        dest.writeString(title_letter);
        dest.writeLong(date_added);
        dest.writeLong(date_modified);
        dest.writeString(mime_type);
        dest.writeLong(duration);
        dest.writeLong(bookmark);
        dest.writeString(artist);
        dest.writeString(artist_key);
        dest.writeString(composer);
        dest.writeString(album);
        dest.writeString(album_key);
        dest.writeString(album_art);
        dest.writeLong(track);
        dest.writeLong(year);
        dest.writeLong(mediastore_id);
        dest.writeString(lyric_path);
        dest.writeLong(is_lossless);
        dest.writeString(artist_image);
        dest.writeString(album_image);
        dest.writeLong(last_playtime);
        dest.writeLong(play_times);
        dest.writeLong(data_from);
        dest.writeString(save_path);
        dest.writeLong(is_played);
        dest.writeLong(song_id);
        dest.writeLong(equalizer_level);
        dest.writeLong(replay_gain_level);
        dest.writeLong(is_offline_cache);
        dest.writeLong(is_faved);
        dest.writeLong(have_high);
        dest.writeLong(bitrate);
        dest.writeLong(has_original);
        dest.writeLong(flag);
        dest.writeString(original_rate);
        dest.writeString(all_rates);
        dest.writeLong(is_deleted);
        dest.writeLong(skip_auto_scan);
        dest.writeLong(is_offline);
        dest.writeLong(has_pay_status);
        dest.writeString(version);
        dest.writeString(cache_path);
        dest.writeLong(play_type);
        dest.writeString(file_url);
        dest.writeString(file_hash);
    }
}
