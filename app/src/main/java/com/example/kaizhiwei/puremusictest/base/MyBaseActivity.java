package com.example.kaizhiwei.puremusictest.base;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.SystemBarTintManager;

import butterknife.ButterKnife;

/**
 * 基类
 */

public abstract class MyBaseActivity extends AppCompatActivity {

    public Context mContext;
    private int count;//记录开启进度条的情况 只能开一个
    protected Toast mToast;
    protected SystemBarTintManager systemBarTintManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(getLayoutId());
        // 默认着色状态栏
        initSystemBar();
        ButterKnife.bind(this);
        mContext = this;
        this.initPresenter();
        this.initData();
        this.initView();
    }

    /**
     * 设置layout前配置
     */
    protected void doBeforeSetcontentView() {
        // 把actvity放到application栈中管理
        //AppManager.getAppManager().addActivity(this);
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


    /*********************
     * 子类实现
     *****************************/
    //获取布局文件
    public abstract int getLayoutId();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    //初始化view
    public abstract void initView() ;

    public abstract void initData();

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(R.color.colorPrimary);
            SystemBarTintManager.SystemBarConfig config = systemBarTintManager.getConfig();
        }
    }

    protected void setTintBarAlpha(float alpha){
        if(systemBarTintManager != null){
            systemBarTintManager.setTintAlpha(alpha);
        }
    }

    protected void setTranslucentStatus(boolean on) {
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

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mUnbinder.unbind();
        //AppManager.getAppManager().finishActivity(this);
    }

    public void showToast(String str){
        if(mToast == null){
            mToast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        }

        mToast.setText(str);
        mToast.show();
    }
}
