package com.example.kaizhiwei.puremusictest.dao;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by kaizhiwei on 17/9/16.
 */
//默认我喜欢的单曲
@Table(name = "favorites_music")
public class FavoriteMusicDao implements Parcelable{
    @Column(name = "id",isId = true,autoGen = true)
    private long _id;

    @Column(name = "musicinfo_id")
    private long musicinfo_id;

    @Column(name = "song_id")
    private long song_id;

    @Column(name = "title")
    private String title;

    @Column(name = "artist_id")
    private String artist_id;

    @Column(name = "artist")
    private String artist;

    @Column(name = "album_id")
    private String album_id;

    @Column(name = "album")
    private String album;

    @Column(name = "fav_time")
    private long fav_time;

    @Column(name = "havehigh")
    private int havehigh;

    @Column(name = "charge")
    private int charge;

    @Column(name = "fav_type")
    private int fav_type;

    @Column(name = "allbitrate")
    private String allbitrate;

    @Column(name = "path")
    private String path;

    @Column(name = "file_from")
    private int file_from;

    @Column(name = "has_original")
    private int has_original;

    @Column(name = "has_mv_mobile")
    private int has_mv_mobile;

    @Column(name = "song_source")
    private String song_source;

    @Column(name = "original_rate")
    private String original_rate;

    @Column(name = "cache_status")
    private int cache_status;

    @Column(name = "version")
    private String version;

    @Column(name = "has_pay_status")
    private int has_pay_status;

    @Column(name = "is_offline")
    private int is_offline;

    public FavoriteMusicDao(){}

    protected FavoriteMusicDao(Parcel in) {
        _id = in.readLong();
        musicinfo_id = in.readLong();
        song_id = in.readLong();
        title = in.readString();
        artist_id = in.readString();
        artist = in.readString();
        album_id = in.readString();
        album = in.readString();
        fav_time = in.readLong();
        havehigh = in.readInt();
        charge = in.readInt();
        fav_type = in.readInt();
        allbitrate = in.readString();
        path = in.readString();
        file_from = in.readInt();
        has_original = in.readInt();
        has_mv_mobile = in.readInt();
        song_source = in.readString();
        original_rate = in.readString();
        cache_status = in.readInt();
        version = in.readString();
        has_pay_status = in.readInt();
        is_offline = in.readInt();
    }

    public static final Creator<FavoriteMusicDao> CREATOR = new Creator<FavoriteMusicDao>() {
        @Override
        public FavoriteMusicDao createFromParcel(Parcel in) {
            return new FavoriteMusicDao(in);
        }

        @Override
        public FavoriteMusicDao[] newArray(int size) {
            return new FavoriteMusicDao[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getMusicinfo_id() {
        return musicinfo_id;
    }

    public void setMusicinfo_id(long musicinfo_id) {
        this.musicinfo_id = musicinfo_id;
    }

    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getFav_time() {
        return fav_time;
    }

    public void setFav_time(long fav_time) {
        this.fav_time = fav_time;
    }

    public int getHavehigh() {
        return havehigh;
    }

    public void setHavehigh(int havehigh) {
        this.havehigh = havehigh;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getFav_type() {
        return fav_type;
    }

    public void setFav_type(int fav_type) {
        this.fav_type = fav_type;
    }

    public String getAllbitrate() {
        return allbitrate;
    }

    public void setAllbitrate(String allbitrate) {
        this.allbitrate = allbitrate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFile_from() {
        return file_from;
    }

    public void setFile_from(int file_from) {
        this.file_from = file_from;
    }

    public int getHas_original() {
        return has_original;
    }

    public void setHas_original(int has_original) {
        this.has_original = has_original;
    }

    public int getHas_mv_mobile() {
        return has_mv_mobile;
    }

    public void setHas_mv_mobile(int has_mv_mobile) {
        this.has_mv_mobile = has_mv_mobile;
    }

    public String getSong_source() {
        return song_source;
    }

    public void setSong_source(String song_source) {
        this.song_source = song_source;
    }

    public String getOriginal_rate() {
        return original_rate;
    }

    public void setOriginal_rate(String original_rate) {
        this.original_rate = original_rate;
    }

    public int getCache_status() {
        return cache_status;
    }

    public void setCache_status(int cache_status) {
        this.cache_status = cache_status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getHas_pay_status() {
        return has_pay_status;
    }

    public void setHas_pay_status(int has_pay_status) {
        this.has_pay_status = has_pay_status;
    }

    public int getIs_offline() {
        return is_offline;
    }

    public void setIs_offline(int is_offline) {
        this.is_offline = is_offline;
    }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeInt(havehigh);
        dest.writeInt(charge);
        dest.writeInt(fav_type);
        dest.writeString(allbitrate);
        dest.writeString(path);
        dest.writeInt(file_from);
        dest.writeInt(has_original);
        dest.writeInt(has_mv_mobile);
        dest.writeString(song_source);
        dest.writeString(original_rate);
        dest.writeInt(cache_status);
        dest.writeString(version);
        dest.writeInt(has_pay_status);
        dest.writeInt(is_offline);
    }
}
