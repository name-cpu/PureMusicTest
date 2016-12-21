package com.example.kaizhiwei.puremusictest.Audio;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;

import java.io.File;
import java.util.List;
/**
 * Created by 24820 on 2016/12/14.
 */
public class AlertDialogHide extends AlertDialog implements View.OnClickListener{
    private TextView btnOK;
    private TextView btnCancel;
    private TextView tvTitle;
    private List<MediaEntity> mListMediaEntity;

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

    public void show(){
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

    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            dismiss();
        }
        else if(v == btnOK){
            if(mListMediaEntity == null){
                dismiss();
                return;
            }

            int successNum = 0;
            for(int i = 0;i < mListMediaEntity.size();i++){
                MediaEntity entity = mListMediaEntity.get(i);
                if(entity == null || entity._id < 0)
                    continue;

                if(MediaLibrary.getInstance().removeMediaEntity(entity)){
                    successNum++;
                }
            }

            String strPromt = "";
            if(successNum == 0){
                strPromt = "删除失败";
            }
            else if(successNum < mListMediaEntity.size()){
                strPromt = "删除部分成功,部分失败";
            }
            else{
                strPromt = "删除成功";
            }
            Toast.makeText(this.getContext(), strPromt, Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }
}
