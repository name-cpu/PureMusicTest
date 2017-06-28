package com.example.kaizhiwei.puremusictest.contract;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by 24820 on 2017/6/27.
 */
public interface BaseContract {
    interface Presenter{
        CompositeSubscription subscriptions = new CompositeSubscription();
    }

    interface View{
        void onError(String strErrMsg);
    }
}
