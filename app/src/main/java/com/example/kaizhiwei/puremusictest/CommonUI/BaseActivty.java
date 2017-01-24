package com.example.kaizhiwei.puremusictest.CommonUI;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 16/12/25.
 */
public class BaseActivty extends Activity implements View.OnClickListener{
    protected TextView tvTitle;
    protected TextView tvRight;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
    }

    @Override
    public void setContentView(int resId){
        super.setContentView(R.layout.baseactivity_layout);
        LinearLayout ll = (LinearLayout)this.findViewById(R.id.llContent);
        View view = this.getLayoutInflater().inflate(resId, null);
        ll.addView(view);
        tvTitle = (TextView)this.findViewById(R.id.tvTitle);
        tvRight = (TextView)this.findViewById(R.id.tvRight);
        tvTitle.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight.setVisibility(View.GONE);
    }

    public void setTitle(String title){
        if(tvTitle != null){
            tvTitle.setText(title);
        }
    }

    public void setRightTest(String title){
        if(tvRight != null){
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(title);
        }
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.common_title_backgroundColor);
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

    @Override
    public void onClick(View v) {
        if(tvTitle == v){
            onActictyFinish();
        }
        else if(tvRight == v){
            onActictyFinish();
        }
    }

    protected void onActictyFinish(){
        finish();
    }

    public void toast(String str){
        if(mToast == null){
            mToast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        }

        mToast.setText(str);
        mToast.show();
    }
}
