package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.UnStandardAdBean;
import com.example.kaizhiwei.puremusictest.contract.UnStandardContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClientBanFen;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 24820 on 2017/6/27.
 */
public class UnStandardAdPrensenter implements UnStandardContract.Presenter {
    private UnStandardContract.View mView;

    public UnStandardAdPrensenter(UnStandardContract.View view){
        mView = view;
    }

    @Override
    public void getUnStandardAd(int ad_pos_id, int width, int height, String from, String product, String version, String cuid, String channel, int operator) {
        ApiService apiService = RetrofitClientBanFen.getInstance().create(ApiService.class);
        Observable<UnStandardAdBean> observable = apiService.getUnStandardAd(ad_pos_id, width, height, from, product, version, cuid, channel, operator);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UnStandardAdBean>() {
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
                    public void onNext(UnStandardAdBean bean) {
                        if(mView != null){
                            mView.onGetUnStandardAdSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
