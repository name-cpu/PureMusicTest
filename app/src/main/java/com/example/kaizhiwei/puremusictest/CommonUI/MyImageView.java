package com.example.kaizhiwei.puremusictest.CommonUI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by kaizhiwei on 16/12/11.
 */
public class MyImageView extends ImageView{
    private int mNormalResId;
    private int mPressResId;

    public MyImageView(Context context) {
        super(context);
        this.setClickable(true);
        this.setFocusable(false);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setClickable(true);
        this.setFocusable(false);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClickable(true);
        this.setFocusable(false);
    }

    public void setResId(int normalResId, int pressResId){
        mNormalResId = normalResId;
        mPressResId = pressResId;
        this.setImageResource(mNormalResId);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.setImageResource(mPressResId);
            Log.i("weikaizhi", "MotionEvent.ACTION_DOWN");
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            this.setImageResource(mNormalResId);
            Log.i("weikaizhi", "MotionEvent.ACTION_UP");
        }

        return super.onTouchEvent(event);
    }
}
