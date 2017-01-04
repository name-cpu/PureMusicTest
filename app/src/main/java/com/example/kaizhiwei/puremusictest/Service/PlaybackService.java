package com.example.kaizhiwei.puremusictest.Service;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.MediaData.VLCInstance;
import com.example.kaizhiwei.puremusictest.Util.WeakHandler;

import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.AndroidUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by kaizhiwei on 16/11/13.
 */
public class PlaybackService extends Service {
    private static final String TAG = "PlaybackService";
    private static final long PLAYBACK_ACTIONS = PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;

    private List<MediaEntity> mLastMediaList = new ArrayList<>();
    private List<MediaEntity>  mMediaList = new ArrayList<>();
    private int                 mCurrentIndex;
    private int                 mCurrentTime;
    private int                 mRepeatMode;       //PreferenceConfig.PLAYMODE_ORDER
    private MediaPlayer         mMediaPlayer;
    private MediaSessionCompat  mMediaSession;
    protected MediaSessionCallback mSessionCallback;
    private boolean mHasAudioFocus = false;
    private PowerManager.WakeLock mWakeLock;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener;

    private ArrayList<Callback> mCallbacks = new ArrayList<Callback>();

    private static final int SHOW_PROGRESS = 0;
    private Handler mHandler = new AudioServiceHandler(this);
    private ExecutorService mThreadPool = Executors.newFixedThreadPool(2);

