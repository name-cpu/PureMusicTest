package com.example.kaizhiwei.puremusictest.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;
/**
 * Created by kaizhiwei on 17/9/9.
 */

public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivSongImage;
    public TextView tvFavoriteMain;
    public TextView tvFavoriteSub;
    public ImageView ibBtnEdit;
    public ImageView ibBtnDelete;
    public int position;

    public FavoriteViewHolder(View itemView) {
        super(itemView);
        ivSongImage = (ImageView)itemView.findViewById(R.id.ivSongImage);
        tvFavoriteMain = (TextView)itemView.findViewById(R.id.tvFavoriteMain);
        tvFavoriteSub = (TextView)itemView.findViewById(R.id.tvFavoriteSub);
        ibBtnEdit = (ImageView)itemView.findViewById(R.id.ibBtnEdit);
        ibBtnDelete = (ImageView)itemView.findViewById(R.id.ibBtnDelete);
    }
}
