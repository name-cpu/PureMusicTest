package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.BangDanListBean;
import com.example.kaizhiwei.puremusictest.bean.BangDanSongDetailInfo;
import com.example.kaizhiwei.puremusictest.bean.GeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanSongDetailInfo;

import retrofit2.http.Query;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public interface BangDanInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void getBangDanInfo(String format, String from, String method, int type, int offset, int size, String fields);

        void getBangDanSongDetail(String from, String version, String format, String method, String songid);
    }

    interface View extends BaseContract.View {
        void onGetBangDanInfoSuccess(BangDanSongDetailInfo bean);
        void onGetBangDanSongDetailInfoSuccess(GeDanSongDetailInfo songDetailInfo);
    }
}
