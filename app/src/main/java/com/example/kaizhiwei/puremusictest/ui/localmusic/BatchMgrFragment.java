package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.service.PlaybackService;
import com.example.kaizhiwei.puremusictest.ui.favorite.FavoriteDialog;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by kaizhiwei on 17/1/4.
 */
public class BatchMgrFragment extends MyBaseFragment implements PlaybackService.Client.Callback, View.OnClickListener, MyItemTouchHelper.ItemDragListener, MyItemTouchHelper.ItemMoveListener {
    @Bind(R.id.titleView)
    CommonTitleView titleView;
    @Bind(R.id.ckCheckAll)
    CheckBox ckCheckAll;
    @Bind(R.id.viewSepratorLine)
    View viewSepratorLine;
    @Bind(R.id.viewSepratorLine2)
    View viewSepratorLine2;
    @Bind(R.id.rvBatchMgr)
    RecyclerView rvBatchMgr;
    @Bind(R.id.mtvLeft)
    MyTextView mtvLeft;
    @Bind(R.id.mtvRight)
    MyTextView mtvRight;
    @Bind(R.id.rlBtn)
    RelativeLayout rlBtn;

    private ItemTouchHelper mItemTouchHelper;
    private BatchMgrAudioAdapter mAdapter;
    private BatchMgrAudioAdapter.IOnBatchMgrAudioListener mAdapterListener = new BatchMgrAudioAdapter.IOnBatchMgrAudioListener() {
        @Override
        public void onItemChecked(BatchMgrAudioAdapter adapter, int position) {
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

    private PlaylistDao mPlaylistDao = null;
    private List<PlaylistMemberDao> mPlaylistMembers;
    private List<MusicInfoDao> mPlaylistMusicInfoDaos;
    private PlaybackService.Client mClient = null;
    private PlaybackService mService;
    private Handler mHandler = new Handler();

    private String mLeftViewText;
    private String mRightViewText;
    private boolean enableWrapItems;
    private OnBatchMgrListener mOutListener;
    public interface OnBatchMgrListener{
        void onBackViewClicked();
        void onLeftViewClicked();
        void onRightViewClicked();
        void onItemSwap(int src, int position);
    }

    @Override
    public void onConnected(PlaybackService service) {
        mService = service;
    }

    @Override
    public void onDisconnected() {
        mService = null;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_batchmgr_audio;
    }

    @Override
    public void initView() {
        titleView.setTitleViewInfo("", "批量管理", "");
        titleView.setTitleViewListener(new CommonTitleView.onTitleClickListener() {
            @Override
            public void onLeftBtnClicked() {
                BatchMgrFragment.this.getActivity().finish();
            }

            @Override
            public void onRightBtnClicked() {

            }
        });
        ckCheckAll.setOnClickListener(this);
        ckCheckAll.setChecked(false);
        mtvLeft.setOnClickListener(this);
        mClient = new PlaybackService.Client(this.getActivity(), this);
        mtvRight.setOnClickListener(this);
        mtvRight.setBorderColor(getResources().getColor(R.color.delete_btn_color));
        mtvRight.setNormalBackgroundColor(getResources().getColor(R.color.delete_btn_color));
        mtvRight.setPressedBackgroundColor(getResources().getColor(R.color.delete_btn_pressed_color));
        rvBatchMgr.setLayoutManager(new LinearLayoutManager(BatchMgrFragment.this.getActivity()));
        rvBatchMgr.addItemDecoration(new RecyclerViewDividerDecoration(BatchMgrFragment.this.getActivity(), RecyclerViewDividerDecoration.HORIZONTAL_LIST));
    }

    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    public void setLeftViewText(String string){
        if(mtvLeft != null){
            mtvLeft.setText(string);
        }
        mLeftViewText = string;
    }

    public void setRightViewText(String string){
        if(mtvRight != null){
            mtvRight.setText(string);
        }
        mRightViewText = string;
    }

    public void setTitle(String string){
        if(titleView != null){
            titleView.setTitleViewInfo("", string, "");
            titleView.setTitleViewListener(new CommonTitleView.onTitleClickListener() {
                @Override
                public void onLeftBtnClicked() {
                    if(mOutListener!= null){
                        mOutListener.onBackViewClicked();
                    }
                }

                @Override
                public void onRightBtnClicked() {

                }
            });
        }
    }

    public void setBatchMgrListener(OnBatchMgrListener listener){
        mOutListener = listener;
    }

    public void enableSwapItems(boolean enable){
        enableWrapItems = enable;
    }

//    public void initData(List<MusicInfoDao> list){
//        List<BatchMgrAudioAdapter.BatchMgrAudioItemData> listAdapterData = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            BatchMgrAudioAdapter.BatchMgrAudioItemData itemData = new BatchMgrAudioAdapter.BatchMgrAudioItemData();
//            itemData.isSelected = false;
//            itemData.musicInfoDao = list.get(i);
//            listAdapterData.add(itemData);
//        }
//
//        mAdapter = new BatchMgrAudioAdapter(BatchMgrFragment.this.getActivity(), listAdapterData);
//        mAdapter.setBatchMgrAudioListener(mAdapterListener);
//        mAdapter.setItemDragListener(BatchMgrFragment.this);
//        rvBatchMgr.setAdapter(mAdapter);
//        MyItemTouchHelper itemTouchCallback = new MyItemTouchHelper(BatchMgrFragment.this);
//        mItemTouchHelper = new ItemTouchHelper(itemTouchCallback);
//        mItemTouchHelper.attachToRecyclerView(rvBatchMgr);
//    }

    public void initData(List<BatchMgrAudioAdapter.BatchMgrAudioItemData> list){
        mAdapter = new BatchMgrAudioAdapter(BatchMgrFragment.this.getActivity(), list);
        mAdapter.setBatchMgrAudioListener(mAdapterListener);
        mAdapter.setItemDragListener(BatchMgrFragment.this);
        rvBatchMgr.setAdapter(mAdapter);
        MyItemTouchHelper itemTouchCallback = new MyItemTouchHelper(BatchMgrFragment.this);
        mItemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(rvBatchMgr);
    }

    public BatchMgrAudioAdapter.BatchMgrAudioItemData getItemByPosition(int position){
        if(mAdapter == null)
            return null;

        return mAdapter.getItem(position);
    }

    public List<Integer> getCheckItems(){
        if(mAdapter == null)
            return null;

        return mAdapter.getCheckedItems();
    }

    @Override
    public void initData() {




//        Bundle bundle = getArguments();
//        mRightBtnIsDelete = bundle.getBoolean(INTENT_RIGHTBTNISDELETE, true);
//        mRightBtnIsRemove = bundle.getBoolean(INTENT_RIGHTBTNISREMOVE, false);
//        mPlaylistDao = bundle.getParcelable(INTENT_PLAYLISTDAO);
//
//        PlaylistModuleProxy.getInstance().asyncGetPlaylistMembers(mPlaylistDao.getList_id(), new BaseHandler() {
//            @Override
//            public void handleBusiness(Message msg) {
//                if (msg.what == BusinessCode.BUSINESS_CODE_SUCCESS) {
//                    List<PlaylistMemberDao> list = (List<PlaylistMemberDao>) msg.obj;
//                    mPlaylistMembers = new ArrayList<>();
//                    mPlaylistMembers.addAll(list);
//
//                    Collections.sort(mPlaylistMembers, new Comparator<PlaylistMemberDao>() {
//                        @Override
//                        public int compare(PlaylistMemberDao lhs, PlaylistMemberDao rhs) {
//                            return lhs.getPlay_order() - rhs.getPlay_order();
//                        }
//                    });
//
//                    List<BatchMgrAudioAdapter.BatchMgrAudioItemData> listAdapterData = new ArrayList<>();
//                    for (int i = 0; i < mPlaylistMembers.size(); i++) {
//                        BatchMgrAudioAdapter.BatchMgrAudioItemData itemData = new BatchMgrAudioAdapter.BatchMgrAudioItemData();
//                        itemData.isSelected = false;
//                        itemData.musicInfoDao = MediaModel.getInstance().getMusicInfoById(mPlaylistMembers.get(i).getMusic_id());
//                        itemData.playlistMemberDao = mPlaylistMembers.get(i);
//                        listAdapterData.add(itemData);
//                    }
//                    mAdapter = new BatchMgrAudioAdapter(BatchMgrFragment.this.getActivity(), listAdapterData);
//                    mAdapter.setBatchMgrAudioListener(mListener);
//                    mAdapter.setItemDragListener(BatchMgrFragment.this);
//                    rvBatchMgr.setAdapter(mAdapter);
//                    MyItemTouchHelper itemTouchCallback = new MyItemTouchHelper(BatchMgrFragment.this);
//                    mItemTouchHelper = new ItemTouchHelper(itemTouchCallback);
//                    mItemTouchHelper.attachToRecyclerView(rvBatchMgr);
//                }
//            }
//        });
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
        for (int i = 0; i < listChecked.size(); i++) {
            listPlaylistMembers.add((mAdapter.getItem(i)).playlistMemberDao);
        }
        if (listChecked.size() == 0) {
            showToast("没有选择歌曲哦");
            return;
        }

        if (v == mtvLeft) {
            if(mOutListener != null){
                mOutListener.onLeftViewClicked();
            }

            FavoriteDialog.Builder builderFavorite = new FavoriteDialog.Builder(this.getActivity());
            FavoriteDialog dialogFavorite = (FavoriteDialog) builderFavorite.create();
            dialogFavorite.setCancelable(true);
            dialogFavorite.setKeyType(LocalBaseMediaLayout.LayoutType.CUSTOME);
            dialogFavorite.setPlaylistMembers(listPlaylistMembers);
            dialogFavorite.show();
            dialogFavorite.setTitle("添加到歌单");
        } else if (v == mtvRight) {
            if(mOutListener != null){
                mOutListener.onRightViewClicked();
            }
//
//            if (mRightBtnIsDelete) {
//                //showDeleteAlterDialog(this, listChecked, "确定删除所选的" + listChecked.size() + "首歌曲吗?", true);
//            } else if (mRightBtnIsRemove) {
//                handleRemoveFromFavorite(listPlaylistMembers);
//                this.getActivity().finish();
//            }
        }
    }

    private void updateUI() {
        if (mAdapter == null)
            return;

        List<Integer> listChecked = mAdapter.getCheckedItems();
        String strLeftText = "";
        if (listChecked.size() == 0)
            strLeftText = mLeftViewText;
        else
            strLeftText = String.format("%s(%d)", mLeftViewText, listChecked.size());
        mtvLeft.setText(strLeftText);

        String strRightText = "";
        if (listChecked.size() == 0)
            strRightText = mRightViewText;
        else
            strRightText = String.format("%s(%d)", mRightViewText, listChecked.size());
        mtvRight.setText(strRightText);
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
        for (int i = 0; i < listMusicInfoDao.size(); i++) {
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
        showToast(strPromt);
    }

    private void handleDeleteMedia(final List<MusicInfoDao> listMusicInfoDao, final boolean isDeleteFile) {
        if (listMusicInfoDao == null || mService == null)
            return;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                //是否包含正在播放的歌曲
//                boolean isContainPlaying = false;
//                MusicInfoDao curMusicInfoDao = null;
//                curMusicInfoDao = mService.getCurrentMedia();
//                int successNum = 0;
//                for (int i = 0; i < listMusicInfoDao.size(); i++) {
//                    MusicInfoDao entity = listMusicInfoDao.get(i);
//                    if (entity == null || entity.get_id() < 0)
//                        continue;
//
//                    if (isDeleteFile) {
//                        File file = new File(entity.get_data());
//                        if (file.exists()) {
//                            boolean bRet = file.delete();
//                            if (bRet)
//                                successNum++;
//                        }
//                    } else {
//                        successNum++;
//                    }
//
//                    if (curMusicInfoDao != null) {
//                        if (curMusicInfoDao.get_id() == entity.get_id()) {
//                            isContainPlaying = true;
//                        }
//                    }
//                }
//
//                mService.mutilDeleteMediaByList(listMusicInfoDao);
//                MediaLibrary.getInstance().mutilRemoveMusicInfoDao(listMusicInfoDao);
//
//                final int finalSuccessNum = successNum;
//                final boolean finalIsContainPlaying = isContainPlaying;
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        String strPromt = "";
//                        if (finalSuccessNum == 0) {
//                            strPromt = "删除失败";
//                        } else if (finalSuccessNum < listMusicInfoDao.size()) {
//                            strPromt = "删除部分成功,部分失败";
//                        } else {
//                            strPromt = "删除成功";
//                        }
//                        showToast(strPromt);
//
//                        if (finalIsContainPlaying && mService != null) {
//                            mService.reCalNextPlayIndex();
//                        }
//                        BatchMgrFragment.this.getActivity().finish();
//                    }
//                });
//            }
//        }).start();
    }

    @Override
    public void onStartDrags(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemMove(int from, int to) {
        if(mOutListener != null){
            mOutListener.onItemSwap(from, to);
        }
        mAdapter.itemMove(from, to);
        //PlaylistModuleProxy.getInstance().asyncUpdatePlaylistMembers(mPlaylistDao.getList_id(), mPlaylistMembers, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
