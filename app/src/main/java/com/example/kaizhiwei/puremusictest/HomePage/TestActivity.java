package com.example.kaizhiwei.puremusictest.HomePage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;

/**
 * Created by kaizhiwei on 17/1/14.
 */
public class TestActivity extends FragmentActivity {
    private FrameLayout flMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //initSlidingMenu();
        setContentView(R.layout.activity_test);
        flMain = (FrameLayout)this.findViewById(R.id.flMain);

        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.flMain, new FavoriteMainFragment());
        transaction.commit();
    }
}
