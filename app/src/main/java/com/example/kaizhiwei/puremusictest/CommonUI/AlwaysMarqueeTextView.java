package com.example.kaizhiwei.puremusictest.CommonUI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class AlwaysMarqueeTextView extends TextView {

    public AlwaysMarqueeTextView(Context context) {
        super(context);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}