package com.example.kaizhiwei.puremusictest.CommonUI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.HomePage.FavoriteLayout;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/1/4.
 */
public class BaseFragment extends Fragment implements View.OnClickListener{
    protected TextView tvTitle;
    private LinearLayout llContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.basefragment_layout, null, false);//关联布局文件
        llContent = (LinearLayout)rootView.findViewById(R.id.llContent);
        tvTitle = (TextView)rootView.findViewById(R.id.tvTitle);
        tvTitle.setOnClickListener(this);
        return rootView;
    }

    public void setContentView(int resId){
        View view = getActivity().getLayoutInflater().inflate(resId, null);
        if(llContent != null){
            llContent.addView(view);
        }
    }

    public void setTitle(String title){
        if(tvTitle != null){
            tvTitle.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        if(tvTitle == v){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.detach(this);
            transaction.commit();
        }
    }
}
