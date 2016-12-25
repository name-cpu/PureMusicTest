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
        this.setClickable(true);
        mNormalDrawable = new GradientDrawable();
        mNormalDrawable.setShape(GradientDrawable.RECTANGLE); // 画框
        mNormalDrawable.setStroke(1, getResources().getColor(R.color.subTextColor)); // 边框内部颜色

        mPressedDrawable = new GradientDrawable();
        mPressedDrawable.setShape(GradientDrawable.RECTANGLE); // 画框
        mPressedDrawable.setColor(getResources().getColor(R.color.default_button_pressed_color)); // 边框内部颜色
        this.setBackgroundDrawable(mNormalDrawable);
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
