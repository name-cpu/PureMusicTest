package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.CommonUI.MySwipeRefreshLayout;
import com.example.kaizhiwei.puremusictest.widget.AutoHeightGridView;
import com.example.kaizhiwei.puremusictest.ui.netmusic.moduleview.ModuleItemAdapter;
import com.example.kaizhiwei.puremusictest.ui.netmusic.moduleview.ModuleItemView;
import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.kaizhiwei.puremusictest.util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.util.FadingEdgeUtil;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.bean.ActiveIndexBean;
import com.example.kaizhiwei.puremusictest.bean.DiyGeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaIndexBean;
import com.example.kaizhiwei.puremusictest.bean.SceneCategoryListBean;
import com.example.kaizhiwei.puremusictest.bean.ShowRedPointBean;
import com.example.kaizhiwei.puremusictest.bean.SugSceneBean;
import com.example.kaizhiwei.puremusictest.bean.UgcdiyBaseInfoBean;
import com.example.kaizhiwei.puremusictest.contract.ResetServerContract;
import com.example.kaizhiwei.puremusictest.presenter.ResetServerPresenter;
import com.viewpagerindicator.LinePageIndicator;

import butterknife.Bind;


public class TuiJIanFragment extends MyBaseFragment implements SwipeRefreshLayout.OnRefreshListener, ResetServerContract.View {
    @Bind(R.id.swlReflash)
    MySwipeRefreshLayout swlReflash;

    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.vpFocus)
    ViewPager vpFocus;

    @Bind(R.id.gvCatogary)
    AutoHeightGridView gvCatogary;

    @Bind(R.id.llMain)
    LinearLayout llMain;

    @Bind(R.id.linePageIndicator)
    LinePageIndicator linePageIndicator;

    private PlazaIndexBean mMusicData;
    private Handler mHandler = new Handler();
    private List<View> mFocusListData;

    private ResetServerContract.Presenter mPreserver;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_netaudio;
    }

    @Override
    protected void initView() {
        swlReflash.setOnRefreshListener(this);
        swlReflash.setDistanceToTriggerSync(50);
        swlReflash.setColorSchemeResources(R.color.common_title_backgroundColor);

        FadingEdgeUtil.setEdgeTopColor(scrollView, getResources().getColor(R.color.blackgray));
        FadingEdgeUtil.setEdgeBottomColor(scrollView, getResources().getColor(R.color.blackgray));
    }

    @Override
    protected void initData() {
        mPreserver = new ResetServerPresenter(this);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if(isVisible && mPreserver != null){
            mPreserver.getPlazaIndex("android", "5.9.9.6", "ppzs", 2, "baidu.ting.plaza.index", "85FB11BCF66936DA386C6AC9CA228F2C", 8);
        }
        isFirst = true;
    }

    private void initFocusView(PlazaIndexBean.ModuleItem moduleItem, PlazaIndexBean.FocusBean focusBean) {
        if (focusBean == null || focusBean.listFocus == null || moduleItem == null)
            return;

        mFocusListData = new ArrayList<>();
        for(int i = 0;i < mMusicData.mFocus.listFocus.size();i++){
            ImageView imageView = new ImageView(this.getActivity());
            mFocusListData.add(imageView);
        }
        FocusPagerAdapter adapter = new FocusPagerAdapter();
        vpFocus.setAdapter(adapter);
        vpFocus.setOffscreenPageLimit(4);

        linePageIndicator.setViewPager(vpFocus);
        final float density = getResources().getDisplayMetrics().density;
        linePageIndicator.setSelectedColor(getResources().getColor(R.color.common_title_backgroundColor));
        linePageIndicator.setUnselectedColor(getResources().getColor(R.color.lightgray));
        linePageIndicator.setStrokeWidth(6 * density);
        linePageIndicator.setLineWidth(8 * density);
    }

    private void initEntryView(String moduleKey){
        if(mMusicData == null || mMusicData.mEntity == null || mMusicData.mEntity.listEntityItem == null)
            return;

        List<ModuleItemAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int i = 0;i < mMusicData.mEntity.listEntityItem.size();i++){
            ModuleItemAdapter.GridViewAdapterItemData itemData = new ModuleItemAdapter.GridViewAdapterItemData();
            itemData.strMain = mMusicData.mEntity.listEntityItem.get(i).title;
            itemData.strIconUrl = mMusicData.mEntity.listEntityItem.get(i).icon;
            itemData.strkey = mMusicData.mEntity.listEntityItem.get(i).title;
            listTem.add(itemData);
        }

        ModuleItemAdapter adapter = new ModuleItemAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.fragment_netaduio_catogary_item);
        gvCatogary.setAdapter(adapter);
    }

    private void initDiyView(PlazaIndexBean.ModuleItem moduleItem, PlazaIndexBean.DiyBean diyBean){
        if(diyBean == null || diyBean.listDiyItem == null || moduleItem == null)
            return;

        float density = DeviceUtil.getDensity(this.getActivity());
        List<ModuleItemAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int i = 0; i < diyBean.listDiyItem.size();i++){
            ModuleItemAdapter.GridViewAdapterItemData itemData = new ModuleItemAdapter.GridViewAdapterItemData();
            itemData.strMain = diyBean.listDiyItem.get(i).title;
            itemData.strIconUrl = diyBean.listDiyItem.get(i).pic;
            itemData.strkey = diyBean.listDiyItem.get(i).type_id;
            listTem.add(itemData);
        }
        ModuleItemAdapter adapter = new ModuleItemAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.layout_module_content_item);
        adapter.setImagehegiht(100* DeviceUtil.getDensity(this.getActivity()));
        adapter.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

        ModuleItemView layout = new ModuleItemView(this.getActivity());
        layout.setModuleInfo(moduleItem.picurl, moduleItem.title, moduleItem.title_more);
        layout.setGridViewAdapter(adapter);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int)(20*density), 0, 0);
        llMain.addView(layout, params);
    }

    private void initMixView(PlazaIndexBean.ModuleItem moduleItem, PlazaIndexBean.MixBean mixData){
        if(mixData == null || mixData.listMix == null || moduleItem == null)
            return;

        float density = DeviceUtil.getDensity(this.getActivity());
        List<ModuleItemAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int j = 0; j < mixData.listMix.size();j++){
            ModuleItemAdapter.GridViewAdapterItemData itemData = new ModuleItemAdapter.GridViewAdapterItemData();
            itemData.strMain = mixData.listMix.get(j).title;
            itemData.strSub = TextUtils.isEmpty(mixData.listMix.get(j).desc) ? mixData.listMix.get(j).author : mixData.listMix.get(j).desc;
            itemData.strIconUrl = mixData.listMix.get(j).pic;
            itemData.strkey = mixData.listMix.get(j).type_id;
            listTem.add(itemData);
        }
        ModuleItemAdapter adapter = new ModuleItemAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.layout_module_content_item);
        ModuleItemView layout = new ModuleItemView(this.getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int topMargin = 0;

        //新音乐导航
        if(moduleItem.style == 13){
            adapter.setImagehegiht((int)(40*density));
            adapter.setTextAligment(Gravity.CENTER);
            adapter.setNeedPressStyle(false);
            layout.setTitleVisible(false);
            topMargin = (int)(5*density);
        }
        //最新mv
        else if(moduleItem.style == 11){
            adapter.setImagehegiht((int)(75*density));
            adapter.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.setNumColumns(2);
            layout.setVerticalSpace(0);
            topMargin = (int)(20*density);
        }
        //今日热点
        else if(moduleItem.style == 3){
            adapter = new ModuleItemAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.layout_daily_hot);
            adapter.setImagehegiht((int)(80*density));
            adapter.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            adapter.setNeedPressStyle(false);
            layout.setNumColumns(1);
            layout.setTitleVisible(false);
            topMargin = 0;
        }
        else{
            adapter.setImagehegiht((int)(100*density));
            adapter.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            topMargin = (int)(20*density);
        }

        layout.setModuleInfo(moduleItem.picurl, moduleItem.title, moduleItem.title_more);
        layout.setGridViewAdapter(adapter);
        if(!TextUtils.isEmpty(moduleItem.style_nums)){
            String[] style_nums = moduleItem.style_nums.split("\\*");
            if(style_nums != null && style_nums.length >=2){
                int column = 1;
                int row = 1;
                int temp = Integer.parseInt(style_nums[1]);
                if(temp > 0){
                    column = temp;
                }

                temp = Integer.parseInt(style_nums[0]);
                if(temp > 0){
                    row = temp;
                }

                layout.setNumColumns(column);
            }
        }

        params.setMargins(0, topMargin, 0, 0);
        llMain.addView(layout, params);
    }

    private void initModView(PlazaIndexBean.ModuleItem moduleItem, PlazaIndexBean.ModBean modData){
        if(modData == null || modData.listMod == null || moduleItem == null)
            return;

        float density = DeviceUtil.getDensity(this.getActivity());
        List<ModuleItemAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int j = 0; j < modData.listMod.size();j++){
            ModuleItemAdapter.GridViewAdapterItemData itemData = new ModuleItemAdapter.GridViewAdapterItemData();
            itemData.strMain = modData.listMod.get(j).title;
            itemData.strSub = modData.listMod.get(j).author;
            itemData.strIconUrl = modData.listMod.get(j).pic;
            itemData.strkey = modData.listMod.get(j).type_id;
            listTem.add(itemData);
        }
        ModuleItemAdapter adapter = new ModuleItemAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.layout_module_content_item);
        adapter.setImagehegiht((int)(100*density));
        adapter.setImageScaleType(ImageView.ScaleType.CENTER_CROP);

        ModuleItemView layout = new ModuleItemView(this.getActivity());
        layout.setModuleInfo(moduleItem.picurl, moduleItem.title, moduleItem.title_more);
        layout.setGridViewAdapter(adapter);

        if(!TextUtils.isEmpty(moduleItem.style_nums)){
            String[] style_nums = moduleItem.style_nums.split("\\*");
            if(style_nums != null && style_nums.length >=2){
                int column = 1;
                int row = 1;
                int temp = Integer.parseInt(style_nums[1]);
                if(temp > 0){
                    column = temp;
                }

                temp = Integer.parseInt(style_nums[0]);
                if(temp > 0){
                    row = temp;
                }

                layout.setNumColumns(column);
            }
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int)(20*density), 0, 0);
        llMain.addView(layout, params);
    }

    private void initRecSongView(PlazaIndexBean.ModuleItem moduleItem, PlazaIndexBean.RecSongBean recsongBean) {
        if (recsongBean == null || recsongBean.listRecSongItem == null || moduleItem == null)
            return;

        float density = DeviceUtil.getDensity(this.getActivity());
        List<ModuleItemAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int j = 0; j < recsongBean.listRecSongItem.size() && j < 3;j++){
            ModuleItemAdapter.GridViewAdapterItemData itemData = new ModuleItemAdapter.GridViewAdapterItemData();
            itemData.strMain = recsongBean.listRecSongItem.get(j).title;
            itemData.strSub = recsongBean.listRecSongItem.get(j).author;
            itemData.strIconUrl = recsongBean.listRecSongItem.get(j).pic_premium;
            itemData.strkey = recsongBean.listRecSongItem.get(j).song_id;
            listTem.add(itemData);
        }
        ModuleItemAdapter adapter = new ModuleItemAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.layout_recsong_item);
        adapter.setImagehegiht((int)(70*density));
        adapter.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adapter.setNeedPressStyle(false);

        ModuleItemView layout = new ModuleItemView(this.getActivity());
        layout.setModuleInfo(moduleItem.picurl, moduleItem.title, moduleItem.title_more);
        layout.setGridViewAdapter(adapter);
        layout.setNumColumns(1);
        layout.setVerticalSpace((int)(2*density));

        if(!TextUtils.isEmpty(moduleItem.style_nums)){
            String[] style_nums = moduleItem.style_nums.split("\\*");
            if(style_nums != null && style_nums.length >=2){
                int column = 1;
                int row = 1;
                int temp = Integer.parseInt(style_nums[1]);
                if(temp > 0){
                    column = temp;
                }

                temp = Integer.parseInt(style_nums[0]);
                if(temp > 0){
                    row = temp;
                }

                layout.setNumColumns(column);
            }
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, (int)(20*density), 0, 0);
        llMain.addView(layout, params);
    }

    public void transformMModule(PlazaIndexBean.ModuleItem moduleItem, Object obj){
        if(obj instanceof PlazaIndexBean.MixBean){
            PlazaIndexBean.MixBean mixBean = (PlazaIndexBean.MixBean)obj;
            initMixView(moduleItem, mixBean);
        }
        else if(obj instanceof PlazaIndexBean.ModBean){
            PlazaIndexBean.ModBean modBean = (PlazaIndexBean.ModBean)obj;
            initModView(moduleItem, modBean);
        }
        else if(obj instanceof PlazaIndexBean.RadioBean){

        }
        else if(obj instanceof PlazaIndexBean.KingBean){

        }
        else if(obj instanceof PlazaIndexBean.NewSongBean){

        }
        else if(obj instanceof PlazaIndexBean.SceneBean){

        }
        else if(obj instanceof PlazaIndexBean.ShowListBean){

        }
        else if(obj instanceof PlazaIndexBean.RecSongBean){
            PlazaIndexBean.RecSongBean recSongBean = (PlazaIndexBean.RecSongBean)obj;
            initRecSongView(moduleItem, recSongBean);
        }
        else if(obj instanceof PlazaIndexBean.DiyBean){
            PlazaIndexBean.DiyBean diyBean = (PlazaIndexBean.DiyBean)obj;
            initDiyView(moduleItem, diyBean);
        }
        else if(obj instanceof PlazaIndexBean.FocusBean){
            PlazaIndexBean.FocusBean focusBean = (PlazaIndexBean.FocusBean)obj;
            initFocusView(moduleItem, focusBean);
        }
    }

    @Override
    public void onRefresh() {
        if(mPreserver != null){
            mPreserver.getPlazaIndex("android", "5.9.9.6", "ppzs", 2, "baidu.ting.plaza.index", "85FB11BCF66936DA386C6AC9CA228F2C", 8);
        }
    }

    @Override
    public void onGetCatogaryListSuccess(SceneCategoryListBean bean) {

    }

    @Override
    public void onGetActiveIndexSuccess(ActiveIndexBean bean) {

    }

    @Override
    public void onShowRedPointSuccess(ShowRedPointBean bean) {

    }

    @Override
    public void onGetSugSceneSuccess(SugSceneBean bean) {

    }

    @Override
    public void onGetPlazaIndexSuccess(PlazaIndexBean bean) {
        if(bean == null || bean.mModule == null || bean.mModule.listModule == null)
            return;

        if(swlReflash.isRefreshing()){
            swlReflash.setRefreshing(false);
        }
        llMain.removeAllViews();
        mMusicData = bean;

        List<PlazaIndexBean.ModuleItem> listModules = bean.mModule.listModule;
        Collections.sort(listModules, new Comparator<PlazaIndexBean.ModuleItem>() {
            @Override
            public int compare(PlazaIndexBean.ModuleItem lhs, PlazaIndexBean.ModuleItem rhs) {
                return lhs.pos - rhs.pos;
            }
        });

        for(int i = 0;i < listModules.size();i++){
            String moduleKey = listModules.get(i).key;
            PlazaIndexBean.ModuleItem moduleItem = mMusicData.getModuleItemByModuleKey(moduleKey);
            Object moduleContent =  mMusicData.getModuleContentByModuleKey(moduleKey);
            transformMModule(moduleItem, moduleContent);
        }
    }

    @Override
    public void onGetUgcdiyBaseInfoSuccess(UgcdiyBaseInfoBean baseInfoBean) {

    }

    @Override
    public void onGetDiyGeDanInfoSuccess(DiyGeDanInfoBean bean) {

    }

    @Override
    public void onError(String strErrMsg) {

    }

    private class FocusPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mMusicData.mFocus.listFocus.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView)mFocusListData.get(position);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(TuiJIanFragment.this.getActivity()).load(mMusicData.mFocus.listFocus.get(position).randpic).into(view);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mFocusListData.get(position));
        }
    }

    /**
     * Created by 24820 on 2017/1/24.
     */
    public static class NetAudioBaseLayout {
    }
}
