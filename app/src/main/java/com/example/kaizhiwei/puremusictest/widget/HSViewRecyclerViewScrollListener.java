package com.example.kaizhiwei.puremusictest.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public abstract class HSViewRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private int mDistanceSlop = 300;
    private boolean isControlShow = true;
    private int mDistanceY = 0;

    public int getmDistanceSlop() {
        return mDistanceSlop;
    }

    public void setmDistanceSlop(int mDistanceSlop) {
        this.mDistanceSlop = mDistanceSlop;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
            int firstVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if(firstVisibleItemPosition == 0){
                if(!isControlShow){
                    isControlShow = true;
                    onShowControlView();
                }
            }
            else{
                if(mDistanceY > mDistanceSlop && isControlShow){
                    isControlShow = false;
                    onHideControlView();
                    mDistanceY = 0;
                }
                else if(mDistanceY < -mDistanceSlop && !isControlShow){
                    isControlShow = true;
                    onShowControlView();
                    mDistanceY = 0;
                }
            }
        }
        //如果隐藏后继续下滑或者已经显示继续上滑不用考虑
        if((isControlShow && dy > 0) || (!isControlShow && dy <= 0)){
            mDistanceY += dy;
        }
    }

    public abstract void onShowControlView();

    public abstract void onHideControlView();
}
