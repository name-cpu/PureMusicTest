package com.example.kaizhiwei.puremusictest.Audio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.CommonUI.BaseFragment;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/21.
 */
public class AudioFilterFragment extends BaseFragment implements AdapterView.OnItemClickListener, AudioListViewAdapter.IAudioListViewListener {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.activty_audio_filter);
        mtvTitle = (TextView)rootView.findViewById(R.id.tvTitle);
        mAudioListView = (AudioListView)rootView.findViewById(R.id.lvFilter);
        mListViewAdapter = new AudioListViewAdapter(this.getActivity(), AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, false);
        mAudioListView.setAdapter(mListViewAdapter);
        //mAllSongListView.setOnItemClickListener(this);
        //mAllSongListView.setOnScrollListener(this);
        Bundle bundle = getArguments();
        int filterType = bundle.getInt(FILTER_TYPE, AudioListViewAdapter.ADAPTER_TYPE_FOLDER);
        String strFilterData = bundle.getString(FILTER_NAME);
        String strTitleName = bundle.getString(TITLE_NAME);
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
        setTitle(strTitleName);
        return rootView;
    }

    public void onResume(){
        super.onResume();
        mListViewAdapter.registerListener(this);
    }

    public void onPause() {
        super.onPause();
        mListViewAdapter.unregisterListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onMoreBtnClick(AudioListViewAdapter adapter, int position) {
        MoreOperationDialog.Builder builder = new MoreOperationDialog.Builder(this.getActivity());
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

    @Override
    public void onRandomPlayClick(AudioListViewAdapter adapter) {

    }

    @Override
    public void onBatchMgrClick(AudioListViewAdapter adapter) {

    }
}
