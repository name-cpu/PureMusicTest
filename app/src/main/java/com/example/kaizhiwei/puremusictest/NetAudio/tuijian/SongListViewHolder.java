package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class SongListViewHolder extends RecyclerView.ViewHolder {
    public TextView tvSongName;
    public TextView tvAlbumName;
    public LinearLayout llContent;
    public ImageView ivMV;
    public ImageView ivSQ;
    public ImageView ivSongImage;
    public ImageView ivRankLevel;
    public TextView tvRankNum;
    public RelativeLayout rlImage;
    public SongListViewHolder(View itemView) {
        super(itemView);
        llContent = (LinearLayout)itemView.findViewById(R.id.llContent);
        tvSongName = (TextView)itemView.findViewById(R.id.tvSongName);
        tvAlbumName = (TextView)itemView.findViewById(R.id.tvAlbumName);
        ivMV = (ImageView)itemView.findViewById(R.id.ivMV);
        ivSQ = (ImageView)itemView.findViewById(R.id.ivSQ);
        ivSongImage = (ImageView)itemView.findViewById(R.id.ivSongImage);
        ivRankLevel = (ImageView)itemView.findViewById(R.id.ivRankLevel);
        ivSongImage = (ImageView)itemView.findViewById(R.id.ivSongImage);
        tvRankNum = (TextView)itemView.findViewById(R.id.tvRankNum);
        rlImage = (RelativeLayout)itemView.findViewById(R.id.rlImage);
    }
}
