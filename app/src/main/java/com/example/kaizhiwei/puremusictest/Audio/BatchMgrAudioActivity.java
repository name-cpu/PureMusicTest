package com.example.kaizhiwei.puremusictest.Audio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Service.PlaybackService;
import com.r0adkll.slidr.Slidr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by kaizhiwei on 17/1/4.
 */
public class BatchMgrAudioActivity extends BaseActivty implements PlaybackService.Client.Callback {
    private MyTextView mtvAddTo;
    private MyTextView mtvDelete;
    private ListView lvBatchMgr;
    private CheckBox ckCheckAll;
    private BatchMgrAudioAdapter mAdapter;
    private BatchMgrAudioAdapter.IOnBatchMgrAudioListener mListener = new BatchMgrAudioAdapter.IOnBatchMgrAudioListener() {
        @Override
        public void onBatchMgrAudioItemCheck(BatchMgrAudioAdapter adapter, int position) {
            updateUI();
            List<Integer> listChecked = mAdapter.getCheckedItems();
            if(listChecked.size() == adapter.getCount()){
                ckCheckAll.setChecked(true);
            }
            else{
                ckCheckAll.setChecked(false);
            }
        }
    };
    //删除文件对话框
    private AlertDialogDeleteOne mAlertDialogDeleteOne;
    private AlertDialogDeleteOne.IOnAlertDialogDeleteListener mAlertDialogDeleteListener = new AlertDialogDeleteOne.IOnAlertDialogDeleteListener() {
        @Override
        public void OnDeleteClick(AlertDialogDeleteOne dialog, boolean isDeleteFile) {
            if(dialog == null)
                return;

            List<MediaEntity> listMediaEntity = dialog.getMediaEntityData();
            if(listMediaEntity == null || listMediaEntity.size() == 0)
                return;

            handleDeleteMedia(listMediaEntity, isDeleteFile);
        }
    };

    private boolean mRightBtnIsDelete = true;
    private boolean mRightBtnIsRemove = false;
    private long mFavoriteId = -1;
    private PlaybackService.Client mClient = null;
    private PlaybackService mService;
    private Handler mHandler = new Handler();

    public static final String INTENT_LIST_DATA = "INTENT_LIST_DATA";
    public static final String INTENT_RIGHTBTNISDELETE = "INTENT_RIGHTBTNISDELETE";     //显示删除按钮
    public static final String INTENT_RIGHTBTNISREMOVE = "INTENT_RIGHTBTNISREMOVE";     //显示移除收藏按钮
    public static final String INTENT_FAVORITEID = "INTENT_FAVORITEID";                 //收藏夹ID号

    @Override
    public void onConnected(PlaybackService service) {
        mService = service;
    }

    @Override
    public void onDisconnected() {
        mService = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batchmgr_audio);
        setTitle("批量管理");

