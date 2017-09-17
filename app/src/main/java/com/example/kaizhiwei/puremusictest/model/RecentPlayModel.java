package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;
import android.database.Cursor;

import com.example.kaizhiwei.puremusictest.dao.DaoManager;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.dao.RecentPlayDao;

import org.xutils.common.util.KeyValue;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/9/11.
 */

public class RecentPlayModel {
    private static RecentPlayModel mInstance;
    private Context mContext;
    private List<IRecentPlayObserver> mObservers = new ArrayList<>();

    public static RecentPlayModel getInstance(){
        if(mInstance == null){
            mInstance = new RecentPlayModel();
        }

        return mInstance;
    }

    private RecentPlayModel(){

    }

    public void addObserver(IRecentPlayObserver observer){
        if(observer == null)
            return;

        if(!mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    public void removeObserver(IRecentPlayObserver observer){
        if(observer == null)
            return;

        if(mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    private void notifyRecentPlayChanged(long playlistId){
        for(int i = 0;i < mObservers.size();i++){
            mObservers.get(i).onRecentPlayChanged(0, playlistId);
        }
    }

    public int getRecentPlaySize(){
        String strSql = "select count(*) from recent_playlist;";
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = DaoManager.getInstance().getDbManager().execQuery(strSql);
            if(cursor != null){
                while (cursor.moveToNext()){
                    count = cursor.getInt(cursor.getColumnIndex("count(*)"));
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void addRecentPlay(RecentPlayDao recentPlayDao){
        if(recentPlayDao == null || recentPlayDao.getInfo_id() < 0)
            return;


        try {
             Selector selector = DaoManager.getInstance().getDbManager().selector(RecentPlayDao.class).where("info_id", " == ", recentPlayDao.getInfo_id());
            List<RecentPlayDao> list = selector.findAll();
            if(list == null || list.size() == 0){
                DaoManager.getInstance().getDbManager().save(recentPlayDao);
            }
            else{
                updateRecentPlay(recentPlayDao);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        notifyRecentPlayChanged(recentPlayDao.get_id());
    }

    public boolean removeRecentPlay(RecentPlayDao recentPlayDao){
        if(recentPlayDao == null || recentPlayDao.getInfo_id() < 0)
            return false;

        int ret = -1;
        try {
            ret = DaoManager.getInstance().getDbManager().delete(RecentPlayDao.class, WhereBuilder.b("info_id" , " == ", recentPlayDao.getInfo_id()));
        } catch (DbException e) {
            e.printStackTrace();
        }
        notifyRecentPlayChanged(recentPlayDao.get_id());
        return ret > 0 ? true : false;
    }

    public boolean updateRecentPlay(RecentPlayDao recentPlayDao){
        if(recentPlayDao == null || recentPlayDao.getInfo_id() < 0)
            return false;

        int ret = -1;
        try {
            ret = DaoManager.getInstance().getDbManager().update(RecentPlayDao.class, WhereBuilder.b("info_id" , " == ", recentPlayDao.getInfo_id()),
                    new KeyValue("time_stamp", recentPlayDao.getTime_stamp()), new KeyValue("has_play_status", recentPlayDao.isHas_play_status()));
        } catch (DbException e) {
            e.printStackTrace();
        }
        notifyRecentPlayChanged(recentPlayDao.get_id());
        return ret > 0 ? true : false;
    }

    public List<RecentPlayDao> getAllRecentPlays(){
        List<RecentPlayDao> list = null;
        try {
            list = DaoManager.getInstance().getDbManager().findAll(RecentPlayDao.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return list;
    }
}
