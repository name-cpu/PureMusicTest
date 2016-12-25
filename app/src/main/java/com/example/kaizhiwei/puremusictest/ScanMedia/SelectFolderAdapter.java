package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaizhiwei on 16/12/25.
 */
public class SelectFolderAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mListData;
    private Map<Integer, View> mMapViewItem;

    public SelectFolderAdapter(Context context, List<String> list){
        mContext = context;
        mListData = list;

        mMapViewItem = new HashMap<>();
    }

    public List<Integer> getCheckedItems(){
        List<Integer> listChecked = new ArrayList<>();
        for (Map.Entry<Integer, View> entry : mMapViewItem.entrySet()) {
            Object tag = entry.getValue().getTag();
            if(tag == null)
                continue;

            if(tag instanceof SelectFolderAdapterHolder){
                SelectFolderAdapterHolder holderTag = (SelectFolderAdapterHolder)tag;
                if(holderTag.cbFolder.isChecked()){
                    listChecked.add(entry.getKey());
                }
            }
        }

        return listChecked;
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
        if(mListData == null || position >= mListData.size())
            return null;

        String strFolderName = mListData.get(position);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SelectFolderAdapterHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.select_folder_item, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new SelectFolderAdapterHolder(view);
            view.setTag(holder);
            convertView = view;
        }
        else{
            holder = (SelectFolderAdapterHolder) convertView.getTag();
        }
        holder.cbFolder.setText(strFolderName);
        mMapViewItem.put(position, convertView);
        return convertView;
    }

    private static class SelectFolderAdapterHolder{
        public CheckBox cbFolder;

        public SelectFolderAdapterHolder(View view){
            cbFolder = (CheckBox)view.findViewById(R.id.cbFolder);
        }
    }
}

