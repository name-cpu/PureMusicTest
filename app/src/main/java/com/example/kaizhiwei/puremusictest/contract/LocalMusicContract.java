package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.List;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public interface LocalMusicContract {
    interface Presenter extends BaseContract.Presenter{
        void getAllMusicInfos();
    }

    interface View extends BaseContract.View {
        void onGetAllMusicInfos(List<MusicInfoDao> list);
    }

}
