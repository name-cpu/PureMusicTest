package com.example.purevideoplayer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public class PureVideoPlayer extends FrameLayout implements IPlayController, IMediaPlayer.OnInfoListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener, TextureView.SurfaceTextureListener {
    private PureVideoController mVideoController;
    private TextureView textureView;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;

    private FrameLayout mContainer;
    private IMediaPlayer mediaPlayer;
    private Uri mUri;

    public PureVideoPlayer(@NonNull Context context) {
        this(context, null, 0);
    }

    public PureVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PureVideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    public void initView(){
        mVideoController = new PureVideoController(this.getContext());
        mVideoController.setPlayController(this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer = new FrameLayout(this.getContext());
        mContainer.setBackgroundColor(getResources().getColor(android.R.color.black));
        this.addView(mContainer, params);

        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textureView = new TextureView(this.getContext());
        textureView.setSurfaceTextureListener(this);
        mContainer.addView(textureView, params);

        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(mVideoController, params);
    }

    public TextureView getTextureView(){
        return textureView;
    }

    public IMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onClick() {

    }

    @Override
    public void onDoubleClick() {

    }

    @Override
    public void onFullScreen() {

    }

    @Override
    public void onExitFullScreen() {

    }

    @Override
    public void onSeekTo(int position) {

    }

    @Override
    public void onChangeBright(int percent) {

    }

    @Override
    public void onChangeVolumn(int percent) {

    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

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
        closeMediaPlayer();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private void openMediaPlayer(){
        if(mUri == null)
            return;

        mediaPlayer = new IjkMediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnInfoListener(this);
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
    }

    private void closeMediaPlayer(){
        if(mediaPlayer == null)
            return;

        mediaPlayer.stop();
        mSurface = null;
        mSurfaceTexture = null;
    }
}
