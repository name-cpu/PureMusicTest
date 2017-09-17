package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
import com.example.kaizhiwei.puremusictest.base.ThreadPool;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.scanmusic.MediaStoreSource;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.dao.DaoManager;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public class MediaModel {
    private static MediaModel mInstance;
    private Context mContext;
    private boolean isScaning = false;
    private List<IMediaDataObserver> mObservers = new ArrayList<>();
    private static final int NOTIFY_MEDIA_INFO = 1;
    private android.os.Handler mSafeHandler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == NOTIFY_MEDIA_INFO){
                int musicId = msg.arg1;
                notifyMediaDataChanged(musicId);
            }
        }
    };


    public static MediaModel getInstance(){
        if(mInstance == null){
            mInstance = new MediaModel();
        }

        return mInstance;
    }

    private MediaModel(){

    }

    public void init(Context context){
        mContext = context;
    }

    public void addObserver(IMediaDataObserver observer){
        if(observer == null)
            return;

        if(!mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    public void removeObserver(IMediaDataObserver observer){
        if(observer == null)
            return;

        if(mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    private void notifyMediaDataChanged(long musicId){
        for(int i = 0;i < mObservers.size();i++){
            mObservers.get(i).onMediaDataChanged(0, musicId);
        }
    }

    private void postNotifyMediaData(long musicId){
        mSafeHandler.removeMessages(NOTIFY_MEDIA_INFO);
        Message message = new Message();
        message.what = NOTIFY_MEDIA_INFO;
        message.arg1 = (int)musicId;
        mSafeHandler.sendMessageDelayed(message, 200);
    }

    public synchronized List<MusicInfoDao> getAllMusicInfos() {
        List<MusicInfoDao> list = null;
        try {
            list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            if (list == null || list.size() == 0) {
                MediaStoreSource storeSource = new MediaStoreSource(mContext);
                isScaning = true;
                list = storeSource.scan();
                for (int i = 0; i < list.size(); i++) {
                    try {
                        DaoManager.getInstance().getDbManager().save(list.get(i));
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                isScaning = false;
            }
            return list;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public int getMusicInfoDaoSize(){
        String strSql = "select count(*) from musicinfo;";
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

    public void getAllMusicInfos(BaseHandler handler){
        while (isScaning){
            try{
                Thread.sleep(200);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        try{
            List<MusicInfoDao> list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public MusicInfoDao getMusicInfoById(long id){
        MusicInfoDao musicInfoDao = null;
        try {
            musicInfoDao = DaoManager.getInstance().getDbManager().findById(MusicInfoDao.class, id);
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            return musicInfoDao;
        }
    }

    public void updateMusicInfo(MusicInfoDao musicInfoDao){
        if(musicInfoDao == null)
            return;

        try {
            DaoManager.getInstance().getDbManager().saveOrUpdate(musicInfoDao);
            postNotifyMediaData(musicInfoDao.get_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteMusicInfo(MusicInfoDao musicInfoDao){
        try {
            int num = DaoManager.getInstance().getDbManager().delete(MusicInfoDao.class, WhereBuilder.b("id", " == ", musicInfoDao.get_id()));
            PlaylistModel.getInstance().removePlaylistMembers(musicInfoDao.get_id());
            FavoriteMusicModel.getInstance().removeFavoriteMusic(musicInfoDao.get_id());
            postNotifyMediaData(musicInfoDao.get_id());
            return (num > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Map<String, List<MusicInfoDao>> queryMusicInfosByFolder(String folder){
        while (isScaning){
            try{
                Thread.sleep(200);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        try {
            List<MusicInfoDao> list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            Map<String, List<MusicInfoDao>> map = new HashMap<>();
            for(int i = 0;i < list.size();i++) {
                MusicInfoDao dao = list.get(i);
                if(dao == null)
                    continue;

                File file = new File(list.get(i).get_data());
                if (file.exists() && file.isFile()) {
                    File parent = file.getParentFile();
                    String parentName = parent.getName();
                    dao.setSave_path(parent.getPath());

                    List<MusicInfoDao> temp = map.get(parentName);
                    if(temp == null){
                        temp = new ArrayList<>();
                    }
                    if(TextUtils.isEmpty(folder) || folder.equalsIgnoreCase(parentName)){
                        temp.add(list.get(i));
                    }
                    map.put(parentName, temp);
                }
            }

            return map;
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<MusicInfoDao> queryMusicInfosByName(String songName){
        try {
            Selector<MusicInfoDao> cursor = DaoManager.getInstance().getDbManager().selector(MusicInfoDao.class).
                    where(WhereBuilder.b("title", "like", "%%" + songName + "%%")).or(WhereBuilder.b("artist", "like", "%%" + songName + "%%"));
            List<MusicInfoDao> list = cursor.findAll();
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, List<MusicInfoDao>> queryMusicInfosByArtist(String artist){
        try {
            List<MusicInfoDao> list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            Map<String, List<MusicInfoDao>> map = new HashMap<>();
            for(int i = 0;i < list.size();i++) {
                MusicInfoDao dao = list.get(i);
                if(dao == null)
                    continue;

                List<MusicInfoDao> temp = map.get(dao.getArtist());
                if(temp == null){
                    temp = new ArrayList<>();
                }
                if(TextUtils.isEmpty(artist) || dao.getArtist().contains(artist)){
                    temp.add(list.get(i));
                    map.put(dao.getArtist(), temp);
                }
            }

            return map;
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<String, List<MusicInfoDao>> queryMusicInfosByAlbum(String album){
        try {
            List<MusicInfoDao> list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            Map<String, List<MusicInfoDao>> map = new HashMap<>();
            for(int i = 0;i < list.size();i++) {
                MusicInfoDao dao = list.get(i);
                if(dao == null)
                    continue;

                List<MusicInfoDao> temp = map.get(dao.getAlbum());
                if(temp == null){
                    temp = new ArrayList<>();
                }
                if(TextUtils.isEmpty(album) || dao.getAlbum().contains(album)){
                    temp.add(list.get(i));
                }
                map.put(dao.getAlbum(), temp);
            }

            return map;
        } catch (DbException e) {
            e.printStackTrace();
        }

        return null;
    }
}
