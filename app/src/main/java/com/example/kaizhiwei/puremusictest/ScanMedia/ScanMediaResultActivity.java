package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.AsyncTask.AsyncTaskScanPath;
import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.MediaData.MediaScanHelper;
import com.example.kaizhiwei.puremusictest.R;
import com.hp.hpl.sparta.Text;

import java.util.HashMap;


/**
 * Created by kaizhiwei on 16/12/30.
 */
public class ScanMediaResultActivity extends BaseActivty implements AsyncTaskScanPath.ScanResultListener {
    private TextView tvScanResult;
    private TextView tvScanProgressing;
    private TextView tvFinish;
    private ProgressBar pbScanProgring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        tvScanResult = (TextView)this.findViewById(R.id.tvScanResult);
        tvScanProgressing = (TextView)this.findViewById(R.id.tvScanProgressing);
        pbScanProgring = (ProgressBar)this.findViewById(R.id.pbScanProgring);
        tvFinish = (TextView)this.findViewById(R.id.tvFinish);
        tvFinish.setVisibility(View.GONE);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pbScanProgring.setProgress(0);
        pbScanProgring.setMax(100);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MediaScanHelper.getInstance().addScanListener(this);
        MediaScanHelper.getInstance().scanFile(this,"");
    }

    @Override
    protected void onPause(){
        super.onPause();
        MediaScanHelper.getInstance().removeScanListener(this);
    }

    @Override
    public void onScanStart() {

    }

    @Override
    public void onScaning(int process, String strFilePath, boolean bAudioFile) {
        tvScanProgressing.setText(strFilePath);
        pbScanProgring.setProgress(process);
    }

    @Override
    public void onScanCompleted(HashMap<String, String> mapResult, int filterNum) {
        tvFinish.setVisibility(View.VISIBLE);
        tvScanResult.setText("扫描完毕共" + mapResult.size() + "首" + ",过滤" + filterNum + "首");
        pbScanProgring.setProgress(100);
    }
}
