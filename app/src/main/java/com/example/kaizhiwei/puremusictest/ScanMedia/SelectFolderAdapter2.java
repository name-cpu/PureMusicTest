package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by kaizhiwei on 16/12/25.
 */
public class SelectFolderAdapter2 extends BaseAdapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Context mContext;
    private List<FolderItemData> mListData;
    private Map<Integer, View> mMapViewItem;
    private ISelectFolderAdapter2Listener mListener;

    public interface ISelectFolderAdapter2Listener{
        void onFolderItemClick(SelectFolderAdapter2 adapter, int position);
        void onItemChecked(SelectFolderAdapter2 adapter, int position, boolean isChecked);
    }

    static public class FolderItemData{
        public String strFolderName;
        public boolean isSelecetd;
    }

    public SelectFolderAdapter2(Context context, List<FolderItemData> list){
        mContext = context;
        mListData = list;
        mMapViewItem = new HashMap<>();
    }

    public void setListener(ISelectFolderAdapter2Listener listener){
        mListener = listener;
    }

    public List<Integer> getCheckedItems(){
        List<Integer> listChecked = new ArrayList<>();
        for (Map.Entry<Integer, View> entry : mMapViewItem.entrySet()) {
            Object tag = entry.getValue().getTag();
            if(tag == null)
                continue;

            if(tag instanceof SelectFolderAdapterHolder2){
                SelectFolderAdapterHolder2 holderTag = (SelectFolderAdapterHolder2)tag;
                if(holderTag.cbFolder.isChecked()){
                    listChecked.add(entry.getKey());
                }
            }
        }

        return listChecked;
    }

    public void setListData(List<FolderItemData> list){
        mListData.clear();
        mListData = list;
        mMapViewItem.clear();
        notifyDataSetChanged();
    }

    public void setAllChcked(boolean isAllChecked){
        for(int i = 0;i < mListData.size();i++){
            mListData.get(i).isSelecetd = isAllChecked;
        }
        notifyDataSetChanged();
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

        SelectFolderAdapterHolder2 holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.select_folder_item2, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new SelectFolderAdapterHolder2(view);
            holder.tvFolderName.setOnClickListener(this);
            holder.cbFolder.setOnClickListener(this);
            view.setTag(holder);
            convertView = view;
        }
        else{
            holder = (SelectFolderAdapterHolder2) convertView.getTag();
        }
        holder.cbFolder.setTag(position);
        holder.tvFolderName.setTag(position);
        holder.tvFolderName.setText(itemData.strFolderName);
        holder.cbFolder.setChecked(itemData.isSelecetd);
        mMapViewItem.put(position, convertView);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if(mListener == null || v.getTag() == null)
            return;

        int position = (int)v.getTag();
        if(v.getClass().getName().equals("android.widget.TextView")){
            mListener.onFolderItemClick(this, position);
        }
        else if(v.getClass().getName().equals("android.widget.CheckBox")){
            CheckBox checkBox = (CheckBox)v;
            boolean isChecked = checkBox.isChecked();
            mListener.onItemChecked(this, position, isChecked);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getTag() == null)
            return;

        int position = (int)buttonView.getTag();
        if(mListener != null){
            mListener.onItemChecked(this, position, isChecked);
        }
    }


    private static class SelectFolderAdapterHolder2{
        public CheckBox cbFolder;
        public TextView tvFolderName;
        public SelectFolderAdapterHolder2(View view){
            cbFolder = (CheckBox)view.findViewById(R.id.cbFolder);
            tvFolderName = (TextView)view.findViewById(R.id.tvFolderName);
        }
    }
}

