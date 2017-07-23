package com.example.purevideoplayer;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.purevideoplayer.R;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public class PureVideoController extends FrameLayout implements View.OnTouchListener, View.OnClickListener {
    private ImageView ivBackground;
    private LinearLayout llLoading;
    private ProgressBar pbLoading;
    private LinearLayout llChangePosition;
    private TextView tvSeekToPosition;
    private ProgressBar pbSeek;
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
    private ProgressBar pbProgress;
    private ImageView ivFullScreen;
    private IPlayController mPlayController;

    private boolean isShowTitleAndBottomCtrl = true;

    public PureVideoController(@NonNull Context context) {
        this(context, null, 0);
    }

    public PureVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PureVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public IPlayController getPlayController() {
        return mPlayController;
    }

    public void setPlayController(IPlayController mPlayController) {
        this.mPlayController = mPlayController;
    }

    private void initView(){
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_purevideoplayer, null, false);
        this.addView(view);
        this.setOnTouchListener(this);
        this.setOnClickListener(this);
        this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        llLoading = (LinearLayout) findViewById(R.id.llLoading);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
        llChangePosition = (LinearLayout)findViewById(R.id.llChangePosition);
        tvSeekToPosition = (TextView)findViewById(R.id.tvSeekToPosition);
        pbSeek = (ProgressBar)findViewById(R.id.pbSeek);
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
        pbProgress = (ProgressBar)findViewById(R.id.pbProgress);
        ivFullScreen = (ImageView)findViewById(R.id.ivFullScreen);
        ivFullScreen.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view == this){
            isShowTitleAndBottomCtrl = !isShowTitleAndBottomCtrl;
            setTitleVisiable(isShowTitleAndBottomCtrl);
            setBottomCtrlVisiable(isShowTitleAndBottomCtrl);
        }
    }

    private void setTitleVisiable(boolean isShow){
        ValueAnimator valueAnimator = null;

        if(isShow){
            valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        }
        else{
            valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                llTitle.setAlpha((float)animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    private void setBottomCtrlVisiable(boolean isShow){
        ValueAnimator valueAnimator = null;

        if(isShow){
            valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        }
        else{
            valueAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                llBottomCtrl.setAlpha((float)animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }
}
