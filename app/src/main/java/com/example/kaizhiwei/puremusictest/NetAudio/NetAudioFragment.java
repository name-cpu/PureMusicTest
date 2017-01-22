package com.example.kaizhiwei.puremusictest.NetAudio;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.kaizhiwei.puremusictest.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.kaizhiwei.puremusictest.NetAudio.Entity.*;

public class NetAudioFragment extends Fragment {
    private Handler handler = new Handler();
    private Button btn;
    private GridView gvCatogary;
    private GridView gvDiy;
    private GridView gvMix;
    private LinearLayout llMain;
    private Handler mHandler = new Handler();
    private float density;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_netaudio, null);
        btn = (Button)rootView.findViewById(R.id.btn);
        density = this.getResources().getDisplayMetrics().density;
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String strUrl = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.8.1&channel=1426d&operator=2&method=baidu.ting.plaza.index&cuid=C28BFA1D8C3FC9010DFB8F7576EABF12&focu_num=8";

//                        Request request = new Request.Builder().url(strUrl)
//                                .get()
//                                .addHeader("Accept-Encoding", "gzip")
//                                .addHeader("cuid", "C28BFA1D8C3FC9010DFB8F7576EABF12")
//                                .addHeader("deviceid", "869804025132064")
//                                .removeHeader("User-Agent")
//                                .addHeader("User-Agent", "android_5.9.8.1;baiduyinyue")
//                                .build();
//
//                        Response response = null;
//                        try {
//                            response = mOkHttpClient.newCall(request).execute();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        if(response != null && response.isSuccessful()){
//                            Log.e("weikaizhi", response.body().toString());
//                        }

//                        HttpGet httpGet = new HttpGet(strUrl);
//                        httpGet.addHeader("Accept-Encoding", "gzip");
//                        httpGet.addHeader("cuid", "C28BFA1D8C3FC9010DFB8F7576EABF12");
//                        httpGet.addHeader("deviceid", "869804025132064");
//                        httpGet.addHeader("User-Agent", "android_5.9.8.1;baiduyinyue");
//
//                        HttpClient httpClient = new DefaultHttpClient();
//                        try {
//                            HttpResponse response = httpClient.execute(httpGet);
//                            Header[] header  = response.getAllHeaders();
//                            for(int i = 0;i < header.length;i++){
//                                Log.i("weikaizhi", header[i].toString());
//                            }
//                            HttpEntity entity = response.getEntity();
//                            if(entity != null){
//                                entity = new BufferedHttpEntity(entity);
//                                InputStream in = entity.getContent();
//                                byte[] read = new byte[1024];
//                                byte[] all = new byte[];
//                                int num = 0;
//                                while((num = in.read(read)) > 0){
//                                    byte[] temp = new byte[all.length + num];
//                                    System.arraycopy(all, 0, temp, 0, all.length);
//                                    System.arraycopy(read, 0, temp, all.length, num);
//                                    all = temp;
//                                }
//
//                                String str = new String(all, "UTF-8");
//                                if(in != null){
//                                    in.close();
//                                }
//                            }
//                            Log.i("weikaizh",  response.getEntity().getContentLength() + "");
//                            Log.i("weikaizh",  response.getEntity().getContent().toString());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        try{
                            InputStream in = getResources().openRawResource(R.raw.a);
                            int length = in.available();
                            byte[] buffer = new byte[length];
                            in.read(buffer);
                            String str = new String(buffer);// EncodingUtils.getString(buffer, "UTF-8");
                            final NetMusicData data = new NetMusicData();
                            data.parser(str);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    List<GridViewAdapter.GridViewAdapterItemData> listTem = new ArrayList<GridViewAdapter.GridViewAdapterItemData>();
                                    for(int i = 0;i < data.mEntity.listEntityItem.size();i++){
                                        GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
                                        itemData.desc = data.mEntity.listEntityItem.get(i).title;
                                        itemData.strIconUrl = data.mEntity.listEntityItem.get(i).icon;
                                        itemData.strkey = data.mEntity.listEntityItem.get(i).title;
                                        listTem.add(itemData);
                                    }

                                    GridViewAdapter adapter = new GridViewAdapter(NetAudioFragment.this.getActivity(), listTem, R.layout.fragment_netaduio_catogary_item);
                                    gvCatogary.setAdapter(adapter);

                                    listTem = new ArrayList<GridViewAdapter.GridViewAdapterItemData>();
                                    for(int i = 0; i < data.mDiy.listDiyItem.size();i++){
                                        GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
                                        itemData.desc = data.mDiy.listDiyItem.get(i).title;
                                        itemData.strIconUrl = data.mDiy.listDiyItem.get(i).pic;
                                        itemData.strkey = data.mDiy.listDiyItem.get(i).listid;
                                        listTem.add(itemData);
                                    }
                                    adapter = new GridViewAdapter(NetAudioFragment.this.getActivity(), listTem, R.layout.fragment_net_audio_recommand_item);
                                    gvDiy.setAdapter(adapter);


                                    for(int i = 0; i < data.mListMix.size();i++){
                                        if(i == 0){
                                            listTem = new ArrayList<GridViewAdapter.GridViewAdapterItemData>();
                                            for(int j = 0;j < data.mListMix.get(i).listMix.size();j++){
                                                GridViewAdapter.GridViewAdapterItemData itemData = new GridViewAdapter.GridViewAdapterItemData();
                                                itemData.desc = data.mListMix.get(i).listMix.get(j).title;
                                                itemData.strIconUrl = data.mListMix.get(i).listMix.get(j).pic;
                                                itemData.strkey = data.mListMix.get(i).listMix.get(j).type_id;
                                                listTem.add(itemData);
                                            }
                                            adapter = new GridViewAdapter(NetAudioFragment.this.getActivity(), listTem, R.layout.fragment_net_audio_recommand_item);
                                            gvMix.setAdapter(adapter);
                                        }

                                    }

                                }
                            });
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
//        lycView = (LycView)this.findViewById(R.id.lycView);
//
//        String strFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "test.lrc";
//        pasrser = new LycParser(strFilePath);
//        lycView.setLycParser(pasrser);
//
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                curTime += 1000;
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int line = pasrser.getCurrentLine(curTime);
//                        lycView.setCurPosition(line);
//                    }
//                });
//            }
//        };
//
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

        //     mOkHttpClient = new OkHttpClient();

        llMain = (LinearLayout)rootView.findViewById(R.id.llMain);
        gvCatogary = (GridView)rootView.findViewById(R.id.gvCatogary);
        gvDiy = (GridView)rootView.findViewById(R.id.gvDiy);
        gvMix = (GridView)rootView.findViewById(R.id.gvMix);
        return rootView;
    }
}
