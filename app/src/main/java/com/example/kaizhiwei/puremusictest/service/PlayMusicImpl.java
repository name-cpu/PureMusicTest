package com.example.kaizhiwei.puremusictest.service;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.RecentPlayDao;
import com.example.kaizhiwei.puremusictest.model.RecentPlayModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by kaizhiwei on 17/8/18.
 */

public class PlayMusicImpl implements IPlayMusic {
    private IMediaPlayer mediaPlayer;
    private Context mContext;
    private List<WeakReference<IPlayMusicListener>> mListeners = new ArrayList<>();
    private int mState = IPlayMusic.STATE_IDLE;
    private List<MusicInfoDao> mPlaylists = new ArrayList<>();
    private long mLastPlayMusicId = -1;
    private long mCurPlayMusicId = 0;
    private PlayMode mPlayMode = PlayMode.PLAYLIST_LOOP;
    private static final String PLAYLIST_FILENAME = "playlist.txt";
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
    private boolean isInit =  false;

    public enum PlayMode {
        ORDER(0),          //顺序播放
        SIGNAL_LOOP(1),    //单曲循环
        PLAYLIST_LOOP(2),  //循环播放
        RANDOM(3)          //随机播放
                {

                };

        private int value;

        private PlayMode(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    private static PlayMusicImpl mInstance;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable playPosRunnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer == null)
                return;

            long duration = mediaPlayer.getDuration();
            if(duration <= 0){
                Log.e("PlayMusicImpl", "duration = " + duration);
                return;
            }

            long curPos = mediaPlayer.getCurrentPosition();
            int percent = (int)(curPos*100/duration);

            for(int i = 0;i < mListeners.size();i++){
                IPlayMusicListener playMusicListener = mListeners.get(i).get();
                if(playMusicListener != null){
                    playMusicListener.onPlayPosUpdate(percent, (int)curPos, (int)duration);
                }
            }

