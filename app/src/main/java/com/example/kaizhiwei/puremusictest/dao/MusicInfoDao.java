package com.example.kaizhiwei.puremusictest.dao;

import android.os.Parcel;
import android.os.Parcelable;
import org.videolan.libvlc.Media;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by kaizhiwei on 16/11/12.
 */

@Table(name = "musicinfo")
public class MusicInfoDao implements Parcelable {
    @Column(name = "id",isId = true,autoGen = true)
    private long _id;
    private String _data;
    private long _size;
    private String _display_name;
    private String title;
    private String title_key;
    private String title_letter;
    private long date_added;
    private long date_modified;
    private String mime_type;
    private long duration;
    private long bookmark;
    private String artist;
    private String artist_key;
    private String composer;
    private String album;
    private String album_key;
    private String album_art;
    private long track;
    private long year;
    private long mediastore_id;
    private String lyric_path;
    private long is_lossless;
    private String artist_image;
    private String album_image;
    private long last_playtime;
    private long play_times;
    private long data_from;
    private String save_path;
    private long is_played;
    private long song_id;
    private long equalizer_level;
    private long replay_gain_level;
    private long is_offline_cache;
    private long is_faved;
    private long have_high;
    private long bitrate;
    private long has_original;
    private long flag;
    private String original_rate;
    private String all_rates;
    private long is_deleted;
    private long skip_auto_scan;
    private long is_offline;
    private long has_pay_status;
    private String version;
    private String cache_path;
    private long play_type;
    private String file_url;
    private String file_hash;

    private Media mMedia;

    public MusicInfoDao(){

    }

    protected MusicInfoDao(Parcel in) {
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
        mediastore_id = in.readLong();
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

    public static final Creator<MusicInfoDao> CREATOR = new Creator<MusicInfoDao>() {
        @Override
        public MusicInfoDao createFromParcel(Parcel in) {
            return new MusicInfoDao(in);
        }

        @Override
        public MusicInfoDao[] newArray(int size) {
            return new MusicInfoDao[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_data() {
        return _data;
    }

    public void set_data(String _data) {
        this._data = _data;
    }

    public long get_size() {
        return _size;
    }

    public void set_size(long _size) {
        this._size = _size;
    }

    public String get_display_name() {
        return _display_name;
    }

    public void set_display_name(String _display_name) {
        this._display_name = _display_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_key() {
        return title_key;
    }

    public void setTitle_key(String title_key) {
        this.title_key = title_key;
    }

    public String getTitle_letter() {
        return title_letter;
    }

    public void setTitle_letter(String title_letter) {
        this.title_letter = title_letter;
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

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getBookmark() {
        return bookmark;
    }

    public void setBookmark(long bookmark) {
        this.bookmark = bookmark;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist_key() {
        return artist_key;
    }

    public void setArtist_key(String artist_key) {
        this.artist_key = artist_key;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum_key() {
        return album_key;
    }

    public void setAlbum_key(String album_key) {
        this.album_key = album_key;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }

    public long getTrack() {
        return track;
    }

    public void setTrack(long track) {
        this.track = track;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getMediastore_id() {
        return mediastore_id;
    }

    public void setMediastore_id(long mediastore_id) {
        this.mediastore_id = mediastore_id;
    }

    public String getLyric_path() {
        return lyric_path;
    }

    public void setLyric_path(String lyric_path) {
        this.lyric_path = lyric_path;
    }

    public long getIs_lossless() {
        return is_lossless;
    }

    public void setIs_lossless(long is_lossless) {
        this.is_lossless = is_lossless;
    }

    public String getArtist_image() {
        return artist_image;
    }

    public void setArtist_image(String artist_image) {
        this.artist_image = artist_image;
    }

    public String getAlbum_image() {
        return album_image;
    }

    public void setAlbum_image(String album_image) {
        this.album_image = album_image;
    }

    public long getLast_playtime() {
        return last_playtime;
    }

    public void setLast_playtime(long last_playtime) {
        this.last_playtime = last_playtime;
    }

    public long getPlay_times() {
        return play_times;
    }

    public void setPlay_times(long play_times) {
        this.play_times = play_times;
    }

    public long getData_from() {
        return data_from;
    }

    public void setData_from(long data_from) {
        this.data_from = data_from;
    }

    public String getSave_path() {
        return save_path;
    }

    public void setSave_path(String save_path) {
        this.save_path = save_path;
    }

    public long getIs_played() {
        return is_played;
    }

    public void setIs_played(long is_played) {
        this.is_played = is_played;
    }

    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }

    public long getEqualizer_level() {
        return equalizer_level;
    }

    public void setEqualizer_level(long equalizer_level) {
        this.equalizer_level = equalizer_level;
    }

    public long getReplay_gain_level() {
        return replay_gain_level;
    }

    public void setReplay_gain_level(long replay_gain_level) {
        this.replay_gain_level = replay_gain_level;
    }

    public long getIs_offline_cache() {
        return is_offline_cache;
    }

    public void setIs_offline_cache(long is_offline_cache) {
        this.is_offline_cache = is_offline_cache;
    }

    public long getIs_faved() {
        return is_faved;
    }

    public void setIs_faved(long is_faved) {
        this.is_faved = is_faved;
    }

    public long getHave_high() {
        return have_high;
    }

    public void setHave_high(long have_high) {
        this.have_high = have_high;
    }

    public long getBitrate() {
        return bitrate;
    }

    public void setBitrate(long bitrate) {
        this.bitrate = bitrate;
    }

    public long getHas_original() {
        return has_original;
    }

    public void setHas_original(long has_original) {
        this.has_original = has_original;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public String getOriginal_rate() {
        return original_rate;
    }

    public void setOriginal_rate(String original_rate) {
        this.original_rate = original_rate;
    }

    public String getAll_rates() {
        return all_rates;
    }

    public void setAll_rates(String all_rates) {
        this.all_rates = all_rates;
    }

    public long getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(long is_deleted) {
        this.is_deleted = is_deleted;
    }

    public long getSkip_auto_scan() {
        return skip_auto_scan;
    }

    public void setSkip_auto_scan(long skip_auto_scan) {
        this.skip_auto_scan = skip_auto_scan;
    }

    public long getIs_offline() {
        return is_offline;
    }

    public void setIs_offline(long is_offline) {
        this.is_offline = is_offline;
    }

    public long getHas_pay_status() {
        return has_pay_status;
    }

    public void setHas_pay_status(long has_pay_status) {
        this.has_pay_status = has_pay_status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCache_path() {
        return cache_path;
    }

    public void setCache_path(String cache_path) {
        this.cache_path = cache_path;
    }

    public long getPlay_type() {
        return play_type;
    }

    public void setPlay_type(long play_type) {
        this.play_type = play_type;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_hash() {
        return file_hash;
    }

    public void setFile_hash(String file_hash) {
        this.file_hash = file_hash;
    }

    public Media getmMedia() {
        return mMedia;
    }

    public void setmMedia(Media mMedia) {
        this.mMedia = mMedia;
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
