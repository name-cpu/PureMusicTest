package com.example.kaizhiwei.puremusictest.Audio;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.PlayingDetail.PlayingActivity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Service.PlaybackService;

import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/20.
 */
public class NowPlayingLayout extends LinearLayout implements View.OnClickListener, PlaybackService.Client.Callback, PlaybackService.Callback {
    private ImageButton mBtnPlayPause;
    private ImageButton mBtnPlayNext;
    private ImageButton mBtnPlaylist;
    private TextView   mtvMain;
    private TextView   mtvSub;
    private ImageView  mImArtist;
    private ProgressBar mPlayProgress;
    private LinearLayout llControl;
    private WeakReference<MediaEntity>  mWeakMediaEntity;
    private boolean mIsPlaying;
    private HomeActivity mHomePage;
    private PlaybackService.Client mClient = new PlaybackService.Client(this.getContext(), this);
    private PlaybackService mService;
    private PlayListDialog mPlayListDialog;
    private PlayListDialog.IPlayListDialogListener mListener = new PlayListDialog.IPlayListDialogListener() {
        @Override
        public void onDeleteClick(PlayListViewAdapter adapter, int position) {
            if(adapter == null || position < 0)
                return;

            if(mService == null)
                return;

            PlayListViewAdapter.PlayListItemData itemData = (PlayListViewAdapter.PlayListItemData)adapter.getItem(position);
            if(itemData == null || itemData.mediaEntity == null)
                return;

            MediaEntity curPlayMedia = mService.getCurrentMedia();
            mPlayListDialog.removeItem(position);
            mService.deleteMediaById(itemData.mediaEntity._id);
            if(curPlayMedia != null){
                if(curPlayMedia._id == itemData.mediaEntity._id){
                    mService.next(false);
                }
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(parent == null || view == null || position < 0 || mService == null)
                return;

            PlayListViewAdapter adapter = (PlayListViewAdapter)parent.getAdapter();
            PlayListViewAdapter.PlayListItemData itemData = (PlayListViewAdapter.PlayListItemData)adapter.getItem(position);
            if(itemData == null || itemData.mediaEntity == null)
                return;

            mService.play(itemData.mediaEntity);
        }

        @Override
        public void onClearPlaylist() {
            if(mService == null)
                return;

            mService.clearPlaylist();
        }

        @Override
        public void onChangePlayMode() {
            if(mService == null || mPlayListDialog == null)
                return;

            int curPlayMode = mService.getRepeatMode();
            int nextPlayMode = (curPlayMode + 1)%(PreferenceConfig.PLAYMODE_NUM);
            mService.setRepeatMode(nextPlayMode);
            mPlayListDialog.updatePlaymodeImg(nextPlayMode, true);
        }
    };

    public NowPlayingLayout(Context context) {
        super(context);
        init();
        mHomePage = (HomeActivity)context;
    }

    public NowPlayingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mHomePage = (HomeActivity)context;
    }

