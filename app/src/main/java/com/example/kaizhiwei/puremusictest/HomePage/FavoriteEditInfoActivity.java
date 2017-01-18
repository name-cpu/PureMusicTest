package com.example.kaizhiwei.puremusictest.HomePage;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Service.PlaybackService;
import com.example.kaizhiwei.puremusictest.Util.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kaizhiwei on 17/1/15.
 */
public class FavoriteEditInfoActivity extends BaseActivty {
    private FavoriteEntity mFaroviteEntity;
    private ImageView ivImage;
    private EditText etName;
    private EditText etDesc;
    private LinearLayout llFirst;
    private String mNewImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_editinfo);
        setTitle("编辑歌单信息");
        setRightTest("保存");
        ivImage = (ImageView)this.findViewById(R.id.ivImage);
        etName = (EditText)this.findViewById(R.id.etName);
        etDesc = (EditText)this.findViewById(R.id.etDesc);
        llFirst = (LinearLayout)this.findViewById(R.id.llFirst);
        llFirst.setOnClickListener(this);
        ivImage.setOnClickListener(this);

        long lFavoriteId = getIntent().getLongExtra(FavoriteMainFragment.FAVORITE_ID, 0);
        mFaroviteEntity = MediaLibrary.getInstance().getFavoriteEntityById(lFavoriteId);
        if(mFaroviteEntity != null){
            etName.setText(mFaroviteEntity.strFavoriteName);
            etName.setSelection(mFaroviteEntity.strFavoriteName.length());
            if(TextUtils.isEmpty(mFaroviteEntity.strFavoriteDesc) == false && mFaroviteEntity.strFavoriteDesc.equalsIgnoreCase("null") == false){
                etDesc.setText(mFaroviteEntity.strFavoriteDesc);
                etDesc.setSelection(mFaroviteEntity.strFavoriteDesc.length());
            }
            setFavoriteImage(mFaroviteEntity.strFavoriteImgPath);
        }
    }

    @Override
    public void onClick(View v) {
       if(tvRight == v){
           if(mFaroviteEntity == null)
               return;

            if(TextUtils.isEmpty(etName.getText().toString())) {
               Toast.makeText(this, "请填写歌单名称~", Toast.LENGTH_SHORT).show();
               return;
           }

           mFaroviteEntity.strFavoriteName = etName.getText().toString();
           mFaroviteEntity.strFavoriteDesc = etDesc.getText().toString();
           mFaroviteEntity.strFavoriteImgPath = mNewImagePath;
           boolean bRet = MediaLibrary.getInstance().modifyFavoriteEntity(mFaroviteEntity);
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
            setFavoriteImage(uri.getPath());
            mNewImagePath = uri.getPath();
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
        Bitmap bitmap = null;
        File file = new File(strPath);
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
            Uri uri = Uri.fromFile(file);
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                InputStream inStream = cr.openInputStream(uri);
                byte[] byData = ImageUtil.toByteArray(inStream);
                bitmap = ImageUtil.decodeSampledBitmapFromData(byData, 100, 100);
                ivImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
