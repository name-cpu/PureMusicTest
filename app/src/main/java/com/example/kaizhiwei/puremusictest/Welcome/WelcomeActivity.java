package com.example.kaizhiwei.puremusictest.Welcome;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.kaizhiwei.puremusictest.Databases.MediaStoreAccessHelper;
import com.example.kaizhiwei.puremusictest.Databases.SongEntity;
import com.example.kaizhiwei.puremusictest.PureMusicApplication;
import com.example.kaizhiwei.puremusictest.R;
import com.viewpagerindicator.LinePageIndicator;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/10/29.
 */
public class WelcomeActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private LinePageIndicator mLinePageIndicator;
    private WelcomePagerAdapter mPagerAdapter;
    private List<Fragment> mListFragment;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        mViewPager = (ViewPager)this.findViewById(R.id.viewPager);
        mLinePageIndicator = (LinePageIndicator)this.findViewById(R.id.linePageIndicator);

        mListFragment = new ArrayList<Fragment>();
        mListFragment.add(new WelcomeFragment1());
        mListFragment.add(new WelcomeFragment2());
        mListFragment.add(new WelcomeFragment3());
        mListFragment.add(new WelcomeFragment4());
        mListFragment.add(new WelcomeFragment5());

        mPagerAdapter = new WelcomePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(5);

        mLinePageIndicator.setViewPager(mViewPager);
        final float density = getResources().getDisplayMetrics().density;
        mLinePageIndicator.setSelectedColor(0x880099CC);
        mLinePageIndicator.setUnselectedColor(0xFF4F4F4F);
        mLinePageIndicator.setStrokeWidth(2 * density);
        mLinePageIndicator.setLineWidth(30 * density);




    }

    class WelcomePagerAdapter extends FragmentStatePagerAdapter{

        public WelcomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WelcomeActivity.this.mListFragment.get(position);
        }

        @Override
        public int getCount() {
            return WelcomeActivity.this.mListFragment.size();
        }
    }
}
