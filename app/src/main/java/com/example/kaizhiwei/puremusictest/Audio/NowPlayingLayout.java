package com.example.kaizhiwei.puremusictest.Audio;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 16/11/20.
 */
public class NowPlayingLayout extends LinearLayout implements View.OnClickListener {
    private ImageButton mBtnPlayPause;
    private ImageButton mBtnPlayNext;
    private ImageButton mBtnPlaylist;
    private TextView   mtvMain;
    private TextView   mtvSub;
    private ImageView  mImArtist;
    private ProgressBar mPlayProgress;

    public NowPlayingLayout(Context context) {
        super(context);
        init();
    }

    public NowPlayingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NowPlayingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NowPlayingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)this.getLayoutParams();
//        params.setMargins(0,0,0,0);
//        this.setLayoutParams(params);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.audio_now_playing, null);
        this.addView(view);

        mBtnPlayPause = (ImageButton)view.findViewById(R.id.btnPlayPause);
        mBtnPlayNext = (ImageButton)view.findViewById(R.id.btnPlayNext);
        mBtnPlaylist = (ImageButton)view.findViewById(R.id.btnPlaylist);
        mBtnPlayPause.setOnClickListener(this);
        mBtnPlayNext.setOnClickListener(this);
        mBtnPlaylist.setOnClickListener(this);
        this.setOnClickListener(this);

        mtvMain = (TextView) view.findViewById(R.id.tvNowPlayingMain);
        mtvSub = (TextView)view.findViewById(R.id.tvNowPlayingSub);
        mImArtist = (ImageView)view.findViewById(R.id.ivArtist);
        mPlayProgress = (ProgressBar) view.findViewById(R.id.pbPlay);
        mPlayProgress.setProgress(0);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onClick(View v) {
        if(v == mBtnPlayPause){

        }
        else if(v == mBtnPlayNext){

        }
        else if(v == mBtnPlaylist){

        }
    }

    public void setPlayingMediaEntrty(MediaEntity media){
        if(media == null)
            return ;

        mtvMain.setText(media.getTitle());
        mtvSub.setText(media.getArtist());
        mBtnPlayPause.setBackgroundResource(R.drawable.pause);
        mPlayProgress.setMax((int)media.getDuration());
    }

    public void updatePlayProgress(float fProgress){

    }
}
