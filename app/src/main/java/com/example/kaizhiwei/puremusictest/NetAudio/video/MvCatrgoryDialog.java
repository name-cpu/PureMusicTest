package com.example.kaizhiwei.puremusictest.NetAudio.video;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseDialog;

import java.util.List;
import java.util.Set;

/**
 * Created by kaizhiwei on 17/8/5.
 */

public class MvCatrgoryDialog extends BaseDialog implements View.OnClickListener {
    private TextView tvAll;
    private TextView tvRecommand;
    private RecyclerView recyclerView;
    private List<String> mListDatas;
    private IMvCatrgoryDialogListener mListener;
    private String mSelectValue;

    public interface IMvCatrgoryDialogListener{
        void onItemClick(int position, String value);
    }

    public MvCatrgoryDialog(@NonNull Context context) {
        super(context);
    }

    public MvCatrgoryDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public IMvCatrgoryDialogListener getListener() {
        return mListener;
    }

    public void setListener(IMvCatrgoryDialogListener mListener) {
        this.mListener = mListener;
    }

    public String getSelectValue() {
        return mSelectValue;
    }

    public void setSelectValue(String mSelectValue) {
        this.mSelectValue = mSelectValue;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null && mListDatas != null && mListDatas.size() > 0){
            mListener.onItemClick(-1, mListDatas.get(0));
        }
        tvAll.setSelected(true);
        dismiss();
    }

    public void setData(List<String> mListDatas) {
        if(mListDatas == null || mListDatas.size() ==0)
            return;

        this.mListDatas = mListDatas;
        tvAll.setText(mListDatas.get(0));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        MvCategoryIndexAdapter adapter = new MvCategoryIndexAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        tvAll = (TextView)this.findViewById(R.id.tvAll);
        tvAll.setOnClickListener(this);
        tvAll.setSelected(true);
        tvRecommand = (TextView)this.findViewById(R.id.tvRecommand);
        recyclerView = (RecyclerView)this.findViewById(R.id.recyclerView);
    }

    private class MvCategoryIndexAdapter extends RecyclerView.Adapter<MvCategoryViewHolder>{

        @Override
        public MvCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MvCatrgoryDialog.this.getContext()).inflate(R.layout.item_artist_index, parent, false);
            return new MvCategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MvCategoryViewHolder holder, final int position) {
            final int realPosition = position + 1;
            String strValue = mListDatas.get(realPosition);
            if(!TextUtils.isEmpty(strValue) && strValue.equalsIgnoreCase(mSelectValue)){
                holder.tvArtistIndexName.setSelected(true);
            }
            else{
                holder.tvArtistIndexName.setSelected(false);
            }
            holder.tvArtistIndexName.setText(strValue);
            holder.tvArtistIndexName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener == null)
                        return;

                    tvAll.setSelected(false);
                    String strValue = mListDatas.get(realPosition);
                    mListener.onItemClick(position, strValue);
                    MvCatrgoryDialog.this.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mListDatas.size() -1;
        }
    }

    private class MvCategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tvArtistIndexName;

        public MvCategoryViewHolder(View itemView) {
            super(itemView);
            tvArtistIndexName = (TextView)itemView.findViewById(R.id.tvArtistIndexName);
        }
    }

    public static class Builder extends BaseDialog.Builder{

        public Builder(Context context) {
            super(context);
        }

        @Override
        public <T extends BaseDialog> T createDialog() {
            return (T) new MvCatrgoryDialog(mContext);
        }

        @Override
        public int getCustomeView() {
            return R.layout.mv_category_dialog;
        }
    }
}
