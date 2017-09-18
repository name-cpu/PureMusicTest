package com.example.kaizhiwei.puremusictest.ui.recentplay;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.service.PlayMusicService;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;
import com.example.kaizhiwei.puremusictest.util.FadingEdgeUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/9/18.
 */

public class RecentPlayMainFragment extends MyBaseFragment {
    @Bind(R.id.commonTitle)
    CommonTitleView commonTitle;
    @Bind(R.id.tabLayoutRecentPlay)
    TabLayout tabLayoutRecentPlay;
    @Bind(R.id.vpRecentPlay)
    ViewPager vpRecentPlay;

    private List<String> mTitles;
    private List<Fragment> mFragements;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recent_play_main;
    }

    @Override
    protected void initView() {
        mFragements = new ArrayList<>();
        mFragements.add(new RecentPlaySongFragment());
        mFragements.add(new RecentPlayGeDanFragment());
        mTitles = new ArrayList<>();
        mTitles.add("歌曲");
        mTitles.add("歌单");
        RecentPlayAdater adater = new RecentPlayAdater(this.getChildFragmentManager());
        vpRecentPlay.setAdapter(adater);
        FadingEdgeUtil.disableViewPagerEdgeEffect(vpRecentPlay);

        tabLayoutRecentPlay.setupWithViewPager(vpRecentPlay);
        setIndicator(tabLayoutRecentPlay, 60,60);

        commonTitle.setTitleViewInfo("最近播放", "", "");
        commonTitle.setTitleViewListener(new CommonTitleView.onTitleClickListener() {
            @Override
            public void onLeftBtnClicked() {
                HomeActivity.getInstance().removeFragment(RecentPlayMainFragment.this);
            }

            @Override
            public void onRightBtnClicked() {

            }
        });
    }

    @Override
    protected void initData() {

    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    private class RecentPlayAdater extends FragmentPagerAdapter{

        public RecentPlayAdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragements.get(position);
        }

        @Override
        public int getCount() {
            return mFragements.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

    }
}
