package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.content.Context;
import android.util.Log;
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
public class SelectFolderAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private List<FolderItemData> mListData;

    static public class FolderItemData{
        public String strFolderName;
        public boolean isSelecetd;
    }

    public SelectFolderAdapter(Context context, List<FolderItemData> list){
        mContext = context;
        mListData = list;
    }

    public List<Integer> getCheckedItems(){
        List<Integer> listChecked = new ArrayList<>();
        for(int i = 0;i < mListData.size();i++){
            if(mListData.get(i).isSelecetd){
                listChecked.add(i);
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

        FolderItemData itemData = mListData.get(position);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SelectFolderAdapterHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.select_folder_item, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new SelectFolderAdapterHolder(view);
            view.setTag(holder);
            holder.cbFolder.setOnClickListener(this);
            convertView = view;
        }
        else{
            holder = (SelectFolderAdapterHolder) convertView.getTag();
        }
        holder.cbFolder.setTag(position);
        holder.cbFolder.setText(itemData.strFolderName);
        holder.cbFolder.setChecked(itemData.isSelecetd);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        boolean isChecked = false;
        int position = (int)v.getTag();
        if(v.getClass().getName().equals("android.widget.CheckBox")){
            CheckBox checkBox = (CheckBox)v;
            isChecked = checkBox.isChecked();
            mListData.get(position).isSelecetd = isChecked;
        }
        Log.i("weikaizhi", "onClick position: " + position + " isChecked: " + isChecked);
    }

    private static class SelectFolderAdapterHolder{
        public CheckBox cbFolder;

        public SelectFolderAdapterHolder(View view){
            cbFolder = (CheckBox)view.findViewById(R.id.cbFolder);
        }
    }
}

