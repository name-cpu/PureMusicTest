package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.List;
/**
 * Created by 24820 on 2016/12/14.
 */
public class AlertDialogDeleteOne extends AlertDialog implements View.OnClickListener{
    private CheckBox ckDeleteFile;
    private TextView btnDelete;
    private TextView btnCancel;
    private TextView tvTitle;
    private List<MusicInfoDao> mListMusicInfoDao;
    private IOnAlertDialogDeleteListener mListener;

    public interface IOnAlertDialogDeleteListener{
        void OnDeleteClick(AlertDialogDeleteOne dialog, boolean isDeleteFile);
    }

    protected AlertDialogDeleteOne(Context context, IOnAlertDialogDeleteListener listener) {
        super(context);
        mListener = listener;
    }

    protected AlertDialogDeleteOne(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog_delete_music);
        ckDeleteFile = (CheckBox) this.findViewById(R.id.cbDeleteFile);
        btnDelete = (TextView) this.findViewById(R.id.btnDelete);
        btnCancel = (TextView) this.findViewById(R.id.btnCancel);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        btnDelete.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void setContentView(){

    }

    public void show(){
        if(ckDeleteFile != null){
            ckDeleteFile.setChecked(false);
        }
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

    public List<MusicInfoDao> getMusicInfoDaoData(){
        return mListMusicInfoDao;
    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            dismiss();
        }
        else if(v == btnDelete){
            if(mListMusicInfoDao == null){
                dismiss();
                return;
            }

            boolean bDeleteFile = ckDeleteFile.isChecked();
            if(mListener != null){
                mListener.OnDeleteClick(this, bDeleteFile);
            }

            dismiss();
        }
    }
}
