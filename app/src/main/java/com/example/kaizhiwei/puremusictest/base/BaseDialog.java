package com.example.kaizhiwei.puremusictest.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by kaizhiwei on 17/8/5.
 */

public abstract class BaseDialog extends Dialog {
    private TextView tvDialogTitle;
    private FrameLayout flCustomeView;

    public BaseDialog(@NonNull Context context) {
        this(context, 0);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.Dialog);
    }

    public void setContentView(View view){
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.setContentView(view);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
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
        wl.gravity = Gravity.BOTTOM;
        window.setAttributes(wl);

        tvDialogTitle = (TextView)view.findViewById(R.id.tvDialogTitle);
        flCustomeView = (FrameLayout)view.findViewById(R.id.flCustomeView);
        initData();
        initView();
    }

    public void setTitle(String str){
        if(tvDialogTitle != null){
            tvDialogTitle.setText(str);
        }
    }

    public abstract void initData();

    public abstract void initView();

    public static abstract class Builder{
        protected BaseDialog baseDialog;
        protected Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        public BaseDialog create() {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final BaseDialog dialog = createDialog();
            View layout = inflater.inflate(R.layout.base_dialog, null);
            View customeView = inflater.inflate(getCustomeView(), null);
            FrameLayout flCustomeView = (FrameLayout)layout.findViewById(R.id.flCustomeView);
            flCustomeView.addView(customeView);
            dialog.setContentView(layout);

            return dialog;
        }

        public abstract <T extends BaseDialog> T createDialog();

        public abstract int getCustomeView();
    }
}
