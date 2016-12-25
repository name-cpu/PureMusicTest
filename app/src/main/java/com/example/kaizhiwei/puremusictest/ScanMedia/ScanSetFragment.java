package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 16/10/30.
 */
public class ScanSetFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan_set, container, false);
        return rootView;
    }
}
