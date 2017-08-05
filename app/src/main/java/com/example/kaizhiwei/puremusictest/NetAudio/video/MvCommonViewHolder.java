package com.example.kaizhiwei.puremusictest.NetAudio.video;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.widget.MaskImageView;

/**
 * Created by kaizhiwei on 17/8/5.
 */
public class MvCommonViewHolder extends RecyclerView.ViewHolder{
    public MaskImageView ivIcon;
    public TextView tvMain;
    public TextView tvSub;

    public MvCommonViewHolder(View itemView) {
        super(itemView);
        ivIcon = (MaskImageView)itemView.findViewById(R.id.ivIcon);
        tvMain = (TextView)itemView.findViewById(R.id.tvMain);
        tvSub = (TextView)itemView.findViewById(R.id.tvSub);
    }
}