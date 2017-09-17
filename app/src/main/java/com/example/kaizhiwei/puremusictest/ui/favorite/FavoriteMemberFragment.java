package com.example.kaizhiwei.puremusictest.ui.favorite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.bean.SearchLrcPicBean;
import com.example.kaizhiwei.puremusictest.contract.PlaylistContract;
import com.example.kaizhiwei.puremusictest.contract.SearchLrcPicContract;
import com.example.kaizhiwei.puremusictest.dao.FavoriteMusicDao;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.FavoriteMusicModel;
import com.example.kaizhiwei.puremusictest.model.IPlaylistDataObserver;
import com.example.kaizhiwei.puremusictest.model.MediaModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.presenter.PlaylistPrensenter;
import com.example.kaizhiwei.puremusictest.presenter.SearchLrcPicPresenter;
import com.example.kaizhiwei.puremusictest.service.IPlayMusic;
import com.example.kaizhiwei.puremusictest.service.IPlayMusicListener;
import com.example.kaizhiwei.puremusictest.service.PlayMusicService;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;
import com.example.kaizhiwei.puremusictest.ui.localmusic.BatchMgrFragment;
import com.example.kaizhiwei.puremusictest.ui.localmusic.LocalBaseMediaLayout;
import com.example.kaizhiwei.puremusictest.ui.localmusic.MoreOperationDialog;
import com.example.kaizhiwei.puremusictest.util.ImageUtil;
import com.example.kaizhiwei.puremusictest.util.RingBellUtil;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.kaizhiwei.puremusictest.ui.localmusic.MoreOperationDialog.MORE_NEXTPLAY_NORMAL;

/**
 * Created by kaizhiwei on 17/1/11.
 */
public class FavoriteMemberFragment extends MyBaseFragment implements View.OnClickListener, PlaylistContract.View, IPlaylistDataObserver,
        FavoriteViewAdapter.OnItemListener, MoreOperationDialog.IMoreOperationDialogListener,
        PlayMusicService.Client.Callback, IPlayMusicListener, SearchLrcPicContract.View {

    @Bind(R.id.ivIcon)
    ImageView ivIcon;
    @Bind(R.id.tvPlayAll)
    TextView tvPlayAll;
    @Bind(R.id.tvFavoriteNum)
    TextView tvFavoriteNum;
    @Bind(R.id.tvManager)
    TextView tvManager;
    @Bind(R.id.tvDownload)
    TextView tvDownload;
    @Bind(R.id.viewSepratorLine)
    View viewSepratorLine;
    @Bind(R.id.llControl)
    RelativeLayout llControl;
    @Bind(R.id.rvFavorite)
    RecyclerView rvFavorite;
    @Bind(R.id.llContent)
    LinearLayout llContent;
    @Bind(R.id.rlHeadView)
    LinearLayout rlHeadView;
    @Bind(R.id.personScrollView)
    PersonalScrollView2 personScrollView;
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.ivMoreOper)
    ImageView ivMoreOper;
    @Bind(R.id.ivEdit)
    ImageView ivEdit;
    @Bind(R.id.llTitle)
    RelativeLayout llTitle;
    @Bind(R.id.rlMain)
    RelativeLayout rlMain;
    private boolean needQueryMember = true;
    private PlaylistDao mCurPlaylistDao;
    private List<MusicInfoDao> mFavoriteMusicDaos;
    private Handler mHandler = new Handler();
    private FavoriteViewAdapter mAdapter;

    private MoreOperationDialog.Builder mMoreDialogbuilder;
    private MoreOperationDialog mMoreDialog;
    private PlaylistPrensenter mPresenter = new PlaylistPrensenter(this);
    private SearchLrcPicPresenter mSearchLrcPicPersenter = new SearchLrcPicPresenter(this);

    private PlayMusicService.Client mClient;
    private PlayMusicService mService;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        if (mMoreDialogbuilder == null) {
            mMoreDialogbuilder = new MoreOperationDialog.Builder(this.getActivity());
        }

        if (mMoreDialog == null) {
            mMoreDialog = (MoreOperationDialog) mMoreDialogbuilder.create();
            mMoreDialog.setListener(this);
        }
        List<Integer> list = new ArrayList<>();
        list.add(MORE_NEXTPLAY_NORMAL);
        list.add(MoreOperationDialog.MORE_LOVE_NORMAL);
        list.add(MoreOperationDialog.MORE_BELL_NORMAL);
        list.add(MoreOperationDialog.MORE_SHARE_NORMAL);
        list.add(MoreOperationDialog.MORE_ADD_NORMA);
        list.add(MoreOperationDialog.MORE_REMOVE_NORMAL);
        mMoreDialog.setMoreOperData(list);

        personScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] ctrlLocation = new int[2];
                llControl.getLocationOnScreen(ctrlLocation);
                int[] titleLocation = new int[2];
                llTitle.getLocationOnScreen(titleLocation);
                Log.e("weikaizhi", "onScrollChange llControl.getTop() = " + ctrlLocation[1] + ", llTitle.getBottom() = " + titleLocation[1] );
                if (ctrlLocation[1] <= titleLocation[1]) {
                    addCtrlBarToMain();
                }
