package com.example.kaizhiwei.puremusictest.MediaData;

import java.util.List;

/**
 * Created by 24820 on 2016/12/12.
 */
public class FavoriteEntity {
    public static final int DEFAULT_ADDONE_TYPE = -1;
    public static final int DEFAULT_FAVORITE_TYPE = 0;
    public static final int DEFAULT_CUSTOME_TYPE = 1;

    public long _id;
    public String strFavoriteName;
    public long favoriteType; //0 默认 1 自定义
    public long favoriteMusicNum;
    public List<FavoritesMusicEntity> listFavoriteInfo;
}
