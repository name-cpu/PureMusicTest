package com.example.kaizhiwei.puremusictest.HomePage;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.Audio.AudioListViewAdapter;
import com.example.kaizhiwei.puremusictest.Audio.LocalBaseMediaLayout;
import com.example.kaizhiwei.puremusictest.CommonUI.BaseFragment;
import com.example.kaizhiwei.puremusictest.CommonUI.MyImageView;
import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.FastBlur;
import com.hp.hpl.sparta.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/1/11.
 */
public class FavoriteMainFragment extends BaseFragment implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private MyImageView mivEdit;
    private MyImageView mivMoreOper;
    private TextView tvPlayAll;
    private TextView tvFavoriteNum;
    private TextView tvManager;
    private RelativeLayout rlMain;
    private LocalBaseMediaLayout lbmLayout;
    private List<MediaEntity> mListFavoriteData;
    private Handler mHandler = new Handler();
    private LinearLayout llTitle;

    public static final String FAVORITE_ID = "FAVORITE_ID";
    public static final String FAVORITE_NAME = "FAVORITE_NAME";

    private LocalBaseMediaLayout.IFragmentInitListener mSubFragmentListener= new LocalBaseMediaLayout.IFragmentInitListener() {
        @Override
        public void onFragmentInitFinish(LinearLayout fragment) {

        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_favorite_main, container, false);
        rlMain = (RelativeLayout)rootView.findViewById(R.id.rlMain);
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
        lbmLayout = new LocalBaseMediaLayout(this.getActivity(), mSubFragmentListener);
        lbmLayout.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, false, false, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.BELOW, R.id.viewSepratorLine);
        lbmLayout.setLayoutParams(params);
        rlMain.addView(lbmLayout);
        initData();

        llTitle = (LinearLayout)rootView.findViewById(R.id.llTitle);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_playlist_default);
        Drawable drawable = FastBlur.BoxBlurFilter(bitmap);
        llTitle.setBackground(drawable);
        return rootView;
    }

    private void initData(){
        Bundle bundle = getArguments();
        final long favoriteId = bundle.getLong(FAVORITE_ID);
        String strFavoriteName = bundle.getString(FAVORITE_NAME);
        tvTitle.setText(strFavoriteName);

        mListFavoriteData = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FavoritesMusicEntity> list = MediaLibrary.getInstance().getFavoriteMusicById(favoriteId);
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

    }
}
