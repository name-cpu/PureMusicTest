package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.CommonUI.MySwipeRefreshLayout;
import com.example.kaizhiwei.puremusictest.NetAudio.AutoHeightGridView;
import com.example.kaizhiwei.puremusictest.NetAudio.GridViewAdapter;
import com.example.kaizhiwei.puremusictest.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.example.kaizhiwei.puremusictest.bean.ActiveIndexBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.bean.DiyGeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaIndexBean;
import com.example.kaizhiwei.puremusictest.bean.SceneCategoryListBean;
import com.example.kaizhiwei.puremusictest.bean.ShowRedPointBean;
import com.example.kaizhiwei.puremusictest.bean.SugSceneBean;
import com.example.kaizhiwei.puremusictest.bean.UgcdiyBaseInfoBean;
import com.example.kaizhiwei.puremusictest.contract.ResetServerContract;
import com.example.kaizhiwei.puremusictest.presenter.ResetServerPresenter;
import com.viewpagerindicator.LinePageIndicator;


public class TuiJIanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ResetServerContract.View {
    private MySwipeRefreshLayout swlReflash;
    private ViewPager vpFocus;
    private List<View> mFocusListData;
    private AutoHeightGridView gvCatogary;
    private LinearLayout llMain;
    private PlazaIndexBean mMusicData;
    private Handler mHandler = new Handler();
    private LinePageIndicator linePageIndicator;
    private float density;
    private ResetServerContract.Presenter mPreserver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_netaudio, null);
        swlReflash = (MySwipeRefreshLayout)rootView.findViewById(R.id.swlReflash);
        swlReflash.setOnRefreshListener(this);
        swlReflash.setDistanceToTriggerSync(50);
        swlReflash.setColorSchemeResources(R.color.common_title_backgroundColor);
        density = this.getResources().getDisplayMetrics().density;
        vpFocus = (ViewPager)rootView.findViewById(R.id.vpFocus);
        llMain = (LinearLayout)rootView.findViewById(R.id.llMain);
        gvCatogary = (AutoHeightGridView)rootView.findViewById(R.id.gvCatogary);
        linePageIndicator = (LinePageIndicator)rootView.findViewById(R.id.linePageIndicator);

        mPreserver = new ResetServerPresenter(this);
        mPreserver.getPlazaIndex("android", "5.9.9.6", "ppzs", 2, "baidu.ting.plaza.index", "85FB11BCF66936DA386C6AC9CA228F2C", 8);
        return rootView;
    }

    private void initFocusView(String moduleKey){
        if(mMusicData == null || mMusicData.mFocus == null || mMusicData.mFocus.listFocus == null)
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

        List<GridViewAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int i = 0;i < mMusicData.mEntity.listEntityItem.size();i++){
            GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
            itemData.strMain = mMusicData.mEntity.listEntityItem.get(i).title;
            itemData.strIconUrl = mMusicData.mEntity.listEntityItem.get(i).icon;
            itemData.strkey = mMusicData.mEntity.listEntityItem.get(i).title;
            listTem.add(itemData);
        }

        GridViewAdapter adapter = new GridViewAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.fragment_netaduio_catogary_item);
        gvCatogary.setAdapter(adapter);
    }

    private void initDiyView(String moduleKey){
        if(mMusicData == null || mMusicData.mDiy == null || mMusicData.mDiy.listDiyItem == null
                || mMusicData.mModule == null || mMusicData.mModule.listModule == null)
            return;

        List<GridViewAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        listTem = new ArrayList<>();
        for(int i = 0; i < mMusicData.mDiy.listDiyItem.size();i++){
            GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
            itemData.strMain = mMusicData.mDiy.listDiyItem.get(i).title;
            itemData.strIconUrl = mMusicData.mDiy.listDiyItem.get(i).pic;
            itemData.strkey = mMusicData.mDiy.listDiyItem.get(i).listid;
            listTem.add(itemData);
        }
        GridViewAdapter adapter = new GridViewAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.fragment_net_audio_recommand_item);
        ModuleItemLayout layout = new ModuleItemLayout(this.getActivity());

        List<PlazaIndexBean.ModuleItem> moduleBeanList = mMusicData.mModule.listModule;
        if(moduleBeanList != null){
            for(int i = 0;i < moduleBeanList.size();i++){
                PlazaIndexBean.ModuleItem moduleBean = moduleBeanList.get(i);
                if(moduleBean.title.equalsIgnoreCase("diy")){
                    layout.setModuleInfo(moduleBean.picurl, moduleBean.title, moduleBean.title_more);
                    break;
                }
            }
        }

        layout.setGridViewAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((int)(10*density), (int)(10*density), (int)(10*density), (int)(10*density));
        llMain.addView(layout, params);
    }

    private void initMixView(String moduleKey){
        if(mMusicData == null || mMusicData.mListMix == null || mMusicData.mModule == null || mMusicData.mModule.listModule == null)
            return;

        for(int i = 0;i < mMusicData.mModule.listModule.size();i++){
            PlazaIndexBean.ModuleItem moduleBean = mMusicData.mModule.listModule.get(i);
            if(moduleBean == null)
                continue;

            if(moduleBean.key.equalsIgnoreCase(moduleKey)){
                PlazaIndexBean.MixBean mixData = mMusicData.findMixDataByModuleKey(moduleBean.key);
                if(mixData == null)
                    continue;

                List<GridViewAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
                listTem = new ArrayList<>();
                for(int j = 0; j < mixData.listMix.size();j++){
                    GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
                    itemData.strMain = mixData.listMix.get(j).title;
                    itemData.strSub = mixData.listMix.get(j).author;
                    itemData.strIconUrl = mixData.listMix.get(j).pic;
                    itemData.strkey = mixData.listMix.get(j).type_id;
                    listTem.add(itemData);
                }
                GridViewAdapter adapter = new GridViewAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.fragment_net_audio_recommand_item);
                ModuleItemLayout layout = new ModuleItemLayout(this.getActivity());
                layout.setModuleInfo(moduleBean.picurl, moduleBean.title, moduleBean.title_more);
                layout.setGridViewAdapter(adapter);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)(10*density), (int)(10*density), (int)(10*density), (int)(10*density));
                llMain.addView(layout, params);
            }
        }
    }

    private void initModView(String moduleName){
        if(mMusicData == null || mMusicData.mListMod == null || mMusicData.mModule == null || mMusicData.mModule.listModule == null)
            return;

        for(int i = 0;i < mMusicData.mModule.listModule.size();i++){
            PlazaIndexBean.ModuleItem moduleBean = mMusicData.mModule.listModule.get(i);
            if(moduleBean == null)
                continue;

            if(moduleBean.key.equalsIgnoreCase(moduleName)){
                PlazaIndexBean.ModBean modData = mMusicData.findModDataByModuleKey(moduleBean.key);
                if(modData == null)
                    continue;

                List<GridViewAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
                listTem = new ArrayList<>();
                for(int j = 0; j < modData.listMod.size();j++){
                    GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
                    itemData.strMain = modData.listMod.get(j).title;
                    itemData.strSub = modData.listMod.get(j).author;
                    itemData.strIconUrl = modData.listMod.get(j).pic;
                    itemData.strkey = modData.listMod.get(j).type_id;
                    listTem.add(itemData);
                }
                GridViewAdapter adapter = new GridViewAdapter(TuiJIanFragment.this.getActivity(), listTem, R.layout.fragment_net_audio_recommand_item);
                ModuleItemLayout layout = new ModuleItemLayout(this.getActivity());
                layout.setModuleInfo(moduleBean.picurl, moduleBean.title, moduleBean.title_more);
                layout.setGridViewAdapter(adapter);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)(10*density), (int)(10*density), (int)(10*density), (int)(10*density));
                llMain.addView(layout, params);
            }
        }
    }

    @Override
    public void onRefresh() {
        mPreserver.getPlazaIndex("android", "5.9.9.6", "ppzs", 2, "baidu.ting.plaza.index", "85FB11BCF66936DA386C6AC9CA228F2C", 8);
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

        Class clazz = this.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for(int i = 0;i < bean.mModule.listModule.size();i++){
            String moduleKey = bean.mModule.listModule.get(i).key;
            String moduleKeyCompare;
            if(moduleKey.toLowerCase().startsWith("mix")){
                moduleKeyCompare = "mix";
            }
            else if(moduleKey.toLowerCase().startsWith("mod")){
                moduleKeyCompare = "mod";
            }
            else{
                moduleKeyCompare = moduleKey;
            }

            for(int j = 0;j < methods.length;j++){
                String methodName = methods[j].getName().toLowerCase();
                if(methodName.equalsIgnoreCase("init" + moduleKeyCompare + "view")){
                    if(methods[j].isAccessible() == false){
                        methods[j].setAccessible(true);
                    }
                    try {
                        methods[j].invoke(this, moduleKey);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
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
