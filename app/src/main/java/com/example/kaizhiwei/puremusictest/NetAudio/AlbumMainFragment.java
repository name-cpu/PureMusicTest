package com.example.kaizhiwei.puremusictest.NetAudio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.Audio.AudioListViewAdapter;
import com.example.kaizhiwei.puremusictest.Audio.BatchMgrAudioActivity;
import com.example.kaizhiwei.puremusictest.Audio.LocalBaseMediaLayout;
import com.example.kaizhiwei.puremusictest.Audio.MoreOperationDialog;
import com.example.kaizhiwei.puremusictest.CommonUI.AlwaysMarqueeTextView;
import com.example.kaizhiwei.puremusictest.CommonUI.MyImageView;
import com.example.kaizhiwei.puremusictest.CommonUI.SystemBarTintManager;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetAlbumInfoData;
import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetGetAlbumInfoRequest;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.BusinessCode;
import com.example.kaizhiwei.puremusictest.Util.ImageUtil;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.bean.ActiveIndexBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaIndexBean;
import com.example.kaizhiwei.puremusictest.bean.SceneCategoryListBean;
import com.example.kaizhiwei.puremusictest.bean.ShowRedPointBean;
import com.example.kaizhiwei.puremusictest.bean.SugSceneBean;
import com.example.kaizhiwei.puremusictest.bean.UgcdiyBaseInfoBean;
import com.example.kaizhiwei.puremusictest.contract.ResetServerContract;
import com.example.kaizhiwei.puremusictest.presenter.ResetServerPresenter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by kaizhiwei on 17/1/11.
 */
public class AlbumMainFragment extends FragmentActivity implements View.OnClickListener, ResetServerContract.View {
    private ImageView ivBack;
    private ImageView imBig;
    private TextView tvAuthor;
    private TextView tvPubshliTime;
    private AlwaysMarqueeTextView tvTitle;
    private MyImageView mivMoreOper;
    private ImageView ivIcon;
    private TextView tvDetail;
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
    private ResetServerContract.Presenter mPresenter;

    public static final String ALBUM_KEY = "ALBUM_KEY";
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

                    Toast.makeText(AlbumMainFragment.this, strPromt, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_netalbum_info);
        initSystemBar();
        //StatusBarUtil.setTranslucentForImageView(AlbumMainFragment.this, 0, null);
        llMain = (LinearLayout) this.findViewById(R.id.llMain);
        ivBack = (ImageView)this.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        tvTitle = (AlwaysMarqueeTextView)this.findViewById(R.id.tvTitle);
        tvFavoriteNum = (TextView)this.findViewById(R.id.tvFavoriteNum);
        mivMoreOper = (MyImageView)this.findViewById(R.id.mivMoreOper);
        mivMoreOper.setResId(R.drawable.btn_menu_more, R.drawable.btn_menu_more_press);
        mivMoreOper.setOnClickListener(this);
        tvPlayAll = (TextView)this.findViewById(R.id.tvPlayAll);
        tvManager = (TextView)this.findViewById(R.id.tvManager);
        tvManager.setOnClickListener(this);
        lbmLayout = new LocalBaseMediaLayout(this, mSubFragmentListener);
        lbmLayout.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_NETWORK, false, false, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.BELOW, R.id.viewSepratorLine);
        lbmLayout.setLayoutParams(params);
        llMain.addView(lbmLayout);
        ivIcon = (ImageView)this.findViewById(R.id.ivIcon);
        llTitle = (LinearLayout)this.findViewById(R.id.llTitle);
        collapsing_toolbar = (CollapsingToolbarLayout)this.findViewById(R.id.collapsing_toolbar);
        tvDetail = (TextView)this.findViewById(R.id.tvDetail);
        imBig = (ImageView)this.findViewById(R.id.imBig);
        tvAuthor = (TextView)this.findViewById(R.id.tvAuthor);
        tvPubshliTime = (TextView)this.findViewById(R.id.tvPubshliTime);
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
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(android.R.color.transparent);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            return;

        String strAlbumKey = bundle.getString(ALBUM_KEY);
        NetGetAlbumInfoRequest request = new NetGetAlbumInfoRequest();
        request.setAlbumKey(strAlbumKey);

        mPresenter = new ResetServerPresenter(this);

        NetEngine.getInstance().asyncGetAlbumInfo(request, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                if(msg.what != BusinessCode.BUSINESS_CODE_SUCCESS || msg.obj == null)
                    return;

                NetAlbumInfoData data = (NetAlbumInfoData)msg.obj;
                if(data.title == null)
                    return;

                tvPubshliTime.setText(data.author);
                tvAuthor.setText("发行时间：" + data.publishtime);
                tvTitle.setText(data.title);
                tvFavoriteNum.setText("/" + data.listSongInfo.size() + "首");
                tvDetail.setText(data.info);
                Glide.with(HomeActivity.getInstance()).load(data.pic_small).into(ivIcon);
                Glide.with(HomeActivity.getInstance()).load(data.pic_big).bitmapTransform(new BlurTransformation(HomeActivity.getInstance(), 25)).into(imBig);

                List<AudioListViewAdapter.AudioNetWorkItemData> list = new ArrayList<AudioListViewAdapter.AudioNetWorkItemData>();
                for(int i = 0;i < data.listSongInfo.size();i++){
                    AudioListViewAdapter.AudioNetWorkItemData itemData = new AudioListViewAdapter.AudioNetWorkItemData();
                    itemData.strMain = data.listSongInfo.get(i).title;
                    itemData.strSub = data.listSongInfo.get(i).author;
                    itemData.strKey = data.listSongInfo.get(i).ting_uid;
                    list.add(itemData);
                }
                lbmLayout.initNetworkAdapterData(list);
            }
        });

//        Uri uri = Uri.parse(entity.strFavoriteImgPath);
//        //Glide.with(this).load(uri).bitmapTransform(new BlurTransformation(this, 25)).crossFade(1000)
//        setFavoriteImage(entity.strFavoriteImgPath, ivIcon, 300, 300);
//        tvTitle.setText(strFavoriteName);
//        mFavoriteEntity = entity;
//        if(favoriteId == MediaLibrary.getInstance().getDefaultFavoriteEntityId()){
//
//        }
//        else{
//            hideShowCtrlBtn(true);
//        }
        hideShowCtrlBtn(true);
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
        mivMoreOper.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void initAdapterData(){
        mListFavoriteData = new ArrayList<>();
    }

    public void onDestory(){
        super.onDestroy();
        lbmLayout.onDestory();
    }

    public void onResume(){
        super.onResume();
        if(lbmLayout != null){
            //lbmLayout.onResume();
        }
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

    @Override
    public void onClick(View v) {
        if(mFavoriteEntity == null)
            return;

        if(v == tvManager){
            Intent intent = new Intent(this, BatchMgrAudioActivity.class);
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
            finish();
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

    @Override
    public void onGetCatogaryListSuccess(SceneCategoryListBean bean) {

    }

    @Override
    public void onGetActiveIndexSuccess(ActiveIndexBean bean) {

    }

    @Override
    public void onShowRedPointSuccess(ShowRedPointBean bean) {

    }

    @Override
    public void onGetSugSceneSuccess(SugSceneBean bean) {

    }

    @Override
    public void onGetPlazaIndexSuccess(PlazaIndexBean bean) {

    }

    @Override
    public void onGetUgcdiyBaseInfoSuccess(UgcdiyBaseInfoBean baseInfoBean) {

    }

    @Override
    public void onError(String strErrMsg) {

    }
}
