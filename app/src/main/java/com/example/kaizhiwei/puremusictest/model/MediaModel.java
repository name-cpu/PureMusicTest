package com.example.kaizhiwei.puremusictest.model.scanmusic;

import android.content.Context;
import android.os.Message;

import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.dao.DaoManager;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import org.xutils.ex.DbException;

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
}
