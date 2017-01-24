package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;

import java.util.List;

/**
 * Created by kaizhiwei on 16/12/25.
 */
public class CrumbView extends HorizontalScrollView {

    private int LIGHT_COLOR, DARK_COLOR;
    private Resources mRes;
    private LinearLayout mContainer;
    private List<String> mListCrumbView;

    public CrumbView(Context context) {
        super(context);
        mRes = context.getResources();
        initView(context);
    }

    public CrumbView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRes = context.getResources();
        initView(context);
    }

    private void initView(Context context) {
        this.setHorizontalScrollBarEnabled(false);
        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setPadding(10, 0,
               10, 0);
        mContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mContainer);
    }

    public void setCrumbViewData(List<String> list){
        mListCrumbView = list;
        updateCrumbs();
    }


    private void updateCrumbs() {
        // 面包屑的数量
        int numCrumbs = mListCrumbView.size();

        mContainer.removeAllViews();
        for(int i = 0; i < numCrumbs; i++){
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.crumb_item_layout, null);
            TextView tv = (TextView) itemView.findViewById(R.id.crumb_name);
            tv.setText(mListCrumbView.get(i) + " > ");
            itemView.setTag(mListCrumbView.get(i));
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mContainer.addView(itemView);
        }

        //调整可见性
        for (int i = 0; i < numCrumbs; i++) {
            final View child = mContainer.getChildAt(i);
            // 高亮
            highLightIndex(child, !(i < numCrumbs - 1));
        }

        // 滑动到最后一个
        post(new Runnable() {
            @Override
            public void run() {
                fullScroll(ScrollView.FOCUS_RIGHT);
            }
        });
    }

    public void highLightIndex(View view, boolean highLight) {
//        TextView text = (TextView) view.findViewById(R.id.crumb_name);
//        ImageView image = (ImageView) view.findViewById(R.id.crumb_icon);
//        if (highLight) {
//            text.setTextColor(LIGHT_COLOR);
//            image.setVisibility(View.GONE);
//        } else {
//            text.setTextColor(DARK_COLOR);
//            image.setVisibility(View.VISIBLE);
//        }
    }
}