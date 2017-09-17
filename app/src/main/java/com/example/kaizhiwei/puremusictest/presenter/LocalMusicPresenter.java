package com.example.kaizhiwei.puremusictest.presenter;

import android.os.Message;

import com.example.kaizhiwei.puremusictest.model.MediaModel;
import com.example.kaizhiwei.puremusictest.model.MediaModelProxy;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.contract.LocalMusicContract;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        MediaModelProxy.getInstance().asyncGetAllMusicInfos(new BaseHandler() {
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
        MediaModelProxy.getInstance().asyncQueryMusicInfosByFolder(folder, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                Map<String, List<MusicInfoDao>> mapRet = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    mapRet = (Map<String, List<MusicInfoDao>>)msg.obj;
                    if(mView != null){
                        mView.onGetMusicInfosByFolder(mapRet);
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
        MediaModelProxy.getInstance().asyncQueryMusicInfosByArtist(artist, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                Map<String, List<MusicInfoDao>> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (Map<String, List<MusicInfoDao>>)msg.obj;
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
        MediaModelProxy.getInstance().asyncQueryMusicInfosByAlbum(album, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                Map<String, List<MusicInfoDao>> list = null;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    list = (Map<String, List<MusicInfoDao>>)msg.obj;
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
    public void queryMusicInfosByFolder(String folder) {
        MediaModelProxy.getInstance().asyncQueryMusicInfosByFolder(folder, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    ArrayList<MusicInfoDao> list = new ArrayList<MusicInfoDao>();
                    Map<String, List<MusicInfoDao>> map = (Map<String, List<MusicInfoDao>>)msg.obj;
                    Set<String> keySet = map.keySet();
                    for(String key : keySet){
                        list.addAll(map.get(key));
                    }
                    if(mView != null){
                        mView.onQueryMusicInfosByFolder(list);
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
        MediaModelProxy.getInstance().asyncQueryMusicInfosByName(songName, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<MusicInfoDao> list = new ArrayList<MusicInfoDao>();
                    Map<String, List<MusicInfoDao>> map = (Map<String, List<MusicInfoDao>>)msg.obj;
                    Set<String> keySet = map.keySet();
                    for(String key : keySet){
                        list.addAll(map.get(key));
                    }
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
        MediaModelProxy.getInstance().asyncQueryMusicInfosByArtist(artist, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<MusicInfoDao> list = new ArrayList<MusicInfoDao>();
                    Map<String, List<MusicInfoDao>> map = (Map<String, List<MusicInfoDao>>)msg.obj;
                    Set<String> keySet = map.keySet();
                    for(String key : keySet){
                        list.addAll(map.get(key));
                    }
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
        MediaModelProxy.getInstance().asyncQueryMusicInfosByAlbum(album, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<MusicInfoDao> list = new ArrayList<MusicInfoDao>();
                    Map<String, List<MusicInfoDao>> map = (Map<String, List<MusicInfoDao>>)msg.obj;
                    Set<String> keySet = map.keySet();
                    for(String key : keySet){
                        list.addAll(map.get(key));
                    }
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
