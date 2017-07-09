package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetAlbumInfoContract;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetArtistListInfoContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/9.
 */

public class ArtistGetArtistListInfoPresenter implements ArtistGetArtistListInfoContract.Presenter {
    private ArtistGetArtistListInfoContract.View mView;

    public ArtistGetArtistListInfoPresenter(ArtistGetArtistListInfoContract.View view){
        mView = view;
    }

    @Override
    public void getArtistListInfo(String from, String version, String channel, String operator,
                                  String method, String format, String offset, String limit, String order, String area, String sex) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<ArtistGetListBean> observable = apiService.getArtistListInfo(from, version, channel, operator, method, format, offset, limit, order, area, sex);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistGetListBean>() {
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
                    public void onNext(ArtistGetListBean bean) {
                        if(mView != null){
                            mView.onGetArtistListInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getArtistListInfoWithFilter(String from, String version, String channel, String operator, String method, String format, String offset, String limit, String order, String area, String sex, String filter) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<ArtistGetListBean> observable = apiService.getArtistListInfoWithFilter(from, version, channel, operator, method, format, offset, limit, order, area, sex, filter);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistGetListBean>() {
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
                    public void onNext(ArtistGetListBean bean) {
                        if(mView != null){
                            mView.onGetArtistListInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
