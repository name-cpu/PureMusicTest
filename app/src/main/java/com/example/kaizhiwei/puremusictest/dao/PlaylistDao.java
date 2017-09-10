package com.example.kaizhiwei.puremusictest.dao;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by kaizhiwei on 17/8/20.
 */

//自建歌单信息
@Table(name = "playlistdatas")
public class PlaylistDao implements Parcelable, Serializable {
    @Column(name = "id",isId = true,autoGen = true)
    private long _id;

    @Column(name = "list_id")
    private long list_id;

    @Column(name = "_data")
    private long _data;

    @Column(name = "name")
    private String name = "";

    @Column(name = "date_added")
    private long date_added;

    @Column(name = "date_modified")
    private long date_modified;

    @Column(name = "song_count")
    private int song_count;

    @Column(name = "img_url")
    private String img_url = "";

    @Column(name = "sort")
    private int sort;

    public PlaylistDao(){

    }

    protected PlaylistDao(Parcel in) {
        _id = in.readLong();
        list_id = in.readLong();
        _data = in.readLong();
        name = in.readString();
        date_added = in.readLong();
        date_modified = in.readLong();
        song_count = in.readInt();
        img_url = in.readString();
        sort = in.readInt();
    }

    public static final Creator<PlaylistDao> CREATOR = new Creator<PlaylistDao>() {
        @Override
        public PlaylistDao createFromParcel(Parcel in) {
            return new PlaylistDao(in);
        }

        @Override
        public PlaylistDao[] newArray(int size) {
            return new PlaylistDao[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getList_id() {
        return list_id;
    }

    public void setList_id(long list_id) {
        this.list_id = list_id;
    }

    public long get_data() {
        return _data;
    }

    public void set_data(long _data) {
        this._data = _data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate_added() {
        return date_added;
    }

    public void setDate_added(long date_added) {
        this.date_added = date_added;
    }

    public long getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(long date_modified) {
        this.date_modified = date_modified;
    }

    public int getSong_count() {
        return song_count;
    }

    public void setSong_count(int song_count) {
        this.song_count = song_count;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeLong(list_id);
        dest.writeLong(_data);
        dest.writeString(name);
        dest.writeLong(date_added);
        dest.writeLong(date_modified);
        dest.writeInt(song_count);
        dest.writeString(img_url);
        dest.writeInt(sort);
    }
}
