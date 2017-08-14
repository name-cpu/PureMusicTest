package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/12/10.
 */
public class PlayListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private List<PlayListItemData> listData;
    private Context mContext;
    private IPlayListViewAdapterListener mListener;

    @Override
    public void onClick(View v) {
        if(v.getTag() == null)
            return;

        if(mListener != null){
            mListener.onDeleteClick(this, (int)(v.getTag()));
        }
    }

    interface IPlayListViewAdapterListener{
        void onDeleteClick(PlayListViewAdapter adapter, int position);
    }

    static public class PlayListItemData {
        public MusicInfoDao MusicInfoDao;
        public boolean isPlaying;  //是否正在播放
        public boolean isCurPlay;  //是否是当前播放的音乐
    }

    public PlayListViewAdapter(Context context, List<MusicInfoDao> list){
        mContext = context;
        listData = new ArrayList<>();
        if(list != null){
            for(int i = 0;i < list.size();i++) {
                PlayListItemData itemData = new PlayListItemData();
                itemData.MusicInfoDao = list.get(i);
                itemData.isCurPlay = false;
                itemData.isPlaying = false;
                listData.add(itemData);
            }
        }
    }
    public void setPlaylistAdapterData(List<MusicInfoDao> list){
        if(list == null)
            return;

        listData.clear();
        for(int i = 0;i < list.size();i++) {
            PlayListItemData itemData = new PlayListItemData();
            itemData.MusicInfoDao = list.get(i);
            itemData.isCurPlay = false;
            itemData.isPlaying = false;
            listData.add(itemData);
        }
        notifyDataSetChanged();
    }

    public void setPlayListAdapterListener(PlayListViewAdapter.IPlayListViewAdapterListener listener){
        if(listener == null)
            return;

        mListener = listener;
    }

    public void removeItem(int position){
        if(position < 0 || position >= listData.size())
            return;

        listData.remove(position);
        notifyDataSetChanged();
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

        PlayListItemData itemData = listData.get(position);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        PlayListViewAdapterHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.playlist_item, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new PlayListViewAdapterHolder(view);
            view.setTag(holder);
            convertView = view;
            holder.ivDelete.setOnClickListener(this);
        }
        else{
            holder = (PlayListViewAdapterHolder) convertView.getTag();
        }

        if(itemData.isCurPlay) {
            if(itemData.isPlaying){
                holder.playingAnimView.startAnim();
            }
            else{
                holder.playingAnimView.stopAnim();
            }
            holder.playingAnimView.setVisibility(View.VISIBLE);
            holder.tvMainTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.tvSubTitle.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        else{
            holder.playingAnimView.stopAnim();
            holder.playingAnimView.setVisibility(View.GONE);
            holder.tvMainTitle.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
            holder.tvSubTitle.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
        }

        holder.ivDelete.setTag(position);
        holder.tvMainTitle.setText(itemData.MusicInfoDao.getTitle());
        holder.tvSubTitle.setText(itemData.MusicInfoDao.getAlbum());
        return convertView;
    }

    public void setItemPlayState(int curPlayMediaIndex, boolean isCurPlay, boolean isPlaying){
        if(listData == null || listData.size() <= 0 || curPlayMediaIndex < 0 || curPlayMediaIndex >= listData.size())
            return ;

        for(int j = 0;j < listData.size();j++){
            PlayListItemData itemData = listData.get(j);
            if(itemData == null)
                continue;

            itemData.isCurPlay = false;
            itemData.isPlaying = false;
        }
        listData.get(curPlayMediaIndex).isCurPlay = isCurPlay;
        listData.get(curPlayMediaIndex).isPlaying = isPlaying;
        notifyDataSetChanged();
    }

    private static class PlayListViewAdapterHolder{
        public ImageView ivDelete;
        public TextView tvMainTitle;
        public TextView tvSubTitle;
        public PlayingAnimView playingAnimView;

        public PlayListViewAdapterHolder(View view){
            ivDelete = (ImageView)view.findViewById(R.id.ivDelete);
            tvMainTitle = (TextView)view.findViewById(R.id.tvMainTitle);
            tvSubTitle = (TextView)view.findViewById(R.id.tvSubTitle);
            playingAnimView = (PlayingAnimView)view.findViewById(R.id.playingAnimView);
        }
    }
}
