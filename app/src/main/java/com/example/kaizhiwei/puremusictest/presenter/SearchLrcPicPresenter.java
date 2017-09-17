package com.example.kaizhiwei.puremusictest.presenter;

import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.bean.SearchLrcPicBean;
import com.example.kaizhiwei.puremusictest.contract.SearchLrcPicContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/9/16.
 */

public class SearchLrcPicPresenter implements SearchLrcPicContract.Presenter {
    private SearchLrcPicContract.View mView;

    public SearchLrcPicPresenter(SearchLrcPicContract.View view){
        mView = view;
    }

    @Override
    public void searchLrcPic(String from, String version, String channel, int operator, String method, String format, String query, long ts, String e, int type,
                             final SearchLrcPicContract.View view) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<SearchLrcPicBean> observable = apiService.searchLrcPic(from, version, channel, operator, method, format, query, ts, e);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchLrcPicBean>() {
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
                    public void onNext(SearchLrcPicBean bean) {
                        if(view != null){
                            view.onSearchLrcPicSuccess(bean);
                        }
                        else{
                            if(mView != null){
                                mView.onSearchLrcPicSuccess(bean);
                            }
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
