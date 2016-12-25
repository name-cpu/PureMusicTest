package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.R;

public class ScanMediaActivity extends BaseActivty{
    private TextView tvFilterCondition;
    private TextView tvSelectFolder;
    private TextView tvScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_media);
        tvFilterCondition = (TextView)this.findViewById(R.id.tvFilterCondition);
        tvSelectFolder = (TextView)this.findViewById(R.id.tvSelectFolder);
        tvScan = (TextView)this.findViewById(R.id.tvScan);
        tvFilterCondition.setOnClickListener(this);
        tvSelectFolder.setOnClickListener(this);
        tvScan.setOnClickListener(this);

        setTitle("扫描歌曲");
    }

    @Override
    public void onClick(View v) {
        if(v == tvScan){

        }
        else if(v == tvSelectFolder){
            Intent intent = new Intent(ScanMediaActivity.this, ScanSelectFolderActivity.class);
            startActivity(intent);
        }
        else if(v == tvFilterCondition){
            Intent intent = new Intent(ScanMediaActivity.this, ScanMediaFilterSetActivity.class);
            startActivity(intent);
        }
        super.onClick(v);
    }
}
