package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseBroadCastContant;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.MediaModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModuleProxy;
import com.example.kaizhiwei.puremusictest.service.PlaybackService;
import com.example.kaizhiwei.puremusictest.ui.favorite.FavoriteDialog;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;
import com.r0adkll.slidr.Slidr;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;

import static com.example.kaizhiwei.puremusictest.ui.favorite.FavoriteMemberFragment.PLAYLIST_DAO;


/**
 * Created by kaizhiwei on 17/1/4.
 */
public class BatchMgrAudioActivity extends MyBaseActivity implements PlaybackService.Client.Callback, View.OnClickListener, MyItemTouchHelper.ItemDragListener, MyItemTouchHelper.ItemMoveListener {
    @Bind(R.id.ckCheckAll)
    CheckBox ckCheckAll;
    @Bind(R.id.viewSepratorLine)
    View viewSepratorLine;
    @Bind(R.id.mtvAddTo)
    MyTextView mtvAddTo;
    @Bind(R.id.mtvDelete)
    MyTextView mtvDelete;
    @Bind(R.id.rlBtn)
    RelativeLayout rlBtn;
    @Bind(R.id.viewSepratorLine2)
    View viewSepratorLine2;
    @Bind(R.id.rvBatchMgr)
    RecyclerView rvBatchMgr;
    @Bind(R.id.titleView)
    CommonTitleView titleView;

    private ItemTouchHelper mItemTouchHelper;
    private BatchMgrAudioAdapter mAdapter;
    private BatchMgrAudioAdapter.IOnBatchMgrAudioListener mListener = new BatchMgrAudioAdapter.IOnBatchMgrAudioListener() {
        @Override
        public void onBatchMgrAudioItemCheck(BatchMgrAudioAdapter adapter, int position) {
            updateUI();
            List<Integer> listChecked = mAdapter.getCheckedItems();
            if (listChecked.size() == adapter.getItemCount()) {
                ckCheckAll.setChecked(true);
            } else {
                ckCheckAll.setChecked(false);
            }
        }
    };
    //删除文件对话框
    private AlertDialogDeleteOne mAlertDialogDeleteOne;
    private AlertDialogDeleteOne.IOnAlertDialogDeleteListener mAlertDialogDeleteListener = new AlertDialogDeleteOne.IOnAlertDialogDeleteListener() {
        @Override
        public void OnDeleteClick(AlertDialogDeleteOne dialog, boolean isDeleteFile) {
            if (dialog == null)
                return;

            List<MusicInfoDao> listMusicInfoDao = dialog.getMusicInfoDaoData();
            if (listMusicInfoDao == null || listMusicInfoDao.size() == 0)
                return;

            handleDeleteMedia(listMusicInfoDao, isDeleteFile);
        }
    };

    private boolean mRightBtnIsDelete = true;
    private boolean mRightBtnIsRemove = false;
    private PlaylistDao mPlaylistDao = null;
    private List<PlaylistMemberDao> mPlaylistMembers;
    private List<MusicInfoDao> mPlaylistMusicInfoDaos;
    private PlaybackService.Client mClient = null;
    private PlaybackService mService;
    private Handler mHandler = new Handler();

    public static final String INTENT_LIST_DATA = "INTENT_LIST_DATA";
    public static final String INTENT_RIGHTBTNISDELETE = "INTENT_RIGHTBTNISDELETE";     //显示删除按钮
    public static final String INTENT_RIGHTBTNISREMOVE = "INTENT_RIGHTBTNISREMOVE";     //显示移除收藏按钮
    public static final String INTENT_PLAYLISTDAO = "INTENT_PLAYLISTDAO";               //收藏夹ID号

    @Override
    public void onConnected(PlaybackService service) {
        mService = service;
    }

