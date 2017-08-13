package com.example.kaizhiwei.puremusictest.ui.home;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.kaizhiwei.puremusictest.ui.netmusic.bangdan.BangDanFragment;
import com.example.kaizhiwei.puremusictest.ui.netmusic.gedan.GeDanFragment;
import com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian.TuiJIanFragment;
import com.example.kaizhiwei.puremusictest.ui.netmusic.video.VideoFragment;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class NetMusicFragment extends MyBaseFragment {
    @Bind(R.id.tabLayoutNetMusic)
    TabLayout tabLayoutNetMusic;
    @Bind(R.id.viewPager)
    ViewPager tabLayout;
    private List<MyBaseFragment> mFragments;
    private List<String> mTitles;
    private NetMusicFragmentAdaper mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_net_music;
    }

    @Override
    protected void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(new TuiJIanFragment());
        mFragments.add(new GeDanFragment());
        mFragments.add(new BangDanFragment());
        mFragments.add(new VideoFragment());

        tabLayoutNetMusic.setBackgroundResource(android.R.color.transparent);
        tabLayoutNetMusic.setTabTextColors(this.getResources().getColor(R.color.black), this.getResources().getColor(R.color.tabSelectTextColor));
        int indicatorColor = this.getResources().getColor(R.color.tabSeperatorLineColor);
        tabLayoutNetMusic.setSelectedTabIndicatorColor(indicatorColor);

        mAdapter = new NetMusicFragmentAdaper(this.getChildFragmentManager());
        tabLayout.setAdapter(mAdapter);

        tabLayoutNetMusic.setupWithViewPager(tabLayout);
        tabLayoutNetMusic.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayoutNetMusic.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void initData() {
        mTitles = new ArrayList<>();
        mTitles.add(getActivity().getString(R.string.tuijian));
        mTitles.add(getActivity().getString(R.string.gedan));
        mTitles.add(getActivity().getString(R.string.bangdan));
        mTitles.add(getActivity().getString(R.string.shipin));
    }

    private class NetMusicFragmentAdaper extends FragmentPagerAdapter{

        public NetMusicFragmentAdaper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
