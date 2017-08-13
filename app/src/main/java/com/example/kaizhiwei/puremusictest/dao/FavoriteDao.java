package com.example.kaizhiwei.puremusictest.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by 24820 on 2016/12/12.
 */
@Table(name = "favorite")
public class FavoriteDao implements Parcelable {
    public static final int DEFAULT_ADDONE_TYPE = -1;
    public static final int DEFAULT_FAVORITE_TYPE = 0;
    public static final int DEFAULT_CUSTOME_TYPE = 1;

    @Column(name = "id",isId = true,autoGen = true)
    private long _id;
    private String strFavoriteName;
    private String strFavoriteDesc;

    protected FavoriteDao(Parcel in) {
        _id = in.readLong();
        strFavoriteName = in.readString();
        strFavoriteDesc = in.readString();
        strFavoriteImgPath = in.readString();
        favoriteType = in.readLong();
        favoriteMusicNum = in.readLong();
        listFavoriteInfo = in.createTypedArrayList(FavoritesMusicEntity.CREATOR);
    }

    public static final Creator<FavoriteDao> CREATOR = new Creator<FavoriteDao>() {
        @Override
        public FavoriteDao createFromParcel(Parcel in) {
            return new FavoriteDao(in);
        }

        @Override
        public FavoriteDao[] newArray(int size) {
            return new FavoriteDao[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getStrFavoriteName() {
        return strFavoriteName;
    }

    public void setStrFavoriteName(String strFavoriteName) {
        this.strFavoriteName = strFavoriteName;
    }

    public String getStrFavoriteDesc() {
        return strFavoriteDesc;
    }

    public void setStrFavoriteDesc(String strFavoriteDesc) {
        this.strFavoriteDesc = strFavoriteDesc;
    }

    public String getStrFavoriteImgPath() {
        return strFavoriteImgPath;
    }

    public void setStrFavoriteImgPath(String strFavoriteImgPath) {
        this.strFavoriteImgPath = strFavoriteImgPath;
    }

    public long getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(long favoriteType) {
        this.favoriteType = favoriteType;
    }

    public long getFavoriteMusicNum() {
        return favoriteMusicNum;
    }

    public void setFavoriteMusicNum(long favoriteMusicNum) {
        this.favoriteMusicNum = favoriteMusicNum;
    }

    public List<FavoritesMusicEntity> getListFavoriteInfo() {
        return listFavoriteInfo;
    }

    public void setListFavoriteInfo(List<FavoritesMusicEntity> listFavoriteInfo) {
        this.listFavoriteInfo = listFavoriteInfo;
    }

    private String strFavoriteImgPath;
    private long favoriteType; //0 默认 1 自定义
    private long favoriteMusicNum;
    private List<FavoritesMusicEntity> listFavoriteInfo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(strFavoriteName);
        dest.writeString(strFavoriteDesc);
        dest.writeString(strFavoriteImgPath);
        dest.writeLong(favoriteType);
        dest.writeLong(favoriteMusicNum);
        dest.writeTypedList(listFavoriteInfo);
    }
}
