package com.example.kaizhiwei.puremusictest.Audio;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
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
    private WeakReference<MediaEntity>  mWeakMediaEntity;
    private boolean mIsPlaying;
    private PlaybackService.Client mClient = new PlaybackService.Client(this.getContext(), this);
    private PlaybackService mService;

    public NowPlayingLayout(Context context) {
        super(context);
        init();
    }

    public NowPlayingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NowPlayingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)this.getLayoutParams();
//        params.setMargins(0,0,0,0);
//        this.setLayoutParams(params);

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
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onClick(View v) {
        if(mService == null)
            return;

        if(v == mBtnPlayPause){
            if(mIsPlaying){
                mService.pause();
            }
            else{
                mService.play();
            }
        }
        else if(v == mBtnPlayNext){
            mService.next(false);
        }
        else if(v == mBtnPlaylist){
            PlayListDialog.Builder builder = new PlayListDialog.Builder(this.getContext());
            PlayListDialog dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setPlaylistData(mService.getPlaylist());
            dialog.show();
        }
    }

    public void setPlayingMediaEntrty(MediaEntity media){
        if(media == null)
            return ;

        mtvMain.setText(media.getTitle());
        mtvSub.setText(media.getArtist());
        //mBtnPlayPause.setBackgroundResource(R.drawable.bt_minibar_pause_normal);
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

    }
}
