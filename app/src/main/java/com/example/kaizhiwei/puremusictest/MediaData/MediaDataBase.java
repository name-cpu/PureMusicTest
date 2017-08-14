package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kaizhiwei.puremusictest.application.PureMusicApplication;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/24.
 */
public class MediaDataBase{
    private Context mContext;
    private MediaDataBaseHelpder mDBHelper;
    private SQLiteDatabase mDB;
    private static MediaDataBase mInstance;
    private static final String TABLE_MUSIC_INFO = "musicInfo";
    private static final String TABLE_FAVORITES_MUSIC = "favorites_music";
    private static final String TABLE_FAVORITES = "favorites";

    public static synchronized MediaDataBase getInstance(){
        if(mInstance == null){
            mInstance = new MediaDataBase(PureMusicApplication.getInstance());
        }
        return mInstance;
    }

    private MediaDataBase(Context context){
        mContext = context;
        mDBHelper = new MediaDataBaseHelpder(context);
        mDB = mDBHelper.getWritableDatabase();
    }

    public boolean isEmptyMusicInfo(){
        String strSelectMusicInfo = "select * from " + TABLE_MUSIC_INFO + ";";
        Cursor c =  mDB.rawQuery(strSelectMusicInfo, null);
        if(c == null || c.getCount() == 0)
            return true;

        return false;
    }

    public void insertMusicInfos(List<MusicInfoDao> list){
        if(list == null)
            return;

        for(int i = 0;i < list.size();i++){
            insertMusicInfo(list.get(i));
        }
    }

    public void insertMusicInfo(MusicInfoDao entity){
        if(mDB == null || entity == null)
            return ;


    }

    public void deleteAllMusicInfo(){
        if(mDB == null)
            return ;

        String strDeleteSql = "delete from " + TABLE_MUSIC_INFO + ";";
        mDB.execSQL(strDeleteSql);
    }

    public boolean deleteMusicInfoByEntityId(MusicInfoDao entity){
        if(entity == null || entity.get_id() < 0)
            return false;

        String strDeleteSql = String.format("update %s set is_deleted = 1 where _id = %d;", TABLE_MUSIC_INFO, entity.get_id());
        mDB.execSQL(strDeleteSql);
        return true;
    }

    public MusicInfoDao queryMusicInfoById(long musicId){

            return null;

    }

    public List<MusicInfoDao> queryAllMusicInfo(){

        return  null;
    }

    public boolean insertFavoriteMusicInfo(FavoritesMusicEntity entity){
        if(mDB == null || entity == null || entity.favorite_id < 0)
            return false;

        String strInsertData = String.format("insert into %s (" +
                        "\"musicinfo_id\", \"song_id\", \"title\", \"artist_id\", \"artist\", \"album_id\", \"album\", \"fav_time\", \"havehigh\", \"charge\"," +
                        " \"fav_type\", \"allbitrate\", \"path\", \"file_from\", \"has_original\", \"has_mv_mobile\", \"song_source\", \"original_rate\", \"cache_status\", \"version\", " +
                        "\"has_pay_status\", \"is_offline\", \"favorite_id\")" +
                        "values(%d, %d, \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %d, %d, %d," +
                        "%d,\"%s\", \"%s\",%d, %d, %d, \"%s\", \"%s\", %d, \"%s\", %d," +
                        "%d, %d);",TABLE_FAVORITES_MUSIC,
                entity.musicinfo_id, entity.song_id, entity.title, entity.artist_id, entity.artist,
                entity.album_id, entity.album, entity.fav_time, entity.havehigh, entity.charge,
                entity.fav_type, entity.allbitrate, entity.path, entity.file_from, entity.has_original,
                entity.has_mv_mobile, entity.song_source, entity.original_rate, entity.cache_status, entity.version,
                entity.has_pay_status, entity.is_offline, entity.favorite_id);

        mDB.execSQL(strInsertData);

        String strQueryId = String.format("select _id from %s where fav_time = %s;", TABLE_FAVORITES_MUSIC,  entity.fav_time);
        Cursor c =  mDB.rawQuery(strQueryId, null);
        boolean bSuccess = false;
        while (c.moveToNext()) {
            entity._id = c.getInt(c.getColumnIndex("_id"));
            bSuccess = true;
        }
        return bSuccess;
    }