    private LocalBinder mBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        public PlaybackService getService(){
            return PlaybackService.this;
        }
    }

    public interface Callback {
        void update();
        void updateProgress();
        void onMediaEvent(Media.Event event);
        void onMediaPlayerEvent(MediaPlayer.Event event);
    }

    private final class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            play();
        }
        @Override
        public void onPause() {
            pause();
        }

        @Override
        public void onStop() {
            stop();
        }

        @Override
        public void onSkipToNext() {
            next(true);
        }

        @Override
        public void onSkipToPrevious() {
            //previous();
        }
    }

    private final Media.EventListener mMediaListener = new Media.EventListener(){

        @Override
        public void onEvent(Media.Event event) {
            switch (event.type) {
                case Media.Event.ParsedChanged:
                    Log.i(TAG, "Media.Event.ParsedChanged");
//                    final MediaWrapper mw = getCurrentMedia();
//                    if (mw != null)
//                        mw.updateMeta(mMediaPlayer);
//                    executeUpdate();
                    break;
                case Media.Event.MetaChanged:
                    Log.i(TAG, "Media.Event.MetaChanged");
                    break;
                case Media.Event.SubItemAdded:
                    Log.i(TAG, "Media.Event.SubItemAdded");
                    break;
                case Media.Event.DurationChanged:
                    Log.i(TAG, "Media.Event.DurationChanged");
                    break;
                case Media.Event.StateChanged:
                    Log.i(TAG, "Media.Event.StateChanged");
                    break;
                case Media.Event.SubItemTreeAdded:
                    Log.i(TAG, "Media.Event.SubItemTreeAdded");
                    break;
            }
            for (Callback callback : mCallbacks)
                callback.onMediaEvent(event);
        }
    };

    private final MediaPlayer.EventListener mMediaPlayerListener = new MediaPlayer.EventListener() {
        @Override
        public void onEvent(MediaPlayer.Event event) {
            switch (event.type) {
                case MediaPlayer.Event.Playing:

                    if (mMediaSession == null)
                        initMediaSession(PlaybackService.this);

                    Log.i(TAG, "MediaPlayer.Event.Playing");
                    executeUpdate();
                    publishState(event.type);
                    executeUpdateProgress();

//                    final MediaWrapper mw = mMediaList.getMedia(mCurrentIndex);
//                    if (mw != null) {
//                        long length = mMediaPlayer.getLength();
//                        MediaDatabase dbManager = MediaDatabase.getInstance();
//                        MediaWrapper m = dbManager.getMedia(mw.getUri());
//                        /**
//                         * 1) There is a media to update
//                         * 2) It has a length of 0
//                         * (dynamic track loading - most notably the OGG container)
//                         * 3) We were able to get a length even after parsing
//                         * (don't want to replace a 0 with a 0)
//                         */
//                        if (m != null && m.getLength() == 0 && length > 0) {
//                            dbManager.updateMedia(mw.getUri(),
//                                    MediaDatabase.INDEX_MEDIA_LENGTH, length);
//                        }
//                    }

                    changeAudioFocus(true);
                    //setRemoteControlClientPlaybackState(event.type);
                    //showNotification();
                    if (!mWakeLock.isHeld())
                        mWakeLock.acquire();
                    break;
                case MediaPlayer.Event.Paused:
                    Log.i(TAG, "MediaPlayer.Event.Paused");
                    executeUpdate();
                    publishState(event.type);
                    executeUpdateProgress();
                    //showNotification();
                    //setRemoteControlClientPlaybackState(event.type);
                    if (mWakeLock.isHeld())
                        mWakeLock.release();
                    break;
                case MediaPlayer.Event.Stopped:
                    Log.i(TAG, "MediaPlayer.Event.Stopped");
                    executeUpdate();
                    publishState(event.type);
                    executeUpdateProgress();
                    //setRemoteControlClientPlaybackState(event.type);
                    if (mWakeLock.isHeld())
                        mWakeLock.release();
                    changeAudioFocus(false);
                    Media media = mMediaPlayer.getMedia();
                    if(media != null){
                        media.setEventListener(null);
                        mMediaPlayer.setEventListener(null);
                        mMediaPlayer.setMedia(null);
                        media.release();
                    }
                    break;
                case MediaPlayer.Event.EndReached:
                    Log.i(TAG, "MediaPlayer.Event.EndReached");
                    executeUpdate();
                    executeUpdateProgress();
                    //determinePrevAndNextIndices(true);
                    next(true);
                    if (mWakeLock.isHeld())
                        mWakeLock.release();
                    changeAudioFocus(false);
                    break;
                case MediaPlayer.Event.EncounteredError:
//                    showToast(getString(
//                            R.string.invalid_location,
//                            mMediaList.getMRL(mCurrentIndex)), Toast.LENGTH_SHORT);
//                    executeUpdate();
//                    executeUpdateProgress();
                      next(true);
                    if (mWakeLock.isHeld())
                        mWakeLock.release();
                    break;
                case MediaPlayer.Event.TimeChanged:
                    break;
                case MediaPlayer.Event.PositionChanged:
                    //updateWidgetPosition(event.getPositionChanged());
                    break;
                case MediaPlayer.Event.Vout:
                    break;
                case MediaPlayer.Event.ESAdded:
//                    if (event.getEsChangedType() == Media.Track.Type.Video) {
//                        if (!handleVout()) {
//                            /* Update notification content intent: resume video or resume audio activity */
//                            showNotification();
//                        }
//                    }
                    break;
                case MediaPlayer.Event.ESDeleted:
                    break;
                case MediaPlayer.Event.PausableChanged:
                    //mPausable = event.getPausable();
                    break;
                case MediaPlayer.Event.SeekableChanged:
                    //mSeekable = event.getSeekable();
                    break;
            }
            for (Callback callback : mCallbacks)
                callback.onMediaPlayerEvent(event);
        }
    };

    private static class AudioServiceHandler extends WeakHandler<PlaybackService> {
        public AudioServiceHandler(PlaybackService cls) {
            super(cls);
        }

        public void handleMessage(Message msg) {
            PlaybackService service = (PlaybackService) this.getOwner();
            int what = msg.what;
            if(what == SHOW_PROGRESS){
                if(service.mCallbacks.size() > 0){
                    service.executeUpdateProgress();
                    removeMessages(what);
                    sendEmptyMessageDelayed(what, 1000);
                }
            }
        }
    }

    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer(VLCInstance.getInstance());
        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        restorePrefData();
    }

    private void initMediaSession(Context context) {
        ComponentName mediaButtonReceiver = new ComponentName(getApplicationContext(), MediaButtonReceiver.class);
        mSessionCallback = new MediaSessionCallback();
        mMediaSession = new MediaSessionCompat(context, "PureMusic", mediaButtonReceiver, null);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setCallback(mSessionCallback);
        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(this,
                MediaButtonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
        mMediaSession.setMediaButtonReceiver(pendingIntent);

        updateMetadata();
    }

    protected void updateMetadata() {
        MediaEntity media = getCurrentMedia();
        if (media == null || mMediaSession == null)
            return;
        String title = media.title;
        //Bitmap cover = AudioUtil.getCover(this, media, 512);
        MediaMetadataCompat.Builder bob = new MediaMetadataCompat.Builder();
        bob.putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                //.putString(MediaMetadataCompat.METADATA_KEY_GENRE, Util.getMediaGenre(this, media))
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, media.track)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, media.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, media.album)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, media.duration);
        //if (cover != null && cover.getConfig() != null) //In case of format not supported
        //    bob.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, cover.copy(cover.getConfig(), false));
        mMediaSession.setMetadata(bob.build());
    }

    public MediaEntity getCurrentMedia() {
        if(mCurrentIndex < 0 || mCurrentIndex >= mMediaList.size())
            return null;
        
        return mMediaList.get(mCurrentIndex);
    }

    private void executeUpdate() {
        executeUpdate(true);
    }

    private void executeUpdate(Boolean updateWidget) {
        for (Callback callback : mCallbacks) {
            callback.update();
        }
        //if (updateWidget)
        //    updateWidget();
        updateMetadata();

    }

    private void executeUpdateProgress() {
        for (Callback callback : mCallbacks) {
            callback.updateProgress();
        }
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.release();
        if(mWakeLock != null){
            if (mWakeLock.isHeld())
                mWakeLock.release();
        }
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public static PlaybackService getService(IBinder iBinder) {
        LocalBinder binder = (LocalBinder) iBinder;
        return binder.getService();
    }

    public void restorePrefData(){
        if(mThreadPool != null){
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> listMedia = PreferenceConfig.getInstance().getPlaylist();
                    for(int i = 0;i < listMedia.size();i++){
                        long mediaEntityId = Long.valueOf(listMedia.get(i));
                        if(mediaEntityId <= 0)
                            continue;

                        MediaEntity entity = MediaLibrary.getInstance().getMediaEntityById(mediaEntityId);
                        mMediaList.add(entity);
                    }
                    mLastMediaList.addAll(mMediaList);

                    mCurrentIndex = PreferenceConfig.getInstance().getPositionInPlaylist();
                    mCurrentTime = PreferenceConfig.getInstance().getPositionInMedia();
                    mRepeatMode = PreferenceConfig.getInstance().getPlayMode();
                    play(mCurrentIndex, false);
                }
            });
        }
    }

    public void deleteMediaById(long mediaEntityId){
        if(mMediaList == null)
            return;

        int deleteIndex = 0;
        for(int i = 0;i < mMediaList.size();i++){
            if(mMediaList.get(i)._id == mediaEntityId){
                mMediaList.remove(i);
                deleteIndex = i;
                break;
            }
        }

        if(deleteIndex <= mCurrentIndex && mCurrentIndex != -1)
            mCurrentIndex--;
        savePlaylistToPrefFile(mMediaList);
    }

    public void mutilDeleteMediaByList(List<MediaEntity> list){
        if(mMediaList == null)
            return;

        for(int i = 0;i < mMediaList.size();i++) {
            for (int j = 0; j < list.size(); j++) {
                if (mMediaList.get(i)._id == list.get(j)._id) {
                    if(i == mCurrentIndex){
                        pause();
                    }
                    if ((i < mCurrentIndex || (i == list.size() - 1)) && mCurrentIndex != -1)
                        mCurrentIndex--;

                    mMediaList.remove(i);
                    i--;
                }
            }
        }
        savePlaylistToPrefFile(mMediaList);
    }

    public void reCalNextPlayIndex(){
        play(mCurrentIndex, true);
    }

    public void clearPlaylist(){
        if(mMediaList == null)
            return;

        stop();
        mCurrentIndex = -1;
        mMediaList.clear();
    }

    public MediaEntity getLastMediaEntityByIndex(int index){
        if(index < 0 || index >= mLastMediaList.size())
            return null;

        return mLastMediaList.get(index);
    }

    public MediaEntity getMediaEntityByIndex(int index){
        if(index < 0 || index >= mMediaList.size())
            return null;

        return mMediaList.get(index);
    }

    private void savePlaylistToPrefFile(List<MediaEntity> list){
        if(list == null)
            return;

        final List<String> listPref = new ArrayList<>();
        for(int i = 0;i < list.size();i++){
            listPref.add("" + list.get(i)._id);
        }

        if(mThreadPool != null){
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    PreferenceConfig.getInstance().setPlaylist(listPref);
                }
            });
        }
    }

    public void play(MediaEntity mediaEntity){
        if(mediaEntity == null || mMediaList == null || mMediaList.size() == 0)
            return;

        for(int i = 0;i < mMediaList.size();i++){
            if(mMediaList.get(i)._id == mediaEntity._id){
                play(i, true);
                break;
            }
        }
    }

    public void play(List<MediaEntity> list, int position){
        if(mMediaList != null && mLastMediaList != null){
            mLastMediaList.clear();
            mLastMediaList.addAll(mMediaList);
            mMediaList.clear();
            mMediaList.addAll(list);
            savePlaylistToPrefFile(mMediaList);
        }
        play(position, true);
    }

    public void play(int index, boolean isPlayNow){
        if(index < 0 || index >= mMediaList.size())
            return;

        //如果之前播放过则对比上次和这次的是否是统一首歌
        if(mCurrentIndex >= 0 && isPlayNow){
            MediaEntity oldMediaEntity = getLastMediaEntityByIndex(mCurrentIndex);
            MediaEntity newMediaEntity = getMediaEntityByIndex(index);
            if(oldMediaEntity == null || newMediaEntity == null)
                return;

            if(oldMediaEntity._id == newMediaEntity._id && mRepeatMode != PreferenceConfig.PLAYMODE_ONECIRCLE){
                mCurrentIndex = index;
                if(!isPlaying()){

                }
                return;
            }
        }

        mCurrentIndex = index;
        PreferenceConfig.getInstance().setPositionInPlaylist(mCurrentIndex);
        String filePath = mMediaList.get(index).getFilePath();
        File file = new File(filePath);
        if(file.exists() == false)
            return;

        Media media  = new Media(VLCInstance.getInstance(), Uri.decode(filePath));
        media.setEventListener(mMediaListener);
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.setVideoTitleDisplay(MediaPlayer.Position.Disable, 0);
        mMediaPlayer.setEventListener(mMediaPlayerListener);
        if(isPlayNow){
            mMediaPlayer.play();
        }
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
    }

    private boolean hasCurrentMedia() {
        return mCurrentIndex >= 0 && mCurrentIndex < mMediaList.size();
    }

    @MainThread
    public synchronized void addCallback(Callback cb) {
        if (!mCallbacks.contains(cb)) {
            mCallbacks.add(cb);
           // if (hasCurrentMedia())
               // mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    }

    protected void publishState(int state){
        if(mMediaSession == null)
            return;

        PlaybackStateCompat.Builder bob = new PlaybackStateCompat.Builder();
        bob.setActions(PLAYBACK_ACTIONS);
        switch (state){
            case MediaPlayer.Event.Playing:
                bob.setState(PlaybackStateCompat.STATE_PLAYING, -1, 1);
                break;
            case MediaPlayer.Event.Paused:
                bob.setState(PlaybackStateCompat.STATE_PAUSED, -1, 0);
                break;
            case MediaPlayer.Event.Stopped:
                bob.setState(PlaybackStateCompat.STATE_STOPPED, -1, 0);
                break;
        }
        PlaybackStateCompat pbstate = bob.build();
        mMediaSession.setPlaybackState(pbstate);
        mMediaSession.setActive(state != PlaybackStateCompat.STATE_STOPPED);
    }

    public long getTIme(){
        return mMediaPlayer.getTime();
    }

    public long getLength(){
        return mMediaPlayer.getLength();
    }

    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    public int getRepeatMode(){
        return mRepeatMode;
    }

    public void setRepeatMode(int mode){
        mRepeatMode = mode;
        PreferenceConfig.getInstance().setPlayMode(mode);
    }

    public void play(){
        if(hasCurrentMedia()){
            mMediaPlayer.play();
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
            updateMetadata();
        }
    }

    public void pause(){
        if(hasCurrentMedia()){
            mMediaPlayer.pause();
            mHandler.removeMessages(SHOW_PROGRESS);
            updateMetadata();
        }
    }

    public void stop(){
        if(mMediaSession != null){
            mMediaSession.setActive(false);
            mMediaSession.release();
            mMediaSession = null;
        }

        if(mMediaPlayer == null)
            return;

        Media media = mMediaPlayer.getMedia();
        if(media != null){
            //media.setEventListener(null);
            //mMediaPlayer.setEventListener(null);
            mMediaPlayer.stop();
            //mMediaPlayer.setMedia(null);
            //media.release();
        }

        mCurrentIndex = -1;
        mHandler.removeMessages(SHOW_PROGRESS);
        executeUpdate();
        executeUpdateProgress();
        changeAudioFocus(false);
    }

    public void previous(boolean bAuto){
        if(mMediaList.size() == 0){
            stop();
            return;
        }

        int previousPosition = 0;
        switch (mRepeatMode){
            case PreferenceConfig.PLAYMODE_ORDER:
                if(bAuto){
                    if(mCurrentIndex != mMediaList.size() - 1){
                        previousPosition = mCurrentIndex - 1;
                    }
                    else{
                        mCurrentIndex = -1;
                        previousPosition = -1;
                        stop();
                        return ;
                    }
                }
                else{
                    previousPosition = (mCurrentIndex - 1)%mMediaList.size();
                }
                break;
            case PreferenceConfig.PLAYMODE_ONECIRCLE:
                if(bAuto){
                    previousPosition = mCurrentIndex;
                }
                else{
                    previousPosition = (mCurrentIndex - 1)%mMediaList.size();
                }
                break;
            case PreferenceConfig.PLAYMODE_ALLCIRCLE:
                previousPosition = (mCurrentIndex - 1)%mMediaList.size();
                break;
            case PreferenceConfig.PLAYMODE_RANDOM:
                previousPosition = (int)(Math.random()*(mMediaList.size()));
                break;
        }

        play(previousPosition, true);
    }

    public void next(boolean bAuto){
        if(mMediaList.size() == 0){
            stop();
            return ;
        }

        int nextPosition = 0;
        switch (mRepeatMode){
            case PreferenceConfig.PLAYMODE_ORDER:
                if(bAuto){
                    if(mCurrentIndex != mMediaList.size() - 1){
                        nextPosition = mCurrentIndex + 1;
                    }
                    else{
                        mCurrentIndex = -1;
                        nextPosition = -1;
                        stop();
                        return ;
                    }
                }
                else{
                    nextPosition = (mCurrentIndex + 1)%mMediaList.size();
                }
                break;
            case PreferenceConfig.PLAYMODE_ONECIRCLE:
                if(bAuto){
                    nextPosition = mCurrentIndex;
                }
                else{
                    nextPosition = (mCurrentIndex + 1)%mMediaList.size();
                }
                break;
            case PreferenceConfig.PLAYMODE_ALLCIRCLE:
                nextPosition = (mCurrentIndex + 1)%mMediaList.size();
                break;
            case PreferenceConfig.PLAYMODE_RANDOM:
                nextPosition = (int)(Math.random()*(mMediaList.size()));
                break;
        }

        play(nextPosition, true);
        //onMediaChanged();
    }

    public List<MediaEntity> getPlaylist(){
        List<MediaEntity> list = new ArrayList<>();
        list.addAll(mMediaList);
        return list;
    }

    public int getCurPlayMediaIndex(){
        return mCurrentIndex;
    }

    public int getPlaylistSize(){
        if(mMediaList == null)
            return 0;
        return mMediaList.size();
    }

    public boolean addSongToNext(MediaEntity entity){
        if(entity == null || entity._id < 0 || mMediaList == null)
            return false;

        boolean isExist = false;
        for(int i = 0;i < mMediaList.size();i++){
            if(mMediaList.get(i)._id == entity._id){
                isExist = true;
                break;
            }
        }
        if(isExist == false) {
            mMediaList.add(mCurrentIndex + 1, entity);
            savePlaylistToPrefFile(mMediaList);
        }
        return true;
    }
//    /**
//     * Set up the remote control and tell the system we want to be the default receiver for the MEDIA buttons
//     */
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    public void changeRemoteControlClient(AudioManager am, boolean acquire) {
//        if (acquire) {
//            Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//            mediaButtonIntent.setComponent(mRemoteControlClientReceiverComponent);
//            PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0);
//
//            // create and register the remote control client
//            mRemoteControlClient = new RemoteControlClient(mediaPendingIntent);
//            am.registerRemoteControlClient(mRemoteControlClient);
//
//            mRemoteControlClient.setTransportControlFlags(
//                    RemoteControlClient.FLAG_KEY_MEDIA_PLAY |
//                            RemoteControlClient.FLAG_KEY_MEDIA_PAUSE |
//                            RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS |
//                            RemoteControlClient.FLAG_KEY_MEDIA_NEXT |
//                            RemoteControlClient.FLAG_KEY_MEDIA_STOP);
//        } else {
//            am.unregisterRemoteControlClient(mRemoteControlClient);
//            mRemoteControlClient = null;
//        }
//    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    private void changeAudioFocusFroyoOrLater(boolean acquire) {
        final AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        if (am == null)
            return;

        if (acquire) {
            if (!mHasAudioFocus) {
                final int result = am.requestAudioFocus(mAudioFocusListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    am.setParameters("bgm_state=true");
                    //am.registerMediaButtonEventReceiver(mRemoteControlClientReceiverComponent);
                    //if (AndroidUtil.isICSOrLater())
                    //    changeRemoteControlClient(am, acquire);
                    mHasAudioFocus = true;
                }
            }
        } else {
            if (mHasAudioFocus) {
                final int result = am.abandonAudioFocus(mAudioFocusListener);
                am.setParameters("bgm_state=false");
                //am.unregisterMediaButtonEventReceiver(mRemoteControlClientReceiverComponent);
                //if (AndroidUtil.isICSOrLater())
                //    changeRemoteControlClient(am, acquire);
                mHasAudioFocus = false;
            }
        }
    }

    private void changeAudioFocus(boolean acquire) {
        if (AndroidUtil.isFroyoOrLater())
            changeAudioFocusFroyoOrLater(acquire);
    }

    @MainThread
    public synchronized void removeCallback(Callback cb) {
        mCallbacks.remove(cb);
    }

    public static class Client {
        public static final String TAG = "PlaybackService.Client";

        @MainThread
        public interface Callback {
            void onConnected(PlaybackService service);
            void onDisconnected();
        }

        private boolean mBound = false;
        private final Callback mCallback;
        private final Context mContext;

        private final ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                Log.d(TAG, "Service Connected");
                if (!mBound)
                    return;

                final PlaybackService service = PlaybackService.getService(iBinder);
                if (service != null)
                    mCallback.onConnected(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "Service Disconnected");
                mCallback.onDisconnected();
            }
        };

        private static Intent getServiceIntent(Context context) {
            return new Intent(context, PlaybackService.class);
        }

        private static void startService(Context context) {
            context.startService(getServiceIntent(context));
        }

        private static void stopService(Context context) {
            context.stopService(getServiceIntent(context));
        }

        public Client(Context context, Callback callback) {
            if (context == null || callback == null)
                throw new IllegalArgumentException("Context and callback can't be null");
            mContext = context;
            mCallback = callback;
        }

        @MainThread
        public void connect() {
            if (mBound)
                throw new IllegalStateException("already connected");
            startService(mContext);
            mBound = mContext.bindService(getServiceIntent(mContext), mServiceConnection, BIND_AUTO_CREATE);
        }

        @MainThread
        public void disconnect() {
            if (mBound) {
                mBound = false;
                mContext.unbindService(mServiceConnection);
            }
        }

        public static void restartService(Context context) {
            stopService(context);
            startService(context);
        }
    }
}
