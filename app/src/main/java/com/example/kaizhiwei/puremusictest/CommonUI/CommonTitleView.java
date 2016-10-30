package com.example.kaizhiwei.puremusictest.CommonUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 16/10/30.
 */



public class CommonTitleView extends LinearLayout implements View.OnClickListener {
    public interface onTitleClickListener{
        public void onLeftBtnClicked();
        public void onRightBtnClicked();
    }

    private Button leftBtn;
    private Button rightBtn;
    private TextView titleTextView;
    private onTitleClickListener mListener;

    public CommonTitleView(Context context) {
        super(context);
        initView(context);
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context){
        if (isInEditMode()) {
            return;
        }

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_commen_title,this);
        leftBtn = (Button) findViewById(R.id.leftBtn);
        rightBtn = (Button) findViewById(R.id.rightBtn);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
    }

    public void setTitleViewInfo(String strLeftBtn, String strTitle, String strRightBtn){
        leftBtn.setText(strLeftBtn);
        rightBtn.setText(strRightBtn);
        titleTextView.setText(strTitle);
    }

    public void setTitleViewListener(onTitleClickListener listener){
        mListener = listener;
    }

    public void setLeftBtnVisible(boolean bShow){
        leftBtn.setVisibility(bShow ? View.VISIBLE : View.GONE);
    }

    public void setRightBtnVisible(boolean bShow){
        rightBtn.setVisibility(bShow ? View.VISIBLE : View.GONE);
    }

    public void setTitleVisible(boolean bShow){
        titleTextView.setVisibility(bShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v == leftBtn){
            if(mListener != null){
                mListener.onLeftBtnClicked();
            }
        }
        else if(v == rightBtn){
            if(mListener != null){
                mListener.onRightBtnClicked();
            }
        }
    }
}
