package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.CommonUI.MyImageView;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class PlayListDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private MyImageView ivPlayMode;
    private List<List<Integer>> listPLaymodeImageRes;
    private ListView lvPlaylist;
    private TextView tvClearAll;
    private TextView tvPromt;
    private TextView tvClose;
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

    public interface IPlayListDialogListener{
        void onDeleteClick(PlayListViewAdapter adapter, int position);
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
        void onClearPlaylist();
        void onChangePlayMode();
    }

    public PlayListDialog(Context context) {
        super(context);
        init();
    }

    protected PlayListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public PlayListDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        listPLaymodeImageRes = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.bt_list_order_normal);
        list.add(R.drawable.bt_list_order_press);
        list.add(R.string.play_mode_order);
        listPLaymodeImageRes.add(list);

        list = new ArrayList<>();
        list.add(R.drawable.bt_list_roundsingle_normal);
        list.add(R.drawable.bt_list_roundsingle_press);
        list.add(R.string.play_mode_roundsingle);
        listPLaymodeImageRes.add(list);

        list = new ArrayList<>();
        list.add(R.drawable.bt_list_button_roundplay_normal);
        list.add(R.drawable.bt_list_button_roundplay_press);
        list.add(R.string.play_mode_round);
        listPLaymodeImageRes.add(list);

        list = new ArrayList<>();
        list.add(R.drawable.bt_list_random_normal);
        list.add(R.drawable.bt_list_random_press);
        list.add(R.string.play_mode_random);
        listPLaymodeImageRes.add(list);
    }

    public void setContentView(View view, boolean isShowClose) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        super.setContentView(view);
        tvClearAll = (TextView) view.findViewById(R.id.tvClearAll);
        ivPlayMode = (MyImageView) view.findViewById(R.id.ivPlayMode);
        lvPlaylist = (ListView) view.findViewById(R.id.lvPlaylist);
        tvClose = (TextView)view.findViewById(R.id.tvClose);
        lvPlaylist.setOnItemClickListener(this);
        tvClearAll.setOnClickListener(this);
        ivPlayMode.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        tvPromt = (TextView)view.findViewById(R.id.tvPromt);
        if(isShowClose){
            tvClose.setVisibility(View.VISIBLE);
        }
        else{
            tvClose.setVisibility(View.GONE);
        }

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
    }

    public void setPlaylistData(List<MusicInfoDao> list){
        mPlaylistAdapter = null;
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

    public void setItemPlayState(int curPlayMediaIndex, boolean isCurPlay, boolean isPlaying){
        if(mPlaylistAdapter == null || curPlayMediaIndex < 0)
            return;

        mPlaylistAdapter.setItemPlayState(curPlayMediaIndex, isCurPlay, isPlaying);
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
                mPlaylistAdapter.setPlaylistAdapterData(new ArrayList<MusicInfoDao>());
                tvPromt.setVisibility(View.VISIBLE);
                lvPlaylist.setVisibility(View.GONE);
            }
        }
        else if(v == ivPlayMode){
            if(mDialogListener != null){
                mDialogListener.onChangePlayMode();
            }
        }
        else if(v == tvClose){
            dismiss();
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

    public void updatePlaymodeImg(int playMode, boolean isShowToast){
        if(playMode < PreferenceConfig.PLAYMODE_ORDER)
            playMode = PreferenceConfig.PLAYMODE_ORDER;

        if(playMode > PreferenceConfig.PLAYMODE_RANDOM)
            playMode = PreferenceConfig.PLAYMODE_RANDOM;

        if(listPLaymodeImageRes == null || ivPlayMode == null)
            return;

        List<Integer> list = listPLaymodeImageRes.get(playMode);
        ivPlayMode.setResId(list.get(0), list.get(1));
        if(isShowToast){
            Toast.makeText(this.getContext(), getContext().getString(list.get(2)), Toast.LENGTH_SHORT).show();
        }
    }

    public static class Builder {
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public PlayListDialog create(boolean isShowClose) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final PlayListDialog dialog = new PlayListDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.playlist_dialog, null);

            dialog.setContentView(layout, isShowClose);

            return dialog;
        }
    }
}
