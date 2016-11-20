package com.example.kaizhiwei.puremusictest.MediaData;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.videolan.libvlc.Media;

import java.io.Serializable;

/**
 * Created by kaizhiwei on 16/11/12.
 */



public class MediaEntrty implements Parcelable, Serializable {

    //meta data
    private String mTitle;
    private String mArtist;
    private String mGenre;
    private String mCopyright;
    private String mAlbum;
    private String mTrackNumber;
    private String mDescription;
    private String mRating;
    private String mDate;
    private String mSetting;
    private String mURL;
    private String mLanguage;
    private String mNowPlaying;
    private String mPublisher;
    private String mEncodedBy;
    private String mArtworkURL;
    private String mTrackID;
    private String mTrackTotal;
    private String mDirector;
    private String mSeason;
    private String mEpisode;
    private String mShowName;
    private String mActors;
    private String mAlbumArtist;
    private String mDiscNumber;

    private Uri mUri;
    private String mFileName;
    private long mDuration;


    private Media mMedia;
    public MediaEntrty(Media media){
        if(media.getType() == Media.Track.Type.Audio){

        }
        mUri = media.getUri();
        mFileName = mUri.getLastPathSegment();
        mDuration = media.getDuration();
        mMedia = media;
        updateMetaData();
    }

    protected MediaEntrty(Parcel in) {
        mTitle = in.readString();
        mArtist = in.readString();
        mGenre = in.readString();
        mCopyright = in.readString();
        mAlbum = in.readString();
        mTrackNumber = in.readString();
        mDescription = in.readString();
        mRating = in.readString();
        mDate = in.readString();
        mSetting = in.readString();
        mURL = in.readString();
        mLanguage = in.readString();
        mNowPlaying = in.readString();
        mPublisher = in.readString();
        mEncodedBy = in.readString();
        mArtworkURL = in.readString();
        mTrackID = in.readString();
        mTrackTotal = in.readString();
        mDirector = in.readString();
        mSeason = in.readString();
        mEpisode = in.readString();
        mShowName = in.readString();
        mActors = in.readString();
        mAlbumArtist = in.readString();
        mDiscNumber = in.readString();
    }

    public static final Creator<MediaEntrty> CREATOR = new Creator<MediaEntrty>() {
        @Override
        public MediaEntrty createFromParcel(Parcel in) {
            return new MediaEntrty(in);
        }

        @Override
        public MediaEntrty[] newArray(int size) {
            return new MediaEntrty[size];
        }
    };

    public String getTitle(){
        return mTitle;
    }

    public String getFileName(){
        return mFileName;
    }

    public String getArtist(){
        return mArtist;
    }

    public String getAlbum(){
        return mAlbum;
    }

    public Uri getUri(){
        return mUri;
    }

    public long getDuration(){
        return mDuration;
    }

    private void updateMetaData(){
        if(mMedia == null)
            return ;

        mTitle = mMedia.getMeta(Media.Meta.Title);
        mArtist = mMedia.getMeta(Media.Meta.Artist);
        mGenre = mMedia.getMeta(Media.Meta.Genre);
        mCopyright = mMedia.getMeta(Media.Meta.Copyright);
        mAlbum = mMedia.getMeta(Media.Meta.Album);
        mTrackNumber = mMedia.getMeta(Media.Meta.TrackNumber);
        mDescription = mMedia.getMeta(Media.Meta.Description);
        mRating = mMedia.getMeta(Media.Meta.Rating);
        mDate = mMedia.getMeta(Media.Meta.Date);
        mSetting = mMedia.getMeta(Media.Meta.Setting);
        mURL = mMedia.getMeta(Media.Meta.URL);
        mLanguage = mMedia.getMeta(Media.Meta.Language);
        mNowPlaying = mMedia.getMeta(Media.Meta.NowPlaying);
        mPublisher = mMedia.getMeta(Media.Meta.Publisher);
        mEncodedBy = mMedia.getMeta(Media.Meta.EncodedBy);
        mArtworkURL = mMedia.getMeta(Media.Meta.ArtworkURL);
        mTrackID = mMedia.getMeta(Media.Meta.TrackID);
        mTrackTotal = mMedia.getMeta(Media.Meta.TrackTotal);
        mDirector = mMedia.getMeta(Media.Meta.Director);
        mSeason = mMedia.getMeta(Media.Meta.Season);
        mEpisode = mMedia.getMeta(Media.Meta.Episode);
        mShowName = mMedia.getMeta(Media.Meta.ShowName);
        mActors = mMedia.getMeta(Media.Meta.Actors);
        mAlbumArtist = mMedia.getMeta(Media.Meta.AlbumArtist);
        mDiscNumber = mMedia.getMeta(Media.Meta.DiscNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mArtist);
        dest.writeString(mGenre);
        dest.writeString(mCopyright);
        dest.writeString(mAlbum);
        dest.writeString(mTrackNumber);
        dest.writeString(mDescription);
        dest.writeString(mRating);
        dest.writeString(mDate);
        dest.writeString(mSetting);
        dest.writeString(mURL);
        dest.writeString(mLanguage);
        dest.writeString(mNowPlaying);
        dest.writeString(mPublisher);
        dest.writeString(mEncodedBy);
        dest.writeString(mArtworkURL);
        dest.writeString(mTrackID);
        dest.writeString(mTrackTotal);
        dest.writeString(mDirector);
        dest.writeString(mSeason);
        dest.writeString(mEpisode);
        dest.writeString(mShowName);
        dest.writeString(mActors);
        dest.writeString(mAlbumArtist);
        dest.writeString(mDiscNumber);
    }
}
