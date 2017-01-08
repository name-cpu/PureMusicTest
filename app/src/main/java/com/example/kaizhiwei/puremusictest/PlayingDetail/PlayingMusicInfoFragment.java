package com.example.kaizhiwei.puremusictest.PlayingDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/1/4.
 */
public class PlayingMusicInfoFragment  extends Fragment implements View.OnClickListener {
    private ImageView ivArtist;
    private TextView tvSongName;
    private TextView tvArtistName;
    private MediaEntity mediaEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playing_musicinfo, null, false);
        ivArtist = (ImageView)rootView.findViewById(R.id.ivArtist);
        tvSongName = (TextView)rootView.findViewById(R.id.tvSongName);
        tvArtistName = (TextView)rootView.findViewById(R.id.tvArtistName);
        ivArtist.setOnClickListener(this);

        if(mediaEntity != null){
            tvSongName.setText(mediaEntity.title);
            tvArtistName.setText(mediaEntity.artist);
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    public void setMusciInfo(MediaEntity entity){
        if(entity == null)
            return;

        mediaEntity = entity;
        if(mediaEntity != null && tvSongName != null && tvArtistName != null){
            tvSongName.setText(mediaEntity.title);
            tvArtistName.setText(mediaEntity.artist);
        }
    }
}