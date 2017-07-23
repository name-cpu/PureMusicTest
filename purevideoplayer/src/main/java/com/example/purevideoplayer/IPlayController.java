package com.example.purevideoplayer;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public interface IPlayController {
    void onStart();
    void onPause();
    void onRetry();
    void onClick();
    void onDoubleClick();
    void onFullScreen();
    void onExitFullScreen();

    void onSeekTo(int position);
    void onChangeBright(int percent);
    void onChangeVolumn(int percent);
}
