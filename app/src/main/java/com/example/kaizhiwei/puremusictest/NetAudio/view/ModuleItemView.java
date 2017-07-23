package com.example.kaizhiwei.puremusictest.NetAudio.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.NetAudio.AutoHeightGridView;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by 24820 on 2017/1/23.
 */
public class ModuleItemView extends LinearLayout{
    private ImageView imModuleLogo;
    private TextView tvModuleName;
    private TextView tvTitleMore;
    private LinearLayout llTitle;
    private AutoHeightGridView gvModule;

    public ModuleItemView(Context context) {
        super(context);
        initUI(context);
    }

    public ModuleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
    }

    public ModuleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(context);
    }

    private void initUI(Context context){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_netaduio_module_item, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(view, params);

        imModuleLogo = (ImageView)view.findViewById(R.id.imModuleLogo);
        tvModuleName = (TextView)view.findViewById(R.id.tvModuleName);
        tvTitleMore = (TextView)view.findViewById(R.id.tvTitleMore);
        gvModule = (AutoHeightGridView)view.findViewById(R.id.gvModule);
        llTitle = (LinearLayout)view.findViewById(R.id.llTitle);
    }

    public void setNumColumns(int column){
        if (gvModule != null){
            gvModule.setNumColumns(column);
        }
    }

    public void setModuleInfo(String strModulePic, String strMobuleTitle, String strModeulMore){
        Glide.with(HomeActivity.getInstance()).load(strModulePic).into(imModuleLogo);
        tvModuleName.setText(strMobuleTitle);
        if(TextUtils.isEmpty(strModeulMore)){
            llTitle.setVisibility(View.GONE);
        }
        else{
            llTitle.setVisibility(View.VISIBLE);
            tvTitleMore.setText(strModeulMore);
        }
    }

    public void setGridViewAdapter(ModuleItemAdapter adapter){
        gvModule.setAdapter(adapter);
    }
}
