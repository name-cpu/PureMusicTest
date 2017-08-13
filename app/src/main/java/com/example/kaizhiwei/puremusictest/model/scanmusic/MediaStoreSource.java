package com.example.kaizhiwei.puremusictest.model.scanmusic;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public class MediaStoreSource extends BaseScanMusic {
    private List<MusicInfoDao> mListMusicInfos;
    private Context mContext;

    public MediaStoreSource(Context context){
        mContext = context;
    }

    public List<MusicInfoDao> getMusicInfos() {
        return mListMusicInfos;
    }

    public void scan(BaseHandler handler){
        new BaseRunnable(handler) {
            @Override
            public void doBusiness() throws Exception {
                mListMusicInfos = new ArrayList<>();
                Cursor c = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,null);
                if(c!=null) {
                    MusicInfoDao musicInfoDao;
                    while (c.moveToNext()) {
                        musicInfoDao = new MusicInfoDao();

                        musicInfoDao.set_data(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
                        musicInfoDao.set_size(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                        musicInfoDao.set_display_name(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                        //musicInfoDao.set_(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                        musicInfoDao.setDate_added(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));
                        musicInfoDao.setDate_modified(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)));
                        musicInfoDao.setMime_type(c.getString(c.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)));
                        musicInfoDao.setTitle_key(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE_KEY)));
                        musicInfoDao.setDuration(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                        musicInfoDao.setBookmark(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.BOOKMARK)));
                        musicInfoDao.setArtist(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                        musicInfoDao.setArtist_key(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST_KEY)));
                        //musicInfoDao.setAlbum_art(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST)));
                        musicInfoDao.setAlbum_key(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                        musicInfoDao.setAlbum(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY)));
                        musicInfoDao.setTrack(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.TRACK)));
                        musicInfoDao.setYear(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.YEAR)));
                        mListMusicInfos.add(musicInfoDao);
                    }
                    c.close();
                }
            }
        };
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onScaning(String fileName, String filePath, int persent) {

    }

    @Override
    public void onFinish() {

    }
}
