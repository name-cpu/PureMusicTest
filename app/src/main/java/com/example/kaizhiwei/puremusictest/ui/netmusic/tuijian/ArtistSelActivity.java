package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetArtistListInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.ArtistGetArtistListInfoPresenter;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by kaizhiwei on 17/6/29.
 */

public class ArtistSelActivity extends MyBaseActivity implements ArtistGetArtistListInfoContract.View, CommonTitleView.onTitleClickListener {
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

    @Bind(R.id.commonTitle)
    CommonTitleView commonTitle;

    private ArtistGetArtistListInfoContract.Presenter mPresenter;
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
        HotPagerAdapter adapter = new HotPagerAdapter();
        vpReMenArtist.setAdapter(adapter);
        initLineIndicator();
    }

    @Override
    public void onLeftBtnClicked() {
        finish();
    }

    @Override
    public void onRightBtnClicked() {

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
        mPresenter = new ArtistGetArtistListInfoPresenter(this);
        if(mPresenter != null){
            mPresenter.getArtistListInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, "2", "baidu.ting.artist.getList"
                    , PureMusicContant.FORMAT_JSON, "0", "48", "1", "0", "0");
        }
    }

    @Override
    public void initView() {
        commonTitle.setTitleViewListener(this);
        commonTitle.setRightBtnVisible(false);
        commonTitle.setTitleVisible(false);
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
            R.id.rlHanGuoNan,R.id.rlHanGuoNv, R.id.rlHanGuoTeam, R.id.rlJapanNan, R.id.rlJapanNv, R.id.rlJapanTeam, R.id.rlOther, R.id.tvMore})
    void onClick(View view){
        if(view.getId() == R.id.tvMore){
            Intent intent = new Intent(this, HotArtistActivity.class);
            startActivity(intent);
        }
        else{
            ArtistTagInfo tagInfo = (ArtistTagInfo)view.getTag();
            Intent intent = new Intent(this, ArtistArtistListActivity.class);
            intent.putExtra(ArtistArtistListActivity.BUNDLE_ARTISTTAGINFO, tagInfo);
            startActivity(intent);
        }
    }

    private class HotPagerAdapter extends PagerAdapter{

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
            RecyclerView recyclerView = new RecyclerView(ArtistSelActivity.this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(ArtistSelActivity.this, PAGE_ARTIST_NUM);
            recyclerView.setLayoutManager(gridLayoutManager);

            ArtistGetListBean bean = new ArtistGetListBean();
            List<ArtistGetListBean.ArtistBean> list = new ArrayList<>();
            bean.setArtist(list);
            int startPos = position*PAGE_ARTIST_NUM;
            for(int i = startPos;i < mArtistGetListBean.getArtist().size() && i < startPos+PAGE_ARTIST_NUM;i++){
                list.add(mArtistGetListBean.getArtist().get(i));
            }

            HotArtistAdapter adapter = new HotArtistAdapter(ArtistSelActivity.this, bean);
            recyclerView.setAdapter(adapter);
            container.addView(recyclerView);
            return recyclerView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
