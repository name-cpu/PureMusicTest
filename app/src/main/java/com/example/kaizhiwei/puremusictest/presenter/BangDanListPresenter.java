package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.BangDanListBean;
import com.example.kaizhiwei.puremusictest.contract.BangDanListContract;
import com.example.kaizhiwei.puremusictest.contract.GeDanListContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public class BangDanListPresenter implements BangDanListContract.Presenter {
    private BangDanListContract.View mView;

    public BangDanListPresenter(BangDanListContract.View view){
        mView = view;
    }

    @Override
    public void getBangDanList(String format, String from, String method, int kflag) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<BangDanListBean> observable = apiService.getBangDanList(format, from, method, kflag);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BangDanListBean>() {
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
                    public void onNext(BangDanListBean bean) {
                        if(mView != null){
                            mView.onGetBangDanListSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
