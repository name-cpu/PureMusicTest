package com.example.kaizhiwei.puremusictest.presenter;

import android.os.Message;

import com.example.kaizhiwei.puremusictest.NetAudio.NetEngine;
import com.example.kaizhiwei.puremusictest.Util.BusinessCode;
import com.example.kaizhiwei.puremusictest.api.ApiService;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.bean.ActiveIndexBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.bean.DiyGeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaIndexBean;
import com.example.kaizhiwei.puremusictest.bean.SceneCategoryListBean;
import com.example.kaizhiwei.puremusictest.bean.ShowRedPointBean;
import com.example.kaizhiwei.puremusictest.bean.SugSceneBean;
import com.example.kaizhiwei.puremusictest.bean.UgcdiyBaseInfoBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.ResetServerContract;
import com.example.kaizhiwei.puremusictest.domin.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 24820 on 2017/6/27.
 */
public class ResetServerPresenter implements ResetServerContract.Presenter {
    private ResetServerContract.View mView;

    public ResetServerPresenter(ResetServerContract.View view){
        mView = view;
    }

    @Override
    public void getCategoryList(String from, String version, String ppzs, int operator, String method) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<SceneCategoryListBean> observable = apiService.getCategoryList(from, version, ppzs, operator, method);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SceneCategoryListBean>() {
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
                    public void onNext(SceneCategoryListBean bean) {
                        if(mView != null){
                            mView.onGetCatogaryListSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getActiveIndex(String from, String version, String ppzs, int operator, String method) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<ActiveIndexBean> observable = apiService.getActiveIndex(from, version, ppzs, operator, method);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActiveIndexBean>() {
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
                    public void onNext(ActiveIndexBean bean) {
                        if(mView != null){
                            mView.onGetActiveIndexSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void showRedPoint(String from, String version, String ppzs, int operator, String method, String format) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<ShowRedPointBean> observable = apiService.showRedPoint(from, version, ppzs, operator, method, format);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShowRedPointBean>() {
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
                    public void onNext(ShowRedPointBean bean) {
                        if(mView != null){
                            mView.onShowRedPointSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getSugScene(String from, String version, String ppzs, int operator, String method) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<SugSceneBean> observable = apiService.getSugScene(from, version, ppzs, operator, method);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SugSceneBean>() {
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
                    public void onNext(SugSceneBean bean) {
                        if(mView != null){
                            mView.onGetSugSceneSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getPlazaIndex(String from, String version, String ppzs, int operator, String method, String cuid, int focu_num) {
        //此处服务器返回的数据格式是动态的，必须手动解析
        final Map<String, String> map = new HashMap<>();
        map.put("channel", ppzs);
        map.put("operator", operator +"");
        map.put("method", method);
        map.put("cuid", cuid);
        map.put("focu_num", focu_num+"");
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
                        PlazaIndexBean plazaIndexBean = new PlazaIndexBean();
                        plazaIndexBean.parser((String) msg.obj);
                        mView.onGetPlazaIndexSuccess(plazaIndexBean);
                    }
                }
            }
        });

//        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
//        Observable<PlazaIndexBean> observable = apiService.getPlazaIndex(from, version, ppzs, operator, method, cuid, focu_num);
//        Subscription subscriber = observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<PlazaIndexBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(PlazaIndexBean bean) {
//                        if(mView != null){
//                            mView.onGetPlazzaIndexSuccess(bean);
//                        }
//                    }
//                });
//        subscriptions.add(subscriber);
    }

    @Override
    public void getUgcdiyBaseInfo(String from, String version, String ppzs, int operator, String method, String param, String timestamp, String sign) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<UgcdiyBaseInfoBean> observable = apiService.getUgcdiyBaseInfo(from, version, ppzs, operator, method, param, timestamp, sign);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UgcdiyBaseInfoBean>() {
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
                    public void onNext(UgcdiyBaseInfoBean bean) {
                        if(mView != null){
                            mView.onGetUgcdiyBaseInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }

    @Override
    public void getDiyGeDanInfo(String format, String from, String method, int listid) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Observable<DiyGeDanInfoBean> observable = apiService.getDiyGeDanInfo(format, from, method, listid);
        Subscription subscriber = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DiyGeDanInfoBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.onError(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(DiyGeDanInfoBean bean) {
                        if (mView != null) {
                            mView.onGetDiyGeDanInfoSuccess(bean);
                        }
                    }
                });
        subscriptions.add(subscriber);
    }
}