    @Override
    public void onDisconnected() {
        mService = null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_batchmgr_audio;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        titleView.setTitleViewInfo("", "批量管理", "");
        titleView.setTitleViewListener(new CommonTitleView.onTitleClickListener() {
            @Override
            public void onLeftBtnClicked() {
                finish();
            }

            @Override
            public void onRightBtnClicked() {

            }
        });
        ckCheckAll.setOnClickListener(this);
        ckCheckAll.setChecked(false);
        mtvAddTo.setOnClickListener(this);
        mtvDelete.setOnClickListener(this);
        mClient = new PlaybackService.Client(this, this);
        mtvDelete.setOnClickListener(this);
        mtvDelete.setBorderColor(getResources().getColor(R.color.delete_btn_color));
        mtvDelete.setNormalBackgroundColor(getResources().getColor(R.color.delete_btn_color));
        mtvDelete.setPressedBackgroundColor(getResources().getColor(R.color.delete_btn_pressed_color));
        Slidr.attach(this);
        rvBatchMgr.setLayoutManager(new LinearLayoutManager(this));
        rvBatchMgr.addItemDecoration(new RecyclerViewDividerDecoration(this, RecyclerViewDividerDecoration.HORIZONTAL_LIST));
    }

    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mRightBtnIsDelete = intent.getBooleanExtra(INTENT_RIGHTBTNISDELETE, true);
        mRightBtnIsRemove = intent.getBooleanExtra(INTENT_RIGHTBTNISREMOVE, false);
        mPlaylistDao = intent.getParcelableExtra(INTENT_PLAYLISTDAO);

