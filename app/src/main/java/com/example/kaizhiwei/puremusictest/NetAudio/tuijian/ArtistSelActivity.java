package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.ActiveIndexBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.bean.DiyGeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaIndexBean;
import com.example.kaizhiwei.puremusictest.bean.SceneCategoryListBean;
import com.example.kaizhiwei.puremusictest.bean.ShowRedPointBean;
import com.example.kaizhiwei.puremusictest.bean.SugSceneBean;
import com.example.kaizhiwei.puremusictest.bean.UgcdiyBaseInfoBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.ResetServerContract;
import com.example.kaizhiwei.puremusictest.presenter.ResetServerPresenter;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by kaizhiwei on 17/6/29.
 */

public class ArtistSelActivity extends MyBaseActivity implements ResetServerContract.View{
    @Bind(R.id.tvMore)
    TextView tvMore;

    @Bind(R.id.rlHuaYuNan)
    RelativeLayout rlHuaYuNan;

    @Bind(R.id.rlHuaYuNv)
    RelativeLayout rlHuaYuNv;

    @Bind(R.id.rlHuaYuTeam)
    RelativeLayout rlHuaYuTeam;

    @Bind(R.id.rlOuMeiNan)
    RelativeLayout rlOuMeiNan;

    @Bind(R.id.rlOuMeiNv)
    RelativeLayout rlOuMeiNv;

    @Bind(R.id.rlOuMeiTeam)
    RelativeLayout rlOuMeiTeam;

    @Bind(R.id.rlHanGuoNan)
    RelativeLayout rlHanGuoNan;

    @Bind(R.id.rlHanGuoNv)
    RelativeLayout rlHanGuoNv;

    @Bind(R.id.rlHanGuoTeam)
    RelativeLayout rlHanGuoTeam;

    @Bind(R.id.rlJapanNan)
    RelativeLayout rlJapanNan;

    @Bind(R.id.rlJapanNv)
    RelativeLayout rlJapanNv;

    @Bind(R.id.rlJapanTeam)
    RelativeLayout rlJapanTeam;

    @Bind(R.id.rlOther)
    RelativeLayout rlOther;

    @Bind(R.id.vpReMenArtist)
    ViewPager vpReMenArtist;

    @Bind(R.id.linePageIndicator)
    LinePageIndicator linePageIndicator;

    private ResetServerContract.Presenter mPresenter;
    private ArtistGetListBean mArtistGetListBean;
    private List<GridView> mListGridViews;
    private static final int PAGE_ARTIST_NUM = 3;
    private static final int MAX_REMEN_NUM = 12;
    private LayoutInflater layoutInflater;


    @Override
    public void onError(String strErrMsg) {
        showToast(strErrMsg);
    }

    @Override
    public void onGetCatogaryListSuccess(SceneCategoryListBean bean) {

    }

    @Override
    public void onGetActiveIndexSuccess(ActiveIndexBean bean) {

    }

    @Override
    public void onShowRedPointSuccess(ShowRedPointBean bean) {

    }

    @Override
    public void onGetSugSceneSuccess(SugSceneBean bean) {

    }

    @Override
    public void onGetPlazaIndexSuccess(PlazaIndexBean bean) {

    }

    @Override
    public void onGetUgcdiyBaseInfoSuccess(UgcdiyBaseInfoBean baseInfoBean) {

    }

    @Override
    public void onGetDiyGeDanInfoSuccess(DiyGeDanInfoBean bean) {

    }

    @Override
    public void onGetArtistListInfoSuccess(ArtistGetListBean bean) {
        if(bean == null || bean.getArtist() == null){
            showToast("data error");
            return;
        }

        if(bean.getArtist().size() > MAX_REMEN_NUM){
            for(int i = MAX_REMEN_NUM; i < bean.getArtist().size();i++){
                bean.getArtist().remove(i);
                i--;
            }
        }
        mArtistGetListBean = bean;
        ReMenAdapter adapter = new ReMenAdapter();
        vpReMenArtist.setAdapter(adapter);
        initLineIndicator();
    }

    static public class ArtistTagInfo implements Parcelable {
        public int area;
        public int sex;
        public String title;

        public ArtistTagInfo() {
        }

        protected ArtistTagInfo(Parcel in) {
            area = in.readInt();
            sex = in.readInt();
            title = in.readString();
        }

