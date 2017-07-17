package com.example.kaizhiwei.puremusictest.HomePage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.kaizhiwei.puremusictest.Audio.LocalAudioFragment;
import com.example.kaizhiwei.puremusictest.Audio.NowPlayingLayout;
import com.example.kaizhiwei.puremusictest.NetAudio.NetMusicFragment;
import com.example.kaizhiwei.puremusictest.Util.SystemBarTintManager;
import com.example.kaizhiwei.puremusictest.NetAudio.tuijian.TuiJIanFragment;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.SlideMenu.SlidingMenu;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends FragmentActivity {
    private TabLayout mTabLayout;
    private TabLayout.TabLayoutOnPageChangeListener mTVl;
    private List<String> mListTitleData;
    private ViewPager mViewPager;
    protected SlidingMenu mSlidingMenu;

    private static HomeActivity mInstance;

    private LocalMusicMainFragment mLocalAudioMainFragment;
    private LocalAudioFragment mLocalAudioFragment;
    private FavoriteMainFragment mFavoriteMainFragment;

    private List<Fragment> mListFragment;
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

    public static HomeActivity getInstance(){
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //initSlidingMenu();
        setContentView(R.layout.activity_home);
        mViewPager = (ViewPager) this.findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) this.findViewById(R.id.tabLayoutMain);
        initSystemBar();

        DeviceUtil.getIMEI();
        DeviceUtil.getUniquePsuedoID();

        mListTitleData = new ArrayList<>();
        mListTitleData.add("我的");
        mListTitleData.add("音乐");

        mListFragment = new ArrayList<>();
        mListFragment.add(new LocalMusicMainFragment());
        mListFragment.add(new NetMusicFragment());

        mViewPager.setLongClickable(true);
        //mViewPager.setOnLongClickListener(this);
        //mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(mListFragment.size());
        mViewPager.setAdapter(mFragemtnPagerAdapter);
        mViewPager.setCurrentItem(0);//设置当前显示标签页为第一页

        for(int i = 0;i < mListTitleData.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mListTitleData.get(i)));
        }
        mTabLayout.setBackgroundResource(android.R.color.transparent);
        mTabLayout.setTabTextColors(this.getResources().getColor(R.color.white), this.getResources().getColor(R.color.tabSelectTextColor));
        int indicatorColor = this.getResources().getColor(R.color.tabSeperatorLineColor);
        mTabLayout.setSelectedTabIndicatorColor(indicatorColor);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //createFloatView();
        mInstance = this;

        //StatusBarUtil.setTranslucentForImageView(HomeActivity.this, null);
        //StatusBarUtil.setTranslucentForImageView(HomeActivity.this, 0, null);
    }

    private void initSlidingMenu() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;

        if(mLocalAudioFragment == null){
            mLocalAudioFragment = new LocalAudioFragment();
        }

//        setBehindContentView(R.layout.main_left_layout);
//        FragmentTransaction mFragementTransaction = getSupportFragmentManager()
//                .beginTransaction();
//        mFragementTransaction.replace(R.id.main_left_fragment, mLocalAudioFragment);
//        mFragementTransaction.commit();

        // customize the SlidingMenu
//        mSlidingMenu = getSlidingMenu();
//        mSlidingMenu.setMode(SlidingMenu.RIGHT);
//        mSlidingMenu.setShadowWidth(mScreenWidth / 40);
//        mSlidingMenu.setBehindOffset(0);
//        mSlidingMenu.setAboveOffset(0);
//        mSlidingMenu.setFadeDegree(0.35f);
//        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        //mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
//        //mSlidingMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);
//        mSlidingMenu.setFadeEnabled(true);
//        mSlidingMenu.setBehindScrollScale(0.333f);

//        mSlidingMenu.setSecondaryMenu(R.layout.main_left_layout);
//        FragmentTransaction mFragementTransaction = getSupportFragmentManager()
//               .beginTransaction();
//        mFragementTransaction.replace(R.id.main_left_fragment,  new LocalAudioFragment());
//        mFragementTransaction.commit();

        //mSlidingMenu.showMenu(false);
//        mSlidingMenu.showSecondaryMenu(false);
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
        if(mLocalAudioFragment == null){
            mLocalAudioFragment = new LocalAudioFragment();
        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        //getFragmentManager().executePendingTransactions();
        transaction.replace(R.id.flContent, mLocalAudioFragment);
        //transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        transaction.addToBackStack("mLocalAudioFragment");
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }


    public void switchToFavoriteFragment(Bundle bundle ){
//        if(mFavoriteMainFragment == null){
//            mFavoriteMainFragment = new FavoriteMainFragment();
//        }

        mFavoriteMainFragment = new FavoriteMainFragment();
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        //getFragmentManager().executePendingTransactions();
        transaction.replace(R.id.flContent, mFavoriteMainFragment);
        mFavoriteMainFragment.setArguments(bundle);
        if(mLocalAudioMainFragment != null){
            transaction.hide(mLocalAudioMainFragment);
        }

        if(mLocalAudioFragment != null){
            transaction.hide(mLocalAudioFragment);
        }
        //transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        transaction.addToBackStack("mFavoriteMainFragment");
        transaction.commit();
//        getSupportFragmentManager().executePendingTransactions();
//        Intent intent = new Intent(HomeActivity.this, TestActivity.class);
//        startActivity(intent);
    }


    public void switchToLocalMusicMain(){
        mLocalAudioMainFragment = (LocalMusicMainFragment) this.getSupportFragmentManager().findFragmentByTag("mLocalAudioMainFragment");
        if(mLocalAudioMainFragment == null){
            mLocalAudioMainFragment = new LocalMusicMainFragment();
        }
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        if(mLocalAudioMainFragment.isAdded() == false){
            transaction.add(R.id.flContent, mLocalAudioMainFragment);
        }
        else{
            transaction.show(mLocalAudioMainFragment);
        }

        if(mLocalAudioFragment != null){
            transaction.hide(mLocalAudioFragment);
        }

        if(mFavoriteMainFragment != null){
            transaction.hide(mFavoriteMainFragment);
        }
        //transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        transaction.addToBackStack("mLocalAudioMainFragment");
        transaction.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        boolean bRet = super.onKeyDown(keyCode, event);
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //mListFragment.get(0).updateData();
        }
        return bRet;
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.
                findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

}
