package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.service.IPlayMusic;
import com.example.kaizhiwei.puremusictest.service.IPlayMusicListener;
import com.example.kaizhiwei.puremusictest.service.PlayMusicImpl;
import com.example.kaizhiwei.puremusictest.service.PlayMusicService;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;
import com.example.kaizhiwei.puremusictest.PlayingDetail.PlayingActivity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.util.DeviceUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/20.
 */
public class NowPlayingLayout extends LinearLayout implements View.OnClickListener, PlayMusicService.Client.Callback, IPlayMusicListener {
    private ImageButton btnPlayPause;
    private ImageButton btnPlayNext;
    private ImageButton btnPlaylist;
    private TextView   tvNowPlayingMain;
    private TextView   tvNowPlayingSub;
    private ImageView  ivArtist;
    private ProgressBar pbPlay;
    private LinearLayout llControl;
    private WeakReference<MusicInfoDao>  mWeakMusicInfoDao;
    private boolean mIsPlaying;
    private HomeActivity mHomePage;
    private PlayMusicService.Client mClient = new PlayMusicService.Client(this.getContext(), this);
    private PlayMusicService mService;
    private PlaylistDialog mPlayListDialog;
    private PlaylistDialog.IPlayListDialogListener mListener = new PlaylistDialog.IPlayListDialogListener() {
        @Override
        public void onDeleteClick(PlayListViewAdapter adapter, int position) {
            if(adapter == null || position < 0)
                return;

            if(mService == null)
                return;

            PlayListViewAdapter.PlayListItemData itemData = (PlayListViewAdapter.PlayListItemData)adapter.getItem(position);
            if(itemData == null || itemData.MusicInfoDao == null)
                return;

            MusicInfoDao curPlayMedia = mService.getCurPlayMusicDao();
            mPlayListDialog.removeItem(position);
            mService.removeMusicInfo(itemData.MusicInfoDao.get_id());
            if(curPlayMedia != null){
                if(curPlayMedia.get_id() == itemData.MusicInfoDao.get_id()){
                    MusicInfoDao musicInfoDao = mService.getMusicInfoByPosition(position);
                    if(musicInfoDao != null){
                        mService.setDataSource(musicInfoDao.get_data());
                    }
                }
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(parent == null || view == null || position < 0 || mService == null)
                return;

            PlayListViewAdapter adapter = (PlayListViewAdapter)parent.getAdapter();
            PlayListViewAdapter.PlayListItemData itemData = (PlayListViewAdapter.PlayListItemData)adapter.getItem(position);
            if(itemData == null || itemData.MusicInfoDao == null)
                return;

            mService.setCurPlayIndex(position);
            mService.setDataSource(itemData.MusicInfoDao.get_data());
        }

        @Override
        public void onClearPlaylist() {
            if(mService == null)
                return;

            mService.clearPlaylist();
            mService.stop();
            resetUI(true);
        }

        @Override
        public void onChangePlayMode() {
            if(mService == null || mPlayListDialog == null)
                return;

            int curPlayMode = mService.getPlayMode().ordinal();
            int nextPlayMode = (curPlayMode + 1)%(PlayMusicImpl.PlayMode.values().length);
            PlayMusicImpl.PlayMode mode = PlayMusicImpl.PlayMode.values()[nextPlayMode];
            mService.setPlayMode(mode);
            mPlayListDialog.updatePlaymodeState(mode, true);
        }
    };

    public NowPlayingLayout(Context context) {
        super(context);
        init();
        mHomePage = (HomeActivity)context;
    }

    public NowPlayingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mHomePage = (HomeActivity)context;
    }

