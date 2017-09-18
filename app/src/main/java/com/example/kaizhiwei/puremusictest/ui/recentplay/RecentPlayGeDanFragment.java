package com.example.kaizhiwei.puremusictest.ui.recentplay;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kaizhiwei on 17/9/18.
 */

public class RecentPlayGeDanFragment extends MyBaseFragment {
    @Bind(R.id.llEmptyContent)
    LinearLayout llEmptyContent;
    @Bind(R.id.rvGeDan)
    RecyclerView rvGeDan;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recent_play_gedan;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
