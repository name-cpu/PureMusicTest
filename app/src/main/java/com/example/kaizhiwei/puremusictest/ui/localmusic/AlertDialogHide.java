package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.List;
/**
 * Created by 24820 on 2016/12/14.
 */
public class AlertDialogHide extends AlertDialog implements View.OnClickListener{
    private TextView btnOK;
    private TextView btnCancel;
    private TextView tvTitle;
    private IAlertDialogHideListener mListener;
    private List<MusicInfoDao> mListMusicInfoDao;

    interface IAlertDialogHideListener{
        void onAlterDialogHideOk(String strFolderPath);
    }

    protected AlertDialogHide(Context context) {
        super(context);
    }

    protected AlertDialogHide(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog_hide);
        btnOK = (TextView) this.findViewById(R.id.btnOK);
        btnCancel = (TextView) this.findViewById(R.id.btnCancel);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void setContentView(){

    }

    public void setAlertDialogListener(IAlertDialogHideListener listener){
        mListener = listener;
    }

    public void show(){
        super.show();
    }

    public void setTitle(String title){
        if(tvTitle != null){
            tvTitle.setText(title);
        }
    }

    public void setMusicInfoDaoData(List<MusicInfoDao> list){
        mListMusicInfoDao = list;
    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            dismiss();
        }
        else if(v == btnOK){
            if(mListMusicInfoDao == null){
                dismiss();
                return;
            }

            int successNum = 0;
            for(int i = 0;i < mListMusicInfoDao.size();i++){
                MusicInfoDao entity = mListMusicInfoDao.get(i);
                if(entity == null || entity.get_id() < 0)
                    continue;

                if(MediaLibrary.getInstance().removeMusicInfoDao(entity)){
                    successNum++;
                }
            }

            String strPromt = "";
            if(successNum == 0){
                strPromt = "隐藏失败";
            }
            else if(successNum < mListMusicInfoDao.size()){
                strPromt = "隐藏成功" + successNum + "首,失败" + (mListMusicInfoDao.size() - successNum) + "首";
            }
            else{
                strPromt = "隐藏成功";
            }
            Toast.makeText(this.getContext(), strPromt, Toast.LENGTH_SHORT).show();
            dismiss();

            if(mListener != null && mListMusicInfoDao != null && mListMusicInfoDao.size() > 0){
                mListener.onAlterDialogHideOk(mListMusicInfoDao.get(0).getSave_path());
            }
        }
    }
}
