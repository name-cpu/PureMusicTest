package com.example.kaizhiwei.puremusictest.ui.recentplay;

import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.dao.FavoriteMusicDao;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.RecentPlayDao;
import com.example.kaizhiwei.puremusictest.model.FavoriteMusicModel;
import com.example.kaizhiwei.puremusictest.model.IRecentPlayObserver;
import com.example.kaizhiwei.puremusictest.model.MediaModel;
import com.example.kaizhiwei.puremusictest.model.RecentPlayModel;
import com.example.kaizhiwei.puremusictest.model.RecentPlayProxy;
import com.example.kaizhiwei.puremusictest.service.IPlayMusic;
import com.example.kaizhiwei.puremusictest.service.IPlayMusicListener;
import com.example.kaizhiwei.puremusictest.service.PlayMusicService;
import com.example.kaizhiwei.puremusictest.ui.favorite.FavoriteDialog;
import com.example.kaizhiwei.puremusictest.ui.favorite.FavoriteViewAdapter;
import com.example.kaizhiwei.puremusictest.ui.localmusic.LocalBaseMediaLayout;
import com.example.kaizhiwei.puremusictest.ui.localmusic.MoreOperationDialog;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;
import com.example.kaizhiwei.puremusictest.util.RingBellUtil;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/9/18.
 */

