package com.example.kaizhiwei.puremusictest;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaStoreAccessHelper;
import com.example.kaizhiwei.puremusictest.MediaData.SongEntity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView mListViewSong;
    private SongListAdapter mSongListAdpater;

    private MediaPlayer mPlayer;
    //频谱
    private Visualizer mVisualizer;
    //均衡器
    private Equalizer mEqualizer;
    //低音控制器
    private BassBoost mBassBoost;
    //预设音场
    private PresetReverb mPresetReverb;

    private BroadcastReceiver headsetReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String strAction = intent.getAction();
            if(strAction.equals(Intent.ACTION_HEADSET_PLUG)){
                if(intent.hasExtra("state")){
                    int state = intent.getIntExtra("state", 0);
                    String str = state == 0 ? "拔出耳机" : "插入耳机";
                    Log.i("weikaizhi", str);
                }
            }
            else if(strAction.equals(Intent.ACTION_MEDIA_BUTTON)){

            }
            else if(strAction.equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)){

            }
        }
    };

    private final static int ACTION_UPDATE_SONG_LIST = 1001;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg){
            int action = msg.what;
            if(action == ACTION_UPDATE_SONG_LIST){
                if(mSongListAdpater == null){
                    mSongListAdpater = new SongListAdapter(MainActivity.this, PureMusicApplication.getInstance().getListSongEntrty());
                }

                mListViewSong.setAdapter(mSongListAdpater);
                mSongListAdpater.notifyDataSetChanged();
            }
        }
    };

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(Intent.ACTION_MEDIA_BUTTON);
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        filter.setPriority(Integer.MAX_VALUE);
        this.registerReceiver(headsetReceiver, filter);

        mListViewSong = (ListView)this.findViewById(R.id.listViewSong);
        mListViewSong.setOnItemClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
                String projection[] = {
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.MIME_TYPE,
                        MediaStore.Audio.Media.DATE_ADDED,
                        MediaStore.Audio.Media.DATE_MODIFIED,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.TRACK,
                        MediaStore.Audio.Media.YEAR,


                        MediaStore.Audio.Media._ID};

                Cursor cursor =  MediaStoreAccessHelper.getInstance().getAllSong(projection, selection, null, null);
                List<SongEntity> listEntrty = new ArrayList<SongEntity>();
                while(cursor.moveToNext()){
                    SongEntity entrty = new SongEntity();
                    String strData = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    entrty.mSongPath = strData;

                    int size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                    entrty.mSize = size;

                    String strDiaplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    entrty.mDisplayName = strDiaplayName;

                    entrty.mMimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE));

                    entrty.mTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));

                    int date_added = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    entrty.mDateAdded = format.format(new Date(date_added));

                    int date_modified = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));
                    entrty.mDateAdded = format.format(new Date(date_modified));

                    entrty.mArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                    entrty.mAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    entrty.mAlbumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    entrty.mDuration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    entrty.mTrack = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK));
                    entrty.mYear = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR));

                    listEntrty.add(entrty);
                    cursor.moveToNext();
                }
                PureMusicApplication.getInstance().addSongEntrty(listEntrty);
                Message msg = MainActivity.this.handler.obtainMessage();
                msg.what = ACTION_UPDATE_SONG_LIST;
                MainActivity.this.handler.sendMessage(msg);
            }
        }).start();

        initMediaPlayer();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                break;
            case KeyEvent.KEYCODE_HEADSETHOOK:
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    void initMediaPlayer(){


    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    void initVisualizer(){
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        mVisualizer.setEnabled(true);
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {
                Log.i(TAG, "onWaveFormDataCapture: " + new String(bytes));
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {

            }
        }, Visualizer.getMaxCaptureRate()/2, true, false);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    void initEqualizer(){
        mEqualizer = new Equalizer(0, mPlayer.getAudioSessionId());
        mEqualizer.setEnabled(true);
        short minEQLevel =  mEqualizer.getBandLevelRange()[0];
        short maxEQLevel = mEqualizer.getBandLevelRange()[1];

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SongEntity entity = (SongEntity)mSongListAdpater.getItem(position);


        if(mPlayer != null && mPlayer.isPlaying()){
            mPlayer.stop();
        }

        mPlayer = MediaPlayer.create(MainActivity.this, Uri.parse(entity.mSongPath));
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.start();

    }
}
