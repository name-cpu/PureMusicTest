package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.SongDetailInfoBean;
import com.example.kaizhiwei.puremusictest.contract.SongDetailInfoContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/9.
 */

public class SongDetailInfoPresenter implements SongDetailInfoContract.Presenter {
    private SongDetailInfoContract.View mView;

    public SongDetailInfoPresenter(SongDetailInfoContract.View view){
        mView = view;
    }

    @Override
    public void getSongDetailInfo(String from, String version, String channel, int operator, String method, String format, String songId, long ts, String e, int nw, int ucf, int res, int l2p, String lpb, int usup, int lebo) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<SongDetailInfoBean> observable = apiService.getSongInfo(from, version, channel, operator, method, format, songId, ts, e, nw, ucf, res, l2p, lpb, usup, lebo);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongDetailInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(mView != null){
                            mView.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(SongDetailInfoBean bean) {
                        if(mView != null){
                            mView.onGetSongDetailInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
