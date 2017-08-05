package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.RecommMvBean;
import com.example.kaizhiwei.puremusictest.contract.RecommMvContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/8/5.
 */

public class RecommMvPresenter implements RecommMvContract.Presenter {
    private RecommMvContract.View mView;

    public RecommMvPresenter(RecommMvContract.View view){
        mView = view;
    }

    @Override
    public void getRecommMv(String from, String version, String channel, int operator, String method, String project, int preid, int mid, int size, int offset,
                            String param, String timestamp, String sign) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<RecommMvBean> observable = apiService.geRecommMv(from, version, channel, operator, method, project, preid,
                mid, size, offset, param, timestamp, sign);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommMvBean>() {
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
                    public void onNext(RecommMvBean bean) {
                        if(mView != null){
                            mView.onGetRecommMvSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
