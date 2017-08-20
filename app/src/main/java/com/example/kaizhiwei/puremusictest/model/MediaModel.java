package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.text.TextUtils;

import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public class MediaModel {
    private static MediaModel mInstance;
    private Context mContext;
    private CountDownLatch mCountDown;

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

    public List<MusicInfoDao> getAllMusicInfos(){
        if(mCountDown == null){
            mCountDown = new CountDownLatch(1);
        }
        try {
            mCountDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<MusicInfoDao> list = null;
        try {
            list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            if(list == null || list.size() == 0){
                MediaStoreSource storeSource = new MediaStoreSource(mContext);
                storeSource.scan(new BaseHandler() {
                    @Override
                    public void handleBusiness(Message msg) {
                        mCountDown.countDown();


                    }
                });
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return list;
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

    public void asyncGetAllMusicInfos(final BaseHandler handler){
        try {
            final List<MusicInfoDao> list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            if(list == null || list.size() == 0){
                MediaStoreSource storeSource = new MediaStoreSource(mContext);
                storeSource.scan(new BaseHandler() {
                    @Override
                    public void handleBusiness(Message msg) {
                        int what = msg.what;
                        if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                            List<MusicInfoDao> list1  = (List<MusicInfoDao>)msg.obj;
                            for(int i = 0;i < list1.size();i++){
                                try {
                                    DaoManager.getInstance().getDbManager().save(list1.get(i));
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list1).sendToTarget();
                        }else{

                        }
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();

                    }
                });
            }
            else{
                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
            }
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

    public void asyncMusicInfosByFolder(final String folder, final BaseHandler handler){
        try {
            final List<MusicInfoDao> list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            if(list == null || list.size() == 0){
                MediaStoreSource storeSource = new MediaStoreSource(mContext);
                storeSource.scan(new BaseHandler() {
                    @Override
                    public void handleBusiness(Message msg) {
                        int what = msg.what;
                        if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                            List<MusicInfoDao> list1  = (List<MusicInfoDao>)msg.obj;
                            List<MusicInfoDao> listRet = new ArrayList<>();

                            for(int i = 0;i < list1.size();i++) {
                                MusicInfoDao dao = list1.get(i);
                                if(dao == null)
                                    continue;

                                File file = new File(list1.get(i).get_data());
                                if (file.exists() && file.isFile()) {
                                    File parent = file.getParentFile();
                                    String parentName = parent.getName();
                                    dao.setSave_path(parent.getPath());

                                    if(TextUtils.isEmpty(folder)){
                                        listRet.add(list1.get(i));
                                    }
                                    else if(parentName.equalsIgnoreCase(folder)) {
                                        listRet.add(list1.get(i));
                                    }
                                }
                            }
                            handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, listRet).sendToTarget();
                        }else{

                        }
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();

                    }
                });
            }
            else{
                List<MusicInfoDao> listRet = new ArrayList<>();
                for(int i = 0;i < list.size();i++) {
                    MusicInfoDao dao = list.get(i);
                    if(dao == null)
                        continue;

                    File file = new File(list.get(i).get_data());
                    if (file.exists() && file.isFile()) {
                        File parent = file.getParentFile();
                        String parentName = parent.getName();
                        dao.setSave_path(parent.getPath());

                        if(TextUtils.isEmpty(folder)){
                            listRet.add(list.get(i));
                        }
                        else if(parentName.equalsIgnoreCase(folder)) {
                            listRet.add(list.get(i));
                        }
                    }
                }

                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, listRet).sendToTarget();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void asyncMusicInfosByArtist(final String artist, final BaseHandler handler){
        try {
            final List<MusicInfoDao> list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            if(list == null || list.size() == 0){
                MediaStoreSource storeSource = new MediaStoreSource(mContext);
                storeSource.scan(new BaseHandler() {
                    @Override
                    public void handleBusiness(Message msg) {
                        int what = msg.what;
                        if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                            List<MusicInfoDao> list1  = (List<MusicInfoDao>)msg.obj;
                            List<MusicInfoDao> listRet = new ArrayList<>();
                            if(TextUtils.isEmpty(artist)){
                                listRet.addAll(list1);
                            }
                            else{
                                for(int i = 0;i < list1.size();i++) {
                                    File file = new File(list1.get(i).get_data());
                                    if (file.exists() && file.isFile()) {
                                        String artistTemp = list1.get(i).getArtist();
                                        if (artistTemp.equalsIgnoreCase(artist)) {
                                            listRet.add(list1.get(i));
                                        }
                                    }
                                }
                            }
                            handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, listRet).sendToTarget();
                        }else{

                        }
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();

                    }
                });
            }
            else{
                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void asyncMusicInfosByAlbum(final String album, final BaseHandler handler){
        try {
            final List<MusicInfoDao> list = DaoManager.getInstance().getDbManager().findAll(MusicInfoDao.class);
            if(list == null || list.size() == 0){
                MediaStoreSource storeSource = new MediaStoreSource(mContext);
                storeSource.scan(new BaseHandler() {
                    @Override
                    public void handleBusiness(Message msg) {
                        int what = msg.what;
                        if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                            List<MusicInfoDao> list1  = (List<MusicInfoDao>)msg.obj;
                            List<MusicInfoDao> listRet = new ArrayList<>();
                            if(TextUtils.isEmpty(album)){
                                listRet.addAll(list1);
                            }
                            else{
                                for(int i = 0;i < list1.size();i++) {
                                    File file = new File(list1.get(i).get_data());
                                    if (file.exists() && file.isFile()) {
                                        String albumTemp = list1.get(i).getAlbum();
                                        if (albumTemp.equalsIgnoreCase(album)) {
                                            listRet.add(list1.get(i));
                                        }
                                    }
                                }
                            }
                            handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, listRet).sendToTarget();
                        }else{

                        }
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();

                    }
                });
            }
            else{
                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void asyncQueryMusicInfosByName(final String songName, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                try {
                    Selector<MusicInfoDao> cursor = DaoManager.getInstance().getDbManager().selector(MusicInfoDao.class).
                        where(WhereBuilder.b("title", "like", "%%" + songName + "%%")).or(WhereBuilder.b("artist", "like", "%%" + songName + "%%"));
                    final List<MusicInfoDao> list = cursor.findAll();
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                } catch (DbException e) {
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();
                    e.printStackTrace();
                }
            }
        };
    }

    public void asyncQueryMusicInfosByArtist(final String artist, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                try {
                    Selector<MusicInfoDao> cursor = DaoManager.getInstance().getDbManager().selector(MusicInfoDao.class).
                            where(WhereBuilder.b("artist", "like", "%%" + artist + "%%"));
                    final List<MusicInfoDao> list = cursor.findAll();
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                } catch (DbException e) {
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();
                    e.printStackTrace();
                }
            }
        };
    }

    public void asyncQueryMusicInfosByAlbum(final String album, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                try {
                    Selector<MusicInfoDao> cursor = DaoManager.getInstance().getDbManager().selector(MusicInfoDao.class).
                            where(WhereBuilder.b("album", "like", "%%" + album + "%%"));
                    final List<MusicInfoDao> list = cursor.findAll();
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                } catch (DbException e) {
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();
                    e.printStackTrace();
                }
            }
        };
    }
}
