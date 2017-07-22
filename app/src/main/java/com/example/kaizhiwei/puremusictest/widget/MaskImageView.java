package com.example.kaizhiwei.puremusictest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/7/21.
 */

public class MaskImageView extends ImageView {
    public MaskImageView(Context context) {
        super(context);
    }

    public MaskImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isPressed()){
            canvas.drawColor(getResources().getColor(R.color.trans_dk_gray)) ;
        }
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        invalidate();
    }
}
