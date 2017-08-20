package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.CommonUI.MyImageView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseDialog;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.service.PlayMusicImpl;
import com.example.kaizhiwei.puremusictest.util.FadingEdgeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class PlaylistDialog extends BaseDialog implements View.OnClickListener, AdapterView.OnItemClickListener {
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

    public PlaylistDialog(Context context) {
        super(context);
    }

    @Override
    public void initData() {
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

    @Override
    public void initView() {
        tvClearAll = (TextView) this.findViewById(R.id.tvClearAll);
        ivPlayMode = (MyImageView) this.findViewById(R.id.ivPlayMode);
        lvPlaylist = (ListView) this.findViewById(R.id.lvPlaylist);
        tvClose = (TextView)this.findViewById(R.id.tvClose);
        lvPlaylist.setOnItemClickListener(this);
        tvClearAll.setOnClickListener(this);
        ivPlayMode.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        tvPromt = (TextView)this.findViewById(R.id.tvPromt);
        tvClose.setVisibility(View.VISIBLE);

        FadingEdgeUtil.setEdgeTopColor(lvPlaylist, this.getContext().getResources().getColor(R.color.blackgray));
        FadingEdgeUtil.setEdgeBottomColor(lvPlaylist, this.getContext().getResources().getColor(R.color.blackgray));
        setTitleVisible(false);
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

    public void setPlayListAdapterListener(PlaylistDialog.IPlayListDialogListener listener){
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

    public void updatePlaymodeState(PlayMusicImpl.PlayMode playMode, boolean isShowToast){
               if(listPLaymodeImageRes == null || ivPlayMode == null)
            return;

        List<Integer> list = listPLaymodeImageRes.get(playMode.ordinal());
        ivPlayMode.setResId(list.get(0), list.get(1));
        if(isShowToast){
            Toast.makeText(this.getContext(), getContext().getString(list.get(2)), Toast.LENGTH_SHORT).show();
        }
    }

    public static class Builder extends BaseDialog.Builder{
        public Builder(Context context) {
            super(context);
        }

        @Override
        protected <T extends BaseDialog> T createDialog() {
            return (T) new PlaylistDialog(mContext);
        }

        @Override
        protected int getCustomeView() {
            return R.layout.playlist_dialog;
        }
    }
}
