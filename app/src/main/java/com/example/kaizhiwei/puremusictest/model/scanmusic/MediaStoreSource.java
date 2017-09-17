package com.example.kaizhiwei.puremusictest.model.scanmusic;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public class MediaStoreSource extends BaseScanMusic {
    private List<MusicInfoDao> mListMusicInfos;
    private Context mContext;
    private HanyuPinyinOutputFormat mPinYinFormat;

    public MediaStoreSource(Context context){
        mContext = context;

        if (mPinYinFormat == null) {
            mPinYinFormat = new HanyuPinyinOutputFormat();
            mPinYinFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            mPinYinFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        }
    }

    public List<MusicInfoDao> getMusicInfos() {
        return mListMusicInfos;
    }

    @Override
    public List<MusicInfoDao> scan() {
        mListMusicInfos = new ArrayList<>();
        Cursor c = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (c != null) {
            MusicInfoDao musicInfoDao;
            while (c.moveToNext()) {
                musicInfoDao = new MusicInfoDao();
                musicInfoDao.set_data(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
                musicInfoDao.set_size(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                musicInfoDao.set_display_name(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                musicInfoDao.setTitle(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                musicInfoDao.setTitle_key(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE_KEY)));

                List<String> letters = getPinYin(musicInfoDao.getTitle());
                if(letters.size() > 0){
                    musicInfoDao.setTitle_letter(letters.get(0));
                }

                musicInfoDao.setDate_added(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));
                musicInfoDao.setDate_modified(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)));
                musicInfoDao.setMime_type(c.getString(c.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)));
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

        return mListMusicInfos;
    }

    public ArrayList<String> getPinYin(String strData){
        String str = strData.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\][1-9].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        ArrayList<String> arrayPinYin = new ArrayList<>();
        String strPinYin = "";
        String strFirstLetter = "";

        for(int i = 0;i < str.length();i++){
            char firstChar = str.toUpperCase(Locale.ENGLISH).charAt(i);
            if (Character.isLetter(firstChar)) {
                String[] vals = new String[0];
                try {
                    vals = PinyinHelper.toHanyuPinyinStringArray(firstChar, mPinYinFormat);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                    continue ;
                }

                boolean isFindPinYin = false;
                List<String> listPinYin = null;
                if (vals != null) {
                    listPinYin = Arrays.asList(vals);
                    if (listPinYin != null && listPinYin.size() > 0) {
                        isFindPinYin = true;
                    }
                }

                if (isFindPinYin) {
                    //去掉声平声降
                    String temp = listPinYin.get(0).substring(0, listPinYin.get(0).length() - 1);
                    strPinYin += temp;

                    if(i == 0){
                        strFirstLetter = strPinYin.substring(0,1);
                    }
                } else {
                    strPinYin += firstChar;
                    if(i == 0){
                        strFirstLetter += firstChar;
                    }
                }
            }
            else {
                strPinYin += "#";
            }
        }

        arrayPinYin.add(strPinYin);
        arrayPinYin.add(strFirstLetter);
        return arrayPinYin;
    }
}
