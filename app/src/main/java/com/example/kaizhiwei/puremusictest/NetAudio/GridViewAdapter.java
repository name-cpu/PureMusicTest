package com.example.kaizhiwei.puremusictest.NetAudio;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
        public String strMain;
        public String strSub;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

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

        holder.ivIcon.setTag(R.id.selected_view, entity.strkey);
        holder.ivIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(position == 0){
                    Intent intent = new Intent(HomeActivity.getInstance(), ArtistSelActivity.class);
                    HomeActivity.getInstance().startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent(HomeActivity.getInstance(), SongCategaryActivity.class);
                    HomeActivity.getInstance().startActivity(intent);
                }
            }
        });
        Glide.with(convertView.getContext()).load(entity.strIconUrl).into(holder.ivIcon);
        holder.tvMain.setText(entity.strMain);
        boolean isSubEmpty = TextUtils.isEmpty(entity.strSub);
        holder.tvSub.setVisibility(isSubEmpty ? View.GONE : View.VISIBLE);
        if(!isSubEmpty){
            holder.tvSub.setText(entity.strSub);
        }
        return convertView;
    }

    private class GridViewAdapterHolder{
        public ImageView ivIcon;
        public TextView tvMain;
        public TextView tvSub;

        public GridViewAdapterHolder(View view){
            ivIcon = (ImageView)view.findViewById(R.id.ivIcon);
            tvMain = (TextView)view.findViewById(R.id.tvMain);
            tvSub = (TextView)view.findViewById(R.id.tvSub);
        }
    }
}
