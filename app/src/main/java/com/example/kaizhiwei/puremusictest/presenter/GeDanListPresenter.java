package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanListBean;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetSongListContract;
import com.example.kaizhiwei.puremusictest.contract.GeDanListContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class GeDanListPresenter implements GeDanListContract.Presenter {
    private GeDanListContract.View mView;

    public GeDanListPresenter(GeDanListContract.View view){
        mView = view;
    }

    @Override
    public void getGeDanList(String format, String from, String method, int page_size, int page_no) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<GeDanListBean> observable = apiService.getGeDanList(format, from, method, page_size, page_no);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GeDanListBean>() {
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
                    public void onNext(GeDanListBean bean) {
                        if(mView != null){
                            mView.onGetGeDanListSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
