package com.example.kaizhiwei.puremusictest.Audio;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by kaizhiwei on 16/11/12.
 */
public class AudioListView extends ListView {
    private AudioActivity mContext;

    public AudioListView(AudioActivity context) {
        super(context);
        init(context);
    }

    public AudioListView(AudioActivity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AudioListView(AudioActivity context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(AudioActivity context){
        this.setDividerHeight(0);
        this.setOnItemClickListener(context);
        this.setOnScrollListener(context);
    }
}
