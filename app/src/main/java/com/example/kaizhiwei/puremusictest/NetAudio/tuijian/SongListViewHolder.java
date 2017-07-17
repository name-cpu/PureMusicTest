package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public SongListViewHolder(View itemView) {
        super(itemView);
        llContent = (LinearLayout)itemView.findViewById(R.id.llContent);
        tvSongName = (TextView)itemView.findViewById(R.id.tvSongName);
        tvAlbumName = (TextView)itemView.findViewById(R.id.tvAlbumName);
        ivMV = (ImageView)itemView.findViewById(R.id.ivMV);
        ivSQ = (ImageView)itemView.findViewById(R.id.ivSQ);
    }
}
