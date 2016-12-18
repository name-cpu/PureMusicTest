package com.example.kaizhiwei.puremusictest.HomePage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.Audio.AlertDialogFavorite;
import com.example.kaizhiwei.puremusictest.Audio.AudioActivity;
import com.example.kaizhiwei.puremusictest.Audio.FavoriteListViewAdapter;
import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;


/**
 * Created by kaizhiwei on 16/12/16.
 */
public class Fragment_LocalMusic extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AlertDialogFavorite.OnAlterDialogFavoriteListener, FavoriteLayout.IFavoriteOperListener {
    private RelativeLayout ryLocal;
    private RelativeLayout ryLastPlay;
    private RelativeLayout ryDownload;
    private FavoriteLayout favoriteLayout;
    private TextView tvAddFavorite;
    private TextView tvManageFavorite;
    private HomeActivity mHomeActivity;

    public Fragment_LocalMusic(HomeActivity home){
        mHomeActivity = home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_localmusic, null, false);//关联布局文件
        ryLocal = (RelativeLayout) rootView.findViewById(R.id.ryLocal);
        ryLastPlay = (RelativeLayout) rootView.findViewById(R.id.ryLastPlay);
        ryDownload = (RelativeLayout) rootView.findViewById(R.id.ryDownload);
        ryLocal.setOnClickListener(this);
        ryLastPlay.setOnClickListener(this);
        ryLastPlay.setOnClickListener(this);

        favoriteLayout = (FavoriteLayout) rootView.findViewById(R.id.favoriteLayout);
        favoriteLayout.setFavoriteEntityData(MediaLibrary.getInstance().getAllFavoriteEntity());
        favoriteLayout.setMode(FavoriteLayout.READONLY_MODE);
        favoriteLayout.setIsHomePage(true);
        favoriteLayout.setFavoriteLayoutListener(this);
        favoriteLayout.show();

        tvAddFavorite = (TextView) rootView.findViewById(R.id.tvAddFavorite);
        tvManageFavorite = (TextView) rootView.findViewById(R.id.tvManageFavorite);
        tvAddFavorite.setOnClickListener(this);
        tvManageFavorite.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == ryLocal){
            mHomeActivity.switchToAudioFragment();
        }
        else if(v == tvManageFavorite){
            if(favoriteLayout.getMode() == FavoriteListViewAdapter.READONLY_MODE){
                favoriteLayout.setMode(FavoriteListViewAdapter.EDIT_MODE);
                tvManageFavorite.setText("完成");
            }
            else{
                favoriteLayout.setMode(FavoriteListViewAdapter.READONLY_MODE);
                tvManageFavorite.setText("管理");
            }
        }
        else if(v == tvAddFavorite){
            AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), this);
            favoriteDialog.show();
            favoriteDialog.setAlertFavoriteListener(this);
            favoriteDialog.setOperType(AlertDialogFavorite.ADD_FAVORITE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName) {
        favoriteLayout.setMode(FavoriteLayout.READONLY_MODE);
        tvManageFavorite.setText("管理");
        if(operType == AlertDialogFavorite.USER_CANCEL){

        }
        else if(operType == AlertDialogFavorite.ADD_FAVORITE){
            FavoriteEntity favoriteEntity = new FavoriteEntity();
            favoriteEntity.favoriteType = FavoriteEntity.DEFAULT_CUSTOME_TYPE;
            favoriteEntity.strFavoriteName = strFavoriteName;
            if(MediaLibrary.getInstance().addFavoriteEntity(favoriteEntity) == false){
                Toast.makeText(this.getContext(), "新建歌单失败",Toast.LENGTH_SHORT).show();
                return;
            }

            favoriteLayout.insertView(1, favoriteEntity);
//            boolean bSuccess = false;
//            for(int i = 0;i < mListMediaEntity.size();i++){
//                MediaEntity mediaEntity = mListMediaEntity.get(i);
//                if(mediaEntity == null)
//                    continue;
//
//                FavoritesMusicEntity favoritesMusicEntity = new FavoritesMusicEntity();
//                favoritesMusicEntity.musicinfo_id = mediaEntity._id;
//                favoritesMusicEntity.artist = mediaEntity.artist;
//                favoritesMusicEntity.album = mediaEntity.album;
//                favoritesMusicEntity.fav_time = System.currentTimeMillis();
//                favoritesMusicEntity.path = mediaEntity._data;
//                favoritesMusicEntity.title = mediaEntity.title;
//                favoritesMusicEntity.favorite_id = favoriteEntity._id;
//
//                bSuccess = MediaLibrary.getInstance().addFavoriteMusicEntity(favoritesMusicEntity);
//            }
//
//            if(bSuccess){
//                strPromt = String.format("成功添加到\"%s\"", favoriteEntity.strFavoriteName);
//            }
//            else{
//                strPromt = String.format("添加失败");
//            }

        }
        else if(operType == AlertDialogFavorite.MODIFY_FAVORITE){
            FavoriteEntity favoriteEntity = dialog.getFavoriteEntity();
            if(favoriteEntity == null)
                return;

            favoriteEntity.strFavoriteName = strFavoriteName;
            if(MediaLibrary.getInstance().modifyFavoriteEntity(favoriteEntity) == false){
                Toast.makeText(this.getContext(), "修改歌单失败",Toast.LENGTH_SHORT).show();
                return;
            }
            favoriteLayout.updateView(favoriteEntity);
        }
    }

    @Override
    public void OnModifyClick(FavoriteLayout layout, int position) {
        AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), this);
        favoriteDialog.show();
        favoriteDialog.setFavoriteEntity((FavoriteEntity)layout.getItem(position));
        favoriteDialog.setOperType(AlertDialogFavorite.MODIFY_FAVORITE);
    }

    @Override
    public void OnDeleteClick(FavoriteLayout layout, int position) {
        FavoriteEntity favoriteEntity = (FavoriteEntity)layout.getItem(position);
        if(favoriteEntity == null || favoriteEntity._id < 0)
            return;

        if(MediaLibrary.getInstance().removeFavoriteEntity(favoriteEntity) == false){
            Toast.makeText(this.getContext(), "修改歌单失败",Toast.LENGTH_SHORT).show();
            return;
        }
        favoriteLayout.removeView(favoriteEntity);
    }
}