    public NowPlayingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mHomePage = (HomeActivity)context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NowPlayingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mClient.connect();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mClient.disconnect();
    }

    public void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.audio_now_playing, null);
        this.addView(view);

        btnPlayPause = (ImageButton)view.findViewById(R.id.btnPlayPause);
        btnPlayNext = (ImageButton)view.findViewById(R.id.btnPlayNext);
        btnPlaylist = (ImageButton)view.findViewById(R.id.btnPlaylist);
        btnPlayPause.setOnClickListener(this);
        btnPlayNext.setOnClickListener(this);
        btnPlaylist.setOnClickListener(this);
        this.setOnClickListener(this);

        tvNowPlayingMain = (TextView) view.findViewById(R.id.tvNowPlayingMain);
        tvNowPlayingSub = (TextView)view.findViewById(R.id.tvNowPlayingSub);
        ivArtist = (ImageView)view.findViewById(R.id.ivArtist);
        pbPlay = (ProgressBar) view.findViewById(R.id.pbPlay);
        pbPlay.setProgress(0);
        llControl = (LinearLayout)view.findViewById(R.id.llControl);
        llControl.setOnClickListener(this);
    }

    private void initView(){
        if(mService == null)
            return;

        int size = mService.getPlaylist().size();
        if(size == 0){
            resetUI(true);
        }
        else{
            MusicInfoDao curMedia = mService.getCurPlayMusicDao();
            if(curMedia != null){
                tvNowPlayingMain.setText(curMedia.getTitle());
                tvNowPlayingSub.setText(curMedia.getArtist());
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void onClick(View v) {
        if(mService == null)
            return;

        List<MusicInfoDao> playlist = mService.getPlaylist();
        if(v == btnPlayPause){
            if(playlist.size() == 0){
                Toast.makeText(this.getContext(), "当前无可播放的歌曲", Toast.LENGTH_SHORT).show();
                return;
            }

            if(mIsPlaying){
                mService.pause();
            }
            else{
                mService.play();
            }
        }
        else if(v == btnPlayNext){
            if(playlist.size() == 0){
                Toast.makeText(this.getContext(), "当前无可播放的歌曲", Toast.LENGTH_SHORT).show();
                return;
            }

            mService.next();
        }
        else if(v == btnPlaylist){
            if(mPlayListDialog == null){
                PlaylistDialog.Builder builder = new PlaylistDialog.Builder(this.getContext());
                mPlayListDialog = (PlaylistDialog)builder.create();
            }
            mPlayListDialog.setMaxDialogHeight(500* DeviceUtil.getDensity(this.getContext()));
            mPlayListDialog.setCancelable(true);
            mPlayListDialog.setPlaylistData(playlist);
            mPlayListDialog.setItemPlayState(mService.getCurPlayIndex(), true, mService.isPlaying());
            mPlayListDialog.setPlayListAdapterListener(mListener);
            mPlayListDialog.updatePlaymodeState(mService.getPlayMode(), false);
            mPlayListDialog.show();
        }
        else if(llControl == v){
            Intent intent = new Intent(this.getContext(), PlayingActivity.class);
            mHomePage.startActivity(intent);
        }
    }

    public void setPlayingMediaEntrty(MusicInfoDao media){
        if(media == null)
            return ;

        tvNowPlayingMain.setText(media.getTitle());
        tvNowPlayingSub.setText(media.getArtist());
        pbPlay.setMax((int)media.getDuration());
    }

    public void updatePlayProgress(int curPos, int duration){
        pbPlay.setMax(duration);
        pbPlay.setProgress(curPos);
    }

    public void setPlayPauseState(boolean isPlaying){
        if(isPlaying){
            btnPlayPause.setBackgroundResource(R.drawable.bt_minibar_pause_normal);
        }
        else{
            btnPlayPause.setBackgroundResource(R.drawable.bt_minibar_play_normal);
        }
        mIsPlaying = isPlaying;
    }

    @Override
    public void onConnect(PlayMusicService service) {
        mService = service;
        mService.addListener(this);
        initView();
    }

    @Override
    public void onDisconnect() {
        mService.removeListener(this);
        mService = null;
    }

    private void resetUI(boolean isStopState){
        if(isStopState){
            tvNowPlayingMain.setText(getResources().getString(R.string.app_name) + " 畅享极致");
            tvNowPlayingSub.setText("");
            pbPlay.setProgress(0);
            pbPlay.setMax(100);
            tvNowPlayingSub.setVisibility(View.GONE);
            tvNowPlayingMain.setGravity(Gravity.CENTER_VERTICAL);
            setPlayPauseState(false);
            btnPlayPause.setEnabled(false);
            btnPlayNext.setEnabled(false);
        }
        else{
            tvNowPlayingMain.setText("");
            tvNowPlayingSub.setText("");
            pbPlay.setProgress(0);
            pbPlay.setMax(100);
            tvNowPlayingSub.setVisibility(View.VISIBLE);
            tvNowPlayingMain.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    @Override
    public void onStateChange(int state) {
        if(state == IPlayMusic.STATE_PAPERED){
            List<MusicInfoDao> playlist = mService.getPlaylist();
            int index = mService.getCurPlayIndex();
            MusicInfoDao musicInfoDao = playlist.get(index);

            if(musicInfoDao != null){
                btnPlayPause.setEnabled(true);
                btnPlayNext.setEnabled(true);
                setPlayingMediaEntrty(musicInfoDao);
                updatePlayProgress(0, (int)musicInfoDao.getDuration());
                if(mPlayListDialog != null && mPlayListDialog.isShowing()){
                    mPlayListDialog.setItemPlayState(mService.getCurPlayIndex(), true, true);
                }
            }
            setPlayPauseState(true);
        }
        else if(state == IPlayMusic.STATE_PLAY){
            List<MusicInfoDao> playlist = mService.getPlaylist();
            int index = mService.getCurPlayIndex();
            MusicInfoDao musicInfoDao = playlist.get(index);

            if(musicInfoDao != null){
                setPlayingMediaEntrty(musicInfoDao);
                updatePlayProgress(0, (int)musicInfoDao.getDuration());
                if(mPlayListDialog != null && mPlayListDialog.isShowing()){
                    mPlayListDialog.setItemPlayState(mService.getCurPlayIndex(), true, true);
                }
            }
            setPlayPauseState(true);
        }
        else if(state == IPlayMusic.STATE_PAUSE){
            List<MusicInfoDao> playlist = mService.getPlaylist();
            int index = mService.getCurPlayIndex();
            MusicInfoDao musicInfoDao = playlist.get(index);

            if(musicInfoDao != null){
                if(mPlayListDialog != null && mPlayListDialog.isShowing()){
                    mPlayListDialog.setItemPlayState(mService.getCurPlayIndex(), true, false);
                }
            }
            setPlayPauseState(false);
        }
        else if(state == IPlayMusic.STATE_ERROR){
            resetUI(true);
        }
    }

    @Override
    public void onPlayPosUpdate(int percent, int curPos, int duration) {
        updatePlayProgress(curPos, duration);
    }

    @Override
    public void onBufferingUpdate(int cur, int total) {

    }
}
