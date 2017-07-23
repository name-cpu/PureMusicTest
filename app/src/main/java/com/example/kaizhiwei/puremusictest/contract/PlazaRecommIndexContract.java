package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.GeDanListBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaRecommIndexBean;

import retrofit2.http.Query;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public interface PlazaRecommIndexContract {
    interface Presenter extends BaseContract.Presenter{
        void getPlazaRecommIndex(String from, String version, String channel, int operator,
                                  String method, String project, int column_id);
    }

    interface View extends BaseContract.View {
        void onGetPlazaRecommIndexSuccess(PlazaRecommIndexBean bean);
    }
}
