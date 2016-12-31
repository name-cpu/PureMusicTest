package com.example.kaizhiwei.puremusictest.Audio;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class PlayListDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView ivPlayMode;
    private ListView lvPlaylist;
    private TextView tvClearAll;
    private TextView tvPromt;
    private PlayListViewAdapter mPlaylistAdapter;
    private IPlayListDialogListener mDialogListener;
    private PlayListViewAdapter.IPlayListViewAdapterListener mPlaylistAdapterListener = new PlayListViewAdapter.IPlayListViewAdapterListener(){
        @Override
        public void onDeleteClick(PlayListViewAdapter adapter, int position) {
            if(adapter == null || position < 0 || mDialogListener == null)
                return;

            mDialogListener.onDeleteClick(adapter, position);
        }
    };

    interface IPlayListDialogListener{
        void onDeleteClick(PlayListViewAdapter adapter, int position);
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
        void onClearPlaylist();
    }

    public PlayListDialog(Context context) {
        super(context);
    }

    protected PlayListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public PlayListDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setContentView(View view) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        super.setContentView(view);
        tvClearAll = (TextView) view.findViewById(R.id.tvClearAll);
        ivPlayMode = (ImageView) view.findViewById(R.id.ivPlayMode);
        lvPlaylist = (ListView) view.findViewById(R.id.lvPlaylist);
        lvPlaylist.setOnItemClickListener(this);
        tvClearAll.setOnClickListener(this);
        ivPlayMode.setOnClickListener(this);
        tvPromt = (TextView)view.findViewById(R.id.tvPromt);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        Window window = this.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 可以在此设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = (int)(50*displayMetrics.density);
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = (int)(300*displayMetrics.density);
        wl.gravity = Gravity.BOTTOM;

        window.setAttributes(wl);
    }

    public void setPlaylistData(List<MediaEntity> list){
        mPlaylistAdapter = new PlayListViewAdapter(this.getContext(), list);
        mPlaylistAdapter.notifyDataSetChanged();
        lvPlaylist.setAdapter(mPlaylistAdapter);
        mPlaylistAdapter.setPlayListAdapterListener(mPlaylistAdapterListener);

        if(list.size() == 0){
            tvPromt.setVisibility(View.VISIBLE);
            lvPlaylist.setVisibility(View.GONE);
        }
        else{
            tvPromt.setVisibility(View.GONE);
            lvPlaylist.setVisibility(View.VISIBLE);
        }
    }

    public void setItemPlayState(MediaEntity mediaEntity,boolean isCurPlay, boolean isPlaying){
        if(mPlaylistAdapter == null || mediaEntity == null)
            return;

        mPlaylistAdapter.setItemPlayState(mediaEntity, isCurPlay, isPlaying);
    }

    public void setPlayListAdapterListener(PlayListDialog.IPlayListDialogListener listener){
        if(listener == null || mPlaylistAdapter == null)
            return;

        mDialogListener = listener;
    }

    protected void onStart() {
        super.onStart();
    }

    public void removeItem(int position){
        if(position < 0 || mPlaylistAdapter == null)
            return;

        mPlaylistAdapter.removeItem(position);
        int iSize = mPlaylistAdapter.getCount();
        if(iSize == 0){
            tvPromt.setVisibility(View.VISIBLE);
            lvPlaylist.setVisibility(View.GONE);
        }
        else{
            tvPromt.setVisibility(View.GONE);
            lvPlaylist.setVisibility(View.VISIBLE);
        }
        mPlaylistAdapter.notifyDataSetChanged();
    }

    /**
     * Called to tell you that you're stopping.
     */
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if(v == tvClearAll){
            if(mDialogListener != null){
                mDialogListener.onClearPlaylist();
                mPlaylistAdapter.setPlaylistAdapterData(new ArrayList<MediaEntity>());
                tvPromt.setVisibility(View.VISIBLE);
                lvPlaylist.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == null || view == null || position < 0)
            return;

        if(mDialogListener == null)
            return;

        mDialogListener.onItemClick(parent, view, position, id);
    }

    public static class Builder {
        private Context context;


        public Builder(Context context) {
            this.context = context;
        }

        public PlayListDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final PlayListDialog dialog = new PlayListDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.playlist_dialog, null);

            dialog.setContentView(layout);

            return dialog;
        }
    }
}
