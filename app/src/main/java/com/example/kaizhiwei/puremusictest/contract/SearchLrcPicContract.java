package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.SearchLrcPicBean;

/**
 * Created by kaizhiwei on 17/9/16.
 */

public interface SearchLrcPicContract {
    interface Presenter extends BaseContract.Presenter{
        void searchLrcPic(String from, String version, String channel,
                          int operator, String method, String format, String query,
                          long ts, String e, int type, View view);
    }

    interface View extends BaseContract.View {
        void onSearchLrcPicSuccess(SearchLrcPicBean bean);
    }
}
