package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/8/14.
 */

public class LocalAudioHeadViewHolder extends RecyclerView.ViewHolder {
    public TextView tvMain;

    public LocalAudioHeadViewHolder(View itemView) {
        super(itemView);
        tvMain = (TextView)itemView.findViewById(R.id.tvMain);
    }
}
