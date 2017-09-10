package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModuleProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kaizhiwei on 17/1/4.
 */
/**
 * Created by kaizhiwei on 16/12/25.
 */
public class BatchMgrAudioAdapter extends RecyclerView.Adapter<BatchMgrAudioAdapter.BatchMgrAudioAdapterHolder>{
    private Context mContext;
    private List<BatchMgrAudioItemData> mListData;
    private IOnBatchMgrAudioListener mListener;
    private MyItemTouchHelper.ItemDragListener mItemDragListener;

    static public class BatchMgrAudioItemData{
        public MusicInfoDao musicInfoDao;
        public PlaylistMemberDao playlistMemberDao;
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

    public void setItemDragListener(MyItemTouchHelper.ItemDragListener listener){
        mItemDragListener = listener;
    }

    @Override
    public BatchMgrAudioAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.batchmgr_audio_item, null);
        return new BatchMgrAudioAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(final BatchMgrAudioAdapterHolder holder, final int position) {
        BatchMgrAudioItemData itemData = mListData.get(position);
        if(itemData == null || itemData.musicInfoDao == null)
            return ;

        holder.cbFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                boolean isChecked = checkBox.isChecked();
                mListData.get(position).isSelected = isChecked;
                if(mListener != null){
                    mListener.onBatchMgrAudioItemCheck(BatchMgrAudioAdapter.this, position);
                }
            }
        });
        holder.cbFolder.setTag(position);
        holder.cbFolder.setText(itemData.musicInfoDao.getTitle());
        holder.cbFolder.setChecked(itemData.isSelected);
        holder.ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mItemDragListener != null){
                    mItemDragListener.onStartDrags(holder);
                }
                return true;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public BatchMgrAudioItemData getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public void itemMove(int from, int to) {
        mListData.get(from).playlistMemberDao.setPlay_order(to);
        mListData.get(to).playlistMemberDao.setPlay_order(from);
        Collections.swap(mListData, from, to);
        notifyItemMoved(from, to);
    }

    public class BatchMgrAudioAdapterHolder extends RecyclerView.ViewHolder{
        public CheckBox cbFolder;
        public ImageView ivDrag;

        public BatchMgrAudioAdapterHolder(View view){
            super(view);
            cbFolder = (CheckBox)view.findViewById(R.id.cbFolder);
            ivDrag = (ImageView)view.findViewById(R.id.ivDrag);
        }
    }
}

