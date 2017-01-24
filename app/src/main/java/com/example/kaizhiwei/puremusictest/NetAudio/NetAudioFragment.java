package com.example.kaizhiwei.puremusictest.NetAudio;

import android.os.Handler;
import android.os.Message;
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
import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.List;
import com.example.kaizhiwei.puremusictest.NetAudio.Entity.*;
import com.example.kaizhiwei.puremusictest.Util.BaseHandler;
import com.example.kaizhiwei.puremusictest.Util.BusinessCode;
import com.viewpagerindicator.LinePageIndicator;


public class NetAudioFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private MySwipeRefreshLayout swlReflash;
    private ViewPager vpFocus;
    private List<View> mFocusListData;
    private AutoHeightGridView gvCatogary;
    private LinearLayout llMain;
    private NetPlazaIndexData mMusicData;
    private Handler mHandler = new Handler();
    private LinePageIndicator linePageIndicator;
    private float density;

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
        updateRecommandData();
        return rootView;
    }

    private void updateRecommandData(){
        NetGetPlazaIndexRequest recommandRequest = new NetGetPlazaIndexRequest();
        NetEngine.getInstance().asyncGetRecommandInfo(recommandRequest, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if (what != BusinessCode.BUSINESS_CODE_SUCCESS || msg.obj == null)
                    return;

                if(swlReflash.isRefreshing()){
                    swlReflash.setRefreshing(false);
                }
                NetPlazaIndexData data = (NetPlazaIndexData) msg.obj;
                mMusicData = data;
                llMain.removeAllViews();
                initFocusViewPager(data);
                initCategaryGridView(data);
                initDiyGridView(data);
                initMixLayout(data);
            }
        });
    }

    private void initFocusViewPager(NetPlazaIndexData data){
        if(data == null || data.mFocus == null)
            return;

        mFocusListData = new ArrayList<>();
        for(int i = 0;i < data.mFocus.listFocus.size();i++){
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

    private void initCategaryGridView(NetPlazaIndexData data){
        List<GridViewAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        for(int i = 0;i < data.mEntity.listEntityItem.size();i++){
            GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
            itemData.strMain = data.mEntity.listEntityItem.get(i).title;
            itemData.strIconUrl = data.mEntity.listEntityItem.get(i).icon;
            itemData.strkey = data.mEntity.listEntityItem.get(i).title;
            listTem.add(itemData);
        }

        GridViewAdapter adapter = new GridViewAdapter(NetAudioFragment.this.getActivity(), listTem, R.layout.fragment_netaduio_catogary_item);
        gvCatogary.setAdapter(adapter);
    }

    private void initDiyGridView(NetPlazaIndexData data){
        NetPlazaIndexData.ModuleItem moduleItem = data.mModule.getModuleItemByKey("diy");
        if(moduleItem == null)
            return;

        List<GridViewAdapter.GridViewAdapterItemData> listTem = new ArrayList<>();
        listTem = new ArrayList<>();
        for(int i = 0; i < data.mDiy.listDiyItem.size();i++){
            GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
            itemData.strMain = data.mDiy.listDiyItem.get(i).title;
            itemData.strIconUrl = data.mDiy.listDiyItem.get(i).pic;
            itemData.strkey = data.mDiy.listDiyItem.get(i).listid;
            listTem.add(itemData);
        }
        GridViewAdapter adapter = new GridViewAdapter(NetAudioFragment.this.getActivity(), listTem, R.layout.fragment_net_audio_recommand_item);
        ModuleItemLayout layout = new ModuleItemLayout(this.getActivity());
        layout.setModuleInfo(moduleItem);
        layout.setGridViewAdapter(adapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((int)(10*density), (int)(10*density), (int)(10*density), (int)(10*density));
        llMain.addView(layout, params);
    }

    private void initMixLayout(NetPlazaIndexData data){
        if(data == null || data.mModule == null)
            return;

        for(int i = 0;i < data.mModule.listModule.size();i++){
            NetPlazaIndexData.ModuleItem moduleItem = data.mModule.listModule.get(i);
            if(moduleItem.key.contains("mix")){

                NetPlazaIndexData.MixData mixData = data.findMixDataByModuleKey(moduleItem.key);
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
                GridViewAdapter adapter = new GridViewAdapter(NetAudioFragment.this.getActivity(), listTem, R.layout.fragment_net_audio_recommand_item);
                ModuleItemLayout layout = new ModuleItemLayout(this.getActivity());
                layout.setModuleInfo(moduleItem);
                layout.setGridViewAdapter(adapter);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)(10*density), (int)(10*density), (int)(10*density), (int)(10*density));
                llMain.addView(layout, params);
            }
        }
    }

    @Override
    public void onRefresh() {
        updateRecommandData();
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
            Glide.with(NetAudioFragment.this.getActivity()).load(mMusicData.mFocus.listFocus.get(position).randpic).into(view);
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
