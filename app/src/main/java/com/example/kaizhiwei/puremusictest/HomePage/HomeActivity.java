package com.example.kaizhiwei.puremusictest.HomePage;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.kaizhiwei.puremusictest.Audio.AudioActivity;
import com.example.kaizhiwei.puremusictest.Audio.NowPlayingLayout;
import com.example.kaizhiwei.puremusictest.CommonUI.SystemBarTintManager;
import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity {
    private TabLayout mTabLayout;
    private TabLayout.TabLayoutOnPageChangeListener mTVl;
    private List<String> mListTitleData;
    private ViewPager mViewPager;

    private List<Fragment_LocalMusic> mListFragment;
    private FragmentPagerAdapter mFragemtnPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if(mListFragment == null || position < 0 || position >= mListFragment.size())
                return null;

            return mListFragment.get(position);
        }

        @Override
        public int getCount() {
            if(mListFragment == null)
                return 0;

            return mListFragment.size();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home);
        mViewPager = (ViewPager) this.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) this.findViewById(R.id.tabLayout);
        initSystemBar();

        mListTitleData = new ArrayList<String>();
        mListTitleData.add("歌曲");

        mListFragment = new ArrayList<>();
        mListFragment.add(new Fragment_LocalMusic(this));

        mViewPager.setLongClickable(true);
        //mViewPager.setOnLongClickListener(this);
        //mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(mListFragment.size());
        mViewPager.setAdapter(mFragemtnPagerAdapter);
        mViewPager.setCurrentItem(0);//设置当前显示标签页为第一页

        for(int i = 0;i < mListTitleData.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mListTitleData.get(i)));
        }
        mTabLayout.setBackgroundResource(R.color.backgroundColor);
        mTabLayout.setTabTextColors(this.getResources().getColor(R.color.mainTextColor), this.getResources().getColor(R.color.tabSelectTextColor));
        int indicatorColor = this.getResources().getColor(R.color.tabSeperatorLineColor);
        mTabLayout.setSelectedTabIndicatorColor(indicatorColor);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //createFloatView();
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.common_title_backgroundColor);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void createFloatView()
    {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBX_8888; // 设置图片格式，效果为背景透明(RGBA_8888)

        // 设置Window flag

      /*

       * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。

       * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |

       * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;

       */

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;


        // 设置悬浮窗的长得宽
        params.width = ViewPager.LayoutParams.MATCH_PARENT;
        params.height = ViewPager.LayoutParams.WRAP_CONTENT;

        //设置悬浮窗的位置
        params.x = 0;
        params.gravity = Gravity.BOTTOM;

        NowPlayingLayout nowPlayingLayout = new NowPlayingLayout(this);
        wm.addView(nowPlayingLayout, params);
    }

    public void switchToAudioFragment(){
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //transaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right);
        transaction.add(R.id.flContent, new AudioActivity(),"AudioActivity");
        transaction.addToBackStack("AudioActivity");
        transaction.commit();
    }
}
