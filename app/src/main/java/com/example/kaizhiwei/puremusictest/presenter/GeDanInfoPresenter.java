package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.GeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanSongDetailInfo;
import com.example.kaizhiwei.puremusictest.contract.GeDanInfoContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/19.
 */

public class GeDanInfoPresenter implements GeDanInfoContract.Presenter {
    private GeDanInfoContract.View mView;

    public GeDanInfoPresenter(GeDanInfoContract.View view){
        mView = view;
    }

    @Override
    public void getGeDanInfo(String format, String from, String method, String listid) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<GeDanInfoBean> observable = apiService.getGeDanInfo(format, from, method, listid);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GeDanInfoBean>() {
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
                    public void onNext(GeDanInfoBean bean) {
                        if(mView != null){
                            mView.onGetGeDanInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getGeDanSongDetail(String from, String version, String format, String method, String songid) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<GeDanSongDetailInfo> observable = apiService.getGeDanSongDetail(format, version, from, method, songid);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GeDanSongDetailInfo>() {
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
                    public void onNext(GeDanSongDetailInfo bean) {
                        if(mView != null){
                            mView.onGetGeDanSongDetailInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
