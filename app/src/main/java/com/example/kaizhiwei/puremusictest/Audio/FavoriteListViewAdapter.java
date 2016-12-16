package com.example.kaizhiwei.puremusictest.Audio;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/12/10.
 */
public class FavoriteListViewAdapter extends BaseAdapter {
    private List<FavoriteEntity> listData;
    private Context mContext;

    public FavoriteListViewAdapter(Context context, List<FavoriteEntity> list){
        mContext = context;
        listData = list;

        FavoriteEntity addOne = new FavoriteEntity();
        addOne.favoriteType = FavoriteEntity.DEFAULT_ADDONE_TYPE;
        addOne.strFavoriteName = "新建歌单";

        if(listData == null){
            listData = new ArrayList<>();
        }
        list.add(0, addOne);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < 0 || position >= listData.size())
            return null;

        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(listData == null || position >= listData.size())
            return null;

        FavoriteEntity entity = listData.get(position);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FavoriteListViewAdapterHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.favorite_dialog_item, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new FavoriteListViewAdapterHolder(view);
            view.setTag(holder);
            convertView = view;
        }
        else{
            holder = (FavoriteListViewAdapterHolder) convertView.getTag();
        }

        holder.tvFavoriteMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
        holder.tvFavoriteSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
        holder.tvFavoriteMain.setText(entity.strFavoriteName);
        holder.tvFavoriteSub.setText(entity.favoriteMusicNum + "首");
        holder.setFavoriteType((int) entity.favoriteType);
        
        return convertView;
    }

    private static class FavoriteListViewAdapterHolder{
        public ImageView ivSongImage;
        public TextView tvFavoriteMain;
        public TextView tvFavoriteSub;
        public ImageView ibBtnMore;

        public FavoriteListViewAdapterHolder(View view){
            ivSongImage = (ImageView)view.findViewById(R.id.ivSongImage);
            tvFavoriteMain = (TextView)view.findViewById(R.id.tvFavoriteMain);
            tvFavoriteSub = (TextView)view.findViewById(R.id.tvFavoriteSub);
            ibBtnMore = (ImageView)view.findViewById(R.id.ibBtnMore);
        }

        public void setFavoriteType(int favoriteType){
            if(favoriteType == FavoriteEntity.DEFAULT_ADDONE_TYPE){
                ivSongImage.setImageResource(R.drawable.ic_mymusic_add_nor);
                tvFavoriteSub.setVisibility(View.GONE);
                ibBtnMore.setVisibility(View.GONE);

                LinearLayout.LayoutParams param =  (LinearLayout.LayoutParams)tvFavoriteMain.getLayoutParams();
                if(param != null){
                    param.weight = 2;
                    param.gravity = Gravity.CENTER_VERTICAL;
                    tvFavoriteMain.setLayoutParams(param);
                    tvFavoriteMain.setGravity(Gravity.CENTER_VERTICAL);
                }
            }
            else if(favoriteType == FavoriteEntity.DEFAULT_FAVORITE_TYPE){
                ivSongImage.setImageResource(R.drawable.ic_mymusic_like);
            }
            else{
                ivSongImage.setImageResource(R.drawable.ic_mymusic_list_item);
            }
        }
    }
}
