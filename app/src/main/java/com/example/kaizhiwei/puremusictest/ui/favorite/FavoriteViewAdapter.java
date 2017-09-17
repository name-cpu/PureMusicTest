package com.example.kaizhiwei.puremusictest.ui.favorite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.ui.localmusic.LocalAudioViewHolder;

import java.util.List;

/**
 * Created by kaizhiwei on 17/9/16.
 */

public class FavoriteViewAdapter extends RecyclerView.Adapter<LocalAudioViewHolder> {
    private Context mContext;
    private List<MusicInfoDao> mDatas;
    private OnItemListener mListener;

    private long mSelectItemId = -1;
    public long getSelectItemId() {
        return mSelectItemId;
    }

    public void setSelectItemId(long mSelectItemId) {
        this.mSelectItemId = mSelectItemId;
        notifyDataSetChanged();
    }


    public interface OnItemListener{
        void onMoreClicked(int position);
        void onItemClicked(int position);
    }

    public FavoriteViewAdapter(Context context, List<MusicInfoDao> list){
        mContext = context;
        mDatas = list;
    }

    public MusicInfoDao getItemByPosition(int position){
        return mDatas.get(position);
    }

    public OnItemListener getListener() {
        return mListener;
    }

    public void setListener(OnItemListener mListener) {
        this.mListener = mListener;
    }


    @Override
    public LocalAudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_audio_allsong, parent, false);
        return new LocalAudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocalAudioViewHolder holder, final int position) {
        MusicInfoDao musicInfoDao = mDatas.get(position);
        holder.tvMain.setText(musicInfoDao.getTitle());
        holder.tvSub.setText(musicInfoDao.getArtist());
        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onMoreClicked(position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onItemClicked(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mListener != null){
                    mListener.onMoreClicked(position);
                }
                return true;
            }
        });

        if(mSelectItemId==(musicInfoDao.get_id())){
            holder.tvMain.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.tvSub.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        else{
            holder.tvMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
            holder.tvSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
        }
    }

    @Override
    public int getItemCount() {
        if(mDatas == null)
            return 0;

        return mDatas.size();
    }
}
