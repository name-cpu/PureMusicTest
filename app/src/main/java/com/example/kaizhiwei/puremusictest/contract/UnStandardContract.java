package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.UnStandardAdBean;

/**
 * Created by 24820 on 2017/6/27.
 */
public interface UnStandardContract {
    interface Presenter extends BaseContract.Presenter{
        void getUnStandardAd(int ad_pos_id,
                             int width,
                             int height,
                             String from,
                             String product,
                             String version,
                             String cuid,
                             String channel,
                             int operator);
    }

    interface View extends BaseContract.View {
        void onGetUnStandardAdSuccess(UnStandardAdBean bean);
    }
}
