package com.example.kaizhiwei.puremusictest.Audio;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;

import java.util.List;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class PlayListDialog extends Dialog implements View.OnClickListener {
    private ImageView ivPlayMode;
    private ListView lvPlaylist;
    private TextView tvClearAll;
    private PlayListViewAdapter mPlaylistAdapter;

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
        tvClearAll.setOnClickListener(this);
        ivPlayMode.setOnClickListener(this);

        Window window = this.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 可以在此设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 50;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;

        window.setAttributes(wl);
    }

    public void setPlaylistData(List<MediaEntity> list){
        mPlaylistAdapter = new PlayListViewAdapter(this.getContext(), list);
        mPlaylistAdapter.notifyDataSetChanged();
        lvPlaylist.setAdapter(mPlaylistAdapter);
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

    @Override
    public void onClick(View v) {

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
