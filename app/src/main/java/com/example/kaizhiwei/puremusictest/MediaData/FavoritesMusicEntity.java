package com.example.kaizhiwei.puremusictest.MediaData;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class FavoritesMusicEntity  implements Serializable, Parcelable {
    public long _id;
    public long musicinfo_id;
    public long song_id;
    public String title;
    public String artist_id;
    public String artist;
    public String album_id ;
    public String album;
    public long fav_time;
    public long havehigh;
    public long charge ;
    public long fav_type;
    public String allbitrate;
    public String path;
    public long file_from;
    public long has_original;
    public long has_mv_mobile;
    public String song_source;
    public String original_rate;
    public long cache_status;
    public String version;
    public long has_pay_status;
    public long is_offline;
    public long favorite_id;

    public FavoritesMusicEntity(){

    }

    protected FavoritesMusicEntity(Parcel in) {
        _id = in.readLong();
        musicinfo_id = in.readLong();
        song_id = in.readLong();
        title = in.readString();
        artist_id = in.readString();
        artist = in.readString();
        album_id = in.readString();
        album = in.readString();
        fav_time = in.readLong();
        havehigh = in.readLong();
        charge = in.readLong();
        fav_type = in.readLong();
        allbitrate = in.readString();
        path = in.readString();
        file_from = in.readLong();
        has_original = in.readLong();
        has_mv_mobile = in.readLong();
        song_source = in.readString();
        original_rate = in.readString();
        cache_status = in.readLong();
        version = in.readString();
        has_pay_status = in.readLong();
        is_offline = in.readLong();
        favorite_id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeLong(musicinfo_id);
        dest.writeLong(song_id);
        dest.writeString(title);
        dest.writeString(artist_id);
        dest.writeString(artist);
        dest.writeString(album_id);
        dest.writeString(album);
        dest.writeLong(fav_time);
        dest.writeLong(havehigh);
        dest.writeLong(charge);
        dest.writeLong(fav_type);
        dest.writeString(allbitrate);
        dest.writeString(path);
        dest.writeLong(file_from);
        dest.writeLong(has_original);
        dest.writeLong(has_mv_mobile);
        dest.writeString(song_source);
        dest.writeString(original_rate);
        dest.writeLong(cache_status);
        dest.writeString(version);
        dest.writeLong(has_pay_status);
        dest.writeLong(is_offline);
        dest.writeLong(favorite_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavoritesMusicEntity> CREATOR = new Creator<FavoritesMusicEntity>() {
        @Override
        public FavoritesMusicEntity createFromParcel(Parcel in) {
            return new FavoritesMusicEntity(in);
        }

        @Override
        public FavoritesMusicEntity[] newArray(int size) {
            return new FavoritesMusicEntity[size];
        }
    };
}
