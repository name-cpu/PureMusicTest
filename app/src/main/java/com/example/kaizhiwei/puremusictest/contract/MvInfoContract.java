package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.MvCategoryBean;
import com.example.kaizhiwei.puremusictest.bean.MvSearchBean;
import com.example.kaizhiwei.puremusictest.bean.SongMvInfoBean;

import retrofit2.http.Query;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public interface MvInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void getMvInfo(String from, String version, String channel, int operator, String provider,String method,
                       String format, String mv_id, String song_id, String definition);

        void getMvCategory(String from, String version, String channel, int operator, String method, String format);

        void getSearchMv(String from, String version, String channel, int operator, String provider, String method, String format,
                    int order, int page_num, int page_size, String query);
    }

    interface View extends BaseContract.View {
        void onGetMvInfoSuccess(SongMvInfoBean bean);
        void onGetMvCategorySuccess(MvCategoryBean bean);
        void onSearchMvSuccess(MvSearchBean bean);
    }
}
