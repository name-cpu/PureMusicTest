package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class ArtistIndexDialog extends Dialog{
    private IArtistIndexDialogListener mListener;
    private Map<String, String> mMapDatas;
    private List<String> mListDatas;
    private ArtistIndexAdapter mAdapter;
    private RecyclerView rvArtistIndex;
    private String mSelectValue;

    public interface IArtistIndexDialogListener{
        void onItemClick(int position, String key, String value);
    }

    public ArtistIndexDialog(Context context) {
        super(context);
        init();
    }

    protected ArtistIndexDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public ArtistIndexDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public IArtistIndexDialogListener getListener() {
        return mListener;
    }

    public void setListener(IArtistIndexDialogListener mListener) {
        this.mListener = mListener;
    }

    public void setSelectIndex(String key) {
        mSelectValue = mMapDatas.get(key);
    }

    private void init(){
        mListDatas = new ArrayList<>();
        mMapDatas = new HashMap<>();
        mMapDatas.put("", this.getContext().getString(R.string.hot_artist));
        mListDatas.add(this.getContext().getString(R.string.hot_artist));
        for(int i = 65;i < 65+26;i++){
            char ch = (char)i;
            mMapDatas.put(new String(new char[]{ch}), new String(new char[]{ch}));
            mListDatas.add(new String(new char[]{ch}));
        }
        mListDatas.add(this.getContext().getString(R.string.Other));
        mMapDatas.put("other", this.getContext().getString(R.string.Other));
    }

    public void setContentView(View view) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        super.setContentView(view);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        Window window = this.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 可以在此设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;

        window.setAttributes(wl);

        rvArtistIndex = (RecyclerView)findViewById(R.id.rvArtistIndex);
        mAdapter = new ArtistIndexAdapter();
        rvArtistIndex.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 4);
        rvArtistIndex.setLayoutManager(gridLayoutManager);
    }

    protected void onStart() {
        super.onStart();
    }

    /**
     * Called to tell you that you're stopping.
     */
    protected void onStop() {
        super.onStop();
    }

    public static class Builder {
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public ArtistIndexDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ArtistIndexDialog dialog = new ArtistIndexDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.artist_index_dialog, null);

            dialog.setContentView(layout);

            return dialog;
        }
    }

    private class ArtistIndexAdapter extends RecyclerView.Adapter<ArtistIndexViewHolder>{

        @Override
        public ArtistIndexViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ArtistIndexDialog.this.getContext()).inflate(R.layout.item_artist_index, parent, false);
            return new ArtistIndexViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ArtistIndexViewHolder holder, final int position) {
            String strValue = mListDatas.get(position);
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

                    String strKey = null;
                    String strValue = mListDatas.get(position);
                    Set<String> keys = mMapDatas.keySet();
                    for(String key : keys){
                        if(mMapDatas.get(key).equalsIgnoreCase(strValue)){
                            strKey = key;
                            break;
                        }
                    }

                    mListener.onItemClick(position, strKey, strValue);
                    ArtistIndexDialog.this.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mListDatas.size();
        }
    }

    private class ArtistIndexViewHolder extends RecyclerView.ViewHolder{
        private TextView tvArtistIndexName;

        public ArtistIndexViewHolder(View itemView) {
            super(itemView);
            tvArtistIndexName = (TextView)itemView.findViewById(R.id.tvArtistIndexName);
        }
    }
}
