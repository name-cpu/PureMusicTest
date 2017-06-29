package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.ActiveIndexBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.bean.DiyGeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaIndexBean;
import com.example.kaizhiwei.puremusictest.bean.SceneCategoryListBean;
import com.example.kaizhiwei.puremusictest.bean.ShowRedPointBean;
import com.example.kaizhiwei.puremusictest.bean.SugSceneBean;
import com.example.kaizhiwei.puremusictest.bean.UgcdiyBaseInfoBean;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 24820 on 2017/6/27.
 */
public interface ResetServerContract {
    interface Presenter extends BaseContract.Presenter{
        void getCategoryList(String from, String version, String ppzs, int operator, String method);

        void getActiveIndex(String from, String version, String ppzs, int operator, String method);

        void showRedPoint(String from, String version, String ppzs, int operator, String method, String format);

        void getSugScene(String from, String version, String ppzs, int operator, String method);

        void getPlazaIndex(String from, String version, String ppzs, int operator, String method, String cuid, int focu_num);

        void getUgcdiyBaseInfo(String from, String version, String ppzs, int operator, String method, String param, String timestamp, String sign);

        void getDiyGeDanInfo(String format,String from, String method, int listid);

        void getArtistListInfo(String from, String version, String channel, String operator,
                               String method, String format, String offset, String limit, String order, String area, String sex);
    }

    interface View extends BaseContract.View {
        void onGetCatogaryListSuccess(SceneCategoryListBean bean);

        void onGetActiveIndexSuccess(ActiveIndexBean bean);

        void onShowRedPointSuccess(ShowRedPointBean bean);

        void onGetSugSceneSuccess(SugSceneBean bean);

        void onGetPlazaIndexSuccess(PlazaIndexBean bean);

        void onGetUgcdiyBaseInfoSuccess(UgcdiyBaseInfoBean baseInfoBean);

        void onGetDiyGeDanInfoSuccess(DiyGeDanInfoBean bean);

        void onGetArtistListInfoSuccess(ArtistGetListBean bean);
    }
}
