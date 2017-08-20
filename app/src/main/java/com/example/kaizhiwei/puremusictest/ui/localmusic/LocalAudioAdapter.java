package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaizhiwei.puremusictest.R;

import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableEntity;

/**
 * Created by kaizhiwei on 17/8/14.
 */

public class LocalAudioAdapter extends IndexableAdapter<LocalAudioAdapter.LocalAudioItemData> {
    private Context mContext;
    private int mContentResId;
    private String mSelectItemId = "-1";
    private ILocalAudioListener mListener;
    private List<LocalAudioItemData> mDatas;

    public interface ILocalAudioListener{
        void onMoreClick(LocalAudioAdapter adapter, int position);
    }

    public ILocalAudioListener getListener() {
        return mListener;
    }

    public void setListener(ILocalAudioListener mListener) {
        this.mListener = mListener;
    }

    public List<LocalAudioItemData> getDatas() {
        return mDatas;
    }

    public void setDatas(List<LocalAudioItemData> mDatas) {
        this.mDatas = mDatas;
        super.setDatas(mDatas);
    }

    public void setDatas(List<LocalAudioAdapter.LocalAudioItemData> datas, IndexCallback<LocalAudioAdapter.LocalAudioItemData> callback) {
        this.mDatas = datas;
        super.setDatas(datas, callback);
    }

    public LocalAudioItemData getItemData(int position){
        if(mDatas == null)
            return null;

        return mDatas.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_audio_index, parent, false);
        LocalAudioHeadViewHolder viewHolder = new LocalAudioHeadViewHolder(view);
        return viewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(mContentResId, parent, false);
        LocalAudioViewHolder viewHolder = new LocalAudioViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        LocalAudioHeadViewHolder viewHolder = (LocalAudioHeadViewHolder)holder;
        viewHolder.tvMain.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(final RecyclerView.ViewHolder holder, LocalAudioItemData entity) {
        LocalAudioViewHolder localAudioViewHolder = (LocalAudioViewHolder)holder;
        localAudioViewHolder.tvMain.setText(entity.getStrMain());
        localAudioViewHolder.tvSub.setText(entity.getStrSub());
        if(localAudioViewHolder.tvThird != null && !TextUtils.isEmpty(entity.getStrThird())){
            localAudioViewHolder.tvThird.setText(entity.getStrThird());
        }

        if(mSelectItemId.equalsIgnoreCase(entity.getId())){
            localAudioViewHolder.tvMain.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            localAudioViewHolder.tvSub.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        else{
            localAudioViewHolder.tvMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
            localAudioViewHolder.tvSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
        }

        localAudioViewHolder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if(mListener != null){
                    LocalAudioAdapter.this.
                    mListener.onMoreClick(LocalAudioAdapter.this, position);
                }
            }
        });
    }

    public static class LocalAudioItemData implements IndexableEntity{
        private String strMain;
        private String strSub;
        private String strThird;
        private String strPic;
        private String id;

        public String getPinYin() {
            return pinYin;
        }

        private String pinYin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String getFieldIndexBy() {
            return strMain;
        }

        @Override
        public void setFieldIndexBy(String indexField) {
            strMain = indexField;
        }

        @Override
        public void setFieldPinyinIndexBy(String pinyin) {
            pinYin = pinyin;
        }

        public String getStrMain() {
            return strMain;
        }

        public void setStrMain(String strMain) {
            this.strMain = strMain;
        }

        public String getStrSub() {
            return strSub;
        }

        public void setStrSub(String strSub) {
            this.strSub = strSub;
        }

        public String getStrThird() {
            return strThird;
        }

        public void setStrThird(String strThird) {
            this.strThird = strThird;
        }

        public String getStrPic() {
            return strPic;
        }

        public void setStrPic(String strPic) {
            this.strPic = strPic;
        }
    }

    public LocalAudioAdapter(Context context){
        mContext = context;
    }

    public int getContentResId() {
        return mContentResId;
    }

    public void setContentResId(int mResId) {
        this.mContentResId = mResId;
    }

    public void setSelectItemId(long id) {
        if(mDatas == null)
            return;

        mSelectItemId = id + "";
    }
}
