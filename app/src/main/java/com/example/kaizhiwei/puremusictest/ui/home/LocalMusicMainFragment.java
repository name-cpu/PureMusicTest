package com.example.kaizhiwei.puremusictest.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.ui.localmusic.AlertDialogFavorite;
import com.example.kaizhiwei.puremusictest.ui.localmusic.FavoriteListViewAdapter;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.HomePage.FavoriteLayout;
import com.example.kaizhiwei.puremusictest.HomePage.FavoriteMainFragment;
import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.service.PlaybackService;


/**
 * Created by kaizhiwei on 16/12/16.
 */
public class LocalMusicMainFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AlertDialogFavorite.OnAlterDialogFavoriteListener, FavoriteLayout.IFavoriteOperListener, PlaybackService.Client.Callback {
    private RelativeLayout ryLocal;
    private RelativeLayout ryLastPlay;
    private RelativeLayout ryDownload;
    private FavoriteLayout favoriteLayout;
    private TextView tvLocalSub;
    private TextView tvLastPlaySub;
    private TextView tvDonwloadSub;
    private TextView tvAddFavorite;
    private MyTextView mtvManageFavorite;
    private ImageView ivPlay;
    private Vibrator vibrator;
    private PlaybackService.Client mClient;
    private PlaybackService mService;

    public LocalMusicMainFragment(){
        super();
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
        ryDownload.setOnClickListener(this);

        favoriteLayout = (FavoriteLayout) rootView.findViewById(R.id.favoriteLayout);
        favoriteLayout.setMode(FavoriteLayout.READONLY_MODE);
        favoriteLayout.setIsHomePage(true);
        favoriteLayout.setFavoriteLayoutListener(this);

        tvAddFavorite = (TextView) rootView.findViewById(R.id.tvAddFavorite);
        mtvManageFavorite = (MyTextView) rootView.findViewById(R.id.mtvManageFavorite);
        tvAddFavorite.setOnClickListener(this);
        mtvManageFavorite.setOnClickListener(this);

        tvLocalSub = (TextView)rootView.findViewById(R.id.tvLocalSub);
        tvLastPlaySub = (TextView)rootView.findViewById(R.id.tvLastPlaySub);
        tvDonwloadSub = (TextView)rootView.findViewById(R.id.tvDonwloadSub);
        ivPlay = (ImageView)rootView.findViewById(R.id.ivPlay);
        ivPlay.setOnClickListener(this);

        tvLocalSub.setText(MediaLibrary.getInstance().getMediaEntitySize() + "首");
        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if(mClient == null){
            mClient = new PlaybackService.Client(this.getActivity(), this);
        }
        mClient.connect();
    }

    public void onDetach() {
        super.onDetach();
        if(mClient != null){
            mClient.disconnect();
        }
    }

    public void updateData(){
        if(favoriteLayout != null){
            favoriteLayout.setFavoriteEntityData(MediaLibrary.getInstance().getAllFavoriteEntity());
            favoriteLayout.show();
        }
    }

    @CallSuper
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void onPause() {
        super.onPause();
        //lbmLayout.onPause();
    }

    public void onStart() {
        super.onStart();
        //lbmLayout.onStart();
    }

    public void onStop() {
        super.onStop();
        //lbmLayout.onStop();
    }

    @Override
    public void onClick(View v) {
        if(v == ryLocal){
            HomeActivity.getInstance().switchToAudioFragment();
        }
        else if(v == mtvManageFavorite){
            if(favoriteLayout.getMode() == FavoriteListViewAdapter.READONLY_MODE){
                favoriteLayout.setMode(FavoriteListViewAdapter.EDIT_MODE);
                mtvManageFavorite.setText("完成");
            }
            else{
                favoriteLayout.setMode(FavoriteListViewAdapter.READONLY_MODE);
                mtvManageFavorite.setText("管理");
            }
        }
        else if(v == tvAddFavorite){
            AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), this);
            favoriteDialog.show();
            favoriteDialog.setAlertFavoriteListener(this);
            favoriteDialog.setOperType(AlertDialogFavorite.ADD_FAVORITE);
        }
        else if(v == ivPlay){
            if(mService != null){
                mService.next(false);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName) {
        favoriteLayout.setMode(FavoriteLayout.READONLY_MODE);
        mtvManageFavorite.setText("管理");
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
//                MusicInfoDao mediaEntity = mListMediaEntity.get(i);
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
    public void onModifyClick(FavoriteLayout layout, int position) {
        AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), this);
        favoriteDialog.show();
        favoriteDialog.setFavoriteEntity((FavoriteEntity)layout.getItem(position));
        favoriteDialog.setOperType(AlertDialogFavorite.MODIFY_FAVORITE);
    }

    @Override
    public void onDeleteClick(FavoriteLayout layout, int position) {
        FavoriteEntity favoriteEntity = (FavoriteEntity)layout.getItem(position);
        if(favoriteEntity == null || favoriteEntity._id < 0)
            return;

        if(MediaLibrary.getInstance().removeFavoriteEntity(favoriteEntity) == false){
            Toast.makeText(this.getContext(), "修改歌单失败",Toast.LENGTH_SHORT).show();
            return;
        }
        favoriteLayout.removeView(favoriteEntity);
    }

    @Override
    public void onItemLongClick(FavoriteLayout layout, int position) {
        vibrator.vibrate(30);
        onClick(mtvManageFavorite);
    }

    @Override
    public void onItemClick(FavoriteLayout layout, int position) {
        if(layout == null)
            return;

        FavoriteEntity  favoriteEntity = (FavoriteEntity)layout.getItem(position);
        if(favoriteEntity == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putLong(FavoriteMainFragment.FAVORITE_ID, favoriteEntity._id);
        bundle.putString(FavoriteMainFragment.FAVORITE_NAME, favoriteEntity.strFavoriteName);
        HomeActivity.getInstance().switchToFavoriteFragment(bundle);
    }

    @Override
    public void onConnected(PlaybackService service) {
        mService = service;
    }

    @Override
    public void onDisconnected() {
        mService = null;
    }
}
