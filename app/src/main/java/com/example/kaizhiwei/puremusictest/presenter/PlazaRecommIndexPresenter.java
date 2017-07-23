package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.PlazaRecommIndexBean;
import com.example.kaizhiwei.puremusictest.contract.PlazaRecommIndexContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public class PlazaRecommIndexPresenter implements PlazaRecommIndexContract.Presenter {
    private PlazaRecommIndexContract.View mView;

    public PlazaRecommIndexPresenter(PlazaRecommIndexContract.View view){
        mView = view;
    }

    @Override
    public void getPlazaRecommIndex(String from, String version, String channel, int operator, String method, String project, int column_id) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<PlazaRecommIndexBean> observable = apiService.getPlazaRecommIndex(from, version, channel, operator, method, project, column_id);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PlazaRecommIndexBean>() {
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
                    public void onNext(PlazaRecommIndexBean bean) {
                        if(mView != null){
                            mView.onGetPlazaRecommIndexSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
