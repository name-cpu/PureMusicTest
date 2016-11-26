package com.example.kaizhiwei.puremusictest.Audio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;

import java.lang.ref.WeakReference;

/**
 * Created by kaizhiwei on 16/11/21.
 */
public class AudioFilterActivity extends Activity implements MediaLibrary.IMediaScanListener, AdapterView.OnItemClickListener{
    private TextView   mtvTitle;
    private AudioListView mAudioListView;
    private AudioListViewAdapter mListViewAdapter;
    private Handler mHandler = new Handler();

    public static final String FILTER_TYPE = "FILTER_TYPE";
    public static final String FILTER_NAME = "FILTER_NAME";
    public static final String TITLE_NAME = "TITLE_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_audio_filter);
        mtvTitle = (TextView)this.findViewById(R.id.tvTitle);
        mAudioListView = (AudioListView)this.findViewById(R.id.lvFilter);
        mListViewAdapter = new AudioListViewAdapter(this, AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, false);
        mAudioListView.setAdapter(mListViewAdapter);
        //mAllSongListView.setOnItemClickListener(this);
        //mAllSongListView.setOnScrollListener(this);

        Intent intent = getIntent();
        int filterType = intent.getIntExtra(FILTER_TYPE, AudioListViewAdapter.ADAPTER_TYPE_FOLDER);
        String strFilterData = intent.getStringExtra(FILTER_NAME);
        String strTitleName = intent.getStringExtra(TITLE_NAME);
        if(filterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
            mListViewAdapter.setFilterFolder(strFilterData);
        }
        else if(filterType == AudioListViewAdapter.ADAPTER_TYPE_ARTIST){
            mListViewAdapter.setFilterArtist(strFilterData);
        }
        else if(filterType == AudioListViewAdapter.ADAPTER_TYPE_ALBUM){
            mListViewAdapter.setFilterAlbum(strFilterData);
        }

        mListViewAdapter.initData(MediaLibrary.getInstance().getAllMediaEntrty());
        mtvTitle.setText(strTitleName);
    }

    @Override
    public void onScanStart() {

    }

    @Override
    public void onScaning(String fileInfo, float progress) {

    }

    @Override
    public void onScanFinish() {
        final WeakReference<MediaLibrary> mlibrary = new WeakReference<MediaLibrary>(MediaLibrary.getInstance());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mlibrary.get() != null){
                    mListViewAdapter.initData(mlibrary.get().getAllMediaEntrty());
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
