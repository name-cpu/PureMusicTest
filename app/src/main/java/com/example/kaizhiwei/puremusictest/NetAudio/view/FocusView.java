package com.example.kaizhiwei.puremusictest.NetAudio.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.bean.PlazaRecommIndexBean;
import com.example.kaizhiwei.puremusictest.widget.MaskImageView;
import com.viewpagerindicator.LinePageIndicator;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public class FocusView extends RelativeLayout {
    private ViewPager vpFocus;
    private LinePageIndicator linePageIndicator;
    private FocusViewPagerAdapter mAdapter;
    private PlazaRecommIndexBean.ModulesBean modulesBean;
    private FocusViewListener mListener;

    public FocusView(@NonNull Context context) {
        this(context, null, 0);
    }

    public FocusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public interface FocusViewListener{
        void onFocusItemClicked(FocusView view, int position, String strkey);
    }

    public FocusViewListener getListener() {
        return mListener;
    }

    public void setListener(FocusViewListener mListener) {
        this.mListener = mListener;
    }

    public PlazaRecommIndexBean.ModulesBean getModulesBean() {
        return modulesBean;
    }

    public void setModulesBean(PlazaRecommIndexBean.ModulesBean modulesBean) {
        this.modulesBean = modulesBean;

        if(mAdapter == null){
            mAdapter = new FocusViewPagerAdapter(this.getContext(), modulesBean);
        }
        vpFocus.setAdapter(mAdapter);
        linePageIndicator.setViewPager(vpFocus);
    }

    private void initView(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_focusview, null, false);
        this.addView(view, layoutParams);
        vpFocus = (ViewPager)this.findViewById(R.id.vpFocus);
        linePageIndicator = (LinePageIndicator)this.findViewById(R.id.linePageIndicator);

        final float density = getResources().getDisplayMetrics().density;
        linePageIndicator.setSelectedColor(getResources().getColor(R.color.common_title_backgroundColor));
        linePageIndicator.setUnselectedColor(getResources().getColor(R.color.lightgray));
        linePageIndicator.setStrokeWidth(6 * density);
        linePageIndicator.setLineWidth(8 * density);
    }

    private class FocusViewPagerAdapter extends PagerAdapter {
        private PlazaRecommIndexBean.ModulesBean modulesBean;
        private Context mContext;

        public FocusViewPagerAdapter(Context context, PlazaRecommIndexBean.ModulesBean modulesBean){
            this.mContext = context;
            this.modulesBean = modulesBean;
        }

        @Override
        public int getCount() {
            if(modulesBean == null || modulesBean.getResult() == null)
                return 0;

            return modulesBean.getResult().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, final int position) {
            MaskImageView imageView = new MaskImageView(mContext);
            Glide.with(mContext).load(modulesBean.getResult().get(position).getPic_url()).into(imageView);
            container.addView(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onFocusItemClicked(FocusView.this, position, modulesBean.getResult().get(position).getCon_id());
                    }
                }
            });
            return imageView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
