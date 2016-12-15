package com.example.kaizhiwei.puremusictest.Audio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.R;

import java.util.List;

/**
 * Created by kaizhiwei on 16/12/10.
 */
public class PlayListViewAdapter extends BaseAdapter {
    private List<MediaEntity> listData;
    private Context mContext;

    public PlayListViewAdapter(Context context, List<MediaEntity> list){
        mContext = context;
        listData = list;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
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

        MediaEntity entity = listData.get(position);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        PlayListViewAdapterHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.playlist_item, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new PlayListViewAdapterHolder(view);
            view.setTag(holder);
            convertView = view;
            //holder.ivDelete.setOnClickListener(this);
        }
        else{
            holder = (PlayListViewAdapterHolder) convertView.getTag();
        }

        holder.tvMainTitle.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
        holder.tvSubTitle.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
        holder.tvMainTitle.setText(entity.title);
        holder.tvSubTitle.setText(entity.album);
        return convertView;
    }

    private static class PlayListViewAdapterHolder{
        public ImageView ivDelete;
        public TextView tvMainTitle;
        public TextView tvSubTitle;

        public PlayListViewAdapterHolder(View view){
            ivDelete = (ImageView)view.findViewById(R.id.ivDelete);
            tvMainTitle = (TextView)view.findViewById(R.id.tvMainTitle);
            tvSubTitle = (TextView)view.findViewById(R.id.tvSubTitle);
        }
    }
}
