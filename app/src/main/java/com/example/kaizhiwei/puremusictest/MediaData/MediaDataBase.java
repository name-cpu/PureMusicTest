package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kaizhiwei.puremusictest.PureMusicApplication;

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

    public void insertMusicInfo(MediaEntity entity){
        if(mDB == null || entity == null)
            return ;

        String strInsertData = String.format("insert into %s (" +
                "\"_data\", \"_size\", \"_display_name\", \"title\", \"title_key\", \"title_letter\", \"date_added\", \"date_modified\", \"mime_type\", \"duration\"," +
                " \"bookmark\", \"artist\", \"artist_key\", \"composer\", \"album\", \"album_key\", \"album_art\", \"track\", \"year\", \"mediastore_id\", " +
                "\"lyric_path\", \"is_lossless\" ,\"artist_image\", \"album_image\", \"last_playtime\", \"play_times\", \"data_from\", \"save_path\", \"is_played\", \"song_id\", " +
                "\"equalizer_level\", \"replay_gain_level\", \"is_offline_cache\", \"is_faved\", \"have_high\", \"bitrate\", \"has_original\", \"flag\", \"original_rate\", \"all_rates\"," +
                " \"is_deleted\", \"skip_auto_scan\", \"is_offline\", \"has_pay_status\", \"version\", \"cache_path\", \"play_type\", \"file_url\", \"file_hash\") " +
                "values(\"%s\", %d, \"%s\", \"%s\", \"%s\", \"%s\", %d, %d, \"%s\", %d, " +
                "%d,\"%s\", \"%s\",\"%s\", \"%s\", \"%s\", \"%s\", %d ,%d, %d, " +
                "\"%s\", %d,  \"%s\",\"%s\", %d, %d, %d, \"%s\", %d, %d," +
                "%d, \"%s\", %d, %d, %d, %d,%d,%d,\"%s\", \"%s\", " +
                "%d,%d,%d,%d,\"%s\",\"%s\",%d, \"%s\", \"%s\");",TABLE_MUSIC_INFO,
                entity._data, entity._size, entity._display_name, entity.title, entity.title_key,
                entity.title_letter, entity.date_added, entity.date_modified, entity.mime_type, entity.duration,
                entity.bookmark, entity.artist, entity.artist_key, entity.composer, entity.album,
                entity.album_key, entity.album_art, entity.track, entity.year, entity.mediastore_id,
                entity.lyric_path, entity.is_lossless, entity.artist_image, entity.album_image, entity.last_playtime,
                entity.play_times, entity.data_from, entity.save_path, entity.is_played, entity.song_id,
                entity.equalizer_level, entity.replay_gain_level, entity.is_offline_cache, entity.is_faved, entity.have_high,
                entity.bitrate, entity.has_original, entity.flag, entity.original_rate, entity.all_rates,
                entity.is_deleted, entity.skip_auto_scan, entity.is_offline, entity.has_pay_status, entity.version,
                entity.cache_path, entity.play_type, entity.file_url, entity.file_hash);


        mDB.execSQL(strInsertData);

        String strQueryId = String.format("select * from %s where _data = \"%s\";", TABLE_MUSIC_INFO,  entity._data);
        Cursor c =  mDB.rawQuery(strQueryId, null);
        while (c.moveToNext()) {
            entity._id = c.getInt(c.getColumnIndex("_id"));
        }
    }

    public void deleteAllMusicInfo(){
        if(mDB == null)
            return ;

        String strDeleteSql = "delete from " + TABLE_MUSIC_INFO + ";";
        mDB.execSQL(strDeleteSql);
    }

    public boolean deleteMusicInfoByEntityId(MediaEntity entity){
        if(entity == null || entity._id < 0)
            return false;

        String strDeleteSql = String.format("update %s set is_deleted = 1 where _id = %d;", TABLE_MUSIC_INFO, entity._id);
        mDB.execSQL(strDeleteSql);
        return true;
    }

    public List<MediaEntity> queryAllMusicInfo(){
        if(mDB == null)
            return null;

        List<MediaEntity> listMediaEntity = new ArrayList<>();
        String strSelectMusicInfo = "select * from " + TABLE_MUSIC_INFO + " where is_deleted = 0 and skip_auto_scan = 0;";
        Cursor c =  mDB.rawQuery(strSelectMusicInfo, null);
        while (c.moveToNext()){
            MediaEntity entity = new MediaEntity();

            entity._id = c.getInt(c.getColumnIndex("_id"));
            entity._data = c.getString(c.getColumnIndex("_data"));
            entity._size = c.getLong(c.getColumnIndex("_size"));
            entity._display_name = c.getString(c.getColumnIndex("_display_name"));
            entity.title = c.getString(c.getColumnIndex("title"));
            entity.title_key = c.getString(c.getColumnIndex("title_key"));
            entity.title_letter = c.getString(c.getColumnIndex("title_letter"));
            entity.date_added = c.getLong(c.getColumnIndex("date_added"));
            entity.date_modified = c.getLong(c.getColumnIndex("date_modified"));
            entity.mime_type = c.getString(c.getColumnIndex("mime_type"));
            entity.duration = c.getLong(c.getColumnIndex("duration"));
            entity.bookmark = c.getLong(c.getColumnIndex("bookmark"));
            entity.artist = c.getString(c.getColumnIndex("artist"));
            entity.artist_key = c.getString(c.getColumnIndex("artist_key"));
            entity.composer = c.getString(c.getColumnIndex("composer"));
            entity.album = c.getString(c.getColumnIndex("album"));
            entity.album_key = c.getString(c.getColumnIndex("album_key"));
            entity.album_art = c.getString(c.getColumnIndex("album_art"));
            entity.track = c.getLong(c.getColumnIndex("track"));
            entity.year = c.getLong(c.getColumnIndex("year"));
            entity.mediastore_id = c.getLong(c.getColumnIndex("mediastore_id"));
            entity.lyric_path = c.getString(c.getColumnIndex("lyric_path"));
            entity.is_lossless = c.getLong(c.getColumnIndex("is_lossless"));
            entity.artist_image = c.getString(c.getColumnIndex("artist_image"));
            entity.album_image = c.getString(c.getColumnIndex("album_image"));
            entity.last_playtime = c.getLong(c.getColumnIndex("last_playtime"));
            entity.play_times = c.getLong(c.getColumnIndex("play_times"));
            entity.data_from = c.getLong(c.getColumnIndex("data_from"));
            entity.save_path = c.getString(c.getColumnIndex("save_path"));
            entity.is_played = c.getLong(c.getColumnIndex("is_played"));
            entity.song_id = c.getLong(c.getColumnIndex("song_id"));
            entity.equalizer_level = c.getLong(c.getColumnIndex("equalizer_level"));
            entity.replay_gain_level = c.getLong(c.getColumnIndex("replay_gain_level"));
            entity.is_offline_cache = c.getLong(c.getColumnIndex("is_offline_cache"));
            entity.is_faved = c.getLong(c.getColumnIndex("is_faved"));
            entity.have_high = c.getLong(c.getColumnIndex("have_high"));
            entity.bitrate = c.getLong(c.getColumnIndex("bitrate"));
            entity.has_original = c.getLong(c.getColumnIndex("has_original"));
            entity.flag = c.getLong(c.getColumnIndex("flag"));
            entity.original_rate = c.getString(c.getColumnIndex("original_rate"));
            entity.all_rates = c.getString(c.getColumnIndex("all_rates"));
            entity.is_deleted = c.getLong(c.getColumnIndex("is_deleted"));
            entity.skip_auto_scan = c.getLong(c.getColumnIndex("skip_auto_scan"));
            entity.is_offline = c.getLong(c.getColumnIndex("is_offline"));
            entity.has_pay_status = c.getLong(c.getColumnIndex("has_pay_status"));
            entity.version = c.getString(c.getColumnIndex("version"));
            entity.cache_path = c.getString(c.getColumnIndex("cache_path"));
            entity.play_type = c.getLong(c.getColumnIndex("play_type"));
            entity.file_url = c.getString(c.getColumnIndex("file_url"));
            entity.file_hash = c.getString(c.getColumnIndex("file_hash"));


            listMediaEntity.add(entity);
        }

        return  listMediaEntity;
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

    public boolean deleteFavoriteMusicInfoByMediaEntityId(long mediaEntityId, long favoriteEntityId){
        if(mDB == null || mediaEntityId < 0)
            return false;

        String strDelete = String.format("delete from %s where musicinfo_id = %d and favorite_id = %d", TABLE_FAVORITES_MUSIC, mediaEntityId, favoriteEntityId);
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
            listFavoriteEntity.add(entity);
        }

        return  listFavoriteEntity;
    }

    public boolean insertFavoriteInfo(FavoriteEntity entity) {
        if (mDB == null || entity == null)
            return false;

        String strInsertData = String.format("insert into %s (" +
                        "\"favoriteName\", \"favoriteType\")" +
                        "values(\"%s\", %d);", TABLE_FAVORITES,
                entity.strFavoriteName, entity.favoriteType);

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
                        "favoriteName = \"%s\"" + " where _id = %d;",
                        TABLE_FAVORITES,  entity.strFavoriteName, entity._id);

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
                    " favoriteType integer DEFAULT(0)\n" +
                    ");";
            db.execSQL(strFarovites);

            String strDefaultFavorite = "insert into " + TABLE_FAVORITES + " values (null, '我喜欢的单曲'" + ", " + FavoriteEntity.DEFAULT_FAVORITE_TYPE + ");";
            db.execSQL(strDefaultFavorite);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String strAlterMusicInfo = "ALTER TABLE musicInfo ADD COLUMN other STRING";
            db.execSQL("strAlterMusicInfo");
        }
    }

}


