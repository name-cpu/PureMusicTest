package com.example.purevideoplayer;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public interface IVidepPlayer {
    void onStart();
    void onPause();
    void onReStart();
    void onClick();
    void onDoubleClick();
    void onFullScreen();
    void onExitFullScreen();
    void onSeekTo(int position);
}
