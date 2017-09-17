package com.example.kaizhiwei.puremusictest.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

/**
 * Created by kaizhiwei on 17/9/16.
 */

public class RingBellUtil {

    public static boolean setRingBell(Context context, String filePath){
        if(context == null)
            return false;

        File file = new File(filePath);
        if(!file.exists())
            return false;

        ContentValues cv = new ContentValues();
        Uri uri = null, newUri = null;
        uri = MediaStore.Audio.Media.getContentUriForPath(filePath);
        Cursor cursor = context.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{filePath}, null);
        if(cursor.moveToFirst() && cursor.getCount() > 0){
            String id = cursor.getString(0);
            cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            cv.put(MediaStore.Audio.Media.IS_ALARM, false);
            cv.put(MediaStore.Audio.Media.IS_MUSIC, false);

            // 把需要设为铃声的歌曲更新铃声库
            context.getContentResolver().update(uri, cv, MediaStore.MediaColumns.DATA + "=?",new String[] { filePath });
            newUri = ContentUris.withAppendedId(uri, Long.valueOf(id));
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
            return true;
        }

        return false;
    }

}