        public static final Creator<ArtistTagInfo> CREATOR = new Creator<ArtistTagInfo>() {
            @Override
            public ArtistTagInfo createFromParcel(Parcel in) {
                return new ArtistTagInfo(in);
            }

            @Override
            public ArtistTagInfo[] newArray(int size) {
                return new ArtistTagInfo[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(area);
            dest.writeInt(sex);
            dest.writeString(title);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_artist_sel;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ResetServerPresenter(this);
        if(mPresenter != null){
            mPresenter.getArtistListInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, "2", "baidu.ting.artist.getList"
                    , PureMusicContant.FORMAT_JSON, "0", "48", "1", "0", "0");
        }
    }

    @Override
    public void initView() {

    }

    private void initLineIndicator(){
        linePageIndicator.setViewPager(vpReMenArtist);
        final float density = getResources().getDisplayMetrics().density;
        linePageIndicator.setSelectedColor(getResources().getColor(R.color.common_title_backgroundColor));
        linePageIndicator.setUnselectedColor(getResources().getColor(R.color.lightgray));
        linePageIndicator.setStrokeWidth(6 * density);
        linePageIndicator.setLineWidth(8 * density);
        linePageIndicator.setCurrentItem(0);
    }

    @Override
    public void initData(){

        mListGridViews = new ArrayList<>();

        layoutInflater = LayoutInflater.from(this);

        ArtistTagInfo tagInfo = new ArtistTagInfo();
        tagInfo.area = 6;
        tagInfo.sex = 1;
        tagInfo.title = getString(R.string.HuaYuNan);
        rlHuaYuNan.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 6;
        tagInfo.sex = 2;
        tagInfo.title = getString(R.string.HuaYuNv);
        rlHuaYuNv.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 6;
        tagInfo.sex = 3;
        tagInfo.title = getString(R.string.HuaYuTeam);
        rlHuaYuTeam.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 3;
        tagInfo.sex = 1;
        tagInfo.title = getString(R.string.OuMeiNan);
        rlOuMeiNan.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 3;
        tagInfo.sex = 2;
        tagInfo.title = getString(R.string.OuMeiNv);
        rlOuMeiNv.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 3;
        tagInfo.sex = 3;
        tagInfo.title = getString(R.string.OuMeiTeam);
        rlOuMeiTeam.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 7;
        tagInfo.sex = 1;
        tagInfo.title = getString(R.string.HanGuoNan);
        rlHanGuoNan.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 7;
        tagInfo.sex = 2;
        tagInfo.title = getString(R.string.HanGuoNv);
        rlHanGuoNv.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 7;
        tagInfo.sex = 3;
        tagInfo.title = getString(R.string.HanGuoTeam);
        rlHanGuoTeam.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 60;
        tagInfo.area = 1;
        tagInfo.title = getString(R.string.JapanNan);
        rlJapanNan.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 60;
        tagInfo.sex = 2;
        tagInfo.title = getString(R.string.JapanNv);
        rlJapanNv.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 60;
        tagInfo.sex = 3;
        tagInfo.title = getString(R.string.JapanTeam);
        rlJapanTeam.setTag(tagInfo);

        tagInfo = new ArtistTagInfo();
        tagInfo.area = 5;
        tagInfo.sex = 0;
        tagInfo.title = getString(R.string.Other);
        rlOther.setTag(tagInfo);
    }

    @OnClick({R.id.rlHuaYuNan, R.id.rlHuaYuNv, R.id.rlHuaYuTeam,R.id.rlOuMeiNan,R.id.rlOuMeiNv,R.id.rlOuMeiTeam,
            R.id.rlHanGuoNan,R.id.rlHanGuoNv, R.id.rlHanGuoTeam, R.id.rlJapanNan, R.id.rlJapanNv, R.id.rlJapanTeam, R.id.rlOther})
    void onClick(View view){
        ArtistTagInfo tagInfo = (ArtistTagInfo)view.getTag();
        Intent intent = new Intent(this, ArtistArtistListActivity.class);
        intent.putExtra(ArtistArtistListActivity.BUNDLE_ARTISTTAGINFO, tagInfo);
        startActivity(intent);
    }


    private class ReMenAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            int count = 0;
            if(mArtistGetListBean.getArtist().size()%PAGE_ARTIST_NUM == 0){
                count = mArtistGetListBean.getArtist().size()/PAGE_ARTIST_NUM;
            }
            else{
                count = mArtistGetListBean.getArtist().size()/PAGE_ARTIST_NUM + 1;
            }
            return count;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = new GridView(ArtistSelActivity.this);
            ReMenGridViewAdapter adapter = new ReMenGridViewAdapter(position);
            gridView.setAdapter(adapter);
            gridView.setNumColumns(PAGE_ARTIST_NUM);
            container.addView(gridView);
            return gridView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class ReMenGridViewAdapter extends BaseAdapter{
        private int mIndex;

        public ReMenGridViewAdapter(int index){
            mIndex = index;
        }

        @Override
        public int getCount() {
            int count = 0;
            int falg = (mArtistGetListBean.getArtist().size() - mIndex * PAGE_ARTIST_NUM)/PAGE_ARTIST_NUM;
            if(falg > 0){
                count = PAGE_ARTIST_NUM;
            }
            else{
                count = mArtistGetListBean.getArtist().size() - mIndex * PAGE_ARTIST_NUM;
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            return mArtistGetListBean.getArtist().get(mIndex*PAGE_ARTIST_NUM+position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                View view = layoutInflater.inflate(R.layout.item_artistsel_remen, parent, false);
                convertView = view;
                holder = new ViewHolder(view);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }

            ArtistGetListBean.ArtistBean artistBean = (ArtistGetListBean.ArtistBean)getItem(position);
            Glide.with(ArtistSelActivity.this).load(artistBean.getAvatar_middle()).placeholder(R.drawable.default_live_ic).into(holder.ivReMenArtistPic);
            holder.tvReMenArtistName.setText(artistBean.getName());
            return convertView;
        }

        private class ViewHolder{
            private ImageView ivReMenArtistPic;
            private TextView tvReMenArtistName;

            public ViewHolder(View view){
                ivReMenArtistPic = (ImageView)view.findViewById(R.id.ivReMenArtistPic);
                tvReMenArtistName = (TextView)view.findViewById(R.id.tvReMenArtistName);
            }
        }
    }
}
