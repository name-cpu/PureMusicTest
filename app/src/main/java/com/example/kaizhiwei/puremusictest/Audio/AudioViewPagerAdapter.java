package com.example.kaizhiwei.puremusictest.Audio;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by kaizhiwei on 16/11/12.
 */
public class AudioViewPagerAdapter extends PagerAdapter {
    private List<View>  mListView;
    private List<String> mListTitle;

    public AudioViewPagerAdapter(List<View> list, List<String> listTitle){
        if(list != null)
            mListView = list;

        if(listTitle != null)
            mListTitle = listTitle;
    }

    @Override
    public int getCount() {
        return mListView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        if(position <0 || mListView == null || mListView.size() <= position)
            return  null;

        View view = mListView.get(position);
        container.addView(view);
        return view;
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * {@link #instantiateItem(View, int)}.
     */
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(position <0 || mListView == null || mListView.size() <= position)
            return ;

        View view = mListView.get(position);
        container.removeView(view);
        mListView.remove(position);
    }

    public CharSequence getPageTitle(int position) {
        if(position <0 || mListTitle == null || mListTitle.size() <= position)
            return null;

        return mListTitle.get(position);
    }
}
