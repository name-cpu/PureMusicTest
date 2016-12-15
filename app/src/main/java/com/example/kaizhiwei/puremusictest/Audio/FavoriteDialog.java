
package com.example.kaizhiwei.puremusictest.Audio;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/27.
 */
public class FavoriteDialog extends Dialog implements View.OnClickListener, AbsListView.OnItemClickListener {
    private ListView lvFavorite;
    private TextView tvTitle;
    private FavoriteListViewAdapter mFavoriteListAdapter;
    private AudioListViewAdapter.AudioSongItemData mLVSongItemData = null;
    private AudioListViewAdapter.AudioFolderItemData mLVFolderItemData = null;
    private AudioListViewAdapter.AudioArtistAlbumItemData mLVArtistAlbumItemData = null;

    public FavoriteDialog(Context context) {
        super(context);
    }

    protected FavoriteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public FavoriteDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setContentView(View view) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        super.setContentView(view);
        lvFavorite = (ListView) view.findViewById(R.id.lvFavorite);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        Window window = this.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 可以在此设置显示动画
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;

        window.setAttributes(wl);
    }

    public void setFavoritelistData(List<FavoriteEntity> list){
        mFavoriteListAdapter = new FavoriteListViewAdapter(this.getContext(), list);
        mFavoriteListAdapter.notifyDataSetChanged();
        lvFavorite.setOnItemClickListener(this);
        lvFavorite.setAdapter(mFavoriteListAdapter);
    }

    public void setSongItemData(AudioListViewAdapter.AudioSongItemData itemData){
        mLVSongItemData = null;
        mLVFolderItemData = null;
        mLVArtistAlbumItemData = null;
        mLVSongItemData = itemData;
    }

    public void setTitle(String strTitle) {
        this.tvTitle.setText(strTitle);
    }

    protected void onStart() {
        super.onStart();
    }

    /**
     * Called to tell you that you're stopping.
     */
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //新组歌单
        if(position == 0){

        }
        //添加到默认的歌单
        else if(position == 1){
            boolean bExist = false;
            boolean bSuccess = false;
            if(mLVSongItemData != null){
                MediaEntity mediaEntity = MediaLibrary.getInstance().getMediaEntityById(mLVSongItemData.id);
                if(mediaEntity == null)
                    return ;

                FavoritesMusicEntity favoritesMusicEntity = new FavoritesMusicEntity();
                favoritesMusicEntity.musicinfo_id = mediaEntity._id;
                favoritesMusicEntity.artist = mediaEntity.artist;
                favoritesMusicEntity.album = mediaEntity.album;
                favoritesMusicEntity.fav_time = System.currentTimeMillis();
                favoritesMusicEntity.path = mediaEntity._data;
                favoritesMusicEntity.title = mediaEntity.title;
                favoritesMusicEntity.favorite_id = MediaLibrary.getInstance().getDefaultFavoriteEntityId();

                if(MediaLibrary.getInstance().queryIsFavoriteByMediaEntityId(mediaEntity._id, favoritesMusicEntity.favorite_id)){
                    bExist = true;
                }
                else{
                    bSuccess= MediaLibrary.getInstance().addFavoriteMusicEntity(favoritesMusicEntity);
                }
            }

            if(bExist){
                Toast.makeText(this.getContext(), "该歌曲已经添加到此歌单", Toast.LENGTH_SHORT).show();
            }
            else{
                if(bSuccess){
                    Toast.makeText(this.getContext(), "成功添加到此歌单", Toast.LENGTH_SHORT).show();
                }
            }
        }

        dismiss();
    }

    public static class Builder {
        private Context context;


        public Builder(Context context) {
            this.context = context;
        }

        public FavoriteDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final FavoriteDialog dialog = new FavoriteDialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.favorite_dialog, null);

            dialog.setContentView(layout);

            return dialog;
        }
    }
}

