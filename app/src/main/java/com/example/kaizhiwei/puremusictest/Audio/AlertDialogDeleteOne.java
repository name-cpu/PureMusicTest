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
public class AlertDialogDeleteOne extends AlertDialog implements View.OnClickListener{
    private CheckBox ckDeleteFile;
    private TextView btnDelete;
    private TextView btnCancel;
    private TextView tvTitle;
    private List<MediaEntity> mListMediaEntity;

    protected AlertDialogDeleteOne(Context context) {
        super(context);
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
        else if(v == btnDelete){
            if(mListMediaEntity == null){
                dismiss();
                return;
            }

            boolean bDeleteFile = ckDeleteFile.isChecked();
            int successNum = 0;
            for(int i = 0;i < mListMediaEntity.size();i++){
                MediaEntity entity = mListMediaEntity.get(i);
                if(entity == null || entity._id < 0)
                    continue;

                if(MediaLibrary.getInstance().removeMediaEntity(entity)){
                    successNum++;
                }
                if(bDeleteFile){
                    File file = new File(entity._data);
                    if(file.exists()){
                        file.delete();
                    }
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
