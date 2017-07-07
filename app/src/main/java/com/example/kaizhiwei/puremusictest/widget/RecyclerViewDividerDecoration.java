package com.example.kaizhiwei.puremusictest.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by kaizhiwei on 17/7/2.
 */

public class RecyclerViewDividerDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Drawable mDivider;
    private int mOrientation;
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    public static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private int density = 1;

    public RecyclerViewDividerDecoration(Context context, int orientation){
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        density = (int)displayMetrics.density;
    }

    public void setOrientation(int orientation){
        if(orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == HORIZONTAL_LIST){
            drawHorizontalLine(c, parent, state);
        }
        else if(mOrientation == VERTICAL_LIST){
            drawVerticalLine(c, parent, state);
        }
    }

    private void drawHorizontalLine(Canvas canvas, RecyclerView parent, RecyclerView.State state){
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for(int i = 0;i < childCount;i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight()/density;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    private void drawVerticalLine(Canvas canvas, RecyclerView parent, RecyclerView.State state){
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for(int i = 0;i < childCount;i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation == HORIZONTAL_LIST){
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight()/density);
        }else {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
