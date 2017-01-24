package com.example.kaizhiwei.puremusictest.PlayingDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/1/4.
 */
public class PlayingArtistInfoFragment extends Fragment {
    private TextView tvArtist;
    private TextView tvAlbum;
    private MediaEntity mediaEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playing_atristinfo, null, false);
        tvArtist = (TextView)rootView.findViewById(R.id.tvArtist);
        tvAlbum = (TextView)rootView.findViewById(R.id.tvAlbum);

        if(mediaEntity != null){
            tvArtist.setText(mediaEntity.artist);
            tvAlbum.setText(mediaEntity.album);
        }
        return rootView;
    }

    public void setArtistAlbumInfo(MediaEntity entity){
        if(entity == null)
            return;
        mediaEntity = entity;
        if(mediaEntity != null && tvArtist != null && tvAlbum != null){
            tvArtist.setText(mediaEntity.artist);
            tvAlbum.setText(mediaEntity.album);
        }
    }
}
