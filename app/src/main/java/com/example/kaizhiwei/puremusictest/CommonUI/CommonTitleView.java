package com.example.kaizhiwei.puremusictest.CommonUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
        void onLeftBtnClicked();
        void onRightBtnClicked();
    }

    private TextView leftBtn;
    private TextView rightBtn;
    private TextView titleTextView;
    private onTitleClickListener mListener;

    public CommonTitleView(Context context) {
        this(context, null, 0);
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_commen_title,this);
        leftBtn = (TextView) findViewById(R.id.leftBtn);
        rightBtn = (TextView) findViewById(R.id.rightBtn);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);

        if(attrs == null)
            return;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommonTitleView);
        String leftText = array.getString(R.styleable.CommonTitleView_leftText);
        if(leftText != null){
            leftBtn.setText(leftText);
        }

        int leftDrawable = array.getResourceId(R.styleable.CommonTitleView_leftDrawable, R.drawable.ic_launcher);
        if(leftDrawable != R.drawable.ic_launcher){
            Drawable drawable= getResources().getDrawable(leftDrawable);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            leftBtn.setCompoundDrawables(drawable, null, null, null);
        }

        String rightText = array.getString(R.styleable.CommonTitleView_rightText);
        if(rightText != null){
            rightBtn.setText(rightText);
        }

        int rightDrawable = array.getResourceId(R.styleable.CommonTitleView_rightDrawable, R.drawable.ic_launcher);
        if(rightDrawable != R.drawable.ic_launcher){
            Drawable drawable= getResources().getDrawable(rightDrawable);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            rightBtn.setCompoundDrawables(null, null, drawable, null);
        }

        String title = array.getString(R.styleable.CommonTitleView_title);
        if(!TextUtils.isEmpty(title)){
            titleTextView.setText(title);
        }
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
