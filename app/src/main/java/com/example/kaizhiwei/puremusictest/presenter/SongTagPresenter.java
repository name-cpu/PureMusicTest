package com.example.kaizhiwei.puremusictest.presenter;

import android.os.Message;

import com.example.kaizhiwei.puremusictest.NetAudio.NetEngine;
import com.example.kaizhiwei.puremusictest.Util.BusinessCode;
import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.bean.AllTagInfoBean;
import com.example.kaizhiwei.puremusictest.bean.HotTagInfoBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.SongTagContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kaizhiwei on 17/7/15.
 */

public class SongTagPresenter implements SongTagContract.Presenter {
    private SongTagContract.View mView;

    public SongTagPresenter(SongTagContract.View view){
        mView = view;
    }

    @Override
    public void getAllTagInfo(String from, String version, String channel, int operator, String method, String format) {
        //此处服务器返回的数据格式是动态的，必须手动解析
        final Map<String, String> map = new HashMap<>();
        map.put("channel", channel);
        map.put("operator", operator +"");
        map.put("method", method);
        map.put("format", format);
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
                        AllTagInfoBean tagInfoBean = new AllTagInfoBean((String) msg.obj);
                        mView.onGetTagInfoSuccess(tagInfoBean);
                    }
                }
            }
        });
    }

    @Override
    public void getHotTagInfo(String from, String version, String channel, int operator, String method, String format, int nums) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<HotTagInfoBean> observable = apiService.getHotTagInfo(from, version, channel, operator, method, format, nums);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotTagInfoBean>() {
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
                    public void onNext(HotTagInfoBean bean) {
                        if(mView != null){
                            mView.onGetHotTagInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
