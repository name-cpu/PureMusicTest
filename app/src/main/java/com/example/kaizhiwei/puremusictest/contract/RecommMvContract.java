package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.PlazaRecommIndexBean;
import com.example.kaizhiwei.puremusictest.bean.RecommMvBean;

import retrofit2.http.Query;

/**
 * Created by kaizhiwei on 17/8/5.
 */

public interface RecommMvContract {
    interface Presenter extends BaseContract.Presenter{
        void getRecommMv(String from, String version, String channel, int operator, String method,
                         String project, int preid, int mid, int size, int offset, String param, String timestamp, String sign);
    }

    interface View extends BaseContract.View {
        void onGetRecommMvSuccess(RecommMvBean bean);
    }
}
