package com.example.kaizhiwei.puremusictest.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.IPlaylistDataObserver;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.service.PlaybackService;
import com.example.kaizhiwei.puremusictest.ui.favorite.AlertDialogFavorite;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by kaizhiwei on 16/12/16.
 */
public class LocalMusicMainFragment extends MyBaseFragment implements View.OnClickListener,  PlaybackService.Client.Callback, IPlaylistDataObserver {
    @Bind(R.id.ivIcon)
    ImageView ivIcon;
    @Bind(R.id.tvLocalMain)
    TextView tvLocalMain;
    @Bind(R.id.tvLocalSub)
    TextView tvLocalSub;
    @Bind(R.id.ivPlay)
    ImageView ivPlay;
    @Bind(R.id.ryLocal)
    RelativeLayout ryLocal;
    @Bind(R.id.ivIconLastPlay)
    ImageView ivIconLastPlay;
    @Bind(R.id.tvLastPlayMain)
    TextView tvLastPlayMain;
    @Bind(R.id.tvLastPlaySub)
    TextView tvLastPlaySub;
    @Bind(R.id.ryLastPlay)
    RelativeLayout ryLastPlay;
    @Bind(R.id.ivIconDownload)
    ImageView ivIconDownload;
    @Bind(R.id.tvDonwloadMain)
    TextView tvDonwloadMain;
    @Bind(R.id.tvDonwloadSub)
    TextView tvDonwloadSub;
    @Bind(R.id.ryDownload)
    RelativeLayout ryDownload;
    @Bind(R.id.tvAddFavorite)
    TextView tvAddFavorite;
    @Bind(R.id.mtvManageFavorite)
    MyTextView mtvManageFavorite;
    @Bind(R.id.rvFavorite)
    RecyclerView rvFavorite;
    @Bind(R.id.llContent)
    LinearLayout llContent;

    private FavoriteViewAdpapter mFavoriteViewAdapter;

    private Vibrator vibrator;
    private PlaybackService.Client mClient;
    private PlaybackService mService;

    public LocalMusicMainFragment() {
        super();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home_localmusic;
    }

    @Override
    protected void initView() {
        ryLocal.setOnClickListener(this);
        ryLastPlay.setOnClickListener(this);
        ryDownload.setOnClickListener(this);

        tvAddFavorite.setOnClickListener(this);
        mtvManageFavorite.setOnClickListener(this);
        ivPlay.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        rvFavorite.setLayoutManager(linearLayoutManager);
        rvFavorite.setNestedScrollingEnabled(false);
        rvFavorite.addItemDecoration(new RecyclerViewDividerDecoration(this.getActivity(), RecyclerViewDividerDecoration.HORIZONTAL_LIST));
        mFavoriteViewAdapter = new FavoriteViewAdpapter(this.getActivity());
        mFavoriteViewAdapter.setAdadpterMode(FavoriteViewAdpapter.AdapterMode.MODE_NORMAL);
        rvFavorite.setAdapter(mFavoriteViewAdapter);
    }

    @Override
    protected void initData() {
        PlaylistModel.getInstance().addObserver(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (mClient == null) {
            mClient = new PlaybackService.Client(this.getActivity(), this);
        }
        mClient.connect();
    }

    public void onDetach() {
        super.onDetach();
        if (mClient != null) {
            mClient.disconnect();
        }
    }

    @CallSuper
    public void onResume() {
        super.onResume();
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
    public void onDestroy(){
        super.onDestroy();
        PlaylistModel.getInstance().removeObserver(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ryLocal) {
            HomeActivity.getInstance().switchToAudioFragment();
        } else if (v == mtvManageFavorite) {
            if (mFavoriteViewAdapter.getAdapterMode() == FavoriteViewAdpapter.AdapterMode.MODE_NORMAL) {
                mFavoriteViewAdapter.setAdadpterMode(FavoriteViewAdpapter.AdapterMode.MODE_EDIT);
                mtvManageFavorite.setText("完成");
            } else {
                mFavoriteViewAdapter.setAdadpterMode(FavoriteViewAdpapter.AdapterMode.MODE_NORMAL);
                mtvManageFavorite.setText("管理");
            }
        } else if (v == tvAddFavorite) {
            AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), new AlertDialogFavorite.OnAlterDialogFavoriteListener() {
                @Override
                public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName) {
                    PlaylistDao dao = new PlaylistDao();
                    long currentTime = System.currentTimeMillis();
                    dao.setDate_added(currentTime);
                    dao.setList_id(currentTime);
                    dao.setName(strFavoriteName);
                    PlaylistModel.getInstance().addPlaylist(dao);
                    mFavoriteViewAdapter.initData();
                }
            });
            favoriteDialog.show();
            favoriteDialog.setOperType(AlertDialogFavorite.ADD_FAVORITE);
        } else if (v == ivPlay) {
            if (mService != null) {
                mService.next(false);
            }
        }
    }

    @Override
    public void onConnected(PlaybackService service) {
        mService = service;
    }

    @Override
    public void onDisconnected() {
        mService = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPlaylistChanged(long playlistId) {
        mFavoriteViewAdapter.initData();
    }

    @Override
    public void onPlaylistMenberChanged(long playlistId, long musicId) {

    }
}
