package com.example.purevideoplayer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public class PureVideoPlayer extends FrameLayout implements IVidepPlayer,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnBufferingUpdateListener,
        TextureView.SurfaceTextureListener,
        IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnTimedTextListener {
    private PureVideoController mVideoController;
    private PureTextureView textureView;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;

    private FrameLayout mContainer;
    private IMediaPlayer mediaPlayer;
    private Uri mUri;
    private Context mContext;
    private Map<Integer, Integer> mapOtherViews = new HashMap<>();

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable playProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer != null && mVideoController != null){
                mVideoController.updatePlayPosition(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                mHandler.postDelayed(playProgressRunnable, 1000);
            }
        }
    };

    public PureVideoPlayer(@NonNull Context context) {
        this(context, null, 0);
    }

    public PureVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PureVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    public void initView(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer = new FrameLayout(mContext);
        mContainer.setBackgroundColor(getResources().getColor(android.R.color.black));
        this.addView(mContainer, params);

        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mVideoController = new PureVideoController(mContext);
        mVideoController.setPlayController(this);
        mContainer.addView(mVideoController, params);

        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        textureView = new PureTextureView(mContext);
        textureView.setSurfaceTextureListener(this);
        mContainer.addView(textureView, 0, params);
    }

    public void setVideoTitle(String strTitle){
        mVideoController.setVideoTitle(strTitle);
    }

    public TextureView getTextureView(){
        return textureView;
    }

    public IMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void onStart() {
        if(mediaPlayer != null){
            if(!mediaPlayer.isPlaying()){
                try{
                    mediaPlayer.start();
                    if(mVideoController != null){
                        mVideoController.updateState(PureVideoController.STATE_PLAY);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPause() {
        if(mediaPlayer != null){
            try{
                mediaPlayer.pause();
                if(mVideoController != null){
                    mVideoController.updateState(PureVideoController.STATE_PAUSE);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReStart() {
        if(mediaPlayer != null){
            mediaPlayer.reset();
        }
        openMediaPlayer();
    }

    @Override
    public void onClick() {

    }

    @Override
    public void onDoubleClick() {
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                onPause();
            }
            else{
                onStart();
            }
        }
    }

    @Override
    public void onFullScreen() {
        Activity activity = (Activity)mContext;
        Window window = activity.getWindow();
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = activity.getActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        mapOtherViews.clear();
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        for(int i = 1; i < decorView.getChildCount();i++){
            View childView = decorView.getChildAt(i);
            int viewId = childView.getId();
            mapOtherViews.put(viewId, childView.getVisibility());
            childView.setVisibility(View.GONE);
        }

        LinearLayout windowView = (LinearLayout) decorView.getChildAt(0);
        int size = windowView.getChildCount();
        for(int i = 0;i < size;i++){
            View childView = windowView.getChildAt(i);
            int viewId = childView.getId();
            if(!(childView instanceof FrameLayout)){
                mapOtherViews.put(viewId, childView.getVisibility());
                childView.setVisibility(View.GONE);
            }
            else{
                this.removeView(mContainer);
                ((ViewGroup)childView).addView(mContainer, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if(mVideoController != null){
            mVideoController.setFullScreen(true);
        }
    }

    @Override
    public void onExitFullScreen() {
        Activity activity = (Activity)mContext;
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = activity.getWindow();
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.clearFlags(flag);

        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        for(int i = 1; i < decorView.getChildCount();i++){
            View childView = decorView.getChildAt(i);
            int viewId = childView.getId();
            if(mapOtherViews.containsKey(viewId)) {
                int vis = mapOtherViews.get(viewId);
                if (vis == 0) {
                    childView.setVisibility(View.VISIBLE);
                } else if (vis == 4) {
                    childView.setVisibility(View.INVISIBLE);
                } else if (vis == 8) {
                    childView.setVisibility(View.GONE);
                }
            }
        }

        LinearLayout windowView = (LinearLayout) decorView.getChildAt(0);
        for(int i = 0;i < windowView.getChildCount();i++){
            View childView = windowView.getChildAt(i);
            int viewId = childView.getId();
            if(mapOtherViews.containsKey(viewId)) {
                int vis = mapOtherViews.get(viewId);
                if (vis == 0) {
                    childView.setVisibility(View.VISIBLE);
                } else if (vis == 4) {
                    childView.setVisibility(View.INVISIBLE);
                } else if (vis == 8) {
                    childView.setVisibility(View.GONE);
                }
            }
        }

        ViewGroup parent = (ViewGroup) mContainer.getParent();
        if(parent != null){
            parent.removeView(mContainer);
        }
        this.addView(mContainer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if(mVideoController != null){
            mVideoController.setFullScreen(false);
        }
    }

    public boolean exitFullScreen(){
        if(mVideoController == null )
            return false;

        if(mVideoController.isFullScreen()){
            onExitFullScreen();
            return true;
        }
        return false;
    }

    @Override
    public void onSeekTo(int position) {
        if(mediaPlayer != null){
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        if(mVideoController == null)
            return false;

        if(what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
            mVideoController.updateState(PureVideoController.STATE_PLAY);
            mHandler.postDelayed(playProgressRunnable, 1000);
        }
        else if(what == IMediaPlayer.MEDIA_INFO_BUFFERING_START){
            int curState = mVideoController.getPlayState();
            if(curState == PureVideoController.STATE_BUFFER_PAUSE || curState == PureVideoController.STATE_PAUSE){
                curState = PureVideoController.STATE_BUFFER_PAUSE;
            }
            else{
                curState = PureVideoController.STATE_BUFFER_RESUME;
            }
            mVideoController.updateState(curState);
        }
        else if(what == IMediaPlayer.MEDIA_INFO_BUFFERING_END){
            int curState = mVideoController.getPlayState();
            if(curState == PureVideoController.STATE_BUFFER_PAUSE || curState == PureVideoController.STATE_PAUSE){
                curState = PureVideoController.STATE_PAUSE;
            }
            else{
                curState = PureVideoController.STATE_PLAY;
            }
            mVideoController.updateState(curState);
        }
        return true;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if(mVideoController != null){
            mVideoController.updateState(PureVideoController.STATE_PAPERED);
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        if(mVideoController != null){
            mVideoController.updateState(PureVideoController.STATE_ERROR);
            mHandler.removeCallbacks(playProgressRunnable);
        }
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if(mVideoController != null){
            mVideoController.updateState(PureVideoController.STATE_COMPLETE);
            mHandler.removeCallbacks(playProgressRunnable);
        }

        if(iMediaPlayer != null){
            iMediaPlayer.reset();
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        if(mVideoController != null){
            mVideoController.updateBufferProgress(i);
        }
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int i2, int i3) {
        textureView.setVideoSize(width, height);
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
        Log.e("weikaizhi", "onTimedText " +  ijkTimedText.getText());
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if(mSurfaceTexture == null){
            mSurfaceTexture = surface;
            openMediaPlayer();
        }
        else{
            textureView.setSurfaceTexture(mSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return mSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private void openMediaPlayer(){
        if(mUri == null)
            return;

        if(mediaPlayer == null){
            mediaPlayer = new IjkMediaPlayer();
        }
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        mediaPlayer.setOnTimedTextListener(this);

        try {
            mediaPlayer.setDataSource(this.getContext(), mUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(mSurface == null){
            mSurface = new Surface(mSurfaceTexture);
        }
        mediaPlayer.setSurface(mSurface);
        mediaPlayer.prepareAsync();

        if(mVideoController != null){
            mVideoController.updateState(PureVideoController.STATE_PAPERING);
        }
    }

    public void closeMediaPlayer(){
        if(mediaPlayer == null)
            return;

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

        if(mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }

        if(mSurface != null) {
            mSurface.release();
            mSurface = null;
        }

        mVideoController.removeView(textureView);
        mVideoController.reset();
        textureView = null;
    }
}
