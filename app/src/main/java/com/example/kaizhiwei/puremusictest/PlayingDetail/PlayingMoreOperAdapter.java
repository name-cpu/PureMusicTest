package com.example.kaizhiwei.puremusictest.PlayingDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.CommonUI.MyImageView;
import com.example.kaizhiwei.puremusictest.R;

import java.util.List;

/**
 * Created by kaizhiwei on 17/1/4.
 */
public class PlayingMoreOperAdapter extends BaseAdapter {
    private List<PlayingMoreOperItemData> mListItemData;
    private Context mContext;

    static class PlayingMoreOperItemData{
        PlayingMoreOperItemData(int normalResId, int pressedResId, String strInfo, boolean isEnable){
            this.normalResId = normalResId;
            this.pressedResId = pressedResId;
            this.strInfo = strInfo;
            this.isEnable = isEnable;
        }
        public int normalResId;
        public int pressedResId;
        public String strInfo;
        public boolean isEnable;
    }

    public PlayingMoreOperAdapter(Context context, List<PlayingMoreOperItemData> list){
        mListItemData = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mListItemData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(mListItemData == null || position < 0 || position >= mListItemData.size())
            return null;

        PlayingMoreOperItemData itemData = mListItemData.get(position);

        MoreOperAdapterHolder holder = null;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            View view = inflater.inflate(R.layout.playing_more_oper_item, null);
            holder = new MoreOperAdapterHolder(view);
            //holder.imMoreOperImage.setOnClickListener(mContext);
            view.setTag(holder);
            convertView = view;
        }
        else{
            holder = (MoreOperAdapterHolder)convertView.getTag();
        }
        holder.tvName.setText(itemData.strInfo);
        holder.mivIcon.setTag(itemData.normalResId, itemData.pressedResId);
        holder.mivIcon.setEnabled(itemData.isEnable);

        return convertView;
    }

    private class MoreOperAdapterHolder{
        public TextView tvName;
        public MyImageView mivIcon;

        public MoreOperAdapterHolder(View view){
            tvName = (TextView)view.findViewById(R.id.tvName);
            mivIcon = (MyImageView)view.findViewById(R.id.mivIcon);
        }
    }
}
