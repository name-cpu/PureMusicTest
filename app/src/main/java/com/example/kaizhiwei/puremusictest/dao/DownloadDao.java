package com.example.kaizhiwei.puremusictest.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by kaizhiwei on 17/8/19.
 */

@Table(name="download")
public class DownloadDao {
    @Column(name = "id",isId = true,autoGen = true)
    private long _id;

    @Column(name = "url")
    private String uri;

    @Column(name = "url_md")
    private String url_md;

    @Column(name = "singer_img")
    private String singer_img;

    @Column(name = "album_img")
    private String album_img;

    @Column(name = "lyric_url")
    private String lyric_url;

    @Column(name = "track_title")
    private String track_title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "album")
    private String album;

    @Column(name = "file_type")
    private String file_type;

    @Column(name = "postfix")
    private String postfix;

    @Column(name = "total_bytes")
    private String total_bytes;

    @Column(name = "save_path")
    private String save_path;

    @Column(name = "save_name")
    private String save_name;

    @Column(name = "file_name")
    private String file_name;
}
