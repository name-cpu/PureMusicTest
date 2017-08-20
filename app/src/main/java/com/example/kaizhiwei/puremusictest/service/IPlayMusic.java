package com.example.kaizhiwei.puremusictest.service;

/**
 * Created by kaizhiwei on 17/8/19.
 */

public interface IPlayMusic {
    //播放错误状态
    public static final int STATE_ERROR = -1;

    //播放空闲状态
    public static final int STATE_IDLE = 0;

    //播放准备中
    public static final int STATE_PAPERING = 1;

    //播放准备完成
    public static final int STATE_PAPERED = 2;

    //正在播放
    public static final int STATE_PLAY = 3;

    //暂停播放
    public static final int STATE_PAUSE = 4;

    //正在播放是缓存不够，自动暂停缓存数据
    public static final int STATE_BUFFER_PAUSE = 5;

    //正在播放时缓存够了，自动恢复播放
    public static final int STATE_BUFFER_RESUME = 6;

    //播放完成
    public static final int STATE_COMPLETE = 7;


    void addListener(IPlayMusicListener listener);

    void removeListener(IPlayMusicListener listener);

    void play();

    void pause();

    //void setDataSource(Uri dataSource);

    void setDataSource(String filePath);

    void prepareAsync();

    void prepare();

    void stop();

    void next();

    void pre();


}
