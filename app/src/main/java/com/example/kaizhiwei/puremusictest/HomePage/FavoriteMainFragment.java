package com.example.kaizhiwei.puremusictest.HomePage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.Audio.AudioListViewAdapter;
import com.example.kaizhiwei.puremusictest.Audio.BatchMgrAudioActivity;
import com.example.kaizhiwei.puremusictest.Audio.LocalBaseMediaLayout;
import com.example.kaizhiwei.puremusictest.Audio.MoreOperationDialog;
import com.example.kaizhiwei.puremusictest.CommonUI.BaseFragment;
import com.example.kaizhiwei.puremusictest.CommonUI.MyImageView;
import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.FastBlur;
import com.example.kaizhiwei.puremusictest.Util.ImageUtil;
import com.hp.hpl.sparta.Text;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by kaizhiwei on 17/1/11.
 */
public class FavoriteMainFragment extends BaseFragment implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private MyImageView mivEdit;
    private MyImageView mivMoreOper;
    private ImageView ivIcon;
    private TextView tvPlayAll;
    private TextView tvFavoriteNum;
    private TextView tvManager;
    private LinearLayout llMain;
    private LocalBaseMediaLayout lbmLayout;
    private List<MediaEntity> mListFavoriteData;
    private Handler mHandler = new Handler();
    private LinearLayout llTitle;
    private FavoriteEntity mFavoriteEntity;
    private CollapsingToolbarLayout collapsing_toolbar;

    public static final String FAVORITE_ID = "FAVORITE_ID";
    public static final String FAVORITE_NAME = "FAVORITE_NAME";

    private LocalBaseMediaLayout.ILocalBaseListener mSubFragmentListener= new LocalBaseMediaLayout.ILocalBaseListener() {
        @Override
        public void onFragmentInitFinish(LinearLayout fragment) {

        }

        @Override
        public void onMoreOperClick(LocalBaseMediaLayout layout, int flag, Object obj) {
            if(obj == null | mFavoriteEntity == null)
                return;

            boolean bRet = false;
            List<MediaEntity> list = (List<MediaEntity>)obj;
            if(flag == MoreOperationDialog.MORE_REMOVE_NORMAL){
                for(int i = 0;i < list.size();i++){
                    bRet = MediaLibrary.getInstance().removeFavoriteMusicEntity(list.get(i)._id, mFavoriteEntity._id);
                }
            }

            initAdapterData();
            final boolean finalBRet = bRet;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String strPromt = "";
                    if(finalBRet){
                        strPromt = "移除成功";
                    }
                    else{
                        strPromt = "移除失败";
                    }

                    Toast.makeText(FavoriteMainFragment.this.getActivity(), strPromt, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_favorite_main, container, false);
        llMain = (LinearLayout) rootView.findViewById(R.id.llMain);
        ivBack = (ImageView)rootView.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        tvTitle = (TextView)rootView.findViewById(R.id.tvTitle);
        tvFavoriteNum = (TextView)rootView.findViewById(R.id.tvFavoriteNum);
        mivEdit = (MyImageView)rootView.findViewById(R.id.mivEdit);
        mivEdit.setOnClickListener(this);
        mivEdit.setResId(R.drawable.btn_edit_playlist_normol, R.drawable.btn_edit_playlist_press);
        mivMoreOper = (MyImageView)rootView.findViewById(R.id.mivMoreOper);
        mivMoreOper.setResId(R.drawable.btn_menu_more, R.drawable.btn_menu_more_press);
        mivMoreOper.setOnClickListener(this);
        tvPlayAll = (TextView)rootView.findViewById(R.id.tvPlayAll);
        tvManager = (TextView)rootView.findViewById(R.id.tvManager);
        tvManager.setOnClickListener(this);
        lbmLayout = new LocalBaseMediaLayout(this.getActivity(), mSubFragmentListener);
        lbmLayout.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, false, false, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.BELOW, R.id.viewSepratorLine);
        lbmLayout.setLayoutParams(params);
        llMain.addView(lbmLayout);
        ivIcon = (ImageView)rootView.findViewById(R.id.ivIcon);
        llTitle = (LinearLayout)rootView.findViewById(R.id.llTitle);
        collapsing_toolbar = (CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing_toolbar);
        initData();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean bDefault = true;
                if(mFavoriteEntity != null && TextUtils.isEmpty(mFavoriteEntity.strFavoriteImgPath) == false){
                    File file = new File(mFavoriteEntity.strFavoriteImgPath);
                    if(file.exists()){
                        bDefault = false;
                    }
                }

//                if(bDefault){
//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_playlist_default);
//                    Drawable drawable = FastBlur.BoxBlurFilter(bitmap);
//                    llTitle.setBackground(drawable);
//                }
//                else{
//                    Bitmap bitmap = BitmapFactory.decodeFile(mFavoriteEntity.strFavoriteImgPath);
//                    Drawable drawable = FastBlur.BoxBlurFilter(bitmap);
//                    llTitle.setBackground(drawable);
//
//                }
            }
        }, 500);

        return rootView;
    }

    private void initData(){
        Bundle bundle = getArguments();
        final long favoriteId = bundle.getLong(FAVORITE_ID);
        String strFavoriteName = bundle.getString(FAVORITE_NAME);
        FavoriteEntity entity = MediaLibrary.getInstance().getFavoriteEntityById(favoriteId);
        if(entity == null)
            return;

        Uri uri = Uri.parse(entity.strFavoriteImgPath);
        //Glide.with(this).load(uri).bitmapTransform(new BlurTransformation(this.getActivity(), 25)).crossFade(1000)
        setFavoriteImage(entity.strFavoriteImgPath, ivIcon, 300, 300);
        tvTitle.setText(strFavoriteName);
        mFavoriteEntity = entity;
        if(favoriteId == MediaLibrary.getInstance().getDefaultFavoriteEntityId()){
            hideShowCtrlBtn(false);
        }
        else{
            hideShowCtrlBtn(true);
        }

        List<Integer> list = new ArrayList<>();
        list.add(MoreOperationDialog.MORE_NEXTPLAY_NORMAL);
        list.add(MoreOperationDialog.MORE_LOVE_NORMAL);
        list.add(MoreOperationDialog.MORE_BELL_NORMAL);
        list.add(MoreOperationDialog.MORE_SHARE_NORMAL);
        list.add(MoreOperationDialog.MORE_ADD_NORMA);
        list.add(MoreOperationDialog.MORE_REMOVE_NORMAL);
        lbmLayout.setMoreOperDialogData(list);
    }

    private void hideShowCtrlBtn(boolean isShow){
        mivEdit.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mivMoreOper.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void initAdapterData(){
        mListFavoriteData = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FavoritesMusicEntity> list = MediaLibrary.getInstance().getFavoriteMusicById(mFavoriteEntity._id);
                for(int i = 0;i < list.size();i++){
                    MediaEntity mediaEntity = MediaLibrary.getInstance().getMediaEntityById(list.get(i).musicinfo_id);
                    if(mediaEntity == null)
                        continue;

                    mListFavoriteData.add(mediaEntity);
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvFavoriteNum.setText("/" + mListFavoriteData.size() + "首");
                        lbmLayout.initAdapterData(mListFavoriteData);
                    }
                });
            }
        }).start();
    }

    public void onDestory(){
        super.onDestroy();
        lbmLayout.onDestory();
    }

    public void onResume(){
        super.onResume();
        lbmLayout.onResume();
        initAdapterData();
    }

    public void onPause() {
        super.onPause();
        lbmLayout.onPause();
    }

    public void onStart() {
        super.onStart();
        lbmLayout.onStart();
    }

    public void onStop() {
        super.onStop();
        lbmLayout.onStop();
    }

    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if(mFavoriteEntity == null)
            return;

        if(mivEdit == v){
            Intent intent = new Intent(HomeActivity.getInstance(), FavoriteEditInfoActivity.class);
            intent.putExtra(FAVORITE_ID, mFavoriteEntity._id);
            HomeActivity.getInstance().startActivity(intent);
        }
        else if(v == tvManager){
            Intent intent = new Intent(this.getActivity(), BatchMgrAudioActivity.class);
            List<MediaEntity> listTemp = new ArrayList<>();
            List<MediaEntity> temp = lbmLayout.getAdapterOriginData();
            if(temp != null){
                listTemp.addAll(temp);
            }
            intent.putExtra(BatchMgrAudioActivity.INTENT_LIST_DATA, (Serializable)listTemp);
            intent.putExtra(BatchMgrAudioActivity.INTENT_RIGHTBTNISDELETE, false);
            intent.putExtra(BatchMgrAudioActivity.INTENT_RIGHTBTNISREMOVE, true);
            intent.putExtra(BatchMgrAudioActivity.INTENT_FAVORITEID, mFavoriteEntity._id);
            HomeActivity.getInstance().startActivityForResult(intent, 2);
            HomeActivity.getInstance().overridePendingTransition(R.anim.anim_left_enter, R.anim.anim_right_exit);
        }
        else if(v == ivBack){
            this.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void setFavoriteImage(String strPath, ImageView ivImage, int reqWidth, int reqHeight){
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
            bitmap = ImageUtil.decodeSampledBitmapFromResource(HomeActivity.getInstance().getResources(), R.drawable.ic_playlist_default, reqWidth, reqHeight);
            ivImage.setImageBitmap(bitmap);
        }
        else{
            bitmap = ImageUtil.decodeSampledBitmapFromPath(strPath, reqWidth, reqHeight);
            ivImage.setImageBitmap(bitmap);
        }
    }
}
