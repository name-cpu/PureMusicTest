package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
import com.example.kaizhiwei.puremusictest.dao.RecentPlayDao;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import java.util.List;

/**
 * Created by kaizhiwei on 17/9/11.
 */
public class RecentPlayProxy {
    private static RecentPlayProxy mInstance;
    private Context mContext;

    public static RecentPlayProxy getInstance(){
        if(mInstance == null){
            mInstance = new RecentPlayProxy();
        }

        return mInstance;
    }

    private RecentPlayProxy(){

    }

    public void asyncAddRecentPlay(final RecentPlayDao recentPlayDao, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                RecentPlayModel.getInstance().addRecentPlay(recentPlayDao);
                if(handler != null){
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS).sendToTarget();
                }
            }
        };
    }

    public void asyncRemoveRecentPlay(final RecentPlayDao recentPlayDao, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                boolean bret = RecentPlayModel.getInstance().removeRecentPlay(recentPlayDao);
                if(handler != null){
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, bret).sendToTarget();
                }
            }
        };
    }

    public void asyncUpdateRecentPlay(final RecentPlayDao recentPlayDao, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                boolean bret = RecentPlayModel.getInstance().updateRecentPlay(recentPlayDao);
                if(handler != null){
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, bret).sendToTarget();
                }
            }
        };
    }

    public void asyncGetAllRecentPlays(final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                List<RecentPlayDao> list = RecentPlayModel.getInstance().getAllRecentPlays();
                if(handler != null){
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                }
            }
        };
    }
}
