package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;
import android.database.Cursor;

import com.example.kaizhiwei.puremusictest.dao.DaoManager;
import com.example.kaizhiwei.puremusictest.dao.FavoriteMusicDao;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/9/17.
 */

public class FavoriteMusicModel {
    private static FavoriteMusicModel mInstance;
    private Context mContext;
    private List<IFavoriteMusicObserver> mObservers = new ArrayList<>();

    public static FavoriteMusicModel getInstance(){
        if(mInstance == null){
            mInstance = new FavoriteMusicModel();
        }

        return mInstance;
    }

    private FavoriteMusicModel(){

    }

    public void addObserver(IFavoriteMusicObserver observer){
        if(observer == null)
            return;

        if(!mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    public void removeObserver(IFavoriteMusicObserver observer){
        if(observer == null)
            return;

        if(mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    private void notifyFavoriteMusicChanged(long playlistId){
        for(int i = 0;i < mObservers.size();i++){
            mObservers.get(i).onFavoriteMusicChanged(0, playlistId);
        }
    }

    public int getFavoriteMusicSize(){
        String strSql = "select count(*) from favorites_music;";
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

    public boolean addFavoriteMusic(FavoriteMusicDao favoriteMusicDao){
        if(favoriteMusicDao == null || favoriteMusicDao.getMusicinfo_id() < 0)
            return false;

        try {
            Selector selector = DaoManager.getInstance().getDbManager().selector(FavoriteMusicDao.class).where("musicinfo_id", " == ", favoriteMusicDao.getMusicinfo_id());
            List<FavoriteMusicDao> list = selector.findAll();
            if(list == null || list.size() == 0){
                DaoManager.getInstance().getDbManager().save(favoriteMusicDao);
            }
            else{
                updateFavoriteMusic(favoriteMusicDao);
            }

            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        notifyFavoriteMusicChanged(favoriteMusicDao.get_id());

        return false;
    }

    public boolean isHasFavorite(long musicId){
        try {
            Selector selector = DaoManager.getInstance().getDbManager().selector(FavoriteMusicDao.class).where("musicinfo_id", " == ", musicId);
            List<FavoriteMusicDao> list = selector.findAll();
            if (list == null || list.size() == 0) {
                return false;
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean removeFavoriteMusic(long musicId){
        int ret = -1;
        try {
            ret = DaoManager.getInstance().getDbManager().delete(FavoriteMusicDao.class, WhereBuilder.b("musicinfo_id" , " == ", musicId));
        } catch (DbException e) {
            e.printStackTrace();
        }
        notifyFavoriteMusicChanged(musicId);
        return ret > 0 ? true : false;
    }

    public boolean updateFavoriteMusic(FavoriteMusicDao FavoriteMusicDao){
        if(FavoriteMusicDao == null || FavoriteMusicDao.getMusicinfo_id() < 0)
            return false;

        int ret = -1;
        try {
            DaoManager.getInstance().getDbManager().saveOrUpdate(FavoriteMusicDao);
//            ret = DaoManager.getInstance().getDbManager().update(FavoriteMusicDao.class, WhereBuilder.b("info_id" , " == ", FavoriteMusicDao.getInfo_id()),
//                    new KeyValue("time_stamp", FavoriteMusicDao.getTime_stamp()), new KeyValue("has_play_status", FavoriteMusicDao.isHas_play_status()));
        } catch (DbException e) {
            e.printStackTrace();
        }
        notifyFavoriteMusicChanged(FavoriteMusicDao.get_id());
        return ret > 0 ? true : false;
    }

    public List<FavoriteMusicDao> getAllFavoriteMusics(){
        List<FavoriteMusicDao> list = null;
        try {
            list = DaoManager.getInstance().getDbManager().findAll(FavoriteMusicDao.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return list;
    }
    
}
