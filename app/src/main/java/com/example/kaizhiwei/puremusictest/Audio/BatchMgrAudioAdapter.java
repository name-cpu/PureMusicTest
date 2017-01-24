package com.example.kaizhiwei.puremusictest.Audio;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/1/4.
 */
/**
 * Created by kaizhiwei on 16/12/25.
 */
public class BatchMgrAudioAdapter extends BaseAdapter implements View.OnClickListener{
    private Context mContext;
    private List<BatchMgrAudioItemData> mListData;
    private IOnBatchMgrAudioListener mListener;

    static public class BatchMgrAudioItemData{
        public MediaEntity mediaEntity;
        public boolean isSelected;
    }

    interface IOnBatchMgrAudioListener{
        void onBatchMgrAudioItemCheck(BatchMgrAudioAdapter adapter, int position);
    }

    public BatchMgrAudioAdapter(Context context, List<BatchMgrAudioItemData> list){
        mContext = context;
        mListData = list;
    }

    public List<Integer> getCheckedItems(){
        List<Integer> listChecked = new ArrayList<>();
        for(int i = 0;i < mListData.size();i++){
            if(mListData.get(i).isSelected){
                listChecked.add(i);
            }
        }
        return listChecked;
    }

    public List<MediaEntity> getCheckedMediaEntity(){
        List<MediaEntity> listChecked = new ArrayList<>();
        for(int i = 0;i < mListData.size();i++){
            if(mListData.get(i).isSelected){
                listChecked.add(mListData.get(i).mediaEntity);
            }
        }
        return listChecked;
    }

    public void setAllItemChecked(boolean isAllChecked){
        if(mListData == null)
            return;

        for(int i = 0;i < mListData.size();i++){
            BatchMgrAudioAdapter.BatchMgrAudioItemData itemData = mListData.get(i);
            itemData.isSelected = isAllChecked;
        }
        notifyDataSetChanged();
    }

    public void setBatchMgrAudioListener(IOnBatchMgrAudioListener listener){
        mListener = listener;
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

        BatchMgrAudioItemData itemData = mListData.get(position);
        if(itemData == null || itemData.mediaEntity == null)
            return null;

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        BatchMgrAudioAdapterHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.batchmgr_audio_item, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new BatchMgrAudioAdapterHolder(view);
            view.setTag(holder);
            holder.cbFolder.setOnClickListener(this);
            convertView = view;
        }
        else{
            holder = (BatchMgrAudioAdapterHolder) convertView.getTag();
        }
        holder.cbFolder.setTag(position);
        holder.cbFolder.setText(itemData.mediaEntity.title);
        holder.cbFolder.setChecked(itemData.isSelected);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        boolean isChecked = false;
        int position = (int)v.getTag();
        if(v.getClass().getName().equals("android.widget.CheckBox")){
            CheckBox checkBox = (CheckBox)v;
            isChecked = checkBox.isChecked();
            mListData.get(position).isSelected = isChecked;
            if(mListener != null){
                mListener.onBatchMgrAudioItemCheck(this, position);
            }
        }
        Log.i("weikaizhi", "onClick position: " + position + " isChecked: " + isChecked);
    }

    private static class BatchMgrAudioAdapterHolder{
        public CheckBox cbFolder;

        public BatchMgrAudioAdapterHolder(View view){
            cbFolder = (CheckBox)view.findViewById(R.id.cbFolder);
        }
    }
}

