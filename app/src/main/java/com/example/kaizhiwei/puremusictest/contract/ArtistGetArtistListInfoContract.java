package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;

/**
 * Created by kaizhiwei on 17/7/9.
 */

public interface ArtistGetArtistListInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void getArtistListInfo(String from, String version, String channel, String operator,
                               String method, String format, String offset, String limit, String order, String area, String sex);

        void getArtistListInfoWithFilter(String from, String version, String channel, String operator,
                                         String method, String format, String offset, String limit, String order, String area, String sex, String filter);
    }

    interface View extends BaseContract.View {
        void onGetArtistListInfoSuccess(ArtistGetListBean bean);
    }
}
