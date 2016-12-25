package com.example.kaizhiwei.puremusictest.ScanMedia;

/**
 * Created by kaizhiwei on 16/12/25.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import java.util.List;

public class ScanMediaFilterSetActivity extends BaseActivty implements SelectFolderDialog.ISelectFolderDialogListener {
    private CheckBox ckFolderLessthan60;
    private MyTextView tvSetFilterFolder;
    private MyTextView tvOK;
    private MyTextView tvCancel123;
    private SelectFolderDialog mSelectFolderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_filter);

        ckFolderLessthan60 = (CheckBox)findViewById(R.id.ckFolderLessthan60);
        tvSetFilterFolder = (MyTextView)findViewById(R.id.tvSetFilterFolder);
        tvOK = (MyTextView)findViewById(R.id.tvOK);
        tvCancel123 = (MyTextView)findViewById(R.id.tvCancel123);
        tvSetFilterFolder.setOnClickListener(this);
        tvOK.setOnClickListener(this);
        tvCancel123.setOnClickListener(this);
        setTitle("扫描设置");
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

            mSelectFolderDialog.setMediaEntityData(MediaLibrary.getInstance().getAllMediaEntrty());
            mSelectFolderDialog.show();
            mSelectFolderDialog.setTitle("音乐目录过滤");
        }
        super.onClick(v);
    }

    @Override
    public void onSelectFolderFinish(SelectFolderDialog dialog, int tag) {
        if(dialog == null)
            return;

        List<String> ss = dialog.getFilterFolders();
    }
}
