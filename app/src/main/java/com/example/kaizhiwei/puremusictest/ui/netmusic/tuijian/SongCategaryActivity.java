package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.AllTagInfoBean;
import com.example.kaizhiwei.puremusictest.bean.HotTagInfoBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.SongTagContract;
import com.example.kaizhiwei.puremusictest.presenter.SongTagPresenter;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewSpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/7/15.
 */

public class SongCategaryActivity extends MyBaseActivity implements SongTagContract.View {
    private SongTagPresenter mPresenter;
    private SongCategaryHotTagAdapter mHotTagAdapter;
    private List<Integer> mHeadResIds;
    private HotTagInfoBean mHotTagInfoBean;

    @Bind(R.id.rvHotTag)
    RecyclerView rvHotTag;
    @Bind(R.id.commonTitle)
    CommonTitleView commonTitle;
    @Bind(R.id.rvSongTag)
    RecyclerView rvSongTag;

    private SongCategaryTagAdapter mSongTagAdapter;
    private List<ItemData> mListTags = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_song_categary;
    }

    @Override
    public void initPresenter() {
        mPresenter = new SongTagPresenter(this);
    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvHotTag.setLayoutManager(gridLayoutManager);
        rvHotTag.addItemDecoration(new RecyclerViewSpaceDecoration(0,0,0,5* DeviceUtil.getDensity(this)));
        mHotTagAdapter = new SongCategaryHotTagAdapter();
        rvHotTag.setAdapter(mHotTagAdapter);

        commonTitle.setTitleVisible(false);
        commonTitle.setRightBtnVisible(false);
        commonTitle.setTitleViewListener(new CommonTitleView.onTitleClickListener() {
            @Override
            public void onLeftBtnClicked() {
                finish();
            }

            @Override
            public void onRightBtnClicked() {

            }
        });

        gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mListTags == null)
                    return 1;

                if(mListTags.get(position).itemType == 0)
                    return 4;
                return 1;
            }
        });
        rvSongTag.setLayoutManager(gridLayoutManager);
        mSongTagAdapter = new SongCategaryTagAdapter();
        rvSongTag.setAdapter(mSongTagAdapter);
        rvSongTag.addItemDecoration(new RecyclerViewSpaceDecoration(5*DeviceUtil.getDensity(this),0,5*DeviceUtil.getDensity(this), 5*DeviceUtil.getDensity(this)));
    }

    @Override
    public void initData() {
        mPresenter.getAllTagInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.PPZS, 2, "baidu.ting.tag.getAllTag", "json");

        mHeadResIds = new ArrayList<>();
        mHeadResIds.add(R.drawable.ic_classify_img01);
        mHeadResIds.add(R.drawable.ic_classify_img02);
        mHeadResIds.add(R.drawable.ic_classify_img03);
        mHeadResIds.add(R.drawable.ic_classify_img04);
        mHeadResIds.add(R.drawable.ic_classify_img05);
        mHeadResIds.add(R.drawable.ic_classify_img06);
        mHeadResIds.add(R.drawable.ic_classify_img07);
        mHeadResIds.add(R.drawable.ic_classify_img08);

        mPresenter.getHotTagInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.PPZS, 2, "baidu.ting.tag.getHotTag", "json", mHeadResIds.size());
    }

    @Override
    public void onError(String strErrMsg) {
        showToast(strErrMsg);
    }

    @Override
    public void onGetTagInfoSuccess(AllTagInfoBean bean) {
        if(bean == null || bean.getTaglist() == null)
            return;

        mHotTagAdapter.notifyDataSetChanged();

        mListTags.clear();
        for(int i = 0;i <bean.getTags().size();i++){
            ItemData itemData = new ItemData();
            itemData.itemType = 0;
            itemData.headerInfo = bean.getTags().get(i);
            itemData.songTagInfo = null;
            mListTags.add(itemData);

            for(int j = 0;j < bean.getTaglist().get(i).size();j++){
                ItemData itemData1 = new ItemData();
                itemData1.itemType = 1;
                itemData1.songTagInfo = bean.getTaglist().get(i).get(j);
                mListTags.add(itemData1);
            }
        }
        mSongTagAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetHotTagInfoSuccess(HotTagInfoBean bean) {
        if(bean == null || bean.getTaglist() == null)
            return;

        mHotTagInfoBean = bean;
        mHotTagAdapter.notifyDataSetChanged();
    }

    private class ItemData{
        int itemType;
        String headerInfo;
        AllTagInfoBean.SongTagInfo songTagInfo;
    }

    private class SongCategaryHotTagAdapter extends RecyclerView.Adapter<SongCategaryBigVH>{

        @Override
        public SongCategaryBigVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SongCategaryActivity.this).inflate(R.layout.item_song_categoray_big, parent, false);
            return new SongCategaryBigVH(view);
        }

        @Override
        public void onBindViewHolder(SongCategaryBigVH holder, final int position) {
            holder.tvTag.setText(mHotTagInfoBean.getTaglist().get(position).getTitle());
            int resIsPos = position%mHeadResIds.size();
            holder.ivTag.setImageResource(mHeadResIds.get(resIsPos));
            holder.ivTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SongCategaryActivity.this, TagSongListActivity.class);
                    intent.putExtra(TagSongListActivity.INTENT_TAGNAME, mHotTagInfoBean.getTaglist().get(position).getTitle());
                    SongCategaryActivity.this.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mHotTagInfoBean == null || mHotTagInfoBean.getTaglist() == null)
                return 0;

            return mHotTagInfoBean.getTaglist().size();
        }
    }

    private class SongCategaryBigVH extends RecyclerView.ViewHolder{
        public ImageView ivTag;
        public TextView tvTag;

        public SongCategaryBigVH(View itemView) {
            super(itemView);
            ivTag = (ImageView)itemView.findViewById(R.id.ivTag);
            tvTag = (TextView)itemView.findViewById(R.id.tvTag);
        }
    }

    private class SongCategaryTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0){
                View view = LayoutInflater.from(SongCategaryActivity.this).inflate(R.layout.item_song_categary_taghead, parent, false);
                return new SongCategaryTagHeadVH(view);
            }
            else if(viewType ==1){
                View view = LayoutInflater.from(SongCategaryActivity.this).inflate(R.layout.item_song_categary_tag, parent, false);
                return new SongCategoryTagVH(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemData itemData = mListTags.get(position);
            if(holder instanceof SongCategaryTagHeadVH){
                SongCategaryTagHeadVH headVH = (SongCategaryTagHeadVH)holder;
                headVH.tvTagHead.setText(itemData.headerInfo);
            }
            else if(holder instanceof SongCategoryTagVH){
                SongCategoryTagVH vh = (SongCategoryTagVH)holder;
                vh.tvTagName.setText(itemData.songTagInfo.getTitle());
                vh.tvTagName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SongCategaryActivity.this, TagSongListActivity.class);
                        intent.putExtra(TagSongListActivity.INTENT_TAGNAME, itemData.songTagInfo.getTitle());
                        SongCategaryActivity.this.startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mListTags.size();
        }

        public int getItemViewType(int position) {
            return mListTags.get(position).itemType;
        }
    }

    private class SongCategaryTagHeadVH extends RecyclerView.ViewHolder{
        public TextView tvTagHead;

        public SongCategaryTagHeadVH(View itemView) {
            super(itemView);
            tvTagHead = (TextView)itemView.findViewById(R.id.tvTagHead);
        }
    }

    private class SongCategoryTagVH extends RecyclerView.ViewHolder{
        public TextView tvTagName;

        public SongCategoryTagVH(View itemView) {
            super(itemView);
            tvTagName = (TextView)itemView.findViewById(R.id.tvTagName);
        }
    }
}
