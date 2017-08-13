package com.example.kaizhiwei.puremusictest.ui.splash;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 16/10/29.
 */
public class WelcomeFragment2 extends Fragment implements  View.OnClickListener{
    private Button btnStartScan;
    private Button btnSetScan;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_welcome_screen2, null);
        btnStartScan = (Button)rootView.findViewById(R.id.btnStartScan);
        btnSetScan = (Button)rootView.findViewById(R.id.btnSetScan);
        btnStartScan.setOnClickListener(this);
        btnSetScan.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == btnStartScan){

        }
        else if(v == btnSetScan){

        }
    }
}