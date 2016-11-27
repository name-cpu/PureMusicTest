package com.example.kaizhiwei.puremusictest.Audio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaizhiwei on 16/11/26.
 */
public class MoreOperationAdapter extends BaseAdapter implements View.OnClickListener {
    static public class MoreOperationItemData{
        public int id;
        public String strText;
        public int resImage;
        public int resSelImage;
        boolean isSelect;

        public MoreOperationItemData(){

        }
    }

    public interface IMoreOperListener{
        public void onMoreItemClick(MoreOperationAdapter adapter, int tag);
    }

    private class MoreOperationItemHolder{
        public TextView tvMoreOperTitle;
        public ImageView imMoreOperImage;

        public MoreOperationItemHolder(View view){
            tvMoreOperTitle = (TextView)view.findViewById(R.id.tvMoreOperTitle);
            imMoreOperImage = (ImageView)view.findViewById(R.id.imMoreOperImage);
        }
    }

    private Context mContext;
    private List<MoreOperationItemData> mListItemData = new ArrayList<>();
    private Map<IMoreOperListener, Object> mMapListener;

    public MoreOperationAdapter(Context context, List<MoreOperationItemData> list){
        if(list != null){
            mListItemData.clear();
            mListItemData.addAll(list);
        }
        mContext = context;
    }

    public void registerListener(IMoreOperListener listener){
        if(mMapListener == null){
            mMapListener = new HashMap<>();
        }

        mMapListener.put(listener,listener);
    }

    public void unregisterListener(IMoreOperListener listener){
        if(mMapListener == null){
            mMapListener = new HashMap<>();
        }

        if(mMapListener.containsKey(listener)){
            mMapListener.remove(listener);
        }
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

        MoreOperationItemData itemData = mListItemData.get(position);

        MoreOperationItemHolder holder = null;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            View view = inflater.inflate(R.layout.more_operation_gridview_item, null);
            holder = new MoreOperationItemHolder(view);
            holder.imMoreOperImage.setOnClickListener(this);
            view.setTag(holder);
            convertView = view;
        }
        else{
            holder = (MoreOperationItemHolder)convertView.getTag();
        }
        holder.tvMoreOperTitle.setText(itemData.strText);
        holder.imMoreOperImage.setTag(itemData.id);

        if(itemData.isSelect && itemData.resSelImage != 0){
            holder.imMoreOperImage.setImageResource(itemData.resSelImage);
        }
        else{
            holder.imMoreOperImage.setImageResource(itemData.resImage);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        for(IMoreOperListener key : mMapListener.keySet()){
            if(key != null){
                key.onMoreItemClick(this, (int)v.getTag());
            }
        }
    }
}
