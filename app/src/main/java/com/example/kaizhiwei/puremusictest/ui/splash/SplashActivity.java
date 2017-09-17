package com.example.kaizhiwei.puremusictest.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.UnStandardAdBean;
import com.example.kaizhiwei.puremusictest.contract.UnStandardContract;
import com.example.kaizhiwei.puremusictest.presenter.UnStandardAdPrensenter;
import com.example.kaizhiwei.puremusictest.ui.favorite.PersonalScrollView2;
import com.example.kaizhiwei.puremusictest.ui.favorite.PullZoomListView;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by kaizhiwei on 16/10/29.
 */
public class SplashActivity extends MyBaseActivity implements UnStandardContract.View {
    private Handler mHandler = new Handler();
    private UnStandardContract.Presenter mPresenter;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    @Bind(R.id.launcher_root_view)
    RelativeLayout launcher_root_view;

    @Override
    public int getLayoutId() {
        return R.layout.launch_activity;
    }

    @Override
    public void initPresenter() {
        mPresenter = new UnStandardAdPrensenter(this);
    }

    @Override
    public void initView() {
        //jiama();
    }

    @Override
    public void initData() {
        mPresenter.getUnStandardAd(19, 1, 1, "android", "music", "5.9.9.6", "B11BCF66936DA386C6AC9CA228F2C", "ppzs", 2);
    }

    @Override
    public void onGetUnStandardAdSuccess(UnStandardAdBean bean) {
        if (bean != null && bean.getMaterial_map() != null) {
            String strPicUrl = bean.getMaterial_map().get(0).getMaterials().getAd_mob_playerskin_turnround().getDisplay_content().get(0).getPicture();
            launcher_root_view.setBackgroundResource(R.color.black);
            Glide.with(this).load(strPicUrl).into(ivLogo);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public void onError(String strErrMsg) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    private class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 80;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            textView.setPadding(50, 50, 50, 50);
            textView.setText(position + 10 + "");
            return textView;
        }
    }

    private static final char[] a = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };

    private String jiama(){
        String key = "2012061372065459";
        String src = "query=%E7%BB%99%E8%87%AA%E5%B7%B1%E7%9A%84%E6%83%85%E4%B9%A6$$&ts=1505563440071";

        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes());
            byte[] md5Byte = md5.digest();
            StringBuilder builder = new StringBuilder(md5Byte.length *2);
            for(int i = 0;i < md5Byte.length;i++){
                builder.append(a[((md5Byte[i] & 0xF0) >>> 4)]);
                builder.append(a[(md5Byte[i] & 0xF)]);
            }

            String dsr = "jPzmjydCM0n7a1Cdib1MGo/bpUDhlMPGDLr3cMpsm2/j47Vg4ladKV2fzp4nrDny";
            byte[] dstByte = dsr.getBytes();


            String strBuild = builder.toString();
            SecretKeySpec secretKeySpec = new SecretKeySpec(strBuild.substring(strBuild.length()/2).getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, new IvParameterSpec(key.getBytes()));
            byte[] aesByte = cipher.doFinal(src.getBytes());
            //String str = new String(aesByte, "utf-8");
            //Log.e("weikaizhi", "jiama str = " + str);


            return dsr;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }


//    private static byte[] a = new byte[64];
//    public static char[] a(byte[] paramArrayOfByte)
//    {
//        int j = paramArrayOfByte.length;
//        int i2 = ((j << 2) + 2) / 3;
//        char[] arrayOfChar = new char[(j + 2) / 3 << 2];
//        int i3 = j + 0;
//        int k = 0;
//        j = 0;
//        if (j >= i3) {
//            return arrayOfChar;
//        }
//        int m = j + 1;
//        int i4 = paramArrayOfByte[j] & 0xFF;
//        int n;
//        if (m < i3)
//        {
//            j = paramArrayOfByte[m] & 0xFF;
//            n = m + 1;
//            m = j;
//            j = n;
//            label83:
//            if (j >= i3) {
//                break label226;
//            }
//            n = j + 1;
//            int i1 = paramArrayOfByte[j] & 0xFF;
//            j = n;
//            n = i1;
//            label110:
//            i1 = k + 1;
//            arrayOfChar[k] = a[(i4 >>> 2)];
//            k = i1 + 1;
//            arrayOfChar[i1] = a[((i4 & 0x3) << 4 | m >>> 4)];
//            if (k >= i2) {
//                break label232;
//            }
//            i = a[((m & 0xF) << 2 | n >>> 6)];
//            label176:
//            arrayOfChar[k] = i;
//            k += 1;
//            if (k >= i2) {
//                break label238;
//            }
//        }
//        label226:
//        label232:
//        label238:
//        for (int i = a[(n & 0x3F)];; i = 61)
//        {
//            arrayOfChar[k] = i;
//            k += 1;
//            break;
//            n = 0;
//            j = m;
//            m = n;
//            break label83;
//            n = 0;
//            break label110;
//            i = 61;
//            break label176;
//        }
//    }
}
