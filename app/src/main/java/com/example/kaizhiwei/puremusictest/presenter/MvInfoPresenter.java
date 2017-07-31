package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.MvCategoryBean;
import com.example.kaizhiwei.puremusictest.bean.MvSearchBean;
import com.example.kaizhiwei.puremusictest.bean.SongMvInfoBean;
import com.example.kaizhiwei.puremusictest.contract.MvInfoContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public class MvInfoPresenter implements MvInfoContract.Presenter {
    private MvInfoContract.View mView;

    public MvInfoPresenter(MvInfoContract.View view){
        mView = view;
    }

    @Override
    public void getMvInfo(String from, String version, String channel, int operator, String provider,String method, String format, String mv_id, String song_id, String definition) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<SongMvInfoBean> observable = apiService.getMvInfo(from, version, channel, operator, provider, method, format, mv_id, song_id, definition);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongMvInfoBean>() {
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
                    public void onNext(SongMvInfoBean bean) {
                        if(mView != null){
                            mView.onGetMvInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getMvCategory(String from, String version, String channel, int operator, String method, String format) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<MvCategoryBean> observable = apiService.getMvCategory(from, version, channel, operator, method, format);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MvCategoryBean>() {
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
                    public void onNext(MvCategoryBean bean) {
                        if(mView != null){
                            mView.onGetMvCategorySuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getSearchMv(String from, String version, String channel, int operator, String provider, String method, String format, int order, int page_num, int page_size, String query) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<MvSearchBean> observable = apiService.getSearchMv(from, version, channel, operator, provider, method, format, order, page_num, page_size, query);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MvSearchBean>() {
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
                    public void onNext(MvSearchBean bean) {
                        if(mView != null){
                            mView.onSearchMvSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
