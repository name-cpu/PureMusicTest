package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.BangDanListBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanListBean;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public interface BangDanListContract {
    interface Presenter extends BaseContract.Presenter{
        void getBangDanList(String format, String from, String method,
                          int kflag);
    }

    interface View extends BaseContract.View {
        void onGetBangDanListSuccess(BangDanListBean bean);
    }
}
