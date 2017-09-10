package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.kaizhiwei.puremusictest.ui.localmusic.BatchMgrAudioAdapter;

/**
 * Created by kaizhiwei on 17/9/9.
 */

public class MyItemTouchHelper extends ItemTouchHelper.Callback {
    private ItemDragListener mDragListener;
    private ItemMoveListener mMoveListener;

    public interface ItemDragListener{
        void onStartDrags(RecyclerView.ViewHolder viewHolder);
    }

    public interface ItemMoveListener{
        void onItemMove(int from, int to);
    }

    public MyItemTouchHelper(ItemMoveListener listener) {
        mMoveListener = listener;
    }

    public ItemDragListener getDragListener() {
        return mDragListener;
    }

    public void setDragListener(ItemDragListener mListener) {
        this.mDragListener = mListener;
    }

    public ItemMoveListener getMoveListener() {
        return mMoveListener;
    }

    public void setMoveListener(ItemMoveListener mListener) {
        this.mMoveListener = mListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 如果你不想上下拖动，可以将 dragFlags = 0
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        // 如果你不想左右滑动，可以将 swipeFlags = 0
        int swipeFlags = 0;

        //最终的动作标识（flags）必须要用makeMovementFlags()方法生成
        int flags = makeMovementFlags(dragFlags, swipeFlags);
        return flags;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if(mMoveListener != null){
            mMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            //viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(android.R.color.darker_gray));
            viewHolder.itemView.setAlpha(0.6f);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(android.R.color.white));
        viewHolder.itemView.setAlpha(1);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float value = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(value);
            viewHolder.itemView.setScaleY(value);
        }
    }
}
