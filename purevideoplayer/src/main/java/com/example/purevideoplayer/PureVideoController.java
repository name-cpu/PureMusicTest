package com.example.purevideoplayer;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public class PureVideoController extends FrameLayout implements View.OnTouchListener, View.OnClickListener{
    private ImageView ivBackground;
    private LinearLayout llLoading;
    private ProgressBar pbLoading;
    private LinearLayout llChangePosition;
    private TextView tvSeekToPosition;
    private ProgressBar pbSeekTo;
    private LinearLayout llChangeBright;
    private ProgressBar pbBright;
    private LinearLayout llChangeVolumn;
    private ProgressBar pbVolumn;
    private LinearLayout llPlayFinish;
    private TextView tvRestart;
    private LinearLayout llPlayError;
    private TextView tvError;
    private TextView tvRetry;
    private LinearLayout llTitle;
    private TextView tvTitle;
    private LinearLayout llBottomCtrl;
    private ImageView ivPlayPause;
    private TextView tvCurTime;
    private TextView tvTotalTime;
    private SeekBar sbPlay;
    private ImageView ivFullScreen;
    private IVidepPlayer mVideoPlayer;

    private Context mContext;
    private boolean isShowTitleAndBottomCtrl = true;
    private boolean isPlaying = false;
    private boolean isFullScreen = false;

    private int mPlayState;
    private float mStartPosX;
    private float mStartPosY;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean needChangeVolumn;
    private boolean needChangeBrigness;
    private boolean needChangeSeek;

    private boolean isFirstChangeSeek = true;
    private int mSeekBeginTime;

    private int mBrightnessMode;
    private float mBrightValue;

    //播放错误状态
    public static final int STATE_ERROR = -1;

    //播放空闲状态
    public static final int STATE_IDLE = 0;

    //播放准备中
    public static final int STATE_PAPERING = 1;

    //播放准备完成
    public static final int STATE_PAPERED = 2;

    //正在播放
    public static final int STATE_PLAY = 3;

    //暂停播放
    public static final int STATE_PAUSE = 4;

    //正在播放是缓存不够，自动暂停缓存数据
    public static final int STATE_BUFFER_PAUSE = 5;

    //正在播放时缓存够了，自动恢复播放
    public static final int STATE_BUFFER_RESUME = 6;

    //播放完成
    public static final int STATE_COMPLETE = 7;

    private int mCurTime;
    private int mTotalTime;
    private GestureDetector mDoubleGesture;

    private class SimpleOnGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

        public boolean onSingleTapUp(MotionEvent e) {
            if(mPlayState == STATE_COMPLETE || mPlayState == STATE_ERROR)
                return false;

            return true;
        }

        public void onLongPress(MotionEvent e) {
            Log.e("weikaizhi", "onLongPress");
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.e("weikaizhi", "onScroll");
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            Log.e("weikaizhi", "onFling");
            return true;
        }

        public void onShowPress(MotionEvent e) {
            Log.e("weikaizhi", "onShowPress");
        }

        public boolean onDown(MotionEvent e) {
            Log.e("weikaizhi", "onDown");
            return true;
        }

        public boolean onDoubleTap(MotionEvent e) {
            Log.e("weikaizhi", "onDoubleTap");
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.e("weikaizhi", "onDoubleTapEvent");
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e("weikaizhi", "onSingleTapConfirmed");
            return true;
        }
    }

    private GestureDetector.OnDoubleTapListener mSimpleGestureListener = new SimpleOnGestureListener() {

        public boolean onDoubleTap(MotionEvent e) {
            if(mVideoPlayer != null){
                mVideoPlayer.onDoubleClick();
            }
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            isShowTitleAndBottomCtrl = !isShowTitleAndBottomCtrl;
            setCtrlViewVisiable(llBottomCtrl, isShowTitleAndBottomCtrl);
            setCtrlViewVisiable(llTitle, isShowTitleAndBottomCtrl);
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.e("weikaizhi", "onScroll");
            return true;
        }

    };

    public PureVideoController(@NonNull Context context) {
        this(context, null, 0);
    }

    public PureVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PureVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initData();
    }

    public IVidepPlayer getPlayController() {
        return mVideoPlayer;
    }

    public void setPlayController(IVidepPlayer mVideoPlayer) {
        this.mVideoPlayer = mVideoPlayer;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    private void initView(){
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_purevideoplayer, null, false);
        this.addView(view);
        this.setOnTouchListener(this);
        this.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        mDoubleGesture = new GestureDetector(this.getContext(), (GestureDetector.OnGestureListener) mSimpleGestureListener);
        mDoubleGesture.setIsLongpressEnabled(false);
        mDoubleGesture.setOnDoubleTapListener(mSimpleGestureListener);

        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        llLoading = (LinearLayout) findViewById(R.id.llLoading);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        llChangePosition = (LinearLayout)findViewById(R.id.llChangePosition);
        tvSeekToPosition = (TextView)findViewById(R.id.tvSeekToPosition);
        pbSeekTo = (ProgressBar)findViewById(R.id.pbSeekTo);
        llChangeBright = (LinearLayout)findViewById(R.id.llChangeBright);
        pbBright = (ProgressBar)findViewById(R.id.pbBright);
        llChangeVolumn = (LinearLayout)findViewById(R.id.llChangeVolumn);
        pbVolumn = (ProgressBar)findViewById(R.id.pbVolumn);
        llPlayFinish = (LinearLayout)findViewById(R.id.llPlayFinish);

        tvRestart = (TextView)findViewById(R.id.tvRestart);
        tvRestart.setOnClickListener(this);

        llPlayError = (LinearLayout)findViewById(R.id.llPlayError);

        tvError = (TextView)findViewById(R.id.tvError);
        tvError.setOnClickListener(this);

        tvRetry = (TextView)findViewById(R.id.tvRetry);
        tvRetry.setOnClickListener(this);

        llTitle = (LinearLayout)findViewById(R.id.llTitle);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setOnClickListener(this);

        llBottomCtrl = (LinearLayout)findViewById(R.id.llBottomCtrl);
        ivPlayPause = (ImageView) findViewById(R.id.ivPlayPause);
        ivPlayPause.setOnClickListener(this);

        tvCurTime = (TextView) findViewById(R.id.tvCurTime);
        tvTotalTime = (TextView)findViewById(R.id.tvTotalTime);
        sbPlay = (SeekBar) findViewById(R.id.sbPlay);
        sbPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int position = seekBar.getProgress();
                if(mVideoPlayer != null){
                    mVideoPlayer.onSeekTo(position*1000);
                }
            }
        });
        ivFullScreen = (ImageView)findViewById(R.id.ivFullScreen);
        ivFullScreen.setOnClickListener(this);
    }

    private void initData(){
        mBrightnessMode = getBrightnessMode();
        mBrightValue = getBrightnessValue();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(!isFullScreen ){
            return this.mDoubleGesture.onTouchEvent(motionEvent);
        }

        int action = motionEvent.getAction();
        if(action == MotionEvent.ACTION_DOWN){

            if(mPlayState == STATE_COMPLETE || mPlayState == STATE_ERROR)
                return false;

            mStartPosX = motionEvent.getRawX();
            mStartPosY = motionEvent.getRawY();
        }
        else if(action == MotionEvent.ACTION_MOVE){
            float x = motionEvent.getRawX();
            float y = motionEvent.getRawY();
            int width = getWidth();
            int height = getHeight();
            float distanceY = y - mStartPosY;
            float distanceX = x - mStartPosX;
            //控制播放进度
            if((!needChangeVolumn && !needChangeBrigness) && (Math.abs(distanceX) > Math.abs(distanceY)) && (Math.abs(distanceX) > ViewConfiguration.getTouchSlop())){
                needChangeVolumn = false;
                needChangeBrigness = false;
                needChangeSeek = true;
            }
            //左边控制声音
            else if((!needChangeBrigness && !needChangeSeek) && (Math.abs(distanceY) > Math.abs(distanceX)) && (mStartPosX < width/3) && (Math.abs(distanceY) > ViewConfiguration.getTouchSlop())){
                needChangeVolumn = true;
                needChangeBrigness = false;
                needChangeSeek = false;
            }
            //右边控制亮度
            else if((!needChangeVolumn && !needChangeSeek) && (Math.abs(distanceY) > Math.abs(distanceX)) && (mStartPosX > width*2/3) && (Math.abs(distanceY) > ViewConfiguration.getTouchSlop())){
                needChangeVolumn = false;
                needChangeBrigness = true;
                needChangeSeek = false;
            }

            if(needChangeVolumn){
                if(changeVolumn(-distanceY)){
                    mStartPosY = y;
                }
            }

            if(needChangeBrigness){
                if(changeBright(-distanceY)){
                    mStartPosY = y;
                }
            }

            if(needChangeSeek){
                changePlaySeekBegin(distanceX);
                isFirstChangeSeek = false;
            }
        }
        else if(action == MotionEvent.ACTION_UP){
            if(needChangeVolumn){
                mHandler.removeCallbacks(hideChangeVolumnRunnable);
                mHandler.postDelayed(hideChangeVolumnRunnable, 1000);
            }

            if(needChangeBrigness){
                mHandler.removeCallbacks(hideChangeBrightRunnable);
                mHandler.postDelayed(hideChangeBrightRunnable, 1000);
            }

            if(needChangeSeek){
                mHandler.removeCallbacks(hideChangePlaySeekRunnable);
                mHandler.postDelayed(hideChangePlaySeekRunnable, 1000);
                float x = motionEvent.getRawX();
                float distanceX = x - mStartPosX;
                changePlaySeekEnd(distanceX);
            }

            needChangeVolumn = false;
            needChangeBrigness = false;
            needChangeSeek = false;
        }
        return this.mDoubleGesture.onTouchEvent(motionEvent);
    }

    @Override
    public void onClick(View view) {
        if(view == ivPlayPause){
            if(isPlaying){
                mVideoPlayer.onPause();
            }
            else{
                mVideoPlayer.onStart();
            }
        }
        else if(view == ivFullScreen){
            if(isFullScreen){
                mVideoPlayer.onExitFullScreen();
            }
            else{
                mVideoPlayer.onFullScreen();
            }
        }
        else if(view == tvRestart){
            mVideoPlayer.onReStart();
        }
    }

    public void updateState(int state){
        mPlayState = state;
        llLoading.setVisibility(View.GONE);
        llChangePosition.setVisibility(View.GONE);
        llChangeBright.setVisibility(View.GONE);
        llChangeVolumn.setVisibility(View.GONE);
        llPlayFinish.setVisibility(View.GONE);
        llPlayError.setVisibility(View.GONE);
        switch (state){
            case STATE_ERROR:
                isPlaying = false;
                llPlayError.setVisibility(VISIBLE);
                break;
            case STATE_IDLE:
                ivPlayPause.setImageResource(R.drawable.ic_player_start);
                sbPlay.setProgress(0);
                break;
            case STATE_PAPERING:
                llLoading.setVisibility(View.VISIBLE);
                pbLoading.setIndeterminate(true);
                break;
            case STATE_PAPERED:

                break;
            case STATE_PLAY:
                isPlaying = true;
                ivPlayPause.setImageResource(R.drawable.ic_player_pause);
                break;
            case STATE_PAUSE:
                isPlaying = false;
                ivPlayPause.setImageResource(R.drawable.ic_player_start);
                break;
            case STATE_BUFFER_PAUSE:
                isPlaying = false;
                llLoading.setVisibility(View.VISIBLE);
                pbLoading.setIndeterminate(true);
                ivPlayPause.setImageResource(R.drawable.ic_player_start);
                break;
            case STATE_BUFFER_RESUME:
                isPlaying = true;
                llLoading.setVisibility(View.VISIBLE);
                pbLoading.setIndeterminate(true);
                ivPlayPause.setImageResource(R.drawable.ic_player_pause);
                break;
            case STATE_COMPLETE:
                isShowTitleAndBottomCtrl = false;
                isPlaying = false;
                llTitle.setVisibility(View.GONE);
                llBottomCtrl.setVisibility(View.GONE);
                llPlayFinish.setVisibility(View.VISIBLE);
                break;
        }
    }

    public int getPlayState(){
        return mPlayState;
    }

    public void updatePlayPosition(long curPosition, long total){
        curPosition = curPosition/1000;
        total = total/1000;
        mCurTime = (int)curPosition;
        mTotalTime = (int)total;
        tvCurTime.setText(String.format("%02d:%02d", curPosition/60, curPosition%60));
        tvTotalTime.setText(String.format("%02d:%02d", total/60, total%60));

        sbPlay.setMax((int)total);
        if(!needChangeSeek){
            sbPlay.setProgress((int)curPosition);
        }
    }

    public void updateBufferProgress(int percent){
        sbPlay.setSecondaryProgress(percent*sbPlay.getMax()/100);
    }

    public void reset(){
        isShowTitleAndBottomCtrl = true;
        isPlaying = false;
        isFullScreen = false;

        needChangeVolumn = false;
        needChangeBrigness= false;
        needChangeSeek= false;

        //setBrightnessMode(mBrightnessMode);
        //setBrightnessValue(mBrightValue);
    }

    public void setVideoTitle(String strTitle){
        tvTitle.setText(strTitle);
    }

    private void setCtrlViewVisiable(final View view, final boolean isShow){
        AlphaAnimation animation;
        if(isShow){
            animation = new AlphaAnimation(0.0f, 1.0f);
        }
        else{
            animation = new AlphaAnimation(1.0f, 0.0f);
        }

        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    private Runnable hideTitleAndBottomCtrlRunnable = new Runnable() {
        @Override
        public void run() {
            ValueAnimator valueAnimator = null;
            valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    llTitle.setAlpha((float)animation.getAnimatedValue());
                    llBottomCtrl.setAlpha((float)animation.getAnimatedValue());
                }
            });
            valueAnimator.setDuration(400);
            valueAnimator.start();
        }
    };

    private boolean changeVolumn(float distance){
        boolean bRet;
        llChangeVolumn.bringToFront();
        llChangeVolumn.setVisibility(View.VISIBLE);
        AudioManager mAudioManager = (AudioManager) this.getContext().getSystemService(Context.AUDIO_SERVICE);
        //音乐音量
        int height = this.getHeight()/2;
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int newColumn = current;
        pbVolumn.setMax(max);
        pbVolumn.setProgress(current);
        int itemheight = height/max;

        if(Math.abs(distance) > itemheight){
            if(distance > 0){
                for(int i = 0;i < Math.abs(distance)/itemheight && newColumn < max;i++){
                    newColumn++;
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                }
            }
            else{
                for(int i = 0;i < Math.abs(distance)/itemheight && newColumn >= 0;i++){
                    newColumn--;
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                }
            }
            bRet = true;
        }
        else{
            bRet = false;
        }

        pbVolumn.setProgress(newColumn);
        return bRet;
    }

    private Runnable hideChangeVolumnRunnable = new Runnable() {
        @Override
        public void run() {
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    llChangeVolumn.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            llChangeVolumn.startAnimation(animation);
        }
    };

    private int getBrightnessMode(){
        int mode = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        try {
            mode = Settings.System.getInt(this.getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);}
        catch (Exception e){
            e.printStackTrace();
        }

        return mode;
    }

    private float getBrightnessValue(){
        int curBrightness = 0;
        try {
            curBrightness = Settings.System.getInt(this.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

//        Activity activity = (Activity)mContext;
//        float value = activity.getWindow().getAttributes().screenBrightness;
        return curBrightness*1.0f/255;
    }

    private void setBrightnessMode(int mode){
        try {
            Settings.System.putInt(this.getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, mode);}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setBrightnessValue(float value){
//        try {
//            Settings.System.putInt(this.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Activity activity = (Activity)mContext;
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness = value;
        activity.getWindow().setAttributes(layoutParams);
    }

    private boolean changeBright(float distance){
        boolean bRet;
        llChangeBright.bringToFront();
        llChangeBright.setVisibility(View.VISIBLE);
        int height = this.getHeight()/2;
        int maxBrightness = 255;
        float curBrightness = 0;

        int mode = getBrightnessMode();
        if(mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
            //setBrightnessMode(Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }

        curBrightness = mBrightValue*maxBrightness;
        pbBright.setMax(maxBrightness);
        float newBrightness = curBrightness;
        if(Math.abs(distance) > this.getHeight()/maxBrightness){
            newBrightness += (int)(distance*maxBrightness/height);
            if(newBrightness < 0){
                newBrightness = 0;
            }
            if(newBrightness > maxBrightness){
                newBrightness = maxBrightness;
            }
            setBrightnessValue(newBrightness/maxBrightness);
            bRet = true;

            Activity activity = (Activity)(mContext);
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            if (newBrightness == -1) {
                lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            }
            else {
                lp.screenBrightness = (newBrightness < 0 ? 0.0f : newBrightness*1.0f/maxBrightness);
            }
            window.setAttributes(lp);
        }
        else{
            bRet = false;
        }

        Log.e("weikaizhi", "newBrightness = " + newBrightness);
        mBrightValue = newBrightness*1.0F/255;
        pbBright.setProgress((int)(newBrightness));
        return bRet;
    }

    private Runnable hideChangeBrightRunnable = new Runnable() {
        @Override
        public void run() {
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    llChangeBright.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            llChangeBright.startAnimation(animation);
        }
    };

    private void changePlaySeekBegin(float distance){
        llChangePosition.bringToFront();
        llChangePosition.setVisibility(View.VISIBLE);

        int width = getWidth()/2;
        pbSeekTo.setMax(mTotalTime);
        if(isFirstChangeSeek){
            mSeekBeginTime = mCurTime;
        }
        int newProgress = mSeekBeginTime;
        newProgress += (int)(distance*mTotalTime/width);
        if(newProgress < 0){
            newProgress = 0;
        }
        if(newProgress > mTotalTime){
            newProgress = mTotalTime;
        }
        pbSeekTo.setProgress(newProgress);
        sbPlay.setProgress(newProgress);
        tvSeekToPosition.setText(String.format("%02d:%02d", newProgress/60, newProgress%60));
    }

    private void changePlaySeekEnd(float distance){
        int width = getWidth()/2;
        int newProgress = mSeekBeginTime;
        newProgress += (int)(distance*mTotalTime/width);
        if(newProgress < 0){
            newProgress = 0;
        }
        if(newProgress > mTotalTime){
            newProgress = mTotalTime;
        }

        if(mVideoPlayer != null){
            mVideoPlayer.onSeekTo(newProgress*1000);
        }
        isFirstChangeSeek = true;
        mSeekBeginTime = 0;
    }

    private Runnable hideChangePlaySeekRunnable = new Runnable() {
        @Override
        public void run() {
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(300);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    llChangePosition.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            llChangePosition.startAnimation(animation);
        }
    };
}
