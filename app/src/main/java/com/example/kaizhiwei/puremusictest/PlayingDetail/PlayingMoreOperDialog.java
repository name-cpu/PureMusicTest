package com.example.kaizhiwei.puremusictest.PlayingDetail;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.Audio.FavoriteDialog;
import com.example.kaizhiwei.puremusictest.Audio.MoreOperationAdapter;
import com.example.kaizhiwei.puremusictest.CommonUI.AndroidShare;
import com.example.kaizhiwei.puremusictest.CommonUI.MyImageView;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.FastBlur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/1/4.
 */
public class PlayingMoreOperDialog extends Dialog implements AdapterView.OnItemClickListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private RelativeLayout rlContent;
    private MyImageView mivClose;
    private GridView gvMoreOper;
    private SeekBar sbVolume;
    private List<PlayingMoreOperAdapter.PlayingMoreOperItemData> mListData;
    private AudioManager mAudioManager;
    private MediaEntity mediaEntity;
    private int mLastVolume;
    private MyVolumeReceiver mVolumeReceiver;
    private static final int MORE_OPER_SEARCHLYRIC = 0;
    private static final int MORE_OPER_SHAREMEDIA = 1;
    private static final int MORE_OPER_ADDTO = 2;
    private static final int MORE_OPER_SETEQ = 3;
    private static final int MORE_OPER_FILEINFO = 4;
    private PlayingMoreOperAdapter mAdapter;
    private PlayingMoreOperAdapter.IPlayingMoreOperListener mMoreOperListener = new PlayingMoreOperAdapter.IPlayingMoreOperListener() {
        @Override
        public void onItemClicked(PlayingMoreOperAdapter adpter, int tag) {
            switch(tag){
                case MORE_OPER_SEARCHLYRIC:
                    break;
                case MORE_OPER_SHAREMEDIA:
                    if(mediaEntity == null)
                        return;

                    dismiss();
                    String strShareTitle = PlayingMoreOperDialog.this.getContext().getResources().getString(R.string.app_name);
                    strShareTitle = String.format("分享一首%s的%s - 来自%s", mediaEntity.artist, mediaEntity.title, strShareTitle);
                    AndroidShare as = new AndroidShare(
                            PlayingMoreOperDialog.this.getContext(),
                            strShareTitle,
                            "http://img6.cache.netease.com/cnews/news2012/img/logo_news.png");
                    as.show();
                    as.setTitle("分享 - " + mediaEntity.title);
                    break;
                case MORE_OPER_ADDTO:
                    dismiss();
                    FavoriteDialog.Builder builderFavorite = new FavoriteDialog.Builder(PlayingMoreOperDialog.this.getContext());
                    FavoriteDialog dialogFavorite = builderFavorite.create();
                    dialogFavorite.setCancelable(true);
                    dialogFavorite.setFavoritelistData(MediaLibrary.getInstance().getAllFavoriteEntity());
                    List<MediaEntity> list = new ArrayList<>();
                    if(mediaEntity != null){
                        list.add(mediaEntity);
                    }
                    dialogFavorite.setMediaEntityData(list);
                    dialogFavorite.show();
                    dialogFavorite.setTitle("添加到歌单");
                    break;
                case MORE_OPER_SETEQ:
                    break;
                case MORE_OPER_FILEINFO:
                    dismiss();
                    AlterDialogMediaInfo dialog = new AlterDialogMediaInfo(PlayingMoreOperDialog.this.getContext());
                    dialog.show();
                    dialog.setMediaEntity(mediaEntity);
                    break;
            }
        }
    };

    public PlayingMoreOperDialog(Context context, int themeResId) {
        super(context,R.style.Dialog);
    }

    public PlayingMoreOperDialog(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.playing_more_operation);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        Window window = this.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 可以在此设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;

        window.setAttributes(wl);

        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        rlContent = (RelativeLayout)this.findViewById(R.id.rlContent);
        rlContent.setOnClickListener(this);
        mivClose = (MyImageView)this.findViewById(R.id.mivClose);
        mivClose.setResId(R.drawable.bt_playpage_close_normal, R.drawable.bt_playpage_close_press);
        mivClose.setOnClickListener(this);
        gvMoreOper = (GridView)this.findViewById(R.id.gvMoreOper);
        gvMoreOper.setOnItemClickListener(this);
        sbVolume = (SeekBar)this.findViewById(R.id.sbVolume);
        sbVolume.setOnSeekBarChangeListener(this);
        initData();
        myRegisterReceiver();
        mAudioManager = (AudioManager)this.getContext().getSystemService(Context.AUDIO_SERVICE);
        sbVolume.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        sbVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        mLastVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.bg_gedancover);
        Drawable drawable = FastBlur.BoxBlurFilter(bitmap);
        rlContent.setBackground(drawable);
    }

    private void initData(){
        mListData = new ArrayList<>();
        PlayingMoreOperAdapter.PlayingMoreOperItemData itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_normal,
            R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", MORE_OPER_SEARCHLYRIC, true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_share_normal,
                R.drawable.bt_playpage_button_share_press, "歌曲分享", MORE_OPER_SHAREMEDIA, true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_addto_normal,
                R.drawable.bt_playpage_button_addto_press, "添加到", MORE_OPER_ADDTO, true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_eq_normal,
                R.drawable.bt_playpage_button_eq_press, "音效设置", MORE_OPER_SETEQ, true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_fileinformation_normal,
                R.drawable.bt_playpage_button_fileinformation_press, "文件详情", MORE_OPER_FILEINFO, true);
        mListData.add(itemData);
        mAdapter = new PlayingMoreOperAdapter(this.getContext(), mListData);
        mAdapter.setPlayingMoreOperListener(mMoreOperListener);
        gvMoreOper.setAdapter(mAdapter);
    }

    private void myRegisterReceiver(){
        mVolumeReceiver = new MyVolumeReceiver() ;
        IntentFilter filter = new IntentFilter() ;
        filter.addAction("android.media.VOLUME_CHANGED_ACTION") ;
        this.getContext().registerReceiver(mVolumeReceiver, filter) ;
    }

    /**
     * 处理音量变化时的界面显示
     * @author long
     */
    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果音量发生变化则更改seekbar的位置
            if(intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")){
                int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) ;// 当前的媒体音量
                sbVolume.setProgress(currVolume) ;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setCurrentMediaEntity(MediaEntity mediaEntity){
        this.mediaEntity = mediaEntity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        if(v == mivClose){
            dismiss();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            if(seekBar == null || mAudioManager == null)
                return;

            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND|AudioManager.FX_KEY_CLICK);//  3 代表  AudioManager.STREAM_MUSIC
        }
        mLastVolume = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mLastVolume = seekBar.getProgress();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int iInc = seekBar.getProgress() - mLastVolume;
        int index = iInc > 0 ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER;
        for(int i = 0;i < Math.abs(iInc);i++){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, AudioManager.FLAG_PLAY_SOUND);
        }
    }
}
