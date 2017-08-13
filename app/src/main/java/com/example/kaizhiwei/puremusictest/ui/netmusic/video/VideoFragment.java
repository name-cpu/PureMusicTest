package com.example.kaizhiwei.puremusictest.ui.netmusic.video;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.kaizhiwei.puremusictest.ui.netmusic.moduleview.FocusView;
import com.example.kaizhiwei.puremusictest.ui.netmusic.moduleview.ModuleItemAdapter;
import com.example.kaizhiwei.puremusictest.ui.netmusic.moduleview.ModuleItemView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.Util.FadingEdgeUtil;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.bean.PlazaRecommIndexBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.PlazaRecommIndexContract;
import com.example.kaizhiwei.puremusictest.presenter.PlazaRecommIndexPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class VideoFragment extends MyBaseFragment implements PlazaRecommIndexContract.View, ModuleItemAdapter.ModuleItemListener, FocusView.FocusViewListener {
    private PlazaRecommIndexPresenter mPresenter;
    private static int VIEWPAGER_STYPE = 1;
    private static int GRIDVIEW_STYPE_13 = 13;
    private static int GRIDVIEW_STYPE_17 = 17;
    private static int GRIDVIEW_STYPE_18 = 18;

    private static int MV_CATEGORY_CONID = 10;

    @Bind(R.id.llContent)
    LinearLayout llContent;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_video;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_IF_CONTENT_SCROLLS);
        FadingEdgeUtil.setEdgeTopColor(scrollView, getResources().getColor(R.color.blackgray));
        FadingEdgeUtil.setEdgeBottomColor(scrollView, getResources().getColor(R.color.blackgray));
    }

    @Override
    protected void initData() {
        mPresenter = new PlazaRecommIndexPresenter(this);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if(mPresenter != null && isVisible){
            mPresenter.getPlazaRecommIndex(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, 0,
                    "baidu.ting.plaza.recommIndex", "daily", 1);
        }
        isFirst = true;
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetPlazaRecommIndexSuccess(PlazaRecommIndexBean bean) {
        if(bean == null || bean.getModules() == null)
            return;

        llContent.removeAllViews();
        for(int i = 0;i < bean.getModules().size();i++){
            PlazaRecommIndexBean.ModulesBean modulesBean = bean.getModules().get(i);
            if(modulesBean == null)
                continue;

            if(Integer.parseInt(modulesBean.getStyle_id()) == VIEWPAGER_STYPE){
                initVPView(modulesBean);
            }
            else if(Integer.parseInt(modulesBean.getStyle_id()) == GRIDVIEW_STYPE_13){
                initGridView_13(modulesBean);
            }
            else if(Integer.parseInt(modulesBean.getStyle_id()) == GRIDVIEW_STYPE_17){
                initGridView_17(modulesBean);
            }
            else if(Integer.parseInt(modulesBean.getStyle_id()) == GRIDVIEW_STYPE_18){
                initGridView_18(modulesBean);
            }
        }
    }

    private void initVPView(PlazaRecommIndexBean.ModulesBean modulesBean){
        if(modulesBean == null || modulesBean.getResult() == null)
            return;

        FocusView focusView = new FocusView(this.getActivity());
        focusView.setModulesBean(modulesBean);
        focusView.setListener(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llContent.addView(focusView, layoutParams);
    }

    private void initGridView_13(PlazaRecommIndexBean.ModulesBean modulesBean){
        final List<ModuleItemAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int i = 0; i < modulesBean.getResult().size();i++){
            ModuleItemAdapter.GridViewAdapterItemData itemData = new ModuleItemAdapter.GridViewAdapterItemData();
            itemData.strMain = modulesBean.getResult().get(i).getCon_title();
            itemData.strIconUrl = modulesBean.getResult().get(i).getPic_url();
            itemData.strkey = modulesBean.getResult().get(i).getCon_id();
            listTem.add(itemData);
        }
        ModuleItemAdapter adapter = new ModuleItemAdapter(VideoFragment.this.getActivity(), listTem, R.layout.layout_module_content_item);
        adapter.setImagehegiht(50* DeviceUtil.getDensity(this.getActivity()));
        adapter.setTextAligment(Gravity.CENTER);
        adapter.setImageScaleType(ImageView.ScaleType.CENTER);
        adapter.setListener(new ModuleItemAdapter.ModuleItemListener() {
            @Override
            public void onModuleItemClicked(ModuleItemAdapter adapter, int position, String strKey) {
                Intent intent = new Intent();
                if(Integer.parseInt(strKey) == MV_CATEGORY_CONID){
                    intent.setClass(VideoFragment.this.getActivity(), MvCategoryActivity.class);
                    VideoFragment.this.startActivity(intent);
                }
                else if(position == 0){
                    intent.setClass(VideoFragment.this.getActivity(), RecommMvActivity.class);
                    intent.putExtra(RecommMvActivity.INTENT_MID, 4);
                    intent.putExtra(RecommMvActivity.INTENT_TITLE, listTem.get(position).strMain);
                    VideoFragment.this.startActivity(intent);
                }
                else if(position == 1){
                    intent.setClass(VideoFragment.this.getActivity(), RecommMvActivity.class);
                    intent.putExtra(RecommMvActivity.INTENT_MID, 3);
                    intent.putExtra(RecommMvActivity.INTENT_TITLE, listTem.get(position).strMain);
                    VideoFragment.this.startActivity(intent);
                }
            }
        });
        adapter.setNeedPressStyle(false);

        ModuleItemView layout = new ModuleItemView(this.getActivity());
        String[] strs = modulesBean.getStyle_nums().split("\\*");
        int num = Integer.parseInt(strs[0]);
        int column = Integer.parseInt(strs[1]);

        layout.setNumColumns(column);
        layout.setModuleInfo("", modulesBean.getTitle(), modulesBean.getTitle_more());
        layout.setGridViewAdapter(adapter);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,15*DeviceUtil.getDensity(this.getActivity()),0,0);
        llContent.addView(layout, layoutParams);
    }

    private void initGridView_17(PlazaRecommIndexBean.ModulesBean modulesBean){
        List<ModuleItemAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int i = 0; i < modulesBean.getResult().size();i++){
            ModuleItemAdapter.GridViewAdapterItemData itemData = new ModuleItemAdapter.GridViewAdapterItemData();
            itemData.strMain = modulesBean.getResult().get(i).getCon_title();
            itemData.strIconUrl = modulesBean.getResult().get(i).getPic_url();
            itemData.strkey = modulesBean.getResult().get(i).getCon_id();
            listTem.add(itemData);
        }
        ModuleItemAdapter adapter = new ModuleItemAdapter(VideoFragment.this.getActivity(), listTem, R.layout.layout_module_content_item);
        adapter.setImagehegiht(100* DeviceUtil.getDensity(this.getActivity()));
        adapter.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adapter.setTextAligment(Gravity.CENTER);
        adapter.setListener(this);

        ModuleItemView layout = new ModuleItemView(this.getActivity());

        layout.setNumColumns(5);
        layout.setModuleInfo("", modulesBean.getTitle(), modulesBean.getTitle_more());
        layout.setGridViewAdapter(adapter);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,15*DeviceUtil.getDensity(this.getActivity()),0,0);
        llContent.addView(layout, layoutParams);
    }

    private void initGridView_18(PlazaRecommIndexBean.ModulesBean modulesBean){
        List<ModuleItemAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int i = 0; i < modulesBean.getResult().size();i++){
            ModuleItemAdapter.GridViewAdapterItemData itemData = new ModuleItemAdapter.GridViewAdapterItemData();
            itemData.strMain = modulesBean.getResult().get(i).getCon_title();
            itemData.strSub = modulesBean.getResult().get(i).getAuthor();
            itemData.strIconUrl = modulesBean.getResult().get(i).getPic_url();
            itemData.strkey = modulesBean.getResult().get(i).getCon_id();
            listTem.add(itemData);
        }
        ModuleItemAdapter adapter = new ModuleItemAdapter(VideoFragment.this.getActivity(), listTem, R.layout.layout_module_content_item);
        adapter.setImagehegiht(100* DeviceUtil.getDensity(this.getActivity()));
        adapter.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adapter.setListener(this);

        ModuleItemView layout = new ModuleItemView(this.getActivity());
        String[] strs = modulesBean.getStyle_nums().split("\\*");
        int num = Integer.parseInt(strs[0]);
        int column = Integer.parseInt(strs[1]);

        layout.setNumColumns(column);
        layout.setModuleInfo("", modulesBean.getTitle(), modulesBean.getTitle_more());
        layout.setGridViewAdapter(adapter);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,15*DeviceUtil.getDensity(this.getActivity()),0,0);
        llContent.addView(layout, layoutParams);
    }

    @Override
    public void onModuleItemClicked(ModuleItemAdapter adapter, int position, String strKey) {
        Intent intent = new Intent(VideoFragment.this.getActivity(), PlayMvActivity.class);
        intent.putExtra(PlayMvActivity.INTENT_MVID, strKey);
        startActivity(intent);
    }

    @Override
    public void onFocusItemClicked(FocusView view, int position, String strkey) {
        Intent intent = new Intent(VideoFragment.this.getActivity(), PlayMvActivity.class);
        intent.putExtra(PlayMvActivity.INTENT_MVID, strkey);
        startActivity(intent);
    }
}