        lvBatchMgr = (ListView)this.findViewById(R.id.lvBatchMgr);
        mtvAddTo = (MyTextView)this.findViewById(R.id.mtvAddTo);
        mtvDelete = (MyTextView)this.findViewById(R.id.mtvDelete);
        ckCheckAll = (CheckBox)this.findViewById(R.id.ckCheckAll);
        ckCheckAll.setOnClickListener(this);
        ckCheckAll.setChecked(false);
        mtvAddTo.setOnClickListener(this);
        mtvDelete.setOnClickListener(this);
        mtvDelete.setBorderColor(getResources().getColor(R.color.delete_btn_color));
        mtvDelete.setNormalBackgroundColor(getResources().getColor(R.color.delete_btn_color));
        mtvDelete.setPressedBackgroundColor(getResources().getColor(R.color.delete_btn_pressed_color));
        initData();
        mClient = new PlaybackService.Client(this, this);
        Slidr.attach(this);
    }

    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    private void initData(){
        Intent intent = getIntent();
        List<MediaEntity> list = (ArrayList<MediaEntity>)intent.getSerializableExtra(INTENT_LIST_DATA);
        mRightBtnIsDelete = intent.getBooleanExtra(INTENT_RIGHTBTNISDELETE, true);
        mRightBtnIsRemove = intent.getBooleanExtra(INTENT_RIGHTBTNISREMOVE, false);
        mFavoriteId = intent.getLongExtra(INTENT_FAVORITEID, -1);

        List<BatchMgrAudioAdapter.BatchMgrAudioItemData> listAdapterData = new ArrayList<>();
        List<MediaEntity> listMediaEntity = new ArrayList<>();
        if(list == null){
            listMediaEntity = MediaLibrary.getInstance().getAllMediaEntrty();
        }
        else{
            listMediaEntity.addAll(list);
        }
        for(int i = 0;i < listMediaEntity.size();i++){
            BatchMgrAudioAdapter.BatchMgrAudioItemData itemData = new BatchMgrAudioAdapter.BatchMgrAudioItemData();
            itemData.isSelected = false;
            itemData.mediaEntity = listMediaEntity.get(i);
            listAdapterData.add(itemData);
        }

        mAdapter = new BatchMgrAudioAdapter(this, listAdapterData);
        mAdapter.setBatchMgrAudioListener(mListener);
        lvBatchMgr.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v == tvTitle){
            super.onClick(v);
            return;
        }

        if(mAdapter == null)
            return;

        if(v == ckCheckAll){
            mAdapter.setAllItemChecked(ckCheckAll.isChecked());
            updateUI();
            return;
        }

        List<MediaEntity> listChecked = mAdapter.getCheckedMediaEntity();
        if(listChecked.size() == 0){
            Toast.makeText(this, "没有选择歌曲哦",Toast.LENGTH_SHORT).show();
            return;
        }

        if(v == mtvAddTo){
            FavoriteDialog.Builder builderFavorite = new FavoriteDialog.Builder(this);
            FavoriteDialog dialogFavorite = builderFavorite.create();
            dialogFavorite.setCancelable(true);
            dialogFavorite.setFavoritelistData(MediaLibrary.getInstance().getAllFavoriteEntity());
            dialogFavorite.setMediaEntityData(listChecked);
            dialogFavorite.show();
            dialogFavorite.setTitle("添加到歌单");
        }
        else if(v == mtvDelete){
            if(mRightBtnIsDelete){
                showDeleteAlterDialog(this, listChecked,"确定删除所选的" + listChecked.size() + "首歌曲吗?", true);
            }
            if(mRightBtnIsRemove){
                handleRemoveFromFavorite(listChecked);
                finish();
            }
        }
    }

    private void updateUI(){
        if(mAdapter == null)
            return;

        List<Integer> listChecked = mAdapter.getCheckedItems();
        String strAddTo = "";
        if(listChecked.size() == 0)
            strAddTo = "添加到";
        else
            strAddTo = String.format("添加到(%d)", listChecked.size());
        mtvAddTo.setText(strAddTo);

        String strDelete = "";
        if(mRightBtnIsDelete){
            if(listChecked.size() == 0)
                strDelete = "删除";
            else
                strDelete = String.format("删除(%d)", listChecked.size());
        }
        if(mRightBtnIsRemove){
            if(listChecked.size() == 0)
                strDelete = "移出列表";
            else
                strDelete = String.format("移出列表(%d)", listChecked.size());
        }
        mtvDelete.setText(strDelete);
    }

    public List<MediaEntity> getCheckedMediaEntity(){
        if(mAdapter == null)
            return null;

        return mAdapter.getCheckedMediaEntity();
    }

    public void showDeleteAlterDialog(Context context, List<MediaEntity> listOperMediaEntity, String strTitle, boolean isNeedReCreate){
        if(isNeedReCreate){
            mAlertDialogDeleteOne = new AlertDialogDeleteOne(context, mAlertDialogDeleteListener);
        }

        if(mAlertDialogDeleteOne == null){
            mAlertDialogDeleteOne =  new AlertDialogDeleteOne(context, mAlertDialogDeleteListener);
        }

        mAlertDialogDeleteOne.show();
        mAlertDialogDeleteOne.setMediaEntityData(listOperMediaEntity);
        mAlertDialogDeleteOne.setTitle(strTitle);
    }

    private void handleRemoveFromFavorite(final List<MediaEntity> listMediaEntity){
        if(listMediaEntity == null || mService == null)
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                int successNum = 0;
                boolean bRet = false;
                for(int i = 0;i < listMediaEntity.size();i++){
                    bRet = MediaLibrary.getInstance().removeFavoriteMusicEntity(listMediaEntity.get(i)._id, mFavoriteId);
                    if(bRet){
                        successNum++;
                    }
                }

                final int finalSuccessNum = successNum;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String strPromt = "";
                        if(finalSuccessNum == 0){
                            strPromt = "移除失败";
                        }
                        else if(finalSuccessNum < listMediaEntity.size()){
                            strPromt = String.format("%d移除成功,%d移除失败", finalSuccessNum, listMediaEntity.size()- finalSuccessNum);
                        }
                        else{
                            strPromt = "移除成功";
                        }
                        Toast.makeText(BatchMgrAudioActivity.this, strPromt, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void handleDeleteMedia(final List<MediaEntity> listMediaEntity, final boolean isDeleteFile){
        if(listMediaEntity == null || mService == null)
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {

                //是否包含正在播放的歌曲
                boolean isContainPlaying = false;
                MediaEntity curMediaEntity = null;
                curMediaEntity = mService.getCurrentMedia();
                int successNum = 0;
                for(int i = 0;i < listMediaEntity.size();i++){
                    MediaEntity entity = listMediaEntity.get(i);
                    if(entity == null || entity._id < 0)
                        continue;

                    if(isDeleteFile){
                        File file = new File(entity._data);
                        if(file.exists()){
                            boolean bRet = file.delete();
                            if(bRet)
                                successNum++;
                        }
                    }
                    else{
                        successNum++;
                    }

                    if(curMediaEntity != null){
                        if(curMediaEntity._id == entity._id){
                            isContainPlaying = true;
                        }
                    }
                }

                mService.mutilDeleteMediaByList(listMediaEntity);
                MediaLibrary.getInstance().mutilRemoveMediaEntity(listMediaEntity);

                final int finalSuccessNum = successNum;
                final boolean finalIsContainPlaying = isContainPlaying;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String strPromt = "";
                        if(finalSuccessNum == 0){
                            strPromt = "删除失败";
                        }
                        else if(finalSuccessNum < listMediaEntity.size()){
                            strPromt = "删除部分成功,部分失败";
                        }
                        else{
                            strPromt = "删除成功";
                        }
                        Toast.makeText(BatchMgrAudioActivity.this, strPromt, Toast.LENGTH_SHORT).show();

                        if(finalIsContainPlaying && mService != null){
                            mService.reCalNextPlayIndex();
                        }
                        finish();
                    }
                });
            }
        }).start();
    }
}
