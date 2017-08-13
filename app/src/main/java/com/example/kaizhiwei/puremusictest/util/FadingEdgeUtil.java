package com.example.kaizhiwei.puremusictest.util;

import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.widget.AbsListView;
import android.widget.EdgeEffect;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * Created by kaizhiwei on 17/8/9.
 */

public class FadingEdgeUtil {

    public static void setEdgeTopColor(ScrollView scrollView, @ColorInt int color){
        Class clazz = ScrollView.class;
        try {
            Field fieldEdgeGlowTop = clazz.getDeclaredField("mEdgeGlowTop");
            fieldEdgeGlowTop.setAccessible(true);
            EdgeEffect mEdgeGlowTop = (EdgeEffect)fieldEdgeGlowTop.get(scrollView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mEdgeGlowTop.setColor(color);
            }
            else{
                Class clazzEdgeEffect = EdgeEffect.class;
                Field fieldPaint = clazzEdgeEffect.getDeclaredField("mPaint");
                fieldPaint.setAccessible(true);
                Paint paint = (Paint)fieldPaint.get(mEdgeGlowTop);
                paint.setColor(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setEdgeBottomColor(ScrollView scrollView, @ColorInt int color){
        Class clazz = ScrollView.class;
        try {
            Field fieldEdgeGlowBottom = clazz.getDeclaredField("mEdgeGlowBottom");
            fieldEdgeGlowBottom.setAccessible(true);
            EdgeEffect mEdgeGlowBottom = (EdgeEffect)fieldEdgeGlowBottom.get(scrollView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mEdgeGlowBottom.setColor(color);
            }
            else{
                Class clazzEdgeEffect = EdgeEffect.class;
                Field fieldPaint = clazzEdgeEffect.getDeclaredField("mPaint");
                fieldPaint.setAccessible(true);
                Paint paint = (Paint)fieldPaint.get(mEdgeGlowBottom);
                paint.setColor(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setEdgeTopColor(AbsListView listView, @ColorInt int color){
        Class clazz = ScrollView.class;
        try {
            Field fieldEdgeGlowTop = clazz.getDeclaredField("mEdgeGlowTop");
            fieldEdgeGlowTop.setAccessible(true);
            EdgeEffect mEdgeGlowTop = (EdgeEffect)fieldEdgeGlowTop.get(listView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mEdgeGlowTop.setColor(color);
            }
            else{
                Class clazzEdgeEffect = EdgeEffect.class;
                Field fieldPaint = clazzEdgeEffect.getDeclaredField("mPaint");
                fieldPaint.setAccessible(true);
                Paint paint = (Paint)fieldPaint.get(mEdgeGlowTop);
                paint.setColor(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setEdgeBottomColor(AbsListView listView, @ColorInt int color){
        Class clazz = ScrollView.class;
        try {
            Field fieldEdgeGlowBottom = clazz.getDeclaredField("mEdgeGlowBottom");
            fieldEdgeGlowBottom.setAccessible(true);
            EdgeEffect mEdgeGlowBottom = (EdgeEffect)fieldEdgeGlowBottom.get(listView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mEdgeGlowBottom.setColor(color);
            }
            else{
                Class clazzEdgeEffect = EdgeEffect.class;
                Field fieldPaint = clazzEdgeEffect.getDeclaredField("mPaint");
                fieldPaint.setAccessible(true);
                Paint paint = (Paint)fieldPaint.get(mEdgeGlowBottom);
                paint.setColor(color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
