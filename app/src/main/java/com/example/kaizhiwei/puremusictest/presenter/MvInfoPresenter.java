package com.example.kaizhiwei.puremusictest.presenter;

import android.os.Message;

import com.example.kaizhiwei.puremusictest.NetAudio.NetEngine;
import com.example.kaizhiwei.puremusictest.Util.BusinessCode;
import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.bean.MvCategoryBean;
import com.example.kaizhiwei.puremusictest.bean.MvSearchBean;
import com.example.kaizhiwei.puremusictest.bean.PlayMvBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.MvInfoContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

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
        //此处服务器返回的数据格式是动态的，必须手动解析
        final Map<String, String> map = new HashMap<>();
        map.put("from", from);
        map.put("version", version);
        map.put("channel", channel);
        map.put("operator", operator + "");
        map.put("provider", provider);
        map.put("method", method);
        map.put("format", format);
        map.put("mv_id", mv_id);
        map.put("song_id", song_id +"");
        map.put("definition", definition);
        NetEngine.getInstance().netGet(PureMusicContant.HOST, PureMusicContant.URL, map, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if(msg.what == BusinessCode.BUSINESS_CODE_ERROR){
                    if(mView != null){
                        mView.onError("error");
                    }
                }
                else if(msg.what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    if(mView != null){
                        PlayMvBean playMvBean = new PlayMvBean();
                        playMvBean.parser((String) msg.obj);
                        mView.onGetMvInfoSuccess(playMvBean);
                    }
                }
            }
        });
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
