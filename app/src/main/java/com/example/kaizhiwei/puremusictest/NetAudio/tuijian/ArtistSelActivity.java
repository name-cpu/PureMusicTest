package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

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

    private ResetServerContract.Presenter mPresenter;
    private ArtistGetListBean mArtistGetListBean;
    private List<GridView> mListGridViews;
    private static final int PAGE_ARTIST_NUM = 3;
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
        if(bean == null){
            showToast("data error");
            return;
        }

        mArtistGetListBean = bean;
        ReMenAdapter adapter = new ReMenAdapter();
        vpReMenArtist.setAdapter(adapter);
    }

    private class TagInfo{
        int area;
        int sex;
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

    @Override
    public void initData(){

        mListGridViews = new ArrayList<>();

        layoutInflater = LayoutInflater.from(this);

        TagInfo tagInfo = new TagInfo();
        tagInfo.area = 6;
        tagInfo.sex = 1;
        rlHuaYuNan.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 6;
        tagInfo.sex = 2;
        rlHuaYuNv.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 6;
        tagInfo.sex = 3;
        rlHuaYuTeam.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 3;
        tagInfo.sex = 1;
        rlOuMeiNan.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 3;
        tagInfo.sex = 2;
        rlOuMeiNv.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 3;
        tagInfo.sex = 3;
        rlOuMeiTeam.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 7;
        tagInfo.sex = 1;
        rlHanGuoNan.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 7;
        tagInfo.sex = 2;
        rlHanGuoNv.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 7;
        tagInfo.sex = 3;
        rlHanGuoTeam.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 60;
        tagInfo.area = 1;
        rlJapanNan.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 60;
        tagInfo.sex = 2;
        rlJapanNv.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 60;
        tagInfo.sex = 3;
        rlJapanTeam.setTag(tagInfo);

        tagInfo = new TagInfo();
        tagInfo.area = 5;
        tagInfo.sex = 0;
        rlOther.setTag(tagInfo);
    }

    @OnClick({R.id.rlHuaYuNan, R.id.rlHuaYuNv, R.id.rlHuaYuTeam,R.id.rlOuMeiNan,R.id.rlOuMeiNv,R.id.rlOuMeiTeam,
            R.id.rlHanGuoNan,R.id.rlHanGuoNv, R.id.rlHanGuoTeam, R.id.rlJapanNan, R.id.rlJapanNv, R.id.rlJapanTeam, R.id.rlOther})
    void onClick(View view){
        TagInfo tagInfo = (TagInfo)view.getTag();
        mPresenter.getArtistListInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, "2", "baidu.ting.artist.getList"
        , PureMusicContant.FORMAT_JSON, "0", "48", "1", "" + tagInfo.area, "" + tagInfo.sex);
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
            Glide.with(ArtistSelActivity.this).load(artistBean.getAvatar_middle()).into(holder.ivReMenArtistPic);
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
