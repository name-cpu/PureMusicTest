package com.example.kaizhiwei.puremusictest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.SongEntity;

import java.util.List;

/**
 * Created by kaizhiwei on 16/10/29.
 */
public class SongListAdapter extends BaseAdapter {
    private Context mContext;
    private List<SongEntity> mListEntrty;

    public SongListAdapter(Context context, List<SongEntity> list){
        mContext = context;
        mListEntrty = list;
    }

    @Override
    public int getCount() {
        return mListEntrty.size();
    }

    @Override
    public Object getItem(int position) {
        return mListEntrty.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SongListAdapterHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.song_item_view, null);
            holder = new SongListAdapterHolder();
            holder.textViewSongName = (TextView)convertView.findViewById(R.id.songName);
            holder.imageViewSong = (ImageView)convertView.findViewById(R.id.songIamgeView);
            convertView.setTag(holder);
        }
        else{
            holder = (SongListAdapterHolder)convertView.getTag();
        }
        SongEntity entity = mListEntrty.get(position);
        if(entity != null){
            holder.textViewSongName.setText(entity.mDisplayName);
            //holder.textViewSongName.setText(entity.mDisplayName);
        }


        return convertView;
    }

    class SongListAdapterHolder{
        public TextView textViewSongName;
        public ImageView imageViewSong;
    }
}
