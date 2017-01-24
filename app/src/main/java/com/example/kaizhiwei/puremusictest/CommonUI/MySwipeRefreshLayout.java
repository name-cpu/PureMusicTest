package com.example.kaizhiwei.puremusictest.CommonUI;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by 24820 on 2017/1/24.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    private int mTouchSlop;
    private float mPreX;

    public MySwipeRefreshLayout(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPreX = MotionEvent.obtain(event).getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                float xDistance = Math.abs(curX - mPreX);
                if(xDistance > mTouchSlop){
                    return false;
                }
                break;
        }

        return super.onInterceptTouchEvent(event);
    }
}
