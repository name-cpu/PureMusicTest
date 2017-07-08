package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumInfoBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetAlbumInfoContract;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetSongListContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/8.
 */

public class ArtistGetAlbumInfoPresenter implements ArtistGetAlbumInfoContract.Presenter {
    private ArtistGetAlbumInfoContract.View mView;

    public ArtistGetAlbumInfoPresenter(ArtistGetAlbumInfoContract.View view){
        mView = view;
    }

    @Override
    public void getArtistAlbumInfo(String from, String version, String channel, int operator, String method, String format, String album_id) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<ArtistAlbumInfoBean> observable = apiService.getArtistAlbumInfo(from, version, channel, operator, method, format, album_id);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistAlbumInfoBean>() {
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
                    public void onNext(ArtistAlbumInfoBean bean) {
                        if(mView != null){
                            mView.onGetAlbumInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
