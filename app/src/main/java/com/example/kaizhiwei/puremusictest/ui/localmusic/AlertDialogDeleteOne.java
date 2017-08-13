package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.R;

import java.util.List;
/**
 * Created by 24820 on 2016/12/14.
 */
public class AlertDialogDeleteOne extends AlertDialog implements View.OnClickListener{
    private CheckBox ckDeleteFile;
    private TextView btnDelete;
    private TextView btnCancel;
    private TextView tvTitle;
    private List<MediaEntity> mListMediaEntity;
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

    public void setMediaEntityData(List<MediaEntity> list){
        mListMediaEntity = list;
    }

    public List<MediaEntity> getMediaEntityData(){
        return mListMediaEntity;
    }

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            dismiss();
        }
        else if(v == btnDelete){
            if(mListMediaEntity == null){
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
