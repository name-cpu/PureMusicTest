package com.example.kaizhiwei.puremusictest.ui.behavior;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by kaizhiwei on 17/9/12.
 */

public class ShowHideBehavior extends CoordinatorLayout.Behavior<View> {
    private int sinceDirectionChange;
private float viewY;
    public ShowHideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
//        return dependency instanceof AppBarLayout;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        int top = dependency.getTop();
//        child.setTranslationY(-top);
//        return true;
//    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       View child, View directTargetChild, View target, int nestedScrollAxes) {

        if(child.getVisibility() == View.VISIBLE&&viewY==0){
            //获取控件距离父布局（coordinatorLayout）底部距离
            viewY=coordinatorLayout.getHeight()-child.getY();
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] comsumed){
        Log.e("weikaizhi", String.format("dx = %d, dy = %d, sinceDirectionChange = %d", dx, dy, sinceDirectionChange));
        if(dy > 0 && sinceDirectionChange < 0 || dy < 0 && sinceDirectionChange > 0){
            child.animate().cancel();
            sinceDirectionChange = 0;
        }

        sinceDirectionChange += dy;
        int vis = child.getVisibility();
        if(sinceDirectionChange > child.getHeight() && vis == View.VISIBLE){
            hide(child);
        }
        else{
            if(sinceDirectionChange < -child.getHeight() && vis != View.VISIBLE){
                show(child);
            }
        }
    }

    private void hide(final View view){
        ViewPropertyAnimator animator = view.animate().translationY(viewY).setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void show(final View view){
        ViewPropertyAnimator animator = view.animate().translationY(0).setDuration(200);
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }
}
