package com.example.kaizhiwei.puremusictest.NetAudio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import java.util.List;


/**
 * Created by 24820 on 2017/1/22.
 */
public class GridViewAdapter extends BaseAdapter {
    private List<GridViewAdapterItemData> mListData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mItemResId;

    static public class GridViewAdapterItemData{
        public String desc;
        public String strIconUrl;
        public String strkey;

        public GridViewAdapterItemData(){

        }
    }

    public GridViewAdapter(Context context, List<GridViewAdapterItemData> list, int itemResId){
        mContext = context;
        mListData = list;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemResId = itemResId;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridViewAdapterItemData entity = mListData.get(position);
        GridViewAdapterHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(mItemResId, null);
            holder = new GridViewAdapterHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (GridViewAdapterHolder)convertView.getTag();
        }

        Glide.with(convertView.getContext()).load(entity.strIconUrl).into(holder.ivIcon);
        holder.tvDesc.setText(entity.desc);
        return convertView;
    }

    private class GridViewAdapterHolder{
        public ImageView ivIcon;
        public TextView tvDesc;

        public GridViewAdapterHolder(View view){
            ivIcon = (ImageView)view.findViewById(R.id.ivIcon);
            tvDesc = (TextView)view.findViewById(R.id.tvDesc);
        }
    }
}
