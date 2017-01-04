package com.example.kaizhiwei.puremusictest.CommonUI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 16/12/25.
 */
public class MyTextView extends TextView {
    private GradientDrawable mNormalDrawable;
    private GradientDrawable mPressedDrawable;
    private int mBorderColor;
    private int mNormalBackgroundColor;
    private int mPressedBackGroundColor;

    public MyTextView(Context context) {
        super(context);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mBorderColor = getResources().getColor(R.color.subTextColor);
        mNormalBackgroundColor = getResources().getColor(R.color.backgroundColor);
        mPressedBackGroundColor = getResources().getColor(R.color.default_button_pressed_color);

        this.setClickable(true);
        setBorderColor(mBorderColor);
        //setNormalBackgroundColor(mNormalBackgroundColor);
        setPressedBackgroundColor(mPressedBackGroundColor);
        this.setBackgroundDrawable(mNormalDrawable);
    }

    public void setBorderColor(int color){
        mBorderColor = color;
        if(mNormalDrawable == null){
            mNormalDrawable = new GradientDrawable();
        }

        if(mNormalDrawable != null){
            mNormalDrawable.setShape(GradientDrawable.RECTANGLE);
            mNormalDrawable.setStroke(1, mBorderColor);
        }
    }

    public void setNormalBackgroundColor(int color){
        mNormalBackgroundColor = color;
        if(mNormalDrawable == null){
            mNormalDrawable = new GradientDrawable();
        }

        if(mNormalDrawable != null){
            mNormalDrawable.setShape(GradientDrawable.RECTANGLE);
            mNormalDrawable.setColor(mNormalBackgroundColor);
        }
    }

    public void setPressedBackgroundColor(int color){
        mPressedBackGroundColor = color;
        if(mPressedDrawable == null){
            mPressedDrawable = new GradientDrawable();
        }
        mPressedDrawable.setShape(GradientDrawable.RECTANGLE);
        mPressedDrawable.setColor(mPressedBackGroundColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.setBackgroundDrawable(mPressedDrawable);
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){
            this.setBackgroundDrawable(mNormalDrawable);
        }

        return super.onTouchEvent(event);
    }
}