    public boolean deleteFavoriteMusicInfo(FavoritesMusicEntity entity, FavoriteEntity favoriteEntity){
        if(mDB == null || entity == null || entity._id < 0)
            return false;

        String strDelete = String.format("delete from %s where _id = %d", TABLE_FAVORITES_MUSIC, entity._id);
        mDB.execSQL(strDelete);
        return true;
    }

    public boolean deleteFavoriteMusicInfoByMusicInfoDaoId(long MusicInfoDaoId, long favoriteEntityId){
        if(mDB == null || MusicInfoDaoId < 0)
            return false;

        String strDelete = String.format("delete from %s where musicinfo_id = %d and favorite_id = %d", TABLE_FAVORITES_MUSIC, MusicInfoDaoId, favoriteEntityId);
        mDB.execSQL(strDelete);
        return true;
    }

    public List<FavoritesMusicEntity> queryAllFavoriteMusicInfo(){
        if(mDB == null)
            return null;

        List<FavoritesMusicEntity> listFavoriteMusicEntity = new ArrayList<>();
        String strSelectMusicInfo = "select * from " + TABLE_FAVORITES_MUSIC + ";";
        Cursor c =  mDB.rawQuery(strSelectMusicInfo, null);
        while (c.moveToNext()){
            FavoritesMusicEntity entity = new FavoritesMusicEntity();

            entity._id = c.getLong(c.getColumnIndex("_id"));
            entity.musicinfo_id = c.getLong(c.getColumnIndex("musicinfo_id"));
            entity.song_id = c.getLong(c.getColumnIndex("song_id"));
            entity.title = c.getString(c.getColumnIndex("title"));
            entity.artist_id = c.getString(c.getColumnIndex("artist_id"));
            entity.artist = c.getString(c.getColumnIndex("artist"));
            entity.album_id = c.getString(c.getColumnIndex("album_id"));
            entity.album = c.getString(c.getColumnIndex("album"));
            entity.fav_time = c.getLong(c.getColumnIndex("fav_time"));
            entity.havehigh = c.getLong(c.getColumnIndex("havehigh"));
            entity.charge = c.getLong(c.getColumnIndex("charge"));
            entity.fav_type = c.getLong(c.getColumnIndex("fav_type"));
            entity.artist = c.getString(c.getColumnIndex("artist"));
            entity.allbitrate = c.getString(c.getColumnIndex("allbitrate"));
            entity.path = c.getString(c.getColumnIndex("path"));
            entity.album = c.getString(c.getColumnIndex("album"));
            entity.file_from = c.getLong(c.getColumnIndex("file_from"));
            entity.has_original = c.getLong(c.getColumnIndex("has_original"));
            entity.has_mv_mobile = c.getLong(c.getColumnIndex("has_mv_mobile"));
            entity.song_source = c.getString(c.getColumnIndex("song_source"));
            entity.original_rate = c.getString(c.getColumnIndex("original_rate"));
            entity.cache_status = c.getLong(c.getColumnIndex("cache_status"));
            entity.version = c.getString(c.getColumnIndex("version"));
            entity.has_pay_status = c.getLong(c.getColumnIndex("has_pay_status"));
            entity.is_offline = c.getLong(c.getColumnIndex("is_offline"));
            entity.favorite_id = c.getLong(c.getColumnIndex("favorite_id"));
            listFavoriteMusicEntity.add(entity);
        }

        return  listFavoriteMusicEntity;
    }

