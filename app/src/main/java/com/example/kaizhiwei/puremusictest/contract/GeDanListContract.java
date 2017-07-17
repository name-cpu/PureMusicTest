package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumInfoBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanListBean;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public interface GeDanListContract {
    interface Presenter extends BaseContract.Presenter{
        void getGeDanList(String format, String from, String method,
                          int page_size, int page_no);
    }

    interface View extends BaseContract.View {
        void onGetGeDanListSuccess(GeDanListBean bean);
    }
}
