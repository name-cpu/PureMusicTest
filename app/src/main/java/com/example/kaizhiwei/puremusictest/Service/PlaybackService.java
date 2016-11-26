package com.example.kaizhiwei.puremusictest.Service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.VLCInstance;

import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/13.
 */
public class PlaybackService extends Service {
    private static final String PREF_MEDIA_LIST = "PREF_MEDIA_LIST";
    private static final String PREF_POSITION_IN_LIST = "PREF_POSITION_IN_LIST";
    private static final String PREF_POSITION_IN_SONG = "PREF_POSITION_IN_SONG";
    private static final String PREF_REPEAT_MODE = "PREF_REPEAT_MODE";

    private List<MediaEntity>  mMediaList;
    private int                 mCurrentIndex;
    private int                 mCurrentTime;
    private int                 mRepeatMode;
    private MediaPlayer         mMediaPlayer;
    final private ArrayList<Callback> mCallbacks = new ArrayList<Callback>();


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

    private final Media.EventListener mMediaListener = new Media.EventListener(){

        @Override
        public void onEvent(Media.Event event) {
//      event      switch (event.type) {
//                case Media.Event.ParsedChanged:
//                    Log.i(TAG, "Media.Event.ParsedChanged");
//                    final MediaWrapper mw = getCurrentMedia();
//                    if (mw != null)
//                        mw.updateMeta(mMediaPlayer);
//                    executeUpdate();
//                    break;
//                case Media.Event.MetaChanged:
//                    break;
//            }
//            for (Callback callback : mCallbacks)
//                callback.onMediaEvent(event);
        }
    };

    private final MediaPlayer.EventListener mMediaPlayerListener = new MediaPlayer.EventListener() {
        @Override
        public void onEvent(MediaPlayer.Event event) {
//            switch (event.type) {
//                case MediaPlayer.Event.Playing:
//
//                    if (mMediaSession == null)
//                        initMediaSession(PlaybackService.this);
//
//                    Log.i(TAG, "MediaPlayer.Event.Playing");
//                    executeUpdate();
//                    publishState(event.type);
//                    executeUpdateProgress();
//
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
//
//                    changeAudioFocus(true);
//                    setRemoteControlClientPlaybackState(event.type);
//                    showNotification();
//                    if (!mWakeLock.isHeld())
//                        mWakeLock.acquire();
//                    break;
//                case MediaPlayer.Event.Paused:
//                    Log.i(TAG, "MediaPlayer.Event.Paused");
//                    executeUpdate();
//                    publishState(event.type);
//                    executeUpdateProgress();
//                    showNotification();
//                    setRemoteControlClientPlaybackState(event.type);
//                    if (mWakeLock.isHeld())
//                        mWakeLock.release();
//                    break;
//                case MediaPlayer.Event.Stopped:
//                    Log.i(TAG, "MediaPlayer.Event.Stopped");
//                    executeUpdate();
//                    publishState(event.type);
//                    executeUpdateProgress();
//                    setRemoteControlClientPlaybackState(event.type);
//                    if (mWakeLock.isHeld())
//                        mWakeLock.release();
//                    changeAudioFocus(false);
//                    break;
//                case MediaPlayer.Event.EndReached:
//                    Log.i(TAG, "MediaPlayer.Event.EndReached");
//                    executeUpdate();
//                    executeUpdateProgress();
//                    determinePrevAndNextIndices(true);
//                    next();
//                    if (mWakeLock.isHeld())
//                        mWakeLock.release();
//                    changeAudioFocus(false);
//                    break;
//                case MediaPlayer.Event.EncounteredError:
//                    showToast(getString(
//                            R.string.invalid_location,
//                            mMediaList.getMRL(mCurrentIndex)), Toast.LENGTH_SHORT);
//                    executeUpdate();
//                    executeUpdateProgress();
//                    next();
//                    if (mWakeLock.isHeld())
//                        mWakeLock.release();
//                    break;
//                case MediaPlayer.Event.TimeChanged:
//                    break;
//                case MediaPlayer.Event.PositionChanged:
//                    updateWidgetPosition(event.getPositionChanged());
//                    break;
//                case MediaPlayer.Event.Vout:
//                    break;
//                case MediaPlayer.Event.ESAdded:
//                    if (event.getEsChangedType() == Media.Track.Type.Video) {
//                        if (!handleVout()) {
//                            /* Update notification content intent: resume video or resume audio activity */
//                            showNotification();
//                        }
//                    }
//                    break;
//                case MediaPlayer.Event.ESDeleted:
//                    break;
//                case MediaPlayer.Event.PausableChanged:
//                    mPausable = event.getPausable();
//                    break;
//                case MediaPlayer.Event.SeekableChanged:
//                    mSeekable = event.getSeekable();
//                    break;
//            }
//            for (Callback callback : mCallbacks)
//                callback.onMediaPlayerEvent(event);
        }
    };

    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer(VLCInstance.getInstance());

    }

    @Override
    public void onDestroy() {
        mMediaPlayer.release();
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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String strMediaList = pref.getString(PREF_MEDIA_LIST, "");
        List<String> listMedia = Arrays.asList(strMediaList.split(" "));
        mCurrentIndex = pref.getInt(PREF_POSITION_IN_LIST, 0);
        mCurrentTime = pref.getInt(PREF_POSITION_IN_SONG, 0);
        mRepeatMode = pref.getInt(PREF_REPEAT_MODE, 0);

        if(mMediaList == null)
            mMediaList = new ArrayList<>();

        for(int i = 0;i < listMedia.size();i++){
            Media media = new Media(VLCInstance.getInstance(), Uri.parse(listMedia.get(i)));
            MediaEntity entrty = new MediaEntity(media);
            mMediaList.add(entrty);
        }
    }

    public void savePrefData(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        pref.edit().putInt(PREF_POSITION_IN_LIST, mCurrentIndex).commit();
        pref.edit().putInt(PREF_POSITION_IN_SONG, mCurrentTime).commit();
        pref.edit().putInt(PREF_REPEAT_MODE, mRepeatMode).commit();

    }

    public void play(List<MediaEntity> list, int position){
        if(mMediaList != null){
            mMediaList.clear();
            mMediaList.addAll(list);
        }

        if(position < 0 || position >= list.size())
            mCurrentIndex = 0;

        String filePath = list.get(position).getFilePath();
        File file = new File(filePath);
        if(file.exists() == false)
            return;

        Media media  = new Media(VLCInstance.getInstance(), Uri.encode(filePath));
        media.setEventListener(mMediaListener);
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.setVideoTitleDisplay(MediaPlayer.Position.Disable, 0);
        mMediaPlayer.setEventListener(mMediaPlayerListener);
        mMediaPlayer.play();
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
