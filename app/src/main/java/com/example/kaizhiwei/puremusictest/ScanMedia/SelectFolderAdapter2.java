package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaizhiwei on 16/12/25.
 */
public class SelectFolderAdapter2 extends BaseAdapter implements View.OnClickListener {
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

            if(tag instanceof SelectFolderAdapterHolder){
                SelectFolderAdapterHolder holderTag = (SelectFolderAdapterHolder)tag;
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

        SelectFolderAdapterHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.select_folder_item2, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new SelectFolderAdapterHolder(view);
            holder.tvFolderName.setOnClickListener(this);
            holder.cbFolder.setOnClickListener(this);
            view.setTag(holder);
            convertView = view;
        }
        else{
            holder = (SelectFolderAdapterHolder) convertView.getTag();
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
        if(v.getTag() == null)
            return;

        int position = (int)v.getTag();
        if((v instanceof TextView) == true && mListener != null){
            mListener.onFolderItemClick(this, position);
        }
        else if((v instanceof CheckBox) && mListener != null){
            mListener.onItemChecked(this, position, ((CheckBox)v).isChecked());
        }
    }

    private static class SelectFolderAdapterHolder{
        public CheckBox cbFolder;
        public TextView tvFolderName;
        public SelectFolderAdapterHolder(View view){
            cbFolder = (CheckBox)view.findViewById(R.id.cbFolder);
            tvFolderName = (TextView)view.findViewById(R.id.tvFolderName);
        }
    }
}

