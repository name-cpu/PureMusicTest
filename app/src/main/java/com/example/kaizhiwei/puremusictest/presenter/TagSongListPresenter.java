package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.BaseSongInfoBean;
import com.example.kaizhiwei.puremusictest.bean.TagSongListBean;
import com.example.kaizhiwei.puremusictest.contract.TagSongListContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class TagSongListPresenter implements TagSongListContract.Presenter {
    private TagSongListContract.View mView;

    public TagSongListPresenter(TagSongListContract.View view){
        mView = view;
    }

    @Override
    public void getTagSongList(String from, String version, String channel, int operator, String method, String format, String tagname, int limit) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<TagSongListBean> observable = apiService.getTagSongList(from, version, channel, operator, method, format, tagname, limit);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TagSongListBean>() {
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
                    public void onNext(TagSongListBean bean) {
                        if(mView != null){
                            mView.onGetTagSongLiistSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getSongBaseInfo(String from, String version, String channel, int operator, String method, String format, String song_id) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<BaseSongInfoBean> observable = apiService.getSongBaseInfo(from, version, channel, operator, method, format, song_id);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseSongInfoBean>() {
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
                    public void onNext(BaseSongInfoBean bean) {
                        if(mView != null){
                            mView.onGetSongBaseInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
