
package com.example.kaizhiwei.puremusictest.Audio;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
public class FavoriteDialog extends Dialog implements View.OnClickListener, AbsListView.OnItemClickListener, AlertDialogFavorite.OnAlterDialogFavoriteListener, FavoriteListViewAdapter.IFavoriteOperListener {
    private ListView lvFavorite;
    private TextView tvTitle;
    private FavoriteListViewAdapter mFavoriteListAdapter;
    private List<MediaEntity> mListMediaEntity;

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

        DisplayMetrics dm = null;
        dm = getContext().getApplicationContext().getResources().getDisplayMetrics();

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
        //wl.height = view.getHeight() > 300 * dm.densityDpi ? 300 * dm.densityDpi : view.getHeight();
        wl.gravity = Gravity.BOTTOM;

        window.setAttributes(wl);
    }

    public void setFavoritelistData(List<FavoriteEntity> list){
        mFavoriteListAdapter = new FavoriteListViewAdapter(this.getContext(), list, FavoriteListViewAdapter.READONLY_MODE, false);
        mFavoriteListAdapter.notifyDataSetChanged();
        mFavoriteListAdapter.setFavoriteAdapterListener(this);
        lvFavorite.setOnItemClickListener(this);
        lvFavorite.setAdapter(mFavoriteListAdapter);
    }

    public void setMediaEntityData(List<MediaEntity> list){
        mListMediaEntity = list;
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
        FavoriteEntity entity = (FavoriteEntity)mFavoriteListAdapter.getItem(position);
        if(entity == null)
            return;

        //新组歌单
        if(position == 0){
            AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), this);
            favoriteDialog.show();
            favoriteDialog.setFavoriteEntity(entity);
            favoriteDialog.setOperType(AlertDialogFavorite.ADD_FAVORITE);
        }
        //添加到已有的歌单
        else{
            boolean bExist = false;
            boolean bSuccess = false;
            if(mListMediaEntity != null){
                boolean isMutil = mListMediaEntity.size() > 1 ? true : false;
                for(int i = 0;i < mListMediaEntity.size();i++){
                    MediaEntity mediaEntity = mListMediaEntity.get(i);
                    if(mediaEntity == null)
                        continue ;

                    FavoritesMusicEntity favoritesMusicEntity = new FavoritesMusicEntity();
                    favoritesMusicEntity.musicinfo_id = mediaEntity._id;
                    favoritesMusicEntity.artist = mediaEntity.artist;
                    favoritesMusicEntity.album = mediaEntity.album;
                    favoritesMusicEntity.fav_time = System.currentTimeMillis();
                    favoritesMusicEntity.path = mediaEntity._data;
                    favoritesMusicEntity.title = mediaEntity.title;
                    favoritesMusicEntity.favorite_id = entity._id;

                    if(MediaLibrary.getInstance().queryIsFavoriteByMediaEntityId(mediaEntity._id, favoritesMusicEntity.favorite_id)){
                        bExist = true;
                    }
                    else{
                        bSuccess= MediaLibrary.getInstance().addFavoriteMusicEntity(favoritesMusicEntity);
                    }
                }

                String strPromt = "";
                if(bExist){
                    strPromt = String.format("歌曲已经添加到\"%s\"", entity.strFavoriteName);

                }
                else{
                    if(bSuccess){
                        strPromt = String.format("成功添加到\"%s\"", entity.strFavoriteName);
                    }
                    else{
                        strPromt = String.format("添加失败");
                    }
                }
                Toast.makeText(this.getContext(), strPromt, Toast.LENGTH_SHORT).show();
            }
        }

        dismiss();
    }

    @Override
    public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName) {
        if(dialog == null || TextUtils.isEmpty(strFavoriteName))
            return;

        String strPromt = "";
        if(operType == AlertDialogFavorite.ADD_FAVORITE){
            FavoriteEntity favoriteEntity = new FavoriteEntity();
            favoriteEntity.favoriteType = FavoriteEntity.DEFAULT_CUSTOME_TYPE;
            favoriteEntity.strFavoriteName = strFavoriteName;
            if(MediaLibrary.getInstance().addFavoriteEntity(favoriteEntity) == false){
                Toast.makeText(this.getContext(), "新建歌单失败",Toast.LENGTH_SHORT).show();
                return;
            }

            boolean bSuccess = false;
            for(int i = 0;i < mListMediaEntity.size();i++){
                MediaEntity mediaEntity = mListMediaEntity.get(i);
                if(mediaEntity == null)
                    continue;

                FavoritesMusicEntity favoritesMusicEntity = new FavoritesMusicEntity();
                favoritesMusicEntity.musicinfo_id = mediaEntity._id;
                favoritesMusicEntity.artist = mediaEntity.artist;
                favoritesMusicEntity.album = mediaEntity.album;
                favoritesMusicEntity.fav_time = System.currentTimeMillis();
                favoritesMusicEntity.path = mediaEntity._data;
                favoritesMusicEntity.title = mediaEntity.title;
                favoritesMusicEntity.favorite_id = favoriteEntity._id;

                bSuccess = MediaLibrary.getInstance().addFavoriteMusicEntity(favoritesMusicEntity);
            }

            if(bSuccess){
                strPromt = String.format("成功添加到\"%s\"", favoriteEntity.strFavoriteName);
            }
            else{
                strPromt = String.format("添加失败");
            }

        }
        else if(operType == AlertDialogFavorite.MODIFY_FAVORITE){
            FavoriteEntity entity = dialog.getFavoriteEntity();
            if(entity == null)return;

            entity.strFavoriteName = strFavoriteName;
            boolean bSuccess = false;
            bSuccess = MediaLibrary.getInstance().modifyFavoriteEntity(entity);
            if(bSuccess){
                strPromt = String.format("修改成功");
            }
            else{
                strPromt = String.format("修改失败");
            }
        }
        Toast.makeText(this.getContext(), strPromt, Toast.LENGTH_SHORT).show();
    }

    //FavoriteListViewAdapter.IFavoriteOperListener
    @Override
    public void OnModifyClick(FavoriteListViewAdapter adapter, int position) {
        if(adapter == null || position < 0)
            return;

        AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), this);
        favoriteDialog.show();
        favoriteDialog.setFavoriteEntity((FavoriteEntity)adapter.getItem(position));
        favoriteDialog.setOperType(AlertDialogFavorite.MODIFY_FAVORITE);
    }

    @Override
    public void OnDeleteClick(FavoriteListViewAdapter adapter, int position) {
        if(adapter == null || position < 0)
            return;
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

