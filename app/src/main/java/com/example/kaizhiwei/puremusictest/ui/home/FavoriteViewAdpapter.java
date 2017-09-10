package com.example.kaizhiwei.puremusictest.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaizhiwei.puremusictest.model.PlaylistModuleProxy;
import com.example.kaizhiwei.puremusictest.ui.favorite.FavoriteMemberFragment;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.ui.favorite.AlertDialogFavorite;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/9/9.
 */

public class FavoriteViewAdpapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private Context mContext;
    private List<PlaylistDao> mDatas = new ArrayList<>();
    private AdapterMode mMode;

    public enum AdapterMode{
        MODE_NORMAL,
        MODE_EDIT
    }

    public FavoriteViewAdpapter(Context context){
        mContext = context;
    }

    public AdapterMode getAdapterMode() {
        return mMode;
    }

    public void setAdadpterMode(AdapterMode mMode) {
        this.mMode = mMode;
        notifyDataSetChanged();
    }

    public void initData(){
        PlaylistModuleProxy.getInstance().asyncGetPlaylists(new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if(msg.what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<PlaylistDao> list = (List<PlaylistDao>)msg.obj;
                    if(list == null)
                        return;
                    mDatas.clear();
                    mDatas.addAll(list);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_homepage_favorite, parent, false);
        FavoriteViewHolder viewHolder = new FavoriteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, final int position) {
        PlaylistDao dao = mDatas.get(position);
        holder.tvFavoriteMain.setText(dao.getName());
        holder.tvFavoriteSub.setText(dao.getSong_count() + "首");
        holder.tvFavoriteMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
        holder.tvFavoriteSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
        if(mMode == AdapterMode.MODE_NORMAL){
            holder.ibBtnDelete.setVisibility(View.GONE);
            holder.ibBtnEdit.setVisibility(View.GONE);
        }
        else{
            holder.ibBtnDelete.setVisibility(View.VISIBLE);
            holder.ibBtnEdit.setVisibility(View.VISIBLE);
        }

        holder.ibBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("确定删除此歌单吗?");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyDataSetChanged();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PlaylistDao playlistDao = mDatas.get(position);
                        boolean bRet = PlaylistModel.getInstance().removePlaylist(playlistDao.getList_id());
                        if(bRet){
                            mDatas.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });
                alertDialog.show();
            }
        });

        holder.ibBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PlaylistDao playlistDao = mDatas.get(position);
                AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(mContext, new AlertDialogFavorite.OnAlterDialogFavoriteListener() {
                    @Override
                    public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName) {
                        if(operType != AlertDialogFavorite.USER_CANCEL){
                            playlistDao.setName(strFavoriteName);
                            playlistDao.setDate_modified(System.currentTimeMillis());
                            PlaylistModel.getInstance().updatePlaylist(playlistDao);
                            notifyDataSetChanged();
                        }
                    }
                });
                favoriteDialog.show();
                favoriteDialog.setFavoriteEntity(playlistDao);
                favoriteDialog.setOperType(AlertDialogFavorite.MODIFY_FAVORITE);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PlaylistDao playlistDao = mDatas.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(FavoriteMemberFragment.PLAYLIST_DAO, playlistDao);
                HomeActivity.getInstance().switchToFavoriteFragment(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mDatas == null)
            return 0;

        return mDatas.size();
    }
}
