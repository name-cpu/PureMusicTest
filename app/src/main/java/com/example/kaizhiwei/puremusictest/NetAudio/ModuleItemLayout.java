package com.example.kaizhiwei.puremusictest.NetAudio;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.NetAudio.Entity.NetPlazaIndexData;
import com.example.kaizhiwei.puremusictest.R;

/**
 * Created by 24820 on 2017/1/23.
 */
public class ModuleItemLayout extends LinearLayout{
    private ImageView imModuleLogo;
    private TextView tvModuleName;
    private TextView tvTitleMore;
    private AutoHeightGridView gvModule;

    public ModuleItemLayout(Context context) {
        super(context);
        initUI(context);
    }

    public ModuleItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
    }

    public ModuleItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
    }

    public void setModuleInfo(NetPlazaIndexData.ModuleItem moduleItem){
        if(moduleItem == null)
            return;

        Glide.with(HomeActivity.getInstance()).load(moduleItem.picurl).into(imModuleLogo);
        tvModuleName.setText(moduleItem.title);
        tvTitleMore.setText(moduleItem.title_more);
    }

    public void setGridViewAdapter(GridViewAdapter adapter){
        gvModule.setAdapter(adapter);
    }
}
