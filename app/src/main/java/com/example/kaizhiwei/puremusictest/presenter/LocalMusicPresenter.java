package com.example.kaizhiwei.puremusictest.presenter;

import android.os.Message;

import com.example.kaizhiwei.puremusictest.model.MediaModel;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.contract.LocalMusicContract;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.List;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public class LocalMusicPresenter implements LocalMusicContract.Presenter {
    private LocalMusicContract.View mView;

    public LocalMusicPresenter(LocalMusicContract.View view){
        mView = view;
    }

    @Override
    public void getAllMusicInfos() {
        MediaModel.getInstance().asyncGetAllMusicInfos(new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                List<MusicInfoDao> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (List<MusicInfoDao>)msg.obj;
                    if(mView != null){
                        mView.onGetAllMusicInfos(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }

    @Override
    public MusicInfoDao getMusicInfoById(long id) {
        return MediaModel.getInstance().getMusicInfoById(id);
    }

    @Override
    public void getMusicInfosByFolder(String folder) {
        MediaModel.getInstance().asyncMusicInfosByFolder(folder, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                List<MusicInfoDao> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (List<MusicInfoDao>)msg.obj;
                    if(mView != null){
                        mView.onGetMusicInfosByFolder(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }

    @Override
    public void getMusicInfosByArtist(String artist) {
        MediaModel.getInstance().asyncMusicInfosByArtist(artist, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                List<MusicInfoDao> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (List<MusicInfoDao>)msg.obj;
                    if(mView != null){
                        mView.onGetMusicInfosByArtist(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }

    @Override
    public void getMusicInfosByAlbum(String album) {
        MediaModel.getInstance().asyncMusicInfosByAlbum(album, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                List<MusicInfoDao> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (List<MusicInfoDao>)msg.obj;
                    if(mView != null){
                        mView.onGetMusicInfosByAlbum(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }

    @Override
    public void queryMusicInfosByName(String songName) {
        MediaModel.getInstance().asyncQueryMusicInfosByName(songName, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                List<MusicInfoDao> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (List<MusicInfoDao>)msg.obj;
                    if(mView != null){
                        mView.onQueryMusicInfosByName(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }

    @Override
    public void queryMusicInfosByArist(String artist) {
        MediaModel.getInstance().asyncQueryMusicInfosByArtist(artist, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                List<MusicInfoDao> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (List<MusicInfoDao>)msg.obj;
                    if(mView != null){
                        mView.onQueryMusicInfosByArist(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }

    @Override
    public void queryMuisicInfosByAlbum(String album) {
        MediaModel.getInstance().asyncQueryMusicInfosByAlbum(album, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                List<MusicInfoDao> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (List<MusicInfoDao>)msg.obj;
                    if(mView != null){
                        mView.onQueryMuisicInfosByAlbum(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }
}
