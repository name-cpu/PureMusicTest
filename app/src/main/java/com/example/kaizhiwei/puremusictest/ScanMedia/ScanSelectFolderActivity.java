package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.R;
import com.r0adkll.slidr.Slidr;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kaizhiwei on 16/12/25.
 */
public class ScanSelectFolderActivity extends BaseActivty implements SelectFolderAdapter2.ISelectFolderAdapter2Listener, CompoundButton.OnCheckedChangeListener {
    private SelectFolderAdapter2 mAdapter;
    private ListView lvFolder;
    private TextView tvParentDir;
    private TextView tvCheckAll;
    private CheckBox ckCheckAll;
    private String mCurParentDir;
    private CrumbView crumbView;
    private static final String HIGNEST_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_folder);
        setTitle("自定义扫描");
        lvFolder = (ListView)this.findViewById(R.id.lvFolder);
        crumbView = (CrumbView)this.findViewById(R.id.crumbView);
        tvCheckAll = (TextView)this.findViewById(R.id.tvCheckAll);
        ckCheckAll = (CheckBox)this.findViewById(R.id.ckCheckAll);
        tvParentDir = (TextView)this.findViewById(R.id.tvParentDir);
        tvCheckAll.setOnClickListener(this);
        ckCheckAll.setOnClickListener(this);
        tvParentDir.setOnClickListener(this);
        ckCheckAll.setChecked(false);
        mCurParentDir = HIGNEST_DIR;
        initData();
        Slidr.attach(this);
    }

    private void initData(){
        resetChcekState();
        List<SelectFolderAdapter2.FolderItemData> listAdapterData = new ArrayList<>();
        File file = new File(mCurParentDir);
        if(file.exists() && file.isDirectory()){
            List<File> listSubFile = Arrays.asList(file.listFiles());
            for(int i = 0;i < listSubFile.size();i++){
                File subFile = listSubFile.get(i);
                if(subFile.isDirectory() == false)
                    continue;

                if(subFile.isHidden())
                    continue;

                SelectFolderAdapter2.FolderItemData itemData = new SelectFolderAdapter2.FolderItemData();
                itemData.strFolderName = subFile.getName();
                itemData.isSelecetd = false;
                listAdapterData.add(itemData);
            }
        }

        //if(mAdapter == null){
            mAdapter = new SelectFolderAdapter2(this, listAdapterData);
            mAdapter.setListener(this);
        //}
        //mAdapter.setListData(listAdapterData);

        if(lvFolder != null){
            lvFolder.setAdapter(mAdapter);
            lvFolder.invalidate();
        }
        crumbView.setCrumbViewData( Arrays.asList(mCurParentDir.split(File.separator)));
    }

    @Override
    public void onFolderItemClick(SelectFolderAdapter2 adapter, int position) {
        if(adapter == null || position < 0)
            return;

        SelectFolderAdapter2.FolderItemData itemData = (SelectFolderAdapter2.FolderItemData)adapter.getItem(position);
        if(itemData != null){
            mCurParentDir = mCurParentDir + File.separator + itemData.strFolderName;
            initData();
        }
    }

    private void resetChcekState(){
        if(ckCheckAll != null && tvCheckAll != null){
            ckCheckAll.setChecked(false);
            tvCheckAll.setText("全选");
        }
    }

    @Override
    public void onItemChecked(SelectFolderAdapter2 adapter, int position, boolean isChecked) {
        if(adapter == null || position < 0)
            return;

        List<Integer> listItems = adapter.getCheckedItems();
        if(listItems.size() == adapter.getCount()){
            ckCheckAll.setChecked(true);
            tvCheckAll.setText("取消全选");
        }
        else{
            resetChcekState();
        }
    }

    public void onBackPressed() {
        if(mCurParentDir.equals(HIGNEST_DIR) == false){
            int index = mCurParentDir.lastIndexOf(File.separator);
            mCurParentDir = mCurParentDir.substring(0, index);
            initData();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if(tvCheckAll == v){
            if(ckCheckAll.isChecked()){
                ckCheckAll.setChecked(false);
            }
            else{
                ckCheckAll.setChecked(true);
            }
            checkAllDir();
        }
        else if(ckCheckAll == v){
            boolean isChecked = ckCheckAll.isChecked();
            checkAllDir();
        }
        else if(tvParentDir == v){
            if(mCurParentDir.equals(HIGNEST_DIR) == false){
                int index = mCurParentDir.lastIndexOf(File.separator);
                mCurParentDir = mCurParentDir.substring(0, index);
                initData();
            }
        }
        super.onClick(v);
    }

    private void checkAllDir(){
        if(ckCheckAll.isChecked()){
            mAdapter.setAllChcked(true);
            tvCheckAll.setText("取消全选");
        }
        else{
            mAdapter.setAllChcked(false);
            tvCheckAll.setText("全选");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkAllDir();
    }
}
