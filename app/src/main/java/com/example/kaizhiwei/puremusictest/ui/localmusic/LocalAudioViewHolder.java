package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.widget.MaskImageView;

/**
 * Created by kaizhiwei on 17/8/14.
 */

public class LocalAudioViewHolder extends RecyclerView.ViewHolder {
    public TextView tvMain;
    public TextView tvSub;
    public TextView tvThird;
    public ImageView ivPic;
    public MaskImageView ivMore;

    public LocalAudioViewHolder(View itemView) {
        super(itemView);
        tvMain = (TextView)itemView.findViewById(R.id.tvMain);
        tvSub = (TextView)itemView.findViewById(R.id.tvSub);
        tvThird = (TextView)itemView.findViewById(R.id.tvThird);
        ivPic = (ImageView)itemView.findViewById(R.id.ivPic);
        ivMore = (MaskImageView)itemView.findViewById(R.id.ivMore);
    }
}
