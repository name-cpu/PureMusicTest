package com.example.kaizhiwei.puremusictest.ui.favorite;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseBroadCastContant;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.contract.PlaylistContract;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.IPlaylistDataObserver;
import com.example.kaizhiwei.puremusictest.model.MediaModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.presenter.PlaylistPrensenter;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;
import com.example.kaizhiwei.puremusictest.ui.localmusic.BatchMgrAudioActivity;
import com.example.kaizhiwei.puremusictest.ui.localmusic.LocalBaseMediaLayout;
import com.example.kaizhiwei.puremusictest.ui.localmusic.MoreOperationDialog;
import com.example.kaizhiwei.puremusictest.util.ImageUtil;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by kaizhiwei on 17/1/11.
 */
public class FavoriteMemberFragment extends MyBaseFragment implements View.OnClickListener, PlaylistContract.View, IPlaylistDataObserver {
    @Bind(R.id.ivIcon)
    ImageView ivIcon;
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.mivEdit)
    ImageView mivEdit;
    @Bind(R.id.mivMoreOper)
    ImageView mivMoreOper;
    @Bind(R.id.llTitle)
    LinearLayout llTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tvPlayAll)
    TextView tvPlayAll;
    @Bind(R.id.tvFavoriteNum)
    TextView tvFavoriteNum;
    @Bind(R.id.tvManager)
    TextView tvManager;
    @Bind(R.id.llControl)
    LinearLayout llControl;
    @Bind(R.id.viewSepratorLine)
    View viewSepratorLine;
    @Bind(R.id.llMain)
    LinearLayout llMain;
    @Bind(R.id.rlMain)
    CoordinatorLayout rlMain;
    @Bind(R.id.lbMediaLayout)
    LocalBaseMediaLayout lbMediaLayout;

    private boolean needQueryMember = true;
    private PlaylistDao mCurPlaylistDao;
    private List<MusicInfoDao> mFavoriteMusicDaos;
    private Handler mHandler = new Handler();

    private PlaylistPrensenter mPresenter = new PlaylistPrensenter(this);

    public static final String PLAYLIST_DAO = "PLAYLIST_DAO";

    private LocalBaseMediaLayout.ILocalBaseListener mSubFragmentListener = new LocalBaseMediaLayout.ILocalBaseListener() {
        @Override
        public void onFragmentInitFinish(LinearLayout fragment) {

        }

        @Override
        public void onMoreOperClick(LocalBaseMediaLayout layout, int flag, Object obj) {
//            if (obj == null || mCurPlaylistDao == null)
//                return;
//
//            boolean bRet = false;
//            List<MusicInfoDao> list = (List<MusicInfoDao>) obj;
//            if (flag == MoreOperationDialog.MORE_REMOVE_NORMAL) {
//                for (int i = 0; i < list.size(); i++) {
//                    mPresenter.removePlaylistMember()
//                    bRet = MediaLibrary.getInstance().removeFavoriteMusicEntity(list.get(i).get_id(), mFavoriteEntity._id);
//                }
//            }
//
//            initAdapterData();
//            final boolean finalBRet = bRet;
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    String strPromt = "";
//                    if (finalBRet) {
//                        strPromt = "移除成功";
//                    } else {
//                        strPromt = "移除失败";
//                    }
//
//                    Toast.makeText(FavoriteMemberFragment.this.getActivity(), strPromt, Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_favorite_main;
    }

    @Override
    protected void initView() {
//        ivBack.setOnClickListener(this);
//        mivEdit.setOnClickListener(this);
//        mivMoreOper.setOnClickListener(this);
//        tvManager.setOnClickListener(this);
//        lbmLayout = new LocalBaseMediaLayout(this.getActivity());
//        //lbmLayout.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, false, false, false);
//        lbmLayout.setBaseMediaListener(mSubFragmentListener);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        params.addRule(RelativeLayout.BELOW, R.id.viewSepratorLine);
//        lbmLayout.setLayoutParams(params);
//        llMain.addView(lbmLayout);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        PlaylistDao playlistDao = bundle.getParcelable(PLAYLIST_DAO);
        if (playlistDao == null)
            return;

        needQueryMember = true;
        mPresenter.queryPlaylistById(playlistDao.getList_id());
        List<Integer> list = new ArrayList<>();
        list.add(MoreOperationDialog.MORE_NEXTPLAY_NORMAL);
        list.add(MoreOperationDialog.MORE_LOVE_NORMAL);
        list.add(MoreOperationDialog.MORE_BELL_NORMAL);
        list.add(MoreOperationDialog.MORE_SHARE_NORMAL);
        list.add(MoreOperationDialog.MORE_ADD_NORMA);
        list.add(MoreOperationDialog.MORE_REMOVE_NORMAL);
        lbMediaLayout.setMoreOperDialogData(list);
        lbMediaLayout.setType(LocalBaseMediaLayout.LayoutType.CUSTOME);
        PlaylistModel.getInstance().addObserver(this);
    }

    private void hideShowCtrlBtn(boolean isShow) {
        mivEdit.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mivMoreOper.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void onDestory() {
        super.onDestroy();
        lbMediaLayout.onDestory();
    }

    public void onResume() {
        super.onResume();
        lbMediaLayout.onResume();
    }

    public void onPause() {
        super.onPause();
        lbMediaLayout.onPause();
    }

    public void onStart() {
        super.onStart();
        lbMediaLayout.onStart();
    }

    public void onStop() {
        super.onStop();
        lbMediaLayout.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        PlaylistModel.getInstance().removeObserver(this);
    }

    public void onDetach() {
        super.onDetach();
    }

    @OnClick({R.id.ivBack, R.id.mivEdit, R.id.mivMoreOper, R.id.tvManager})
    public void onClick(View v) {
        if (mCurPlaylistDao == null)
            return;

        if (mivEdit == v) {
            Intent intent = new Intent(HomeActivity.getInstance(), FavoriteEditInfoActivity.class);
            intent.putExtra(FavoriteEditInfoActivity.PLAYLIST_DAO, (Parcelable) mCurPlaylistDao);
            HomeActivity.getInstance().startActivity(intent);
        } else if (v == tvManager) {
            Intent intent = new Intent(this.getActivity(), BatchMgrAudioActivity.class);
            List<MusicInfoDao> listTemp = new ArrayList<>();
            List<MusicInfoDao> temp = new ArrayList<>();
            if (temp != null) {
                listTemp.addAll(mFavoriteMusicDaos);
            }
            intent.putExtra(BatchMgrAudioActivity.INTENT_LIST_DATA, (Serializable) listTemp);
            intent.putExtra(BatchMgrAudioActivity.INTENT_RIGHTBTNISDELETE, false);
            intent.putExtra(BatchMgrAudioActivity.INTENT_RIGHTBTNISREMOVE, true);
            intent.putExtra(BatchMgrAudioActivity.INTENT_PLAYLISTDAO, (Parcelable) mCurPlaylistDao);
            HomeActivity.getInstance().startActivityForResult(intent, 2);
            HomeActivity.getInstance().overridePendingTransition(R.anim.anim_right_enter, R.anim.anim_right_exit);
        } else if (v == ivBack) {
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void setFavoriteImage(String strPath, ImageView ivImage, int reqWidth, int reqHeight) {
        if (ivImage == null)
            return;

        Bitmap bitmap = null;
        File file = null;
        boolean bDefault = false;
        if (TextUtils.isEmpty(strPath)) {
            bDefault = true;
        }

        if (bDefault == false) {
            file = new File(strPath);
            if (file.exists() == false) {
                bDefault = true;
            } else {
                bDefault = false;
            }
        }

        if (bDefault) {
            bitmap = ImageUtil.decodeSampledBitmapFromResource(HomeActivity.getInstance().getResources(), R.drawable.ic_playlist_default, reqWidth, reqHeight);
            ivImage.setImageBitmap(bitmap);
        } else {
            bitmap = ImageUtil.decodeSampledBitmapFromPath(strPath, reqWidth, reqHeight);
            ivImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetPlaylists(List<PlaylistDao> list) {

    }

    @Override
    public void onQueryPlaylistById(List<PlaylistDao> list) {
        if(list == null || list.size() == 0)
            return;

        mCurPlaylistDao = list.get(0);
        Uri uri = Uri.parse(mCurPlaylistDao.getImg_url());
        //Glide.with(this).load(uri).bitmapTransform(new BlurTransformation(this.getActivity(), 25)).crossFade(1000)
        //setFavoriteImage(entity.strFavoriteImgPath, ivIcon, 300, 300);
        tvTitle.setText(mCurPlaylistDao.getName());
//        if (favoriteId == MediaLibrary.getInstance().getDefaultFavoriteEntityId()) {
//            hideShowCtrlBtn(false);
//        } else {
//            hideShowCtrlBtn(true);
//        }

        if(needQueryMember){
            mPresenter.getPlaylistMembers(mCurPlaylistDao.getList_id());
        }
    }

    @Override
    public void onGetPlaylistMembers(final List<PlaylistMemberDao> list) {
        if(list == null)
            return;

        Collections.sort(list, new Comparator<PlaylistMemberDao>() {
            @Override
            public int compare(PlaylistMemberDao lhs, PlaylistMemberDao rhs) {
                return lhs.getPlay_order() - rhs.getPlay_order();
            }
        });

        mFavoriteMusicDaos = new ArrayList<>();
        for(int i = 0;i < list.size();i++){
            MusicInfoDao dao = MediaModel.getInstance().getMusicInfoById(list.get(i).getMusic_id());
            if(dao != null){
                mFavoriteMusicDaos.add(dao);
            }
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvFavoriteNum.setText("/" + mFavoriteMusicDaos.size() + "首");
                lbMediaLayout.initAdaterData(mFavoriteMusicDaos);
            }
        });
    }

    @Override
    public void onPlaylistChanged(long playlistId) {
        if(playlistId != mCurPlaylistDao.getList_id())
            return;

        needQueryMember = false;
        mPresenter.queryPlaylistById(mCurPlaylistDao.getList_id());
    }

    @Override
    public void onPlaylistMenberChanged(long playlistId, long musicId) {
        if(playlistId != mCurPlaylistDao.getList_id())
            return;

        mPresenter.getPlaylistMembers(mCurPlaylistDao.getList_id());
    }
}
