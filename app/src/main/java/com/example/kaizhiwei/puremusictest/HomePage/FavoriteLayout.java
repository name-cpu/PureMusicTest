package com.example.kaizhiwei.puremusictest.HomePage;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.ImageUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/12/18.
 */
public class FavoriteLayout extends LinearLayout{
    private List<FavoriteEntity> mFavoriteListData;
    private List<View> mFavoriteItemView;
    private Context mContext;
    private int mOperMode;
    private boolean mIsHomePage;
    public static final int EDIT_MODE = 0;
    public static final int READONLY_MODE = 1;
    private IFavoriteOperListener mListener;

    public interface IFavoriteOperListener{
        void onModifyClick(FavoriteLayout layout, int position);
        void onDeleteClick(FavoriteLayout layout, int position);
        void onItemLongClick(FavoriteLayout layout, int position);
        void onItemClick(FavoriteLayout layout, int position);
    }

    public FavoriteLayout(Context context) {
        super(context, null);
        mContext = context;
        init();
    }

    public FavoriteLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;
        init();
    }

    public FavoriteLayout(Context context, List<FavoriteEntity> list, int mode, boolean bHomePage){
        super(context);
        mContext = context;
        mFavoriteListData = list;
        init();
        mOperMode = mode;
    }

    public void setMode(int mode){
        mOperMode = mode;
        updateItemView();
    }

    public int getMode(){
        return mOperMode;
    }

    public void setIsHomePage(boolean bHomePage){
        mIsHomePage = bHomePage;
    }

    public boolean getIsHomePage(){
        return mIsHomePage;
    }

    public void setFavoriteLayoutListener(IFavoriteOperListener listener){
        mListener = listener;
    }

    private void updateItemView(){
        int iCount = this.getChildCount();
        for(int i = 0; i < iCount;i++){
            View childView = this.getChildAt(i);
            if(childView == null || childView.getTag() == null)
                continue;

            FavoriteLayoutHolder holder = (FavoriteLayoutHolder)childView.getTag();
            holder.setFavoriteType((int) mFavoriteListData.get(i).favoriteType, mOperMode, mFavoriteListData.get(i).strFavoriteImgPath);
        }
    }

    public void init(){

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(0,0,0,0);
        this.setLayoutParams(param);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public void setFavoriteEntityData(List<FavoriteEntity> list){
        if(list == null)
            return;

        if(mFavoriteListData == null){
            mFavoriteListData = new ArrayList<>();
        }
        mFavoriteListData.clear();
        mFavoriteListData.addAll(list);
    }

    public void show(){
        if(mFavoriteListData == null)
            return;

        FavoriteEntity addOne = new FavoriteEntity();
        addOne.favoriteType = FavoriteEntity.DEFAULT_ADDONE_TYPE;
        addOne._id = -1;
        addOne.strFavoriteName = "新建歌单";

        if(mIsHomePage == false){
            mFavoriteListData.add(0, addOne);
        }

        if(mFavoriteItemView == null){
            mFavoriteItemView = new ArrayList<>();
        }

        this.removeAllViews();
        for(int i = 0;i < mFavoriteListData.size();i++){
            FavoriteEntity entity = mFavoriteListData.get(i);
            if(entity == null)
                continue;

            View item = getView(i, null, null);
            item.setClickable(true);
            this.addView(item);
        }
    }

    public void insertView(int position, FavoriteEntity entity){
        if(position < 0 || entity == null)
            return;

        mFavoriteListData.add(position, entity);
        View item = getView(position, null, null);
        item.setClickable(true);
        this.addView(item, position);

        for(int i = 0;i < mFavoriteListData.size();i++){
            if(entity._id == mFavoriteListData.get(i)._id){

            }
            else{
                getView(i, this.getChildAt(i), null);
            }
        }
    }

    public void removeView(FavoriteEntity entity){
        if(entity == null)
            return;

        for(int i = 0;i < mFavoriteListData.size();i++){
            if(entity._id == mFavoriteListData.get(i)._id){
                mFavoriteListData.remove(i);
                this.removeViewAt(i);
            }
        }

        for(int i = 0;i < mFavoriteListData.size();i++){
            if(entity._id == mFavoriteListData.get(i)._id){

            }
            else{
                getView(i, this.getChildAt(i), null);
            }
        }
    }

    public void updateView(FavoriteEntity entity){
        if(entity == null)
            return;

        for(int i = 0;i < mFavoriteListData.size();i++){
            if(entity._id == mFavoriteListData.get(i)._id){
                mFavoriteListData.remove(i);
                mFavoriteListData.add(i, entity);
                View childView = this.getChildAt(i);
                if(childView == null || childView.getTag() == null)
                    return;

                FavoriteLayoutHolder holder = (FavoriteLayoutHolder)childView.getTag();
                holder.tvFavoriteMain.setText(entity.strFavoriteName);
                holder.tvFavoriteSub.setText(entity.favoriteMusicNum + "首");
                break;
            }
        }
    }

    public Object getItem(int position) {
        if(position < 0 || position >= mFavoriteListData.size())
            return null;

        return mFavoriteListData.get(position);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if(mFavoriteListData == null || position >= mFavoriteListData.size())
            return null;

        Log.i("weikaizhi", "getView " + position);
        FavoriteEntity entity = mFavoriteListData.get(position);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FavoriteLayoutHolder holder = null;
        if(convertView == null){
            View view = inflater.inflate(R.layout.favorite_dialog_item, null);
            view.setBackgroundResource(R.color.backgroundColor);
            holder = new FavoriteLayoutHolder(view);
            holder.ibBtnEdit.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mListener != null)
                    mListener.onDeleteClick(FavoriteLayout.this, position);
                }
            });

            holder.ibBtnDelete.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onDeleteClick(FavoriteLayout.this, position);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    if(mListener != null){
                        mListener.onItemLongClick(FavoriteLayout.this, position);
                    }
                    return false;
                }
            });
            view.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onItemClick(FavoriteLayout.this, position);
                }
            });

            view.setTag(holder);
            convertView = view;
        }
        else{
            holder = (FavoriteLayoutHolder) convertView.getTag();
        }

        holder.setPosition(position);
        holder.tvFavoriteMain.setTextColor(mContext.getResources().getColor(R.color.mainTextColor));
        holder.tvFavoriteSub.setTextColor(mContext.getResources().getColor(R.color.subTextColor));
        holder.tvFavoriteMain.setText(entity.strFavoriteName);
        holder.tvFavoriteSub.setText(entity.favoriteMusicNum + "首");
        holder.setFavoriteType((int) entity.favoriteType, mOperMode, entity.strFavoriteImgPath);

        return convertView;
    }

    private static class FavoriteLayoutHolder{
        public ImageView ivSongImage;
        public TextView tvFavoriteMain;
        public TextView tvFavoriteSub;
        public ImageView ibBtnMore;
        public ImageView ibBtnEdit;
        public ImageView ibBtnDelete;
        public int position;

        public void setPosition(int position){
            this.position = position;
        }

        public FavoriteLayoutHolder(View view){
            ivSongImage = (ImageView)view.findViewById(R.id.ivSongImage);
            tvFavoriteMain = (TextView)view.findViewById(R.id.tvFavoriteMain);
            tvFavoriteSub = (TextView)view.findViewById(R.id.tvFavoriteSub);
            ibBtnMore = (ImageView)view.findViewById(R.id.ibBtnMore);
            ibBtnEdit = (ImageView)view.findViewById(R.id.ibBtnEdit);
            ibBtnDelete = (ImageView)view.findViewById(R.id.ibBtnDelete);
            ibBtnEdit.setClickable(true);
            ibBtnDelete.setClickable(true);
        }

        public void setFavoriteType(int favoriteType, int mode, String strPath){
            if(favoriteType == FavoriteEntity.DEFAULT_ADDONE_TYPE){
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
            else if(favoriteType == FavoriteEntity.DEFAULT_FAVORITE_TYPE){
                ivSongImage.setImageResource(R.drawable.ic_mymusic_like);
                ibBtnMore.setVisibility(View.VISIBLE);
                ibBtnEdit.setVisibility(View.GONE);
                ibBtnDelete.setVisibility(View.GONE);
            }
            else{
                //ivSongImage.setImageResource(R.drawable.ic_mymusic_list_item);
                setFavoriteImage(strPath, ivSongImage);
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



