package com.example.kaizhiwei.puremusictest.ui.favorite;

import android.os.Bundle;
import android.os.Message;
import android.widget.FrameLayout;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.MediaModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModuleProxy;
import com.example.kaizhiwei.puremusictest.ui.localmusic.BatchMgrAudioAdapter;
import com.example.kaizhiwei.puremusictest.ui.localmusic.BatchMgrFragment;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/9/17.
 */

public class BatchMgrFavoriteActivity extends MyBaseActivity implements BatchMgrFragment.OnBatchMgrListener {
    @Bind(R.id.flContent)
    FrameLayout flContent;
    private BatchMgrFragment fragment;
    public static final String INTENT_PLAYLIST_ID = "INTENT_PLAYLIST_ID";
    private long mPlaylistId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_batch_mgr_favorite;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        fragment = new BatchMgrFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.flContent, fragment).commit();
        fragment.setTitle("批量管理");
        fragment.setLeftViewText("添加到");
        fragment.setRightViewText("移除列表");
        fragment.enableSwapItems(true);
        fragment.setBatchMgrListener(this);
    }

    @Override
    public void initData() {
        mPlaylistId = getIntent().getLongExtra(INTENT_PLAYLIST_ID, -1);
        PlaylistModuleProxy.getInstance().asyncGetPlaylistMembers(mPlaylistId, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if(msg.what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<PlaylistMemberDao> list = (List<PlaylistMemberDao>)msg.obj;
                    List<BatchMgrAudioAdapter.BatchMgrAudioItemData> listAdapterData = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        BatchMgrAudioAdapter.BatchMgrAudioItemData itemData = new BatchMgrAudioAdapter.BatchMgrAudioItemData();
                        itemData.isSelected = false;
                        itemData.musicInfoDao = MediaModel.getInstance().getMusicInfoById(list.get(i).getMusic_id());
                        itemData.obj = list.get(i);
                        listAdapterData.add(itemData);
                    }
                    fragment.initData(listAdapterData);
                }
            }
        });
    }

    @Override
    public void onBackViewClicked() {
        finish();
    }

    @Override
    public void onLeftViewClicked() {

    }

    @Override
    public void onRightViewClicked() {
        int successNum = 0;
        List<Integer> listChecked = fragment.getCheckItems();
        for(int i = 0;i < listChecked.size();i++){
            BatchMgrAudioAdapter.BatchMgrAudioItemData itemData = fragment.getItemByPosition(listChecked.get(i));
            if(itemData == null || itemData.obj == null)
                continue;

            PlaylistMemberDao playlistMemberDao = (PlaylistMemberDao)itemData.obj;
            boolean bRet = PlaylistModel.getInstance().removePlaylistMember(mPlaylistId, playlistMemberDao.getMusic_id());
            if(bRet){
                successNum++;
            }
        }

        showToast("成功" + successNum + "个，失败" + (listChecked.size() - successNum) + "个");
        finish();
    }

    @Override
    public void onItemSwap(int src, int position) {

    }
}