        PlaylistModuleProxy.getInstance().asyncGetPlaylistMembers(mPlaylistDao.getList_id(), new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if(msg.what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<PlaylistMemberDao> list = (List<PlaylistMemberDao>)msg.obj;
                    mPlaylistMembers = new ArrayList<>();
                    mPlaylistMembers.addAll(list);

                    Collections.sort(mPlaylistMembers, new Comparator<PlaylistMemberDao>() {
                        @Override
                        public int compare(PlaylistMemberDao lhs, PlaylistMemberDao rhs) {
                            return lhs.getPlay_order() - rhs.getPlay_order();
                        }
                    });

                    List<BatchMgrAudioAdapter.BatchMgrAudioItemData> listAdapterData = new ArrayList<>();
                    for (int i = 0; i < mPlaylistMembers.size(); i++) {
                        BatchMgrAudioAdapter.BatchMgrAudioItemData itemData = new BatchMgrAudioAdapter.BatchMgrAudioItemData();
                        itemData.isSelected = false;
                        itemData.musicInfoDao = MediaModel.getInstance().getMusicInfoById(mPlaylistMembers.get(i).getMusic_id());
                        itemData.playlistMemberDao = mPlaylistMembers.get(i);
                        listAdapterData.add(itemData);
                    }
                    mAdapter = new BatchMgrAudioAdapter(BatchMgrAudioActivity.this, listAdapterData);
                    mAdapter.setBatchMgrAudioListener(mListener);
                    mAdapter.setItemDragListener(BatchMgrAudioActivity.this);
                    rvBatchMgr.setAdapter(mAdapter);
                    MyItemTouchHelper itemTouchCallback = new MyItemTouchHelper(BatchMgrAudioActivity.this);
                    mItemTouchHelper = new ItemTouchHelper(itemTouchCallback);
                    mItemTouchHelper.attachToRecyclerView(rvBatchMgr);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mAdapter == null)
            return;

        if (v == ckCheckAll) {
            mAdapter.setAllItemChecked(ckCheckAll.isChecked());
            updateUI();
            return;
        }

        List<Integer> listChecked = mAdapter.getCheckedItems();
        List<PlaylistMemberDao> listPlaylistMembers = new ArrayList<>();
        for(int i = 0;i < listChecked.size();i++){
            listPlaylistMembers.add((mAdapter.getItem(i)).playlistMemberDao);
        }
        if (listChecked.size() == 0) {
            Toast.makeText(this, "没有选择歌曲哦", Toast.LENGTH_SHORT).show();
            return;
        }

        if (v == mtvAddTo) {
            FavoriteDialog.Builder builderFavorite = new FavoriteDialog.Builder(this);
            FavoriteDialog dialogFavorite = (FavoriteDialog) builderFavorite.create();
            dialogFavorite.setCancelable(true);
            dialogFavorite.setKeyType(LocalBaseMediaLayout.LayoutType.CUSTOME);
            dialogFavorite.setPlaylistMembers(listPlaylistMembers);
            dialogFavorite.show();
            dialogFavorite.setTitle("添加到歌单");
        } else if (v == mtvDelete) {
            if (mRightBtnIsDelete) {
                //showDeleteAlterDialog(this, listChecked, "确定删除所选的" + listChecked.size() + "首歌曲吗?", true);
            }
            else if (mRightBtnIsRemove) {
                handleRemoveFromFavorite(listPlaylistMembers);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updateUI() {
        if (mAdapter == null)
            return;

        List<Integer> listChecked = mAdapter.getCheckedItems();
        String strAddTo = "";
        if (listChecked.size() == 0)
            strAddTo = "添加到";
        else
            strAddTo = String.format("添加到(%d)", listChecked.size());
        mtvAddTo.setText(strAddTo);

        String strDelete = "";
        if (mRightBtnIsDelete) {
            if (listChecked.size() == 0)
                strDelete = "删除";
            else
                strDelete = String.format("删除(%d)", listChecked.size());
        }
        if (mRightBtnIsRemove) {
            if (listChecked.size() == 0)
                strDelete = "移出列表";
            else
                strDelete = String.format("移出列表(%d)", listChecked.size());
        }
        mtvDelete.setText(strDelete);
    }

    public void showDeleteAlterDialog(Context context, List<MusicInfoDao> listOperMusicInfoDao, String strTitle, boolean isNeedReCreate) {
        if (isNeedReCreate) {
            mAlertDialogDeleteOne = new AlertDialogDeleteOne(context, mAlertDialogDeleteListener);
        }

        if (mAlertDialogDeleteOne == null) {
            mAlertDialogDeleteOne = new AlertDialogDeleteOne(context, mAlertDialogDeleteListener);
        }

        mAlertDialogDeleteOne.show();
        mAlertDialogDeleteOne.setMusicInfoDaoData(listOperMusicInfoDao);
        mAlertDialogDeleteOne.setTitle(strTitle);
    }

    private void handleRemoveFromFavorite(final List<PlaylistMemberDao> listMusicInfoDao) {
        if (listMusicInfoDao == null || mService == null)
            return;

        List<Long> list = new ArrayList<>();
        for(int i = 0;i < listMusicInfoDao.size();i++){
            list.add(listMusicInfoDao.get(i).getMusic_id());
        }
        int successNum = PlaylistModel.getInstance().removePlaylistMembers(mPlaylistDao.getList_id(), list);
        String strPromt = "";
        if (successNum == 0) {
            strPromt = "移除失败";
        } else if (successNum < listMusicInfoDao.size()) {
            strPromt = String.format("%d移除成功,%d移除失败", successNum, listMusicInfoDao.size() - successNum);
        } else {
            strPromt = "移除成功";
        }
        mPlaylistDao.setSong_count(mPlaylistDao.getSong_count() - successNum);
        PlaylistModel.getInstance().updatePlaylist(mPlaylistDao);
        Toast.makeText(BatchMgrAudioActivity.this, strPromt, Toast.LENGTH_SHORT).show();
    }

    private void handleDeleteMedia(final List<MusicInfoDao> listMusicInfoDao, final boolean isDeleteFile) {
        if (listMusicInfoDao == null || mService == null)
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {

                //是否包含正在播放的歌曲
                boolean isContainPlaying = false;
                MusicInfoDao curMusicInfoDao = null;
                curMusicInfoDao = mService.getCurrentMedia();
                int successNum = 0;
                for (int i = 0; i < listMusicInfoDao.size(); i++) {
                    MusicInfoDao entity = listMusicInfoDao.get(i);
                    if (entity == null || entity.get_id() < 0)
                        continue;

                    if (isDeleteFile) {
                        File file = new File(entity.get_data());
                        if (file.exists()) {
                            boolean bRet = file.delete();
                            if (bRet)
                                successNum++;
                        }
                    } else {
                        successNum++;
                    }

                    if (curMusicInfoDao != null) {
                        if (curMusicInfoDao.get_id() == entity.get_id()) {
                            isContainPlaying = true;
                        }
                    }
                }

                mService.mutilDeleteMediaByList(listMusicInfoDao);
                MediaLibrary.getInstance().mutilRemoveMusicInfoDao(listMusicInfoDao);

                final int finalSuccessNum = successNum;
                final boolean finalIsContainPlaying = isContainPlaying;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String strPromt = "";
                        if (finalSuccessNum == 0) {
                            strPromt = "删除失败";
                        } else if (finalSuccessNum < listMusicInfoDao.size()) {
                            strPromt = "删除部分成功,部分失败";
                        } else {
                            strPromt = "删除成功";
                        }
                        Toast.makeText(BatchMgrAudioActivity.this, strPromt, Toast.LENGTH_SHORT).show();

                        if (finalIsContainPlaying && mService != null) {
                            mService.reCalNextPlayIndex();
                        }
                        finish();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onStartDrags(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemMove(int from, int to) {
        mAdapter.itemMove(from, to);
        PlaylistModuleProxy.getInstance().asyncUpdatePlaylistMembers(mPlaylistDao.getList_id(), mPlaylistMembers, null);
    }
}
