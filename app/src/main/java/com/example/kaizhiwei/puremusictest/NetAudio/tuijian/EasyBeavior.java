package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kaizhiwei on 17/7/8.
 */

public class EasyBeavior extends CoordinatorLayout.Behavior<TextView> {
    private int mStartY;

    public EasyBeavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child,
                                                    View dependency) {
        if(mStartY == 0) {
            mStartY = (int) dependency.getY();
        }    //计算toolbar从开始移动到最后的百分比
        float percent = dependency.getY()/mStartY;    //改变child的坐标(从消失，到可见)
        child.setY(child.getHeight()*(1-percent) - child.getHeight());    return true;
    }

}
