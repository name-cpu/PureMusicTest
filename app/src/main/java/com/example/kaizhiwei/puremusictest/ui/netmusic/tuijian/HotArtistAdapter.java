package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;

/**
 * Created by kaizhiwei on 17/7/9.
 */

public class HotArtistAdapter extends RecyclerView.Adapter<HotArtistViewHolder> {
    private ArtistGetListBean mArtistGetListBean;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public HotArtistAdapter(Context context, ArtistGetListBean bean){
        layoutInflater = LayoutInflater.from(context);
        mArtistGetListBean = bean;
        mContext = context;
    }

    @Override
    public HotArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_artistsel_remen, parent, false);
        return new HotArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotArtistViewHolder holder, final int position) {
        ArtistGetListBean.ArtistBean artistBean = mArtistGetListBean.getArtist().get(position);
        Glide.with(mContext).load(artistBean.getAvatar_middle()).placeholder(R.drawable.default_live_ic).into(holder.ivReMenArtistPic);
        holder.tvReMenArtistName.setText(artistBean.getName());
        holder.ivReMenArtistPic.setBackgroundResource(R.drawable.default_selector);
        holder.ivReMenArtistPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArtistSongListActivity.class);
                intent.putExtra(ArtistSongListActivity.ARTIST_BEAN, mArtistGetListBean.getArtist().get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mArtistGetListBean == null || mArtistGetListBean.getArtist() == null)
            return 0;

        return mArtistGetListBean.getArtist().size();
    }
}
