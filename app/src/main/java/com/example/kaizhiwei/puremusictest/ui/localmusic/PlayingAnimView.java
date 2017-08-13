package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/1/4.
 */
//正在播放动画控件
public class PlayingAnimView extends View{
    private int mMaxValue = 0;
    private int mMaxNum = 3;
    private int mLineWidth = 5;
    private int mLineSperetorWidth = 5;
    private boolean isStarted = false;
    private Paint mLinePaint;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            PlayingAnimView.this.invalidate();
            handler.postDelayed(runnable, 100);
        }
    };
    private List<List<Double>> listFakeData;
    private int mCurIndex = 0;

    public PlayingAnimView(Context context) {
        super(context);
        init();
    }

    public PlayingAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayingAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mLinePaint = new Paint();
        mLinePaint.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        listFakeData = new ArrayList<>();
        List<Double> listSub = new ArrayList<>();
        listSub.add(0.1);
        listSub.add(0.3);
        listSub.add(0.6);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(0.2);
        listSub.add(0.4);
        listSub.add(0.7);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(0.3);
        listSub.add(0.5);
        listSub.add(0.8);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(0.4);
        listSub.add(0.6);
        listSub.add(0.9);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(0.5);
        listSub.add(0.7);
        listSub.add(1.0);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(0.6);
        listSub.add(0.8);
        listSub.add(0.1);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(0.7);
        listSub.add(0.9);
        listSub.add(0.2);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(0.8);
        listSub.add(1.0);
        listSub.add(0.3);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(0.9);
        listSub.add(0.1);
        listSub.add(0.4);
        listFakeData.add(listSub);

        listSub = new ArrayList<>();
        listSub.add(1.0);
        listSub.add(0.2);
        listSub.add(0.5);
        listFakeData.add(listSub);

        listFakeData.add(listSub);
    }

    public void startAnim(){
        if(isStarted)
            return;

        handler.postDelayed(runnable, 100);
        isStarted = true;
    }

    public void stopAnim(){
        isStarted = false;
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        mCurIndex++;
        mCurIndex = mCurIndex%10;
        List<Double> list = listFakeData.get(mCurIndex);

        int leftRightMargin = (width - mMaxNum*mLineWidth - (mMaxNum - 1)*mLineSperetorWidth)/2;
        int topBottomMagin = height/4;
        for(int i = 0;i < mMaxNum;i++){
            int left = leftRightMargin + i*(mLineSperetorWidth + mLineWidth);
            Rect rect = new Rect(left, (int)(list.get(i)*height/2) + topBottomMagin , left+mLineWidth, height-topBottomMagin);
            canvas.drawRect(rect, mLinePaint);
        }
    }
}
