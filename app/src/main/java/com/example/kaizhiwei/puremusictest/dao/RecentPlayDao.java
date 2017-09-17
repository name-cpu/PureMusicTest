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
    private long info_id;

    @Column(name = "time_stamp")
    private long time_stamp;

    @Column(name = "has_play_status")
    private boolean has_play_status;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getInfo_id() {
        return info_id;
    }

    public void setInfo_id(long info_id) {
        this.info_id = info_id;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public boolean isHas_play_status() {
        return has_play_status;
    }

    public void setHas_play_status(boolean has_play_status) {
        this.has_play_status = has_play_status;
    }

}
