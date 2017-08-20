package com.example.kaizhiwei.puremusictest.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.List;

/**
 * Created by kaizhiwei on 17/8/18.
 */

public class PlayMusicService extends Service implements IPlayMusic {
    private static PlayMusicImpl playMusicImpl = PlayMusicImpl.getInstance();
    private LocalBinder binder = new LocalBinder();

    @Override
    public void addListener(IPlayMusicListener listener) {
        playMusicImpl.addListener(listener);
    }

    @Override
    public void removeListener(IPlayMusicListener listener) {
        playMusicImpl.removeListener(listener);
    }

    @Override
    public void play() {
        playMusicImpl.play();
    }

    @Override
    public void pause() {
        playMusicImpl.pause();
    }

    @Override
    public void setDataSource(String filePath) {
        playMusicImpl.setDataSource(filePath);
    }

    @Override
    public void prepareAsync() {
        playMusicImpl.prepareAsync();
    }

    @Override
    public void prepare() {
        playMusicImpl.prepare();
    }

    @Override
    public void stop() {
        playMusicImpl.stop();
    }

    @Override
    public void next() {
        playMusicImpl.next();
    }

    @Override
    public void pre() {
        playMusicImpl.pre();
    }

    public void seekTo(int postition){
        playMusicImpl.seekTo(postition);
    }

    public List<MusicInfoDao> getPlaylist(){
        return playMusicImpl.getPlaylist();
    }

    public void setPlaylist(List<MusicInfoDao> list){
        playMusicImpl.setPlaylist(list);
    }

    public void clearPlaylist(){
        playMusicImpl.clearPlaylist();
    }

    public void addMusicInfo(MusicInfoDao musicInfoDao, int postion){
        playMusicImpl.addMusicInfo(musicInfoDao, postion);
    }

    public void removeMusicInfo(long id){
        playMusicImpl.removeMusicInfo(id);
    }

    public PlayMusicImpl.PlayMode getPlayMode(){
        return playMusicImpl.getPlayMode();
    }

    public void setPlayMode(PlayMusicImpl.PlayMode mode){
        playMusicImpl.setPlayMode(mode);
    }

    public int getCurPlayIndex(){
        return playMusicImpl.getCurPlayIndex();
    }

    public void setCurPlayIndex(int index){
        playMusicImpl.setCurPlayIndex(index);
    }

    public MusicInfoDao getCurPlayMusicDao(){
        return playMusicImpl.getCurPlayMusicDao();
    }

    public MusicInfoDao getMusicInfoByPosition(int position){
        return playMusicImpl.getMusicInfoByPosition(position);
    }

    public MusicInfoDao getMusicInfoById(long id){
        return playMusicImpl.getMusicInfoById(id);
    }

    public boolean isPlaying(){
        return playMusicImpl.isPlaying();
    }

    /**
     * 创建Binder对象，返回给客户端即Activity使用，提供数据交换的接口
     */
    public class LocalBinder extends Binder {
        // 声明一个方法，getService。（提供给客户端调用）
        PlayMusicService getService() {
            // 返回当前对象LocalService,这样我们就可在客户端端调用Service的公共方法了
            return PlayMusicService.this;
        }
    }

    public static class Client{
        private Context mContext;
        private Callback mCallback;

        public interface Callback{
            void onConnect(PlayMusicService service);
            void onDisconnect();
        }

        private ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocalBinder stub = (LocalBinder)service;
                PlayMusicService playMusicService = stub.getService();
                if(mCallback != null){
                    mCallback.onConnect(playMusicService);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if(mCallback != null){
                    mCallback.onDisconnect();
                }
            }
        };

        public Client(Context context, Callback callback){
            mContext = context;
            mCallback = callback;
            playMusicImpl.init(context);
        }

        public void connect(){
            Intent intent = new Intent(mContext, PlayMusicService.class);
            mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

        public void disconnect(){
            mContext.unbindService(serviceConnection);
        }

    }

    public PlayMusicService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
