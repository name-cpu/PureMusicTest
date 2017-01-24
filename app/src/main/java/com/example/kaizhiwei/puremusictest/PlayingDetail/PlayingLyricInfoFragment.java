package com.example.kaizhiwei.puremusictest.PlayingDetail;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaizhiwei.puremusictest.PlayingDetail.Lyric.LrcView;
import com.example.kaizhiwei.puremusictest.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kaizhiwei on 17/1/4.
 */
public class PlayingLyricInfoFragment  extends Fragment {
    private LrcView lrc_big;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playing_lyric, null, false);//关联布局文件
        lrc_big = (LrcView)rootView.findViewById(R.id.lrc_big);
        String strPath = Environment.getExternalStorageDirectory() + File.separator + "test.lrc";
        File file = new File(strPath);
        StringBuilder lineTxt = new StringBuilder();
        if(file.isFile() && file.exists()) { //判断文件是否存在
            try {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), "utf8");//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    lineTxt.append(str);
                }
                read.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lrc_big.loadLrc(lineTxt.toString());

        return rootView;
    }

    public void updateLyric(long time){
        if(lrc_big != null){
            lrc_big.updateTime(time);
        }
    }
}