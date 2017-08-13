package com.example.kaizhiwei.puremusictest.ui.netmusic.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.util.DeviceUtil;
import java.util.List;

/**
 * Created by kaizhiwei on 17/8/5.
 */
public class MvCommAdapter extends RecyclerView.Adapter<MvCommonViewHolder>{
    private Context context;
    private List<ItemData> mListDatas;

    public static class ItemData{
        public String strMain;
        public String strSub;
        public String pic;
        public String key;
    }

    public MvCommAdapter(Context context, List<ItemData> list){
        this.context = context;
        mListDatas = list;
    }

    @Override
    public MvCommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_module_content_item, parent, false);
        return new MvCommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MvCommonViewHolder holder, int position) {
        final ItemData mvInfo = mListDatas.get(position);
        holder.tvMain.setText(mvInfo.strMain);
        holder.tvSub.setText(mvInfo.strSub);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams )holder.ivIcon.getLayoutParams();
        layoutParams.height = 100* DeviceUtil.getDensity(context);
        holder.ivIcon.setLayoutParams(layoutParams);
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayMvActivity.class);
                intent.putExtra(PlayMvActivity.INTENT_MVID, mvInfo.key);
                context.startActivity(intent);
            }
        });
        Glide.with(context).load(mvInfo.pic).into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        if(mListDatas == null)
            return 0;

        return mListDatas.size();
    }
}