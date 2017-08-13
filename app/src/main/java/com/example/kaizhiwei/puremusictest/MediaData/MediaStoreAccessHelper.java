package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.example.kaizhiwei.puremusictest.application.PureMusicApplication;

/**
 * Created by kaizhiwei on 16/10/29.
 */
public class MediaStoreAccessHelper {
    private static MediaStoreAccessHelper instance;

    public synchronized static MediaStoreAccessHelper getInstance(){
        if(instance == null){
            instance = new MediaStoreAccessHelper();
        }
        return instance;
    }

    private MediaStoreAccessHelper(){

    }

    public Cursor getAllSong(@Nullable String[] projection,
                             @Nullable String selection, @Nullable String[] selectionArgs,
                             @Nullable String sortOrder){
        PureMusicApplication app = PureMusicApplication.getInstance();
        Context context = app.getApplicationContext();
        ContentResolver cr = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return cr.query(uri, projection, selection, selectionArgs, sortOrder);
    }
}
