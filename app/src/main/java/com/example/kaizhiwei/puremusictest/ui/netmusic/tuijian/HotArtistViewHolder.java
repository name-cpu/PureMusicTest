package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/7/9.
 */

public class HotArtistViewHolder extends RecyclerView.ViewHolder {
    public HotArtistViewHolder(View itemView) {
        super(itemView);
        ivReMenArtistPic = (ImageView)itemView.findViewById(R.id.ivReMenArtistPic);
        tvReMenArtistName = (TextView)itemView.findViewById(R.id.tvReMenArtistName);
    }

    ImageView ivReMenArtistPic;
    TextView tvReMenArtistName;
}
