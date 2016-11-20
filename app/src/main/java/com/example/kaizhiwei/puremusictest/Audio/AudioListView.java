package com.example.kaizhiwei.puremusictest.Audio;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 16/11/12.
 */
public class AudioListView extends ListView {
    private AudioActivity mContext;

    public AudioListView(Context context) {
        super(context);
        if(isInEditMode())
            return ;

        mContext = (AudioActivity)context;
        init();
    }

    public AudioListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(isInEditMode())
            return ;

        mContext = (AudioActivity)context;
        init();
    }

    public AudioListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(isInEditMode())
            return ;

        mContext = (AudioActivity)context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AudioListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        if(isInEditMode())
            return ;

        init();
    }

    public void init(){
        this.setDividerHeight(0);
        this.setOnItemClickListener(mContext);
        this.setOnScrollListener(mContext);
        this.setBackgroundResource(R.color.backgroundColor);
    }
}
