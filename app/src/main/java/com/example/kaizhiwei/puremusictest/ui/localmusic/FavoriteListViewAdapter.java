package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/12/10.
 */
public class FavoriteListViewAdapter extends BaseAdapter implements View.OnClickListener{
    private List<PlaylistDao> listData;
    private Context mContext;
    private int mOperMode;
    public static final int EDIT_MODE = 0;
    public static final int READONLY_MODE = 1;

    private int Modify_Flag = 1000;
    private int Delete_Flag = 2000;
    private IFavoriteOperListener mListener;

    private PlaylistDao mDefaultPlaylist;
    private PlaylistDao mAddOne;

    public static final int DEFAULT_LIST_ID = -1;
    public static final int ADD_ONE_LIST_ID = -2;

    @Override
    public void onClick(View v) {
        if(mListener == null)
            return;

        int tag = (int)v.getTag() - Delete_Flag;
        if(tag >= 0){
            mListener.OnDeleteClick(this, tag);
        }
        else{
            mListener.OnModifyClick(this, tag + Delete_Flag - Modify_Flag);
        }
    }

    public interface IFavoriteOperListener{
        void OnModifyClick(FavoriteListViewAdapter adapter, int position);
        void OnDeleteClick(FavoriteListViewAdapter adapter, int position);
    }

    public FavoriteListViewAdapter(Context context, List<PlaylistDao> list, int mode, boolean bHomePage){
        mContext = context;
        if(list == null){
            listData = new ArrayList<>();
        }
        else{
            listData = list;
        }

        mDefaultPlaylist = new PlaylistDao();
        mDefaultPlaylist.setName(context.getString(R.string.i_like_danqu));
        mDefaultPlaylist.setList_id(DEFAULT_LIST_ID);

        mAddOne = new PlaylistDao();
        mAddOne.setName(context.getString(R.string.new_playlist));
        mAddOne.setList_id(ADD_ONE_LIST_ID);

        listData.add(0, mDefaultPlaylist);
        if(bHomePage == false){
            listData.add(0, mAddOne);
        }

        mOperMode = mode;
    }

    public void setMode(int mode){
        mOperMode = mode;
        notifyDataSetChanged();
    }

    public int getMode(){
        return mOperMode;
    }

    public void setFavoriteAdapterListener(IFavoriteOperListener listener){
        mListener = listener;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < 0 || position >= listData.size())
            return null;

        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(listData == null || position >= listData.size())
            return null;

        PlaylistDao entity = listData.get(position);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FavoriteListViewAdapterHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.favorite_dialog_item, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new FavoriteListViewAdapterHolder(view);
            holder.ibBtnEdit.setOnClickListener(this);
            holder.ibBtnDelete.setOnClickListener(this);
            holder.ibBtnEdit.setTag(position + Modify_Flag);
            holder.ibBtnDelete.setTag(position + Delete_Flag);
            view.setTag(holder);
            convertView = view;
        }
        else{
            holder = (FavoriteListViewAdapterHolder) convertView.getTag();
        }

        holder.tvFavoriteMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
        holder.tvFavoriteSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
        holder.tvFavoriteMain.setText(entity.getName());
        holder.tvFavoriteSub.setText(entity.getSong_count() + "首");
        holder.setFavoriteType(entity.getList_id(), mOperMode);

        return convertView;
    }

    private static class FavoriteListViewAdapterHolder{
        public ImageView ivSongImage;
        public TextView tvFavoriteMain;
        public TextView tvFavoriteSub;
        public ImageView ibBtnMore;
        public ImageView ibBtnEdit;
        public ImageView ibBtnDelete;

        public FavoriteListViewAdapterHolder(View view){
            ivSongImage = (ImageView)view.findViewById(R.id.ivSongImage);
            tvFavoriteMain = (TextView)view.findViewById(R.id.tvFavoriteMain);
            tvFavoriteSub = (TextView)view.findViewById(R.id.tvFavoriteSub);
            ibBtnMore = (ImageView)view.findViewById(R.id.ibBtnMore);
            ibBtnEdit = (ImageView)view.findViewById(R.id.ibBtnEdit);
            ibBtnDelete = (ImageView)view.findViewById(R.id.ibBtnDelete);
            ibBtnEdit.setClickable(true);
            ibBtnDelete.setClickable(true);
        }

        public void setFavoriteType(long list_id, int mode){
            if(list_id == DEFAULT_LIST_ID){
                ivSongImage.setImageResource(R.drawable.ic_mymusic_add_nor);
                tvFavoriteSub.setVisibility(View.GONE);
                ibBtnMore.setVisibility(View.GONE);
                ibBtnEdit.setVisibility(View.GONE);
                ibBtnDelete.setVisibility(View.GONE);

                LinearLayout.LayoutParams param =  (LinearLayout.LayoutParams)tvFavoriteMain.getLayoutParams();
                if(param != null){
                    param.weight = 2;
                    param.gravity = Gravity.CENTER_VERTICAL;
                    tvFavoriteMain.setLayoutParams(param);
                    tvFavoriteMain.setGravity(Gravity.CENTER_VERTICAL);
                }
            }
            else if(list_id == ADD_ONE_LIST_ID){
                ivSongImage.setImageResource(R.drawable.ic_mymusic_like);
                ibBtnMore.setVisibility(View.VISIBLE);
                ibBtnEdit.setVisibility(View.GONE);
                ibBtnDelete.setVisibility(View.GONE);
            }
            else{
                //ivSongImage.setImageResource(R.drawable.ic_mymusic_list_item);
                //setFavoriteImage(strPath, ivSongImage);
                if(mode == EDIT_MODE){
                    ibBtnMore.setVisibility(View.GONE);
                    ibBtnEdit.setVisibility(View.VISIBLE);
                    ibBtnDelete.setVisibility(View.VISIBLE);
                }
                else{
                    ibBtnMore.setVisibility(View.VISIBLE);
                    ibBtnEdit.setVisibility(View.GONE);
                    ibBtnDelete.setVisibility(View.GONE);
                }
            }
        }

        public void setFavoriteImage(String strPath, ImageView ivImage){
            if(ivImage == null)
                return;

            Bitmap bitmap = null;
            File file = null;
            boolean bDefault = false;
            if(TextUtils.isEmpty(strPath)){
                bDefault = true;
            }

            if(bDefault == false){
                file = new File(strPath);
                if(file.exists() == false){
                    bDefault = true;
                }
                else{
                    bDefault = false;
                }
            }

            if(bDefault){
                bitmap = ImageUtil.decodeSampledBitmapFromResource(HomeActivity.getInstance().getResources(), R.drawable.ic_playlist_default, 100, 100);
                ivImage.setImageBitmap(bitmap);
            }
            else{
                bitmap = ImageUtil.decodeSampledBitmapFromPath(strPath, 100, 100);
                ivImage.setImageBitmap(bitmap);
            }
        }
    }
}
