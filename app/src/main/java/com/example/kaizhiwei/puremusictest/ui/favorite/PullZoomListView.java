package com.example.kaizhiwei.puremusictest.ui.favorite;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/9/16.
 */

public class PullZoomListView extends ListView {
    private LinearLayout mHeadView;
    private int mHeadViewHeight;
    private LinearLayout.LayoutParams mParam;
    private int mMaxHeight = 200;
    private int mDownY = -1;
    private int mDiffY = -1;

    public PullZoomListView(Context context) {
        this(context, null, 0);
    }

    public PullZoomListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullZoomListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeadView();
    }

    private void initHeadView(){
        mHeadView = (LinearLayout) View.inflate(getContext(), R.layout.pull_zoom_head_view, null);
        mHeadView.measure(0, 0);
        mHeadViewHeight = mHeadView.getMeasuredHeight();
        this.addView(mHeadView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isDisplayHeaderView()){
                    mParam = (LinearLayout.LayoutParams) mHeadView.getLayoutParams();
                    mDownY = (int)event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(isDisplayHeaderView()){
                    if(mDownY == -1){
                        mDownY = (int)event.getY();
                    }

                    int moveY = (int)event.getY();
                    int diffY = moveY - mDownY;
                    if(diffY >= mMaxHeight){
                        diffY = mMaxHeight;
                    }
                    if(diffY > 0){
                        mDiffY = diffY;
                        mParam.height = mHeadViewHeight + mDiffY;
                        mHeadView.setLayoutParams(mParam);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mDiffY > 0 && isDisplayHeaderView()){
                    reset();
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private boolean isDisplayHeaderView(){
        int[] location = new int[2];
        mHeadView.getLocationOnScreen(location);
        int headViewYOnScreen = location[1];
        return headViewYOnScreen > 0 ? true : false;
    }

    private void reset(){
        mDiffY = -1;
        ValueAnimator animator;
        animator = ValueAnimator.ofInt(mHeadViewHeight + mDiffY, mHeadViewHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int)animation.getAnimatedValue();
                mParam.height = height;
                mHeadView.setLayoutParams(mParam);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    public View getView(){
        return mHeadView;
    }
}
