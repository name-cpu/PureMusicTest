package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.BangDanListBean;
import com.example.kaizhiwei.puremusictest.bean.BangDanSongDetailInfo;
import com.example.kaizhiwei.puremusictest.contract.BangDanInfoContract;
import com.example.kaizhiwei.puremusictest.contract.BangDanListContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public class BangDanInfoPresenter implements BangDanInfoContract.Presenter {
    private BangDanInfoContract.View mView;

    public BangDanInfoPresenter(BangDanInfoContract.View view){
        mView = view;
    }

    @Override
    public void getBangDanInfo(String format, String from, String method, int type, int offset, int size, String fields) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<BangDanSongDetailInfo> observable = apiService.getBangdanSongDetail(format, from, method, type, offset, size, fields);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BangDanSongDetailInfo>() {
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
                    public void onNext(BangDanSongDetailInfo bean) {
                        if(mView != null){
                            mView.onGetBangDanInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getBangDanSongDetail(String from, String version, String format, String method, String songid) {

    }
}
