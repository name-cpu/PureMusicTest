package com.example.kaizhiwei.puremusictest.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kaizhiwei on 17/7/9.
 */
public class RecyclerViewSpaceDecoration extends RecyclerView.ItemDecoration {
    private int leftSpace;
    private int topSpace;
    private int rightSpace;
    private int bottomSpace;

    public RecyclerViewSpaceDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        this.leftSpace = leftSpace;
        this.topSpace = topSpace;
        this.rightSpace = rightSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = leftSpace;
        outRect.right = rightSpace;
        outRect.bottom = bottomSpace;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
            int position = gridLayoutManager.getPosition(view);
            int spanCount = gridLayoutManager.getSpanCount();

        }
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = topSpace;
    }
}