package com.example.purevideoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by kaizhiwei on 17/7/30.
 */

public class PureTextureView extends TextureView{
    private int videoWidth;
    private int videoHeight;

    public PureTextureView(Context context) {
        super(context);
    }

    public PureTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PureTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setVideoSize(int width, int height){
        if(width != videoWidth || height != videoHeight){
            this.videoHeight = height;
            this.videoWidth = width;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float rotate = getRotation();
        if(rotate == 90.0f || rotate == 270.0f){
            int temp = widthMeasureSpec;
            widthMeasureSpec = heightMeasureSpec;
            heightMeasureSpec = temp;
        }

        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            width = widthSpec;
            height = heightSpec;
            if(videoWidth*heightSpec < videoHeight*widthSpec){
                width = videoWidth*heightSpec/videoHeight;
            }
            else if(videoWidth*heightSpec > videoHeight*widthSpec)
                height = videoHeight*widthSpec/videoWidth;
        }
        else if(widthMode == MeasureSpec.EXACTLY){
            Log.e("weikaizhi", "onMeasure widthMode == MeasureSpec.EXACTLY");
        }
        else if(heightMode == MeasureSpec.EXACTLY){
            Log.e("weikaizhi", "onMeasure heightMode == MeasureSpec.EXACTLY");
        }
        else{

        }

        setMeasuredDimension(width, height);
    }

}
