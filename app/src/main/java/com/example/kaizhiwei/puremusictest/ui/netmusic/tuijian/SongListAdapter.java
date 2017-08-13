package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;

import java.util.List;

/**
 * Created by kaizhiwei on 17/7/16.
 */
public class SongListAdapter extends RecyclerView.Adapter<SongListViewHolder>{
    private Context mContext;
    private List<SongItemData> mDatas;
    private OnSongItemListener mListener;

    public static class SongItemData{
        public String title;
        public int has_mv;
        public int havehigh;
        public String song_id;
        public int has_mv_mobile;
        public String author;
        public String artist_id;
        public String album_title;
        public String album_id;
        public String songImageUrl;
        public int songRankLevelResId;
        public String songRankNum;
    }

    public interface OnSongItemListener{
        void onSongItemClick(SongListAdapter adapter, int position);
    }

    public SongListAdapter(Context context){
        mContext = context;
    }

    public OnSongItemListener getListener() {
        return mListener;
    }

    public void setListener(OnSongItemListener mListener) {
        this.mListener = mListener;
    }

    public List<SongItemData> getDatas() {
        return mDatas;
    }

    public void setDatas(List<SongItemData> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_artist_songlist, parent, false);
        return new SongListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongListViewHolder holder, final int position) {
        SongItemData itemData = mDatas.get(position);
        holder.tvSongName.setText(itemData.title);
        String strAlbumTitle = itemData.album_title;
        if(TextUtils.isEmpty(strAlbumTitle)){
            holder.tvAlbumName.setText(mContext.getString(R.string.solo));
        }
        else{
            holder.tvAlbumName.setText("《" + strAlbumTitle + "》");
        }

        if(itemData.has_mv == 1){
            holder.ivMV.setVisibility(View.VISIBLE);
        }
        else{
            holder.ivMV.setVisibility(View.GONE);
        }

        if(itemData.havehigh == 2){
            holder.ivSQ.setVisibility(View.VISIBLE);
        }
        else{
            holder.ivSQ.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(itemData.songImageUrl)){
            holder.rlImage.setVisibility(View.GONE);
        }
        else{
            holder.rlImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(itemData.songImageUrl).into(holder.ivSongImage);
            holder.tvRankNum.setText(itemData.songRankNum);
            holder.ivRankLevel.setImageResource(itemData.songRankLevelResId);
        }

        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onSongItemClick(SongListAdapter.this, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mDatas == null)
            return 0;

        return mDatas.size();
    }
}