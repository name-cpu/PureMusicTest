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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/21.
 */
public class AudioFilterActivity extends Activity implements MediaLibrary.IMediaScanListener, AdapterView.OnItemClickListener, AudioListViewAdapter.IAudioListViewListener {
    private TextView   mtvTitle;
    private AudioListView mAudioListView;
    private AudioListViewAdapter mListViewAdapter;
    private Handler mHandler = new Handler();
    private int mFilterType;
    private String mFilterData;

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
        mFilterType = filterType;
        mFilterData = strFilterData;

        mListViewAdapter.initData(MediaLibrary.getInstance().getAllMediaEntrty());
        mtvTitle.setText(strTitleName);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MediaLibrary.getInstance().registerListener(this);
        mListViewAdapter.registerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaLibrary.getInstance().unregisterListener(this);
        mListViewAdapter.unregisterListener(this);
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

    @Override
    public void onMoreBtnClick(AudioListViewAdapter adapter, int position) {
        MoreOperationDialog.Builder builder = new MoreOperationDialog.Builder(this);
        MoreOperationDialog dialog = builder.create();
        List<Integer> list = new ArrayList<>();

        int adapterType = adapter.getAdapterType();
        if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG){
            AudioListViewAdapter.AudioItemData itemData = adapter.getAudioItemData(position);
            AudioListViewAdapter.AudioSongItemData songItemData = null;
            if(itemData instanceof AudioListViewAdapter.AudioSongItemData){
                songItemData = (AudioListViewAdapter.AudioSongItemData)itemData;
            }

            if(songItemData != null){
                dialog.setTitle(songItemData.mMainTitle);
                list.add(MoreOperationDialog.MORE_NEXTPLAY_NORMAL);
                list.add(MoreOperationDialog.MORE_LOVE_NORMAL);
                list.add(MoreOperationDialog.MORE_BELL_NORMAL);
                list.add(MoreOperationDialog.MORE_SHARE_NORMAL);
                list.add(MoreOperationDialog.MORE_ADD_NORMA);
                list.add(MoreOperationDialog.MORE_DELETE_NORMAL);
            }
        }

        dialog.setMoreOperData(list);
        dialog.setCancelable(true);
        dialog.show();
    }
}