//                else if(){
//                    removeCtrlBarToMain();
//                }

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        rvFavorite.setLayoutManager(linearLayoutManager);
        rvFavorite.setNestedScrollingEnabled(false);
        rvFavorite.addItemDecoration(new RecyclerViewDividerDecoration(this.getActivity(), RecyclerViewDividerDecoration.HORIZONTAL_LIST));
        showFullLoadingView("音乐马上回来");
    }

    private void addCtrlBarToMain() {
        int height = llControl.getHeight();
        ViewGroup parent = (ViewGroup) llControl.getParent();
        parent.removeView(llControl);
        RelativeLayout.LayoutParams newParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        newParam.addRule(RelativeLayout.BELOW, R.id.llTitle);
        rlMain.addView(llControl, newParam);
    }

    private void removeCtrlBarToMain() {
        rlMain.removeView(llTitle);
        LinearLayout.LayoutParams newParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, llControl.getHeight());
        llContent.addView(llTitle, newParam);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        PlaylistDao playlistDao = bundle.getParcelable(PLAYLIST_DAO);
        if (playlistDao == null)
            return;

        needQueryMember = true;
        mPresenter.queryPlaylistById(playlistDao.getList_id());

        //lbMediaLayout.setType(LocalBaseMediaLayout.LayoutType.CUSTOME);
        PlaylistModel.getInstance().addObserver(this);
        mClient = new PlayMusicService.Client(this.getActivity(), this);
        mClient.connect();
    }

    private void hideShowCtrlBtn(boolean isShow) {
        ivEdit.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivMoreOper.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void onResume() {
        super.onResume();
        //lbMediaLayout.onResume();
    }

    public void onPause() {
        super.onPause();
        //lbMediaLayout.onPause();
    }

    public void onStart() {
        super.onStart();
        //lbMediaLayout.onStart();
    }

    public void onStop() {
        super.onStop();
        //lbMediaLayout.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // lbMediaLayout.onDestory();
        PlaylistModel.getInstance().removeObserver(this);
    }

    public void onDetach() {
        super.onDetach();
    }

    @OnClick({R.id.ivBack, R.id.ivEdit, R.id.ivMoreOper, R.id.tvManager})
    public void onClick(View v) {
        if (mCurPlaylistDao == null)
            return;

        if (ivEdit == v) {
            Intent intent = new Intent(HomeActivity.getInstance(), FavoriteEditInfoActivity.class);
            intent.putExtra(FavoriteEditInfoActivity.PLAYLIST_DAO, (Parcelable) mCurPlaylistDao);
            HomeActivity.getInstance().startActivity(intent);
        } else if (v == tvManager) {
            Intent intent = new Intent(this.getActivity(), BatchMgrFavoriteActivity.class);
            intent.putExtra(BatchMgrFavoriteActivity.INTENT_PLAYLIST_ID, mCurPlaylistDao.getList_id());
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
        if (list == null || list.size() == 0)
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

        if (needQueryMember) {
            mPresenter.getPlaylistMembers(mCurPlaylistDao.getList_id());
        }
    }

    @Override
    public void onGetPlaylistMembers(final List<PlaylistMemberDao> list) {
        if (list == null)
            return;

        Collections.sort(list, new Comparator<PlaylistMemberDao>() {
            @Override
            public int compare(PlaylistMemberDao lhs, PlaylistMemberDao rhs) {
                return lhs.getPlay_order() - rhs.getPlay_order();
            }
        });

        mFavoriteMusicDaos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MusicInfoDao dao = MediaModel.getInstance().getMusicInfoById(list.get(i).getMusic_id());
            if (dao != null) {
                mFavoriteMusicDaos.add(dao);
            }
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvFavoriteNum.setText("/" + mFavoriteMusicDaos.size() + "首");
                mAdapter = new FavoriteViewAdapter(FavoriteMemberFragment.this.getActivity(), mFavoriteMusicDaos);
                mAdapter.setListener(FavoriteMemberFragment.this);
                rvFavorite.setAdapter(mAdapter);
                hideFullLoadingView();
            }
        }, 500);
    }

    @Override
    public void onPlaylistChanged(long playlistId) {
        if (playlistId != mCurPlaylistDao.getList_id())
            return;

        needQueryMember = false;
        mPresenter.queryPlaylistById(mCurPlaylistDao.getList_id());
    }

    @Override
    public void onPlaylistMemberChanged(long playlistId, long musicId) {
        if (playlistId != mCurPlaylistDao.getList_id())
            return;

        mPresenter.getPlaylistMembers(mCurPlaylistDao.getList_id());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onMoreClicked(int position) {
        MusicInfoDao musicInfoDao = mFavoriteMusicDaos.get(position);
        if (musicInfoDao == null)
            return;

        mMoreDialog.setKey(position + "");
        mMoreDialog.show();
        mMoreDialog.setTitle(musicInfoDao.getTitle());
        mMoreDialog.setFavorite(FavoriteMusicModel.getInstance().isHasFavorite(musicInfoDao.get_id()));
    }

    @Override
    public void onItemClicked(int position) {
        if (mService == null || mAdapter == null)
            return;

        mAdapter.setSelectItemId(mFavoriteMusicDaos.get(position).get_id());
        mService.setPlaylist(mFavoriteMusicDaos);
        mService.setCurPlayIndex(position);
        mService.setDataSource(mFavoriteMusicDaos.get(position).get_data());
        mService.prepareAsync();

        //"第三条跑道$$刘浩龙";
        String param = String.format("%s$$%s", mFavoriteMusicDaos.get(position).getTitle(), mFavoriteMusicDaos.get(position).getArtist());
        mSearchLrcPicPersenter.searchLrcPic("android", "6.0.3.1", "xiaomi", 2, "baidu.ting.search.lrcpic", "json",
                param, 1505547640341L, "rqCNe1a9CwfBNPxnUUMbXVipabBzT5%2FpZsq1%2BqF1%2BpeOLc5vuWeTNK2JLBYhBejgp8Nnc%2BWTvSA1g6eF8zVmKQ%3D%3D", 2,
                new SearchLrcPicContract.View() {
                    @Override
                    public void onSearchLrcPicSuccess(SearchLrcPicBean bean) {

                    }

                    @Override
                    public void onError(String strErrMsg) {

                    }
                });
    }

    @Override
    public void onMoreItemClick(MoreOperationDialog dialog, int tag) {
        String key = dialog.getKey();
        int position = Integer.parseInt(key);
        MusicInfoDao musicInfoDao = mFavoriteMusicDaos.get(position);
        if (musicInfoDao == null)
            return;

        mMoreDialog.dismiss();
        boolean bRet = false;
        switch (tag) {
            case MoreOperationDialog.MORE_NEXTPLAY_NORMAL:
                if (mService == null)
                    return;
                mService.addToNextPlay(musicInfoDao);
                break;
            case MoreOperationDialog.MORE_LOVE_NORMAL:
                bRet = FavoriteMusicModel.getInstance().isHasFavorite(musicInfoDao.get_id());
                if(!bRet){
                    FavoriteMusicDao dao = new FavoriteMusicDao();
                    dao.setMusicinfo_id(musicInfoDao.get_id());
                    dao.setFav_time(System.currentTimeMillis());
                    dao.setAlbum(musicInfoDao.getAlbum());
                    dao.setArtist(musicInfoDao.getArtist());
                    FavoriteMusicModel.getInstance().addFavoriteMusic(dao);
                }
                else{
                    FavoriteMusicModel.getInstance().removeFavoriteMusic(musicInfoDao.get_id());
                }

                if(bRet){
                    showToast("已取消喜欢");
                }
                else{
                    showToast("已添加到我喜欢的单曲");
                }
                break;
            case MoreOperationDialog.MORE_BELL_NORMAL:
                bRet = RingBellUtil.setRingBell(this.getActivity(), musicInfoDao.get_data());
                if (bRet) {
                    showToast("设置成功");
                }
                break;
            case MoreOperationDialog.MORE_SHARE_NORMAL:
                break;
            case MoreOperationDialog.MORE_ADD_NORMA:
                FavoriteDialog.Builder builderFavorite = new FavoriteDialog.Builder(this.getActivity());
                FavoriteDialog dialogFavorite = (FavoriteDialog)builderFavorite.create();
                dialogFavorite.setCancelable(true);
                dialogFavorite.setKeyType(LocalBaseMediaLayout.LayoutType.ALLSONG);
                dialogFavorite.setStrKey(musicInfoDao.get_id() + "");
                dialogFavorite.show();
                dialogFavorite.setTitle("添加到歌单");
                break;
            case MoreOperationDialog.MORE_REMOVE_NORMAL:
                bRet = mPresenter.removePlaylistMember(mCurPlaylistDao.getList_id(), musicInfoDao.get_id());
                mCurPlaylistDao.setSong_count(mCurPlaylistDao.getSong_count() - 1);
                if (bRet) {
                    showToast("移除成功");
                }
                break;
        }
    }

    @Override
    public void onConnect(PlayMusicService service) {
        mService = service;
        mService.addListener(this);
    }

    @Override
    public void onDisconnect() {
        mService.removeListener(this);
        mService = null;
    }

    @Override
    public void onStateChange(int state) {
        if(state == IPlayMusic.STATE_PAPERED || state == IPlayMusic.STATE_PLAY){
            MusicInfoDao musicInfoDao = mService.getCurPlayMusicDao();
            if(musicInfoDao == null)
                return;

            mAdapter.setSelectItemId(musicInfoDao.get_id());
        } else if (state == IPlayMusic.STATE_PAUSE) {

        }
    }

    @Override
    public void onPlayPosUpdate(int percent, int curPos, int duration) {

    }

    @Override
    public void onBufferingUpdate(int cur, int total) {

    }

    @Override
    public void onSearchLrcPicSuccess(SearchLrcPicBean bean) {
        if(bean  == null)
            return;


    }
}
