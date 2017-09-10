package com.example.kaizhiwei.puremusictest.ui.favorite;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseBroadCastContant;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;
import com.example.kaizhiwei.puremusictest.util.ImageUtil;
import com.example.kaizhiwei.puremusictest.util.RxBus;

import java.io.File;
import java.io.InputStream;

/**
 * Created by kaizhiwei on 17/1/15.
 */
public class FavoriteEditInfoActivity extends BaseActivty {
    private PlaylistDao playlistDao;
    private ImageView ivImage;
    private EditText etName;
    private LinearLayout llFirst;
    private String mNewImagePath;

    public static final String PLAYLIST_DAO = "PLAYLIST_DAO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_editinfo);
        setTitle("编辑歌单信息");
        setRightTest("保存");
        ivImage = (ImageView)this.findViewById(R.id.ivImage);
        etName = (EditText)this.findViewById(R.id.etName);
        llFirst = (LinearLayout)this.findViewById(R.id.llFirst);
        llFirst.setOnClickListener(this);
        ivImage.setOnClickListener(this);

        playlistDao = getIntent().getParcelableExtra(PLAYLIST_DAO);
        etName.setText(playlistDao.getName());
        etName.setSelection(playlistDao.getName().length());
        setFavoriteImage(playlistDao.getImg_url());
    }

    @Override
    public void onClick(View v) {
       if(tvRight == v){
           if(playlistDao == null)
               return;

            if(TextUtils.isEmpty(etName.getText().toString())) {
               Toast.makeText(this, "请填写歌单名称~", Toast.LENGTH_SHORT).show();
               return;
           }

           playlistDao.setName(etName.getText().toString());
           playlistDao.setImg_url(mNewImagePath);
           playlistDao.setDate_modified(System.currentTimeMillis());
           boolean bRet = PlaylistModel.getInstance().updatePlaylist(playlistDao);
           String str = "";
           if(bRet){
               str = "保存成功";
           }
           else{
               str = "保存失败";
           }
           Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
        }
        else if(v == llFirst || v == ivImage){
           Intent intent = new Intent();
           /* 开启Pictures画面Type设定为image */
           intent.setType("image/*");
           /* 使用Intent.ACTION_GET_CONTENT这个Action */
           intent.setAction(Intent.ACTION_GET_CONTENT);
           /* 取得相片后返回本画面 */
           startActivityForResult(intent, 1);
       }

        super.onClick(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            setFavoriteImage(uri.toString());
            mNewImagePath = uri.toString();
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void setFavoriteImage(String strPath){
        boolean bDefault = false;
        if(TextUtils.isEmpty(strPath)){
            bDefault = true;
        }

        if(bDefault){
            Glide.with(this).load(R.drawable.ic_playlist_default).into(ivImage);
        }
        else{
            Uri uri = Uri.parse(strPath);
            Glide.with(this).load(uri).into(ivImage);
        }
    }
}
