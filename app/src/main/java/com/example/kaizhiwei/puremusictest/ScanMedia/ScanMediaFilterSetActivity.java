package com.example.kaizhiwei.puremusictest.ScanMedia;

/**
 * Created by kaizhiwei on 16/12/25.
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScanMediaFilterSetActivity extends BaseActivty implements SelectFolderDialog.ISelectFolderDialogListener {
    private CheckBox ckFolderLessthan60;
    private TextView tvFilterFOlderName;
    private MyTextView tvSetFilterFolder;
    private MyTextView tvOK;
    private MyTextView tvCancel123;
    private SelectFolderDialog mSelectFolderDialog;
    private List<String> mListFilterFoler = new ArrayList<>();

    public static final int RETURN_CODE = 101;
    public static final String FILDTER_LESS_THAN60 = "FILDTER_LESS_THAN60";
    public static final String FILTER_FOLDER = "FILTER_FOLDER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_filter);

        ckFolderLessthan60 = (CheckBox)findViewById(R.id.ckFolderLessthan60);
        tvSetFilterFolder = (MyTextView)findViewById(R.id.tvSetFilterFolder);
        tvFilterFOlderName = (TextView)findViewById(R.id.tvFilterFOlderName);
        tvOK = (MyTextView)findViewById(R.id.tvOK);
        tvCancel123 = (MyTextView)findViewById(R.id.tvCancel123);
        tvSetFilterFolder.setOnClickListener(this);
        tvOK.setOnClickListener(this);
        tvCancel123.setOnClickListener(this);
        setTitle("扫描设置");

        ckFolderLessthan60.setChecked(PreferenceConfig.getInstance().getScanFilterByDuration());
        mListFilterFoler = PreferenceConfig.getInstance().getScanFilterByFolderName();
        updateFilterFolderTextView();
    }

    @Override
    public void onDestory(){
        super.onDestroy();
        if(mSelectFolderDialog != null){
            mSelectFolderDialog.unregisterListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == tvSetFilterFolder){
            if(mSelectFolderDialog == null){
                SelectFolderDialog.Builder builderFavorite = new SelectFolderDialog.Builder(this);
                mSelectFolderDialog = builderFavorite.create();
                mSelectFolderDialog.setCancelable(true);
                mSelectFolderDialog.registerListener(this);
            }

            mSelectFolderDialog.setMediaEntityData(MediaLibrary.getInstance().getAllMediaEntrty(), mListFilterFoler);
            mSelectFolderDialog.show();
            mSelectFolderDialog.setTitle("音乐目录过滤");
        }
        else if(v == tvOK){
            Intent data = new Intent();
            data.putExtra(FILDTER_LESS_THAN60, ckFolderLessthan60.isChecked());
            data.putStringArrayListExtra(FILTER_FOLDER, (ArrayList<String>)mListFilterFoler);
            setResult(RETURN_CODE, data);
            PreferenceConfig.getInstance().setScanFilterByDuration(ckFolderLessthan60.isChecked());
            PreferenceConfig.getInstance().setScanFilterByFolderName(mListFilterFoler);
            finish();
        }
        else if(v == tvCancel123){
            finish();
        }
        super.onClick(v);
    }

    @Override
    public void onSelectFolderFinish(SelectFolderDialog dialog, int tag) {
        if(dialog == null)
            return;

        mListFilterFoler.clear();
        List<String> list = dialog.getFilterFolders();
        mListFilterFoler.addAll(list);
        updateFilterFolderTextView();
    }

    private void updateFilterFolderTextView(){
        String str = "";
        for (int i = 0;i < mListFilterFoler.size();i++) {
            str += mListFilterFoler.get(i);
            str += ";";
        }
        if(TextUtils.isEmpty(str))
            tvFilterFOlderName.setText("无");
        else
            tvFilterFOlderName.setText(str);
    }
}
