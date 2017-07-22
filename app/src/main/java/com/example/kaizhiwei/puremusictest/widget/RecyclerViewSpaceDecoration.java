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

    public int getFirstItemTopSpcace() {
        return firstItemTopSpcace;
    }

    public void setFirstItemTopSpcace(int firstItemTopSpcace) {
        this.firstItemTopSpcace = firstItemTopSpcace;
    }

    private int firstItemTopSpcace = 0;

    public RecyclerViewSpaceDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        this.leftSpace = leftSpace;
        this.topSpace = topSpace;
        this.rightSpace = rightSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {


        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            GridLayoutManager gridLayoutManager = (GridLayoutManager)layoutManager;
            int position = gridLayoutManager.getPosition(view);
            int spanCount = gridLayoutManager.getSpanCount();
            int spanSize = gridLayoutManager.getSpanSizeLookup().getSpanSize(position);
            if(spanSize == spanCount){
                outRect.left = leftSpace;
                outRect.right = rightSpace;
                outRect.bottom = 0;
                // Add top margin only for the first item to avoid double space between items
                if (parent.getChildPosition(view) == 0)
                    outRect.top = topSpace;
            }
            else {
                outRect.left = leftSpace;
                outRect.bottom = bottomSpace;
                if((position)%(spanCount) == 0){
                    outRect.right = 0;
                }
                else{
                    outRect.right = rightSpace;
                }
                if (((position-1) / spanCount == 0))
                    outRect.top = firstItemTopSpcace;
            }
        }
        else if(layoutManager instanceof LinearLayoutManager){
            outRect.left = leftSpace;
            outRect.right = rightSpace;
            outRect.bottom = bottomSpace;
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = topSpace;
        }
    }
}