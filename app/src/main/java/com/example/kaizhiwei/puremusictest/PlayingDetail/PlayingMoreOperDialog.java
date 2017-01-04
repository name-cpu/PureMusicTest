package com.example.kaizhiwei.puremusictest.PlayingDetail;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
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

import com.example.kaizhiwei.puremusictest.Audio.MoreOperationAdapter;
import com.example.kaizhiwei.puremusictest.CommonUI.MyImageView;
import com.example.kaizhiwei.puremusictest.R;

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
    private PlayingMoreOperAdapter mAdapter;
    private List<PlayingMoreOperAdapter.PlayingMoreOperItemData> mListData;
    private AudioManager mAudioManager;
    private int mLastVolume;
    private MyVolumeReceiver mVolumeReceiver;

    public PlayingMoreOperDialog(Context context, int themeResId) {
        super(context,R.style.Dialog);
    }

    public PlayingMoreOperDialog(Context context) {
        super(context);
    }

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
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
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
    }

    private void initData(){
        mListData = new ArrayList<>();
        PlayingMoreOperAdapter.PlayingMoreOperItemData itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_press_black,
            R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_press_black,
                R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_press_black,
                R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_press_black,
                R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_press_black,
                R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_press_black,
                R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_press_black,
                R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", true);
        mListData.add(itemData);
        itemData = new PlayingMoreOperAdapter.PlayingMoreOperItemData(R.drawable.bt_playpage_button_searchpicandlyric_press_black,
                R.drawable.bt_playpage_button_searchpicandlyric_press, "查找歌曲", true);
        mListData.add(itemData);
        mAdapter = new PlayingMoreOperAdapter(this.getContext(), mListData);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            if(seekBar == null || mAudioManager == null)
                return;

            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);//  3 代表  AudioManager.STREAM_MUSIC


        }
        mLastVolume = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mLastVolume = seekBar.getProgress();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {


        sbVolume.setOnSeekBarChangeListener(null);
        int iInc = seekBar.getProgress() - mLastVolume;
        int index = iInc > 0 ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER;
        for(int i = 0;i < Math.abs(iInc);i++){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, AudioManager.FLAG_PLAY_SOUND);
        }


    }
}
