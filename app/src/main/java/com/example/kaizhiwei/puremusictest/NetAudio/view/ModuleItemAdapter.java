package com.example.kaizhiwei.puremusictest.NetAudio.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.NetAudio.tuijian.ArtistSelActivity;
import com.example.kaizhiwei.puremusictest.NetAudio.tuijian.SongCategaryActivity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.widget.MaskImageView;

import java.util.List;


/**
 * Created by 24820 on 2017/1/22.
 */
public class ModuleItemAdapter extends BaseAdapter {
    private List<GridViewAdapterItemData> mListData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mItemResId;
    private int mTextAligment = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private int mImageHeight;
    private ImageView.ScaleType mImageScaleType = ImageView.ScaleType.FIT_CENTER;

    private ModuleItemListener mListener;
    private boolean needPressStyle = true;

    static public class GridViewAdapterItemData{
        public String strMain;
        public String strSub;
        public String strIconUrl;
        public String strkey;

        public GridViewAdapterItemData(){

        }
    }

    public interface ModuleItemListener{
        void onModuleItemClicked(ModuleItemAdapter adapter, int position, String strKey);
    }

    public ModuleItemAdapter(Context context, List<GridViewAdapterItemData> list, int itemResId){
        mContext = context;
        mListData = list;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemResId = itemResId;
    }

    public boolean isNeedPressStyle() {
        return needPressStyle;
    }

    public void setNeedPressStyle(boolean needPressStyle) {
        this.needPressStyle = needPressStyle;
    }

    public ModuleItemListener getListener() {
        return mListener;
    }

    public void setListener(ModuleItemListener mListener) {
        this.mListener = mListener;
    }


    public void setImagehegiht(int imagehegiht){
        mImageHeight = imagehegiht;
    }

    public ImageView.ScaleType getImageScaleType() {
        return mImageScaleType;
    }

    public void setImageScaleType(ImageView.ScaleType mImageScaleType) {
        this.mImageScaleType = mImageScaleType;
    }

    public int getmTextAligment() {
        return mTextAligment;
    }

    public void setmTextAligment(int mTextAligment) {
        this.mTextAligment = mTextAligment;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final GridViewAdapterItemData entity = mListData.get(position);
        GridViewAdapterHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(mItemResId, null);
            holder = new GridViewAdapterHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (GridViewAdapterHolder)convertView.getTag();
        }

        holder.ivIcon.setNeedPressedStyle(needPressStyle);
        holder.ivIcon.setScaleType(mImageScaleType);
//        holder.ivIcon.setMaxHeight(mImageHeight);
//        holder.ivIcon.setMinimumHeight(mImageHeight);
        holder.ivIcon.setTag(R.id.selected_view, entity.strkey);
        holder.ivIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onModuleItemClicked(ModuleItemAdapter.this, position, entity.strkey);
                }
            }
        });
        Glide.with(convertView.getContext()).load(entity.strIconUrl).into(holder.ivIcon);
        holder.tvMain.setText(entity.strMain);
        boolean isSubEmpty = TextUtils.isEmpty(entity.strSub);
        holder.tvSub.setVisibility(isSubEmpty ? View.GONE : View.VISIBLE);
        if(!isSubEmpty){
            holder.tvMain.setMaxLines(1);
            holder.tvSub.setText(entity.strSub);
        }
        else{
            holder.tvMain.setMaxLines(2);
        }
        holder.tvMain.setGravity(mTextAligment);
        return convertView;
    }

    private class GridViewAdapterHolder{
        public MaskImageView ivIcon;
        public TextView tvMain;
        public TextView tvSub;

        public GridViewAdapterHolder(View view){
            ivIcon = (MaskImageView)view.findViewById(R.id.ivIcon);
            tvMain = (TextView)view.findViewById(R.id.tvMain);
            tvSub = (TextView)view.findViewById(R.id.tvSub);
        }
    }
}