public class RecentPlaySongFragment extends MyBaseFragment implements IRecentPlayObserver, MoreOperationDialog.IMoreOperationDialogListener, PlayMusicService.Client.Callback, IPlayMusicListener, FavoriteViewAdapter.OnItemListener {
    @Bind(R.id.tvRandomPlay)
    TextView tvRandomPlay;
    @Bind(R.id.tvBatchMgr)
    TextView tvBatchMgr;
    @Bind(R.id.tvDownload)
    TextView tvDownload;
    @Bind(R.id.rlCtrlBar)
    RelativeLayout rlCtrlBar;
    @Bind(R.id.rvSong)
    RecyclerView rvSong;
    MoreOperationDialog.Builder mMoreDialogbuilder;
    MoreOperationDialog mMoreDialog;
    private List<Integer> mMoreOper;
    private List<MusicInfoDao> mMusicInfoDaos = new ArrayList<>();
    private PlayMusicService.Client mClient;
    private PlayMusicService mService;
    private FavoriteViewAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recent_play_song;
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        rvSong.setLayoutManager(linearLayoutManager);
        rvSong.addItemDecoration(new RecyclerViewDividerDecoration(this.getActivity(), RecyclerViewDividerDecoration.HORIZONTAL_LIST));
    }

    @Override
    protected void initData() {
        mMoreOper = new ArrayList<>();
        mMoreOper.add(MoreOperationDialog.MORE_NEXTPLAY_NORMAL);
        mMoreOper.add(MoreOperationDialog.MORE_DOWNLOAD_NORMAL);
        mMoreOper.add(MoreOperationDialog.MORE_SHARE_NORMAL);
        mMoreOper.add(MoreOperationDialog.MORE_ADD_NORMA);
        mMoreOper.add(MoreOperationDialog.MORE_REMOVE_NORMAL);
        RecentPlayModel.getInstance().addObserver(this);
        initAdapterData();
        mClient = new PlayMusicService.Client(this.getActivity(), this);
        mClient.connect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RecentPlayModel.getInstance().removeObserver(this);
    }

    private void initAdapterData(){
        RecentPlayProxy.getInstance().asyncGetAllRecentPlays(new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if(msg.what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    mMusicInfoDaos.clear();
                    List<RecentPlayDao> list = (List<RecentPlayDao>)msg.obj;
                    for(int i = 0;i <list.size();i++){
                        MusicInfoDao musicInfoDao = MediaModel.getInstance().getMusicInfoById(list.get(i).getInfo_id());
                        if(musicInfoDao == null)
                            continue;

                        mMusicInfoDaos.add(musicInfoDao);
                    }
                    mAdapter = new FavoriteViewAdapter(RecentPlaySongFragment.this.getActivity(), mMusicInfoDaos);
                    mAdapter.setListener(RecentPlaySongFragment.this);
                    rvSong.setAdapter(mAdapter);
                }
            }
        });
    }

    @Override
    public void onRecentPlayChanged(int type, long id) {
        initAdapterData();
    }

    @Override
    public void onConnect(PlayMusicService service) {
        mService = service;
        mService.addListener(this);

        MusicInfoDao musicInfoDao = mService.getCurPlayMusicDao();
        if(musicInfoDao == null)
            return;
        mAdapter.setSelectItemId(musicInfoDao.get_id());
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
    public void onMoreClicked(int position) {
        MusicInfoDao musicInfoDao = mMusicInfoDaos.get(position);
        if(musicInfoDao == null)
            return;

        if (mMoreDialogbuilder == null) {
            mMoreDialogbuilder = new MoreOperationDialog.Builder(RecentPlaySongFragment.this.getActivity());
        }

        if (mMoreDialog == null) {
            mMoreDialog = (MoreOperationDialog) mMoreDialogbuilder.create();
            mMoreDialog.setListener(RecentPlaySongFragment.this);
            mMoreDialog.setMoreOperData(mMoreOper);
        }
        mMoreDialog.setKey(musicInfoDao);
        mMoreDialog.setTitle(musicInfoDao.getTitle());
        mMoreDialog.setFavorite(FavoriteMusicModel.getInstance().isHasFavorite(musicInfoDao.get_id()));
        mMoreDialog.show();
    }

    @Override
    public void onItemClicked(int position) {
        if (mService == null || mAdapter == null)
            return;

        MusicInfoDao musicInfoDao = mMusicInfoDaos.get(position);
        if(musicInfoDao == null)
            return;

        mAdapter.setSelectItemId(musicInfoDao.get_id());
        mService.setPlaylist(mMusicInfoDaos);
        mService.setCurPlayIndex(position);
        mService.setDataSource(musicInfoDao.get_data());
        mService.prepareAsync();
    }

    @Override
    public void onMoreItemClick(MoreOperationDialog dialog, int tag) {
        mMoreDialog.dismiss();
        MusicInfoDao musicInfoDao = (MusicInfoDao)dialog.getKey();
        if(musicInfoDao == null)
            return;

        boolean bRet;
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
                bRet = RecentPlayModel.getInstance().removeRecentPlay(musicInfoDao.get_id());
                if (bRet) {
                    showToast("移除成功");
                    mMusicInfoDaos.remove(musicInfoDao);
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

//    private class RecnetPlayAdapter extends RecyclerView.Adapter<RecentPlayViewHolder>{
//
//        @Override
//        public RecentPlayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(RecentPlaySongFragment.this.getActivity()).inflate(R.layout.item_local_audio_allsong, parent, false);
//            return new RecentPlayViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(RecentPlayViewHolder holder, final int position) {
//            final MusicInfoDao songListInfo = mMusicInfoDaos.get(position);
//            holder.tvMain.setText(songListInfo.getTitle());
//            holder.tvSub.setText(songListInfo.getArtist());
//            holder.ivMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            if(mMusicInfoDaos == null)
//                return 0;
//
//            return mMusicInfoDaos.size();
//        }
//    }
//
//    private class RecentPlayViewHolder extends RecyclerView.ViewHolder{
//        public MaskImageView ivMore;
//        public TextView tvMain;
//        public TextView tvSub;
//
//        public RecentPlayViewHolder(View itemView) {
//            super(itemView);
//            tvMain = (TextView)itemView.findViewById(R.id.tvMain);
//            tvSub = (TextView)itemView.findViewById(R.id.tvSub);
//            ivMore = (MaskImageView) itemView.findViewById(R.id.ivMore);
//            Typeface typeFace = Typeface.createFromAsset(RecentPlaySongFragment.this.getActivity().getAssets(),"arial.ttf");
//            tvMain.setTypeface(typeFace);
//            tvSub.setTypeface(typeFace);
//        }
//    }
}
