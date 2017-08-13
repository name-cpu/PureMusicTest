package com.example.kaizhiwei.puremusictest.PlayingDetail;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.util.StringUtil;

/**
 * Created by 24820 on 2016/12/14.
 */
public class AlterDialogMediaInfo extends AlertDialog implements View.OnClickListener{
    private TextView btnOK;
    private TextView tvMediaName;
    private TextView tvArtist;
    private TextView tvAlbum;
    private TextView tvFilePath;
    private TextView tvFileFormat;
    private TextView tvFileSize;
    private MediaEntity mediaEntity;

    public AlterDialogMediaInfo(Context context) {
        super(context);
    }

    protected AlterDialogMediaInfo(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog_mediainfo);
        tvMediaName = (TextView) this.findViewById(R.id.tvMediaName);
        tvArtist = (TextView) this.findViewById(R.id.tvArtist);
        tvAlbum = (TextView) this.findViewById(R.id.tvAlbum);
        tvFilePath = (TextView) this.findViewById(R.id.tvFilePath);
        tvFileFormat = (TextView) this.findViewById(R.id.tvFileFormat);
        tvFileSize = (TextView) this.findViewById(R.id.tvFileSize);
        btnOK = (TextView)this.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(this);
    }

    public void setContentView(){

    }

    public void setMediaEntity(MediaEntity mediaEntity){
        this.mediaEntity = mediaEntity;
        initUi();
    }

    private void initUi(){
        if(mediaEntity != null && tvMediaName != null){
            tvMediaName.setText(mediaEntity.title);
            tvArtist.setText(mediaEntity.artist);
            tvAlbum.setText(mediaEntity.album);
            tvFilePath.setText(mediaEntity._data);
            tvFileFormat.setText(mediaEntity.bitrate + "Kbps");
            tvFileSize.setText(StringUtil.getDataSize(mediaEntity._size));
        }
    }

    public void show(){
        super.show();
    }

    @Override
    public void onClick(View v) {
        if(v == btnOK){
            dismiss();
        }
    }
}
