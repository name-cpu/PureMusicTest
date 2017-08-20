package com.example.kaizhiwei.puremusictest.dao;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by kaizhiwei on 17/8/20.
 */

//自建歌单中的歌曲信息
@Table(name = "playlistmemberdatas")
public class PlaylistMemberDao implements Parcelable, Serializable {
    @Column(name = "id",isId = true,autoGen = true)
    private int _id;

    @Column(name = "music_id")
    private long music_id;

    @Column(name = "playlist_id")
    private long playlist_id;

    @Column(name = "play_order")
    private int play_order;

    @Column(name = "is_local")
    private int is_local;

    protected PlaylistMemberDao(Parcel in) {
        _id = in.readInt();
        music_id = in.readInt();
        playlist_id = in.readLong();
        play_order = in.readInt();
        is_local = in.readInt();
    }

    public PlaylistMemberDao(){

    }

    public static final Creator<PlaylistMemberDao> CREATOR = new Creator<PlaylistMemberDao>() {
        @Override
        public PlaylistMemberDao createFromParcel(Parcel in) {
            return new PlaylistMemberDao(in);
        }

        @Override
        public PlaylistMemberDao[] newArray(int size) {
            return new PlaylistMemberDao[size];
        }
    };

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public long getMusic_id() {
        return music_id;
    }

    public void setMusic_id(long music_id) {
        this.music_id = music_id;
    }

    public long getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(long playlist_id) {
        this.playlist_id = playlist_id;
    }

    public int getPlay_order() {
        return play_order;
    }

    public void setPlay_order(int play_order) {
        this.play_order = play_order;
    }

    public int getIs_local() {
        return is_local;
    }

    public void setIs_local(int is_local) {
        this.is_local = is_local;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeLong(music_id);
        dest.writeLong(playlist_id);
        dest.writeInt(play_order);
        dest.writeInt(is_local);
    }
}
