package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;

import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;

import java.util.List;

/**
 * Created by kaizhiwei on 17/9/10.
 */

public class PlaylistModuleProxy {
    private static PlaylistModuleProxy mInstance;
    private Context mContext;

    public static PlaylistModuleProxy getInstance(){
        if(mInstance == null){
            mInstance = new PlaylistModuleProxy();
        }

        return mInstance;
    }

    private PlaylistModuleProxy(){

    }

    public void asyncQueryPlaylistById(final long list_id, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                List<PlaylistDao> list = PlaylistModel.getInstance().queryPlaylistById(list_id);
                if(list ==null) {
                    if (handler != null) {
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();
                    }
                }
                else{
                    if(handler != null){
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                    }
                }
            }
        };
    }

    public void asyncGetPlaylists(final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                List<PlaylistDao> list = PlaylistModel.getInstance().getPlaylists();
                if(handler != null){
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                }
            }
        };
    }

    public void asyncGetPlaylistMembers(final long list_id, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                List<PlaylistMemberDao> list = PlaylistModel.getInstance().getPlaylistMembers(list_id);
                if(list ==null) {
                    if (handler != null) {
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();
                    }
                }
                else{
                    if(handler != null){
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                    }
                }
            }
        };
    }

    public void asyncUpdatePlaylistMembers(final long playlist_id, final List<PlaylistMemberDao> list, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                for(int i = 0;i < list.size();i++){
                    PlaylistModel.getInstance().updatePlaylistMember(playlist_id, list.get(i));
                }
                if (handler != null) {
                    handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();
                }
            }
        };
    }
}