            mHandler.postDelayed(playPosRunnable, 1000);
        }
    };

    private Runnable savePlaylistRunnable = new Runnable() {
        @Override
        public void run() {
            scheduledThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    savePlaylist();
                }
            });
        }
    };

    private IMediaPlayer.OnPreparedListener preparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            updateState(IPlayMusic.STATE_PAPERED);
            mHandler.postDelayed(playPosRunnable, 1000);
        }
    };

    private IMediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int progress) {
            for(int i = 0;i < mListeners.size();i++){
                IPlayMusicListener playMusicListener = mListeners.get(i).get();
                if(playMusicListener != null){
                    playMusicListener.onBufferingUpdate(progress, 0);
                }
            }
        }
    };

    private IMediaPlayer.OnCompletionListener completionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            updateState(IPlayMusic.STATE_COMPLETE);
            mHandler.removeCallbacks(playPosRunnable);
            addToRecentPlayDB();
            next();
        }
    };

    private IMediaPlayer.OnInfoListener infoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer,  int what, int extra) {
            if(what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                updateState(IPlayMusic.STATE_PLAY);
                mHandler.postDelayed(playPosRunnable, 1000);
            }
            else if(what == IMediaPlayer.MEDIA_INFO_BUFFERING_START){
                int curState = mState;
                if(curState == IPlayMusic.STATE_BUFFER_PAUSE || curState == IPlayMusic.STATE_PAUSE){
                    curState = IPlayMusic.STATE_BUFFER_PAUSE;
                }
                else{
                    curState = IPlayMusic.STATE_BUFFER_RESUME;
                }
                updateState(curState);
            }
            else if(what == IMediaPlayer.MEDIA_INFO_BUFFERING_END){
                int curState = mState;
                if(curState == IPlayMusic.STATE_BUFFER_PAUSE || curState == IPlayMusic.STATE_PAUSE){
                    curState = IPlayMusic.STATE_PAUSE;
                }
                else{
                    curState = IPlayMusic.STATE_PLAY;
                }
                updateState(curState);
            }
            return true;
        }
    };

    private IMediaPlayer.OnSeekCompleteListener seekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {

        }
    };

    private IMediaPlayer.OnErrorListener errorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            updateState(IPlayMusic.STATE_ERROR);
            mHandler.removeCallbacks(playPosRunnable);
            return false;
        }
    };

    public static PlayMusicImpl getInstance(){
        if(mInstance == null){
            synchronized (PlayMusicImpl.class){
                if(mInstance == null){
                    mInstance = new PlayMusicImpl();
                }
            }
        }

        return mInstance;
    }

    public void init(Context context){
        mContext = context;
        if(!isInit){
            restorePlaylist();
            isInit = true;
        }
    }

    private PlayMusicImpl(){
    }

    public int getPositionById(long musicId){
        for(int i = 0;i < mPlaylists.size();i++) {
            if (musicId == mPlaylists.get(i).get_id())
                return i;
        }
        return -1;
    }

    private void updateState(int state){
        for(int i = 0;i < mListeners.size();i++){
            IPlayMusicListener playMusicListener = mListeners.get(i).get();
            if(playMusicListener != null){
                playMusicListener.onStateChange(state);
            }
        }
        mState = state;
    }

    private void restorePlaylist(){
        if(mContext == null)
            return;

        String playlistPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mContext.getPackageName() + File.separator;
        File file = new File(playlistPath);
        if(!file.exists()){
            file.mkdirs();
        }

        playlistPath += PLAYLIST_FILENAME;
        file = new File(playlistPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ObjectInputStream ois = null;
        try {
            //获取输入流
            ois = new ObjectInputStream(new FileInputStream(file));
            //获取文件中的数据
            List<MusicInfoDao> playlist = (List<MusicInfoDao>) ois.readObject();
            int curPos = ois.readInt();
            if(playlist != null){
                mPlaylists.clear();
                mPlaylists.addAll(playlist);
            }
            mLastPlayMusicId = mCurPlayMusicId;
            mCurPlayMusicId = curPos;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (ois!=null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePlaylist(){
        if(mContext == null)
            return;

        String playlistPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mContext.getPackageName() + File.separator;
        File file = new File(playlistPath);
        if(!file.exists()){
            file.mkdirs();
        }

        playlistPath += PLAYLIST_FILENAME;
        file = new File(playlistPath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ObjectOutputStream ous = null;
        try {
            //获取输入流
            ous = new ObjectOutputStream(new FileOutputStream(file));
            ous.writeObject(mPlaylists);
            ous.writeInt(getPositionById(mCurPlayMusicId));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (ous!=null) {
                    ous.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPlaylist(List<MusicInfoDao> playlist){
        if(playlist == null)
            return;

        boolean isSame = true;
        if(playlist.size() != mPlaylists.size()){
            isSame = false;
        }
        else{
            for(int i = 0;i < mPlaylists.size();i++){
                if(mPlaylists.get(i).get_id() != playlist.get(i).get_id()){
                    isSame = false;
                    break;
                }
            }
        }

        if(isSame)
            return;

        mPlaylists.clear();
        mPlaylists.addAll(playlist);
        scheduledThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                savePlaylist();
            }
        });
    }

    public List<MusicInfoDao> getPlaylist(){
        List<MusicInfoDao> newList = new ArrayList<>();
        newList.addAll(mPlaylists);
        return newList;
    }

    public void clearPlaylist(){
        mPlaylists.clear();
        mCurPlayMusicId = -1;
        mLastPlayMusicId = -1;
        mHandler.removeCallbacks(savePlaylistRunnable);
        mHandler.postDelayed(savePlaylistRunnable, 1000);
    }

    public void addToNextPlay(MusicInfoDao musicInfoDao){
        if(musicInfoDao == null)
            return;

        if(musicInfoDao.get_id() == mCurPlayMusicId)
            return;

        boolean isExist = false;
        int index = -1;
        for(int i = 0;i < mPlaylists.size();i++){
            if(mPlaylists.get(i).get_id() == musicInfoDao.get_id()){
                isExist = true;
                index = i;
                break;
            }
        }

        if(mPlaylists.size() == 0){
            mPlaylists.add(musicInfoDao);
        }
        else{
            int pos = getPositionById(mCurPlayMusicId);
            //如果存在，将对应的歌曲排到正在播放的下一个位置
            if(isExist){
                mPlaylists.add(pos+1, musicInfoDao);
                if(index <= pos){
                    mPlaylists.remove(index);
                }
                else{
                    mPlaylists.remove(index+1);
                }
            }
            //如果不存在，加入播放列表，并将对应的歌曲排到正在播放的下一个位置
            else{
                mPlaylists.add(pos+1, musicInfoDao);
            }
        }
    }

    public void addMusicInfo(MusicInfoDao musicInfoDao, int postion){
        if(musicInfoDao == null || postion < 0 || postion >= mPlaylists.size())
            return;

        mPlaylists.add(postion, musicInfoDao);
        mHandler.removeCallbacks(savePlaylistRunnable);
        mHandler.postDelayed(savePlaylistRunnable, 1000);
    }

    public void removeMusicInfo(long id){
        if(id < 0)
            return;

        for(int i = 0;i < mPlaylists.size();i++){
            if(mPlaylists.get(i).get_id() == id){
                mPlaylists.remove(i);
                break;
            }
        }
        mHandler.removeCallbacks(savePlaylistRunnable);
        mHandler.postDelayed(savePlaylistRunnable, 1000);
    }


    public PlayMode getPlayMode(){
        return mPlayMode;
    }

    public void setPlayMode(PlayMode mode){
        mPlayMode = mode;
    }

    public int getCurPlayIndex(){
        return getPositionById(mCurPlayMusicId);
    }

    public void setCurPlayIndex(int index){
        if(index < 0 || index >= mPlaylists.size())
            return;

        mLastPlayMusicId = mCurPlayMusicId;
        mCurPlayMusicId = mPlaylists.get(index).get_id();
    }

    public void seekTo(int postition){
        if(mediaPlayer == null)
            return;

        if(TextUtils.isEmpty(mediaPlayer.getDataSource()))
            return;

        mediaPlayer.seekTo(postition);
    }

    @Override
    public void addListener(IPlayMusicListener listener) {
        if(listener == null)
            return;

        boolean bExist = false;
        for(int i = 0;i < mListeners.size();i++){
            IPlayMusicListener playMusicListener = mListeners.get(i).get();
            if(playMusicListener == listener){
                bExist = true;
                break;
            }
        }

        if(!bExist){
            mListeners.add(new WeakReference<>(listener));
        }
    }

    @Override
    public void removeListener(IPlayMusicListener listener) {
        if(listener == null)
            return;

        for(int i = 0;i < mListeners.size();i++){
            IPlayMusicListener playMusicListener = mListeners.get(i).get();
            if(playMusicListener == listener){
                mListeners.remove(i);
                break;
            }
        }
    }

    @Override
    public void play()  {
        if(mediaPlayer == null && mPlaylists != null){
            int pos = getPositionById(mCurPlayMusicId);
            setDataSource(mPlaylists.get(pos).get_data());
            return;
        }

        if(mediaPlayer.isPlayable() && TextUtils.isEmpty(mediaPlayer.getDataSource()) == false){
            mediaPlayer.start();
            updateState(IPlayMusic.STATE_PLAY);
        }
    }

    @Override
    public void pause()  {
        if(mediaPlayer == null)
            return;

        if(mediaPlayer.isPlayable() && TextUtils.isEmpty(mediaPlayer.getDataSource()) == false){
            mediaPlayer.pause();
            updateState(IPlayMusic.STATE_PAUSE);
        }
    }

    @Override
    public void setDataSource(String filePath){
        if(mPlaylists == null || TextUtils.isEmpty(filePath))
            return;

        boolean isSameAsCur = false;
        int pos = getPositionById(mLastPlayMusicId);
        if(pos >= 0 && mPlaylists.get(pos).get_data().equalsIgnoreCase(filePath)){
            isSameAsCur = true;
        }

        if(isSameAsCur && mediaPlayer != null){
            if(mediaPlayer.isPlaying()){

            }
            else{
                play();
            }
            return;
        }

        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.reset();
            }
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = new IjkMediaPlayer();
        mediaPlayer.setOnPreparedListener(preparedListener);
        mediaPlayer.setOnErrorListener(errorListener);
        mediaPlayer.setOnCompletionListener(completionListener);
        mediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
        mediaPlayer.setOnSeekCompleteListener(seekCompleteListener);
        mediaPlayer.setOnInfoListener(infoListener);
        mHandler.removeCallbacks(playPosRunnable);
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void prepareAsync()  {
        if(mediaPlayer == null || TextUtils.isEmpty(mediaPlayer.getDataSource()))
            return;


    }

    @Override
    public void prepare()  {

    }

    @Override
    public void stop()  {
        if(mediaPlayer == null)
            return;

        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void next()  {
        int nextPos = getPositionById(mCurPlayMusicId);
        if(mPlayMode == PlayMode.RANDOM){
            int min = 0;
            int max = mPlaylists.size();
            Random random = new Random();
            int s = random.nextInt(max)%(max-min+1) + min;
            nextPos = s;
        }
        else{
            nextPos++;
        }

        nextPos = nextPos%mPlaylists.size();
        setCurPlayIndex(nextPos);
        setDataSource(mPlaylists.get(nextPos).get_data());
        prepareAsync();
    }

    @Override
    public void pre()  {
        int prePos = getPositionById(mCurPlayMusicId);
        if(mPlayMode == PlayMode.RANDOM){
            int min = 0;
            int max = mPlaylists.size();
            Random random = new Random();
            int s = random.nextInt(max)%(max-min+1) + min;
            prePos = s;
        }
        else{
            prePos--;
        }

        prePos = (prePos+mPlaylists.size())%mPlaylists.size();
        setCurPlayIndex(prePos);
        setDataSource(mPlaylists.get(prePos).get_data());
        prepareAsync();
    }

    public boolean isPlaying(){
        if(mediaPlayer == null)
            return false;

        return mediaPlayer.isPlaying();
    }

    public MusicInfoDao getCurPlayMusicDao(){
        if(mPlaylists == null)
            return null;

        int pos = getPositionById(mCurPlayMusicId);
        if(pos < 0)
            return null;

        MusicInfoDao musicDao = mPlaylists.get(pos);
        return musicDao;
    }

    public MusicInfoDao getMusicInfoByPosition(int position){
        if(mPlaylists == null || position < 0 || position >= mPlaylists.size())
            return null;

        MusicInfoDao musicDao = mPlaylists.get(position);
        return musicDao;
    }

    public MusicInfoDao getMusicInfoById(long id){
        if(mPlaylists == null || id < 0)
            return null;

        for(int i = 0;i < mPlaylists.size();i++){
            if(mPlaylists.get(i).get_id() == id){
                return mPlaylists.get(i);
            }
        }

        return null;
    }

    public void addToRecentPlayDB(){
        RecentPlayDao recentPlayDao = new RecentPlayDao();
        recentPlayDao.setInfo_id(mCurPlayMusicId);
        recentPlayDao.setTime_stamp(System.currentTimeMillis());
        recentPlayDao.setHas_play_status(true);
        RecentPlayModel.getInstance().addRecentPlay(recentPlayDao);
    }
}
