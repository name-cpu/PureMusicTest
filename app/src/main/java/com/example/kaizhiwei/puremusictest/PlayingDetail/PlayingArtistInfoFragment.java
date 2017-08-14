package com.example.kaizhiwei.puremusictest.PlayingDetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/1/4.
 */
public class PlayingArtistInfoFragment extends Fragment {
    private TextView tvArtist;
    private TextView tvAlbum;
    private com.example.kaizhiwei.puremusictest.dao.MusicInfoDao MusicInfoDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playing_atristinfo, null, false);
        tvArtist = (TextView)rootView.findViewById(R.id.tvArtist);
        tvAlbum = (TextView)rootView.findViewById(R.id.tvAlbum);

        if(MusicInfoDao != null){
            tvArtist.setText(MusicInfoDao.getArtist());
            tvAlbum.setText(MusicInfoDao.getAlbum());
        }
        return rootView;
    }

    public void setArtistAlbumInfo(com.example.kaizhiwei.puremusictest.dao.MusicInfoDao entity){
        if(entity == null)
            return;
        MusicInfoDao = entity;
        if(MusicInfoDao != null && tvArtist != null && tvAlbum != null){
            tvArtist.setText(MusicInfoDao.getArtist());
            tvAlbum.setText(MusicInfoDao.getAlbum());
        }
    }
}