    public List<FavoriteEntity> queryAllFavoriteInfo(){
        if(mDB == null)
            return null;

        List<FavoriteEntity> listFavoriteEntity = new ArrayList<>();
        String strSelectFavoritInfo = "select * from " + TABLE_FAVORITES + ";";
        Cursor c =  mDB.rawQuery(strSelectFavoritInfo, null);
        while (c.moveToNext()){
            FavoriteEntity entity = new FavoriteEntity();

            entity._id = c.getLong(c.getColumnIndex("_id"));
            entity.strFavoriteName = c.getString(c.getColumnIndex("favoriteName"));
            entity.favoriteType = c.getLong(c.getColumnIndex("favoriteType"));
            entity.strFavoriteDesc = c.getString(c.getColumnIndex("favoriteDesc"));
            entity.strFavoriteImgPath = c.getString(c.getColumnIndex("favoriteImgPath"));
            listFavoriteEntity.add(entity);
        }

        return  listFavoriteEntity;
    }

    public boolean insertFavoriteInfo(FavoriteEntity entity) {
        if (mDB == null || entity == null)
            return false;

        String strInsertData = String.format("insert into %s (" +
                        "\"favoriteName\", \"favoriteDesc\", \"favoriteImgPath\", \"favoriteType\") " +
                        "values(\"%s\",\"%s\",\"%s\", %d);", TABLE_FAVORITES,
                entity.strFavoriteName, entity.strFavoriteDesc, entity.strFavoriteImgPath, entity.favoriteType);

        mDB.execSQL(strInsertData);

        boolean bSuccess = false;
        String strQueryId = String.format("select _id from %s where favoriteName = \"%s\";", TABLE_FAVORITES, entity.strFavoriteName);
        Cursor c = mDB.rawQuery(strQueryId, null);
        if (c == null)
            return bSuccess;

        while (c.moveToNext()) {
            entity._id = c.getInt(c.getColumnIndex("_id"));
            bSuccess = true;
        }
        return bSuccess;
    }

    public boolean modifyFavoriteInfo(FavoriteEntity entity){
        if(mDB == null || entity == null)
            return false;

        String strUpdateData = String.format("update %s set " +
                        "favoriteName = \"%s\", favoriteDesc = \"%s\", favoriteImgPath = \"%s\"" + " where _id = %d;",
                        TABLE_FAVORITES,  entity.strFavoriteName, entity.strFavoriteDesc, entity.strFavoriteImgPath, entity._id);

        mDB.execSQL(strUpdateData);
        return true;
    }

    public boolean deleteFavoriteInfo(FavoriteEntity entity){
        if(mDB == null || entity == null)
            return false;

        String strUpdateData = String.format("delete from %s where _id = %d;",
                TABLE_FAVORITES, entity._id);
        mDB.execSQL(strUpdateData);
        return true;
    }

    public class MediaDataBaseHelpder extends SQLiteOpenHelper {
        private static final String DEFAULT_DATABSE_NAME = "PureMusic.db";
        private static final int DEFAULT_DATABSE_VERSION = 1;

        public MediaDataBaseHelpder(Context context) {
            super(context, DEFAULT_DATABSE_NAME, null, DEFAULT_DATABSE_VERSION);
        }

        public MediaDataBaseHelpder(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, null, version);
        }

