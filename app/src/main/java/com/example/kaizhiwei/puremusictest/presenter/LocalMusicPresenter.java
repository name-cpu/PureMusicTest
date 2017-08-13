package com.example.kaizhiwei.puremusictest.presenter;

import android.os.Message;

import com.example.kaizhiwei.puremusictest.Util.BusinessCode;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.contract.LocalMusicContract;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.model.scanmusic.MediaModel;

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
}
