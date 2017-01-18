package com.example.kaizhiwei.puremusictest.Audio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
public class AudioFilterFragment extends BaseFragment{
    private LocalBaseMediaLayout lbmLayout;
    private LinearLayout llMain;
    private int mFilterType;
    private String mFilterData;

    public static final String FILTER_TYPE = "FILTER_TYPE";
    public static final String FILTER_NAME = "FILTER_NAME";
    public static final String TITLE_NAME = "TITLE_NAME";
    private LocalBaseMediaLayout.ILocalBaseListener mSubFragmentListener= new LocalBaseMediaLayout.ILocalBaseListener() {
        @Override
        public void onFragmentInitFinish(LinearLayout fragment) {

        }

        @Override
        public void onMoreOperClick(LocalBaseMediaLayout layout, int flag, Object obj) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.activty_audio_filter);
        llMain = (LinearLayout)rootView.findViewById(R.id.llMain);
        lbmLayout = (LocalBaseMediaLayout)rootView.findViewById(R.id.lbmLayout);
        lbmLayout.setBaseMediaListener(mSubFragmentListener);
        Bundle bundle = getArguments();
        int filterType = bundle.getInt(FILTER_TYPE, AudioListViewAdapter.ADAPTER_TYPE_FOLDER);
        String strFilterData = bundle.getString(FILTER_NAME);
        String strTitleName = bundle.getString(TITLE_NAME);

        lbmLayout.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, false, true, true);
        if(filterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
            lbmLayout.setFilterFolder(strFilterData);
        }
        else if(filterType == AudioListViewAdapter.ADAPTER_TYPE_ARTIST){
            lbmLayout.setFilterArtist(strFilterData);
        }
        else if(filterType == AudioListViewAdapter.ADAPTER_TYPE_ALBUM){
            lbmLayout.setFilterAlbum(strFilterData);
        }
        mFilterType = filterType;
        mFilterData = strFilterData;

        setTitle(strTitleName);
        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    public void onDestory(){
        super.onDestroy();
        lbmLayout.onDestory();
    }

    public void onResume(){
        super.onResume();
        lbmLayout.onResume();
        lbmLayout.initAdapterData();
    }

    public void onPause() {
        super.onPause();
        lbmLayout.onPause();
    }

    public void onStart() {
        super.onStart();
        lbmLayout.onStart();
    }

    public void onStop() {
        super.onStop();
        lbmLayout.onStop();
    }

    public void onDetach() {
        super.onDetach();
    }
}
