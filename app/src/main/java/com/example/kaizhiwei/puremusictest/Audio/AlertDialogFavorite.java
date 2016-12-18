package com.example.kaizhiwei.puremusictest.Audio;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;

import java.util.List;
/**
 * Created by 24820 on 2016/12/14.
 */
public class AlertDialogFavorite extends AlertDialog implements View.OnClickListener{
    private EditText etFavoriteName;
    private TextView btnDelete;
    private TextView btnCancel;
    private TextView tvTitle;
    private int mOperType;
    private OnAlterDialogFavoriteListener mListener;
    private FavoriteEntity mFavoriteEntity;
    public static final int USER_CANCEL = -1;
    public static final int ADD_FAVORITE = 0;
    public static final int MODIFY_FAVORITE = 1;

    public interface OnAlterDialogFavoriteListener{
        public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName);
    }

    public AlertDialogFavorite(Context context, OnAlterDialogFavoriteListener listener) {
        super(context);
        mListener = listener;
    }

    public AlertDialogFavorite(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog_oper_favorite);
        etFavoriteName = (EditText) this.findViewById(R.id.etFavoriteName);
        btnDelete = (TextView) this.findViewById(R.id.btnDelete);
        btnCancel = (TextView) this.findViewById(R.id.btnCancel);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void setOperType(int type){
        mOperType = type;
        if(mOperType == ADD_FAVORITE){
            tvTitle.setText("新建歌单");
        }
        else if(mOperType == MODIFY_FAVORITE){
            tvTitle.setText("重命名");
            if(mFavoriteEntity != null){
                etFavoriteName.setText(mFavoriteEntity.strFavoriteName);
            }
        }

        //this.getWindow().setBackgroundDrawable(new ColorDrawable(0));

//        //etFavoriteName.requestFocus();
//        etFavoriteName.requestFocus();
//        InputMethodManager imm = (InputMethodManager) etFavoriteName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etFavoriteName.setFocusable(true);
    }

    public void setAlertFavoriteListener(OnAlterDialogFavoriteListener listener){
        mListener = listener;
    }

    public void show(){
        super.show();
    }

    public void setFavoriteEntity(FavoriteEntity entity){
        if(entity == null)
            return;

        mFavoriteEntity = entity;
    }

    public FavoriteEntity getFavoriteEntity(){
        return mFavoriteEntity;
    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            if(mListener != null){
                mListener.OnFinish(this, USER_CANCEL, "");
            }
            dismiss();
        }
        else if(v == btnDelete){
            String strFavoriteName = etFavoriteName.getText().toString();
            if(mOperType == ADD_FAVORITE){
                if(TextUtils.isEmpty(strFavoriteName)){
                    Toast.makeText(this.getContext(), "要输入歌单名字哦~", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(MediaLibrary.getInstance().isExistFavorite(strFavoriteName, -1)){
                    Toast.makeText(this.getContext(), "该歌单已经存在,换个名字吧~", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            else if(mOperType == MODIFY_FAVORITE){
                if(TextUtils.isEmpty(strFavoriteName)){
                    Toast.makeText(this.getContext(), "歌单名字不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(MediaLibrary.getInstance().isExistFavorite(strFavoriteName, mFavoriteEntity._id)){
                    Toast.makeText(this.getContext(), "该歌单已经存在,换个名字吧~", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if(mListener != null){
                mListener.OnFinish(this, mOperType, strFavoriteName);
            }
            dismiss();
        }
    }
}
