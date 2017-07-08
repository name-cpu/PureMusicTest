package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistInfoBean;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetSongListContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/2.
 */

public class ArtistGetSongListPresenter implements ArtistGetSongListContract.Presenter {
    private ArtistGetSongListContract.View mView;

    public ArtistGetSongListPresenter(ArtistGetSongListContract.View view){
        mView = view;
    }

    @Override
    public void getSongList(String from, String version, String channel, int operator, String method, String format, String order, String tinguid, String artistid, int offset, int limits) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<ArtistGetSongListBean> observable = apiService.getSongList(from, version, channel, operator, method, format, order, tinguid, artistid, offset, limits);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistGetSongListBean>() {
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
                    public void onNext(ArtistGetSongListBean bean) {
                        if(mView != null){
                            mView.onGetSongListSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getArtistInfo(String from, String version, String channel, int operator, String method, String format, String tinguid, String artistid) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<ArtistInfoBean> observable = apiService.getArtistInfo(from, version, channel, operator, method, format, tinguid, artistid);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistInfoBean>() {
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
                    public void onNext(ArtistInfoBean bean) {
                        if(mView != null){
                            mView.onGetArtistInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getArtistAlbumList(String from, String version, String channel, int operator, String method, String format, String order, String tinguid, int offset, int limits) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<ArtistAlbumListBean> observable = apiService.getArtistAlbumList(from, version, channel, operator, method, format, order, tinguid, offset, limits);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistAlbumListBean>() {
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
                    public void onNext(ArtistAlbumListBean bean) {
                        if(mView != null){
                            mView.onGetArtistAlbumListSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
