package com.example.kaizhiwei.puremusictest.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by kaizhiwei on 17/8/19.
 */

@Table(name = "recent_playlist")
public class RecentPlayDao {
    @Column(name = "id",isId = true,autoGen = true)
    private long _id;

    @Column(name = "info_id")
    private String info_id;

    @Column(name = "time_stamp")
    private long time_stamp;

    @Column(name = "has_play_status")
    private boolean has_play_status;
}
