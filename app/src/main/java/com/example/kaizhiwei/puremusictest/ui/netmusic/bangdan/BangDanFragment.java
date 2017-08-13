package com.example.kaizhiwei.puremusictest.ui.netmusic.bangdan;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.bean.BangDanListBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.BangDanListContract;
import com.example.kaizhiwei.puremusictest.presenter.BangDanListPresenter;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class BangDanFragment extends MyBaseFragment implements BangDanListContract.View{
    private BangDanListPresenter mPresenter;
    private BangDanListBean mBangDanListBean;
    private BangDanAdapter mAdapter;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_bangdan;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if(mPresenter != null && isVisible){
            mPresenter.getBangDanList(PureMusicContant.FORMAT_JSON, PureMusicContant.DEVICE_TYPE, "baidu.ting.billboard.billCategory", 1);
        }
        isFirst = true;
    }

    @Override
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDividerDecoration(this.getActivity(), RecyclerViewDividerDecoration.HORIZONTAL_LIST));
        mAdapter = new BangDanAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mPresenter = new BangDanListPresenter(this);
    }

    @Override
    public void onError(String strErrMsg) {
        showToast(strErrMsg);
    }

    @Override
    public void onGetBangDanListSuccess(BangDanListBean bean) {
        mBangDanListBean = bean;
        mAdapter.notifyDataSetChanged();
    }

    private class BangDanAdapter extends RecyclerView.Adapter<BangDanViewHolder>{

        @Override
        public BangDanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BangDanFragment.this.getActivity()).inflate(R.layout.item_bangdan, parent, false);
            return new BangDanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BangDanViewHolder holder, final int position) {
            final BangDanListBean.ContentBeanX songListInfo = mBangDanListBean.getContent().get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BangDanFragment.this.getActivity(), BangDanSongListActivity.class);
                    intent.putExtra(BangDanSongListActivity.INTENT_BANGDANINFO, songListInfo);
                    BangDanFragment.this.getActivity().startActivity(intent);
                }
            });
            Glide.with(BangDanFragment.this.getActivity()).load(songListInfo.getPic_s192()).into(holder.ivBangDan);

            List<TextView> list = new ArrayList<>();
            list.add(holder.tvFirst);
            list.add(holder.tvSecond);
            list.add(holder.tvThird);
            for(int i = 0;i < songListInfo.getContent().size() && i < list.size();i++){
                String str = (i+1) + " " + songListInfo.getContent().get(i).getTitle() + " - " + songListInfo.getContent().get(i).getAuthor();
                list.get(i).setText(str);
            }
        }

        @Override
        public int getItemCount() {
            if(mBangDanListBean == null || mBangDanListBean.getContent() == null)
                return 0;

            return mBangDanListBean.getContent().size();
        }
    }

    private class BangDanViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivBangDan;
        public TextView tvFirst;
        public TextView tvSecond;
        public TextView tvThird;

        public BangDanViewHolder(View itemView) {
            super(itemView);
            ivBangDan = (ImageView) itemView.findViewById(R.id.ivBangDan);
            tvFirst = (TextView)itemView.findViewById(R.id.tvFirst);
            tvSecond = (TextView)itemView.findViewById(R.id.tvSecond);
            tvThird = (TextView)itemView.findViewById(R.id.tvThird);

            Typeface typeFace = Typeface.createFromAsset(BangDanFragment.this.getActivity().getAssets(),"arial.ttf");
            tvFirst.setTypeface(typeFace);
            tvThird.setTypeface(typeFace);
            tvSecond.setTypeface(typeFace);
        }
    }
}