        public MediaDataBaseHelpder(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, DEFAULT_DATABSE_NAME, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //新建musicinfo表
            String strMusicInfoTable = "CREATE TABLE IF NOT EXISTS" + " " + TABLE_MUSIC_INFO + "(\n" +
                    "  _id integer PRIMARY KEY,\n" +
                    "  _data text,\n" +
                    "  _size integer,\n" +
                    "  _display_name text,\n" +
                    "  title text,\n" +
                    "  title_key text,\n" +
                    "  title_letter text,\n" +
                    "  date_added integer,\n" +
                    "  date_modified integer,\n" +
                    "  mime_type text,\n" +
                    "  duration integer,\n" +
                    "  bookmark integer,\n" +
                    "  artist text,\n" +
                    "  artist_key text,\n" +
                    "  composer text,\n" +
                    "  album text,\n" +
                    "  album_key text,\n" +
                    "  album_art text,\n" +
                    "  track integer,\n" +
                    "  year integer,\n" +
                    "  mediastore_id integer,\n" +
                    "  lyric_path text,\n" +
                    "  is_lossless integer,\n" +
                    "  artist_image text,\n" +
                    "  album_image text,\n" +
                    "  last_playtime integer,\n" +
                    "  play_times integer,\n" +
                    "  data_from integer,\n" +
                    "  save_path text,\n" +
                    "  is_played integer,\n" +
                    "  song_id integer,\n" +
                    "  equalizer_level integer,\n" +
                    "  replay_gain_level text,\n" +
                    "  is_offline_cache integer,\n" +
                    "  is_faved integer DEFAULT(0),\n" +
                    "  have_high integer DEFAULT(0),\n" +
                    "  bitrate integer DEFAULT(0),\n" +
                    "  has_original integer DEFAULT(0),\n" +
                    "  flag integer DEFAULT(0),\n" +
                    "  original_rate text,\n" +
                    "  all_rates text,\n" +
                    "  is_deleted integer DEFAULT(0),\n" +
                    "  skip_auto_scan integer DEFAULT(0),\n" +
                    "  is_offline integer DEFAULT(0),\n" +
                    "  has_pay_status integer DEFAULT(0),\n" +
                    "  version text DEFAULT(''),\n" +
                    "  cache_path text,\n" +
                    "  play_type integer DEFAULT(0),\n" +
                    "  file_url text,\n" +
                    "  file_hash text\n" +
                    ");";
            db.execSQL(strMusicInfoTable);


            //创建favorites_music
            String strFavoriteTable = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES_MUSIC +" (\n" +
                    "  _id integer PRIMARY KEY AUTOINCREMENT,\n" +
                    "  musicinfo_id integer DEFAULT(-1),\n" +
                    "  song_id integer DEFAULT(-1),\n" +
                    "  title text,\n" +
                    "  artist_id text,\n" +
                    "  artist text,\n" +
                    "  album_id text,\n" +
                    "  album text,\n" +
                    "  fav_time integer,\n" +
                    "  havehigh integer NOT NULL,\n" +
                    "  charge integer,\n" +
                    "  fav_type integer,\n" +
                    "  allbitrate text,\n" +
                    "  path text,\n" +
                    "  file_from integer DEFAULT(0),\n" +
                    "  has_original integer DEFAULT(0),\n" +
                    "  has_mv_mobile integer DEFAULT(0),\n" +
                    "  song_source text,\n" +
                    "  original_rate text,\n" +
                    "  cache_status integer DEFAULT(-1),\n" +
                    "  version text DEFAULT(''),\n" +
                    "  has_pay_status integer DEFAULT(0),\n" +
                    "  is_offline integer DEFAULT(0),\n" +
                    "  favorite_id integer DEFAULT(-1)\n" +
                    ");";

            db.execSQL(strFavoriteTable);

            //创建favorites表
            String strFarovites = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + " (\n" +
                    " _id integer PRIMARY KEY AUTOINCREMENT,\n" +
                    " favoriteName text,\n" +
                    " favoriteDesc text,\n" +
                    " favoriteImgPath text,\n" +
                    " favoriteType integer DEFAULT(0)\n" +
                    ");";
            db.execSQL(strFarovites);

            String strDefaultFavorite = String.format("insert into %s values(null, \"我喜欢的单曲\", \"\", \"\", %d);",TABLE_FAVORITES,FavoriteEntity.DEFAULT_FAVORITE_TYPE);
            db.execSQL(strDefaultFavorite);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String strAlterMusicInfo = "ALTER TABLE musicInfo ADD COLUMN other STRING";
            db.execSQL("strAlterMusicInfo");
        }
    }

}