    public NowPlayingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mHomePage = (HomeActivity)context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NowPlayingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mClient.connect();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mClient.disconnect();
    }

    public void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.audio_now_playing, null);
        this.addView(view);

        mBtnPlayPause = (ImageButton)view.findViewById(R.id.btnPlayPause);
        mBtnPlayNext = (ImageButton)view.findViewById(R.id.btnPlayNext);
        mBtnPlaylist = (ImageButton)view.findViewById(R.id.btnPlaylist);
        mBtnPlayPause.setOnClickListener(this);
        mBtnPlayNext.setOnClickListener(this);
        mBtnPlaylist.setOnClickListener(this);
        this.setOnClickListener(this);

        mtvMain = (TextView) view.findViewById(R.id.tvNowPlayingMain);
        mtvSub = (TextView)view.findViewById(R.id.tvNowPlayingSub);
        mImArtist = (ImageView)view.findViewById(R.id.ivArtist);
        mPlayProgress = (ProgressBar) view.findViewById(R.id.pbPlay);
        mPlayProgress.setProgress(0);
        llControl = (LinearLayout)view.findViewById(R.id.llControl);
        llControl.setOnClickListener(this);
    }

    private void initData(){
        if(mService == null)
            return;

        int size = mService.getPlaylistSize();
        if(size == 0){
            resetUI(true);
        }
        else{
            MediaEntity curMedia = mService.getCurrentMedia();
            if(curMedia != null){
                mtvMain.setText(curMedia.title);
                mtvSub.setText(curMedia.artist);
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onClick(View v) {
        if(mService == null)
            return;

        if(v == mBtnPlayPause){
            if(mService.getPlaylistSize() == 0){
                Toast.makeText(this.getContext(), "当前无可播放的歌曲", Toast.LENGTH_SHORT).show();
                return;
            }

            if(mIsPlaying){
                mService.pause();
            }
            else{
                mService.play();
            }
        }
        else if(v == mBtnPlayNext){
            if(mService.getPlaylistSize() == 0){
                Toast.makeText(this.getContext(), "当前无可播放的歌曲", Toast.LENGTH_SHORT).show();
                return;
            }

            mService.next(false);
        }
        else if(v == mBtnPlaylist){
            if(mPlayListDialog == null){
                PlayListDialog.Builder builder = new PlayListDialog.Builder(this.getContext());
                mPlayListDialog = builder.create(false);
            }
            mPlayListDialog.setCancelable(true);
            mPlayListDialog.setPlaylistData(mService.getPlaylist());
            mPlayListDialog.setItemPlayState(mService.getCurPlayMediaIndex(), true, mService.isPlaying());
            mPlayListDialog.setPlayListAdapterListener(mListener);
            mPlayListDialog.updatePlaymodeImg(mService.getRepeatMode(), false);
            mPlayListDialog.show();
        }
        else if(llControl == v){
            Intent intent = new Intent(this.getContext(), PlayingActivity.class);
            intent.putExtra(PlayingActivity.PLAYING_MEDIA_ENTITY, (Parcelable) mService.getCurrentMedia());
            mHomePage.startActivity(intent);
        }
    }

    public void setPlayingMediaEntrty(MediaEntity media){
        if(media == null)
            return ;

        mtvMain.setText(media.getTitle());
        mtvSub.setText(media.getArtist());
        mPlayProgress.setMax((int)media.getDuration());
        mWeakMediaEntity = new WeakReference<MediaEntity>(media);
    }

    public void updatePlayProgress(int iCur, int iMax){
        mPlayProgress.setMax(iMax);
        mPlayProgress.setProgress(iCur);
    }

    public void setPlayPauseState(boolean isPlaying){
        if(isPlaying){
            mBtnPlayPause.setBackgroundResource(R.drawable.bt_minibar_pause_normal);
        }
        else{
            mBtnPlayPause.setBackgroundResource(R.drawable.bt_minibar_play_normal);
        }
        mIsPlaying = isPlaying;
    }

    //PlaybackService.Client.Callback
    @Override
    public void onConnected(PlaybackService service) {
        mService = service;
        mService.addCallback(this);
        initData();
    }

    @Override
    public void onDisconnected() {
        mService = null;
    }

    //PlaybackService.Callback
    @Override
    public void update() {

    }

    @Override
    public void updateProgress() {
        if(mService == null)
            return ;

        int iTime = (int)mService.getTIme();
        int iLengtht = (int)mService.getLength();
        updatePlayProgress(iTime, iLengtht);
    }

    @Override
    public void onMediaEvent(Media.Event event) {
        if(mService == null || event == null)
            return;

        setPlayPauseState(mService.isPlaying());
    }

    @Override
    public void onMediaPlayerEvent(MediaPlayer.Event event) {
        if(event.type == MediaPlayer.Event.Playing){
            MediaEntity mediaEntity = mService.getCurrentMedia();
            if(mediaEntity != null){
                resetUI(false);
                mtvMain.setText(mediaEntity.getTitle());
                mtvSub.setText(mediaEntity.getArtist());
                if(mPlayListDialog != null && mPlayListDialog.isShowing()){
                    mPlayListDialog.setItemPlayState(mService.getCurPlayMediaIndex(), true, true);
                }
            }
        }
        else if(event.type == MediaPlayer.Event.Paused){
            MediaEntity mediaEntity = mService.getCurrentMedia();
            if(mediaEntity != null){
                if(mPlayListDialog != null && mPlayListDialog.isShowing()){
                    mPlayListDialog.setItemPlayState(mService.getCurPlayMediaIndex(), true, false);
                }
            }
        }
        else if(event.type == MediaPlayer.Event.Stopped){
            resetUI(true);
        }
    }

    private void resetUI(boolean isStopState){
        if(isStopState){
            mtvMain.setText(getResources().getString(R.string.app_name) + " 畅享极致");
            mtvSub.setText("");
            mPlayProgress.setProgress(0);
            mtvSub.setVisibility(View.GONE);
            mtvMain.setGravity(Gravity.CENTER_VERTICAL);
        }
        else{
            mtvMain.setText("");
            mtvSub.setText("");
            mPlayProgress.setProgress(0);
            mtvSub.setVisibility(View.VISIBLE);
            mtvMain.setGravity(Gravity.CENTER_VERTICAL);
        }

    }
}
