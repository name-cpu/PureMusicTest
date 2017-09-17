package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import java.util.List;
import java.util.Map;
/**
 * Created by kaizhiwei on 17/9/17.
 */

public class MediaModelProxy {
    private static MediaModelProxy mInstance;
    private Context mContext;

    public static MediaModelProxy getInstance(){
        if(mInstance == null){
            mInstance = new MediaModelProxy();
        }

        return mInstance;
    }

    private MediaModelProxy(){

    }

    public void init(Context context){
        mContext = context;
        MediaModel.getInstance().init(context);
        new BaseRunnable(null) {
            @Override
            public void doBusiness() throws Exception {
                MediaModel.getInstance().getAllMusicInfos();
            }
        };
    }

    public void asyncGetAllMusicInfos(final BaseHandler handler){
        new BaseRunnable(handler) {
            @Override
            public void doBusiness() throws Exception {
                List<MusicInfoDao> listRet = MediaModel.getInstance().getAllMusicInfos();
                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, listRet).sendToTarget();
            }
        };
    }

    public void asyncQueryMusicInfosByFolder(final String folder, final BaseHandler handler){
        new BaseRunnable(handler) {
            @Override
            public void doBusiness() throws Exception {
                Map<String, List<MusicInfoDao>> mapRet = MediaModel.getInstance().queryMusicInfosByFolder(folder);
                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, mapRet).sendToTarget();
            }
        };
    }

    public void asyncQueryMusicInfosByArtist(final String artist, final BaseHandler handler){
        new BaseRunnable(handler) {
            @Override
            public void doBusiness() throws Exception {
                Map<String, List<MusicInfoDao>> mapRet = MediaModel.getInstance().queryMusicInfosByArtist(artist);
                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, mapRet).sendToTarget();
            }
        };
    }

    public void asyncQueryMusicInfosByName(final String songName, final BaseHandler handler){
        new BaseRunnable(handler) {
            @Override
            public void doBusiness() throws Exception {
                List<MusicInfoDao> listRet = MediaModel.getInstance().queryMusicInfosByName(songName);
                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, listRet).sendToTarget();
            }
        };
    }

    public void asyncQueryMusicInfosByAlbum(final String album, final BaseHandler handler){
        new BaseRunnable(handler) {
            @Override
            public void doBusiness() throws Exception {
                Map<String, List<MusicInfoDao>> mapRet = MediaModel.getInstance().queryMusicInfosByAlbum(album);
                handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, mapRet).sendToTarget();
            }
        };
    }
}
