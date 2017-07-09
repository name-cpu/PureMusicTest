package com.example.kaizhiwei.puremusictest.api;

import com.example.kaizhiwei.puremusictest.bean.ActiveIndexBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumInfoBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistInfoBean;
import com.example.kaizhiwei.puremusictest.bean.DiyGeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaIndexBean;
import com.example.kaizhiwei.puremusictest.bean.SceneCategoryListBean;
import com.example.kaizhiwei.puremusictest.bean.ShowRedPointBean;
import com.example.kaizhiwei.puremusictest.bean.SongDetailInfoBean;
import com.example.kaizhiwei.puremusictest.bean.SugSceneBean;
import com.example.kaizhiwei.puremusictest.bean.UgcdiyBaseInfoBean;
import com.example.kaizhiwei.puremusictest.bean.UnStandardAdBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 24820 on 2017/6/27.
 */
public interface ApiService {

    @GET("/api/v2/unstandard")
    Observable<UnStandardAdBean> getUnStandardAd(@Query("ad_pos_id") int ad_pos_id,
                                                 @Query("width") int width,
                                                 @Query("height") int height,
                                                 @Query("from") String from,
                                                 @Query("product") String product,
                                                 @Query("version") String version,
                                                 @Query("cuid") String cuid,
                                                 @Query("channel") String channel,
                                                 @Query("operator") int operator);

    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.scene.getCategoryList
    @GET("/v1/restserver/ting")
    Observable<SceneCategoryListBean> getCategoryList(@Query("from") String from,
                                                      @Query("version") String version,
                                                      @Query("channel") String ppzs,
                                                      @Query("operator") int operator,
                                                      @Query("method") String method);

//    GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.active.index HTTP/1.1
//    Accept-Encoding: gzip
//    cuid: 85FB11BCF66936DA386C6AC9CA228F2C
//    deviceid: 869336027278629
//    Host: tingapi.ting.baidu.com
//    Connection: Keep-Alive
//    User-Agent: android_5.9.9.6;baiduyinyue
    @GET("/v1/restserver/ting")
    Observable<ActiveIndexBean> getActiveIndex(@Query("from") String from,
                                               @Query("version") String version,
                                               @Query("channel") String ppzs,
                                               @Query("operator") int operator,
                                               @Query("method") String method);

    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.active.showRedPoint&format=json&from=android&version=5.9.9.6
    @GET("/v1/restserver/ting")
    Observable<ShowRedPointBean> showRedPoint(@Query("from") String from,
                                              @Query("version") String version,
                                              @Query("channel") String ppzs,
                                              @Query("operator") int operator,
                                              @Query("method") String method,
                                              @Query("format") String format);

    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.scene.getSugScene HTTP/1.1
    @GET("/v1/restserver/ting")
    Observable<SugSceneBean> getSugScene(@Query("from") String from,
                                         @Query("version") String version,
                                         @Query("channel") String ppzs,
                                         @Query("operator") int operator,
                                         @Query("method") String method);

    //获取所有推荐列表
    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.plaza.index&cuid=85FB11BCF66936DA386C6AC9CA228F2C&focu_num=8
    @GET("/v1/restserver/ting")
    Observable<PlazaIndexBean> getPlazaIndex(@Query("from") String from,
                                             @Query("version") String version,
                                             @Query("channel") String ppzs,
                                             @Query("operator") int operator,
                                             @Query("method") String method,
                                             @Query("cuid") String cuid,
                                             @Query("focu_num") int focu_num);

    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.ugcdiy.getBaseInfo&param=Evu%2FIh%2BwWg%2Fl5ThHRV%2BaIcXGzBwb20xHQLpHwGDsR02OLU6tDMQbKMiU4Ywx4Yyj1d72ZsMALrBmSqLZJlOe0%2FXe915M%2BmiT%2BNtPSA65KA3Fi9BN65TpI6Wh3bzdnwx2d8WX%2F0yWjv4CItL9AUZiYQ%3D%3D&timestamp=1498657673&sign=21d5c3c3a61bb779647d462bfdb2834f
    @GET("/v1/restserver/ting")
    Observable<UgcdiyBaseInfoBean> getUgcdiyBaseInfo(@Query("from") String from,
                                                     @Query("version") String version,
                                                     @Query("channel") String ppzs,
                                                     @Query("operator") int operator,
                                                     @Query("method") String method,
                                                     @Query("param") String param,
                                                     @Query("timestamp") String timestamp,
                                                     @Query("sign") String sign);


    //获取单个歌单的信息
    //GET /v1/restserver/ting?format=json&from=webapp_music&method=baidu.ting.diy.gedanInfo&listid=365741240
    @GET("/v1/restserver/ting")
    Observable<DiyGeDanInfoBean> getDiyGeDanInfo(@Query("format") String format,
                                                 @Query("from") String from,
                                                 @Query("method") String method,
                                                 @Query("listid") int listid);

    //获取歌手列表
    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.artist.getList&format=json&offset=0&limit=48&order=1&area=0&sex=0
    @GET("/v1/restserver/ting")
    Observable<ArtistGetListBean> getArtistListInfo(@Query("from") String from,
                                                    @Query("version") String version,
                                                    @Query("channel") String channel,
                                                    @Query("operator") String operator,
                                                    @Query("method") String method,
                                                    @Query("format") String format,
                                                    @Query("offset") String offset,
                                                    @Query("limit") String limit,
                                                    @Query("order") String order,
                                                    @Query("area") String area,
                                                    @Query("sex") String sex);


    //获取歌手列表并带过滤功能
    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.artist.getList&format=json&offset=0&limit=48&order=1&area=0&sex=0
    @GET("/v1/restserver/ting")
    Observable<ArtistGetListBean> getArtistListInfoWithFilter(@Query("from") String from,
                                                    @Query("version") String version,
                                                    @Query("channel") String channel,
                                                    @Query("operator") String operator,
                                                    @Query("method") String method,
                                                    @Query("format") String format,
                                                    @Query("offset") String offset,
                                                    @Query("limit") String limit,
                                                    @Query("order") String order,
                                                    @Query("area") String area,
                                                    @Query("sex") String sex,
                                                    @Query("abc") String abc);

    //获取指定歌手的歌曲列表
    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.artist.getSongList&format=json&order=2&tinguid=83626&artistid=83626&offset=0&limits=50
    @GET("/v1/restserver/ting")
    Observable<ArtistGetSongListBean> getSongList(@Query("from") String from,
                                                  @Query("version") String version,
                                                  @Query("channel") String channel,
                                                  @Query("operator") int operator,
                                                  @Query("method") String method,
                                                  @Query("format") String format,
                                                  @Query("order") String order,
                                                  @Query("tinguid") String tinguid,
                                                  @Query("artistid") String artistid,
                                                  @Query("offset") int offset,
                                                  @Query("limits") int limits);

    //获取歌手本人信息
    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.artist.getinfo&format=json&tinguid=1105&artistid=1105
    @GET("/v1/restserver/ting")
    Observable<ArtistInfoBean> getArtistInfo(@Query("from") String from,
                                             @Query("version") String version,
                                             @Query("channel") String channel,
                                             @Query("operator") int operator,
                                             @Query("method") String method,
                                             @Query("format") String format,
                                             @Query("tinguid") String tinguid,
                                             @Query("artistid") String artistid);

    //获取歌手专辑列表
    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.artist.getAlbumList&format=json&order=1&tinguid=7994&offset=30&limits=30
    @GET("/v1/restserver/ting")
    Observable<ArtistAlbumListBean> getArtistAlbumList(@Query("from") String from,
                                                       @Query("version") String version,
                                                       @Query("channel") String channel,
                                                       @Query("operator") int operator,
                                                       @Query("method") String method,
                                                       @Query("format") String format,
                                                       @Query("order") String order,
                                                       @Query("tinguid") String tinguid,
                                                       @Query("offset") int offset,
                                                       @Query("limits") int limits);

    //获取专辑信息
    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.album.getAlbumInfo&format=json&album_id=541223882
    @GET("/v1/restserver/ting")
    Observable<ArtistAlbumInfoBean> getArtistAlbumInfo(@Query("from") String from,
                                                       @Query("version") String version,
                                                       @Query("channel") String channel,
                                                       @Query("operator") int operator,
                                                       @Query("method") String method,
                                                       @Query("format") String format,
                                                       @Query("album_id") String album_id);

    //获取歌曲信息
    //GET /v1/restserver/ting?from=android&version=5.9.9.6&channel=ppzs&operator=2&method=baidu.ting.song.getInfos&format=json&songid=7280177&ts=1499570522192&
    // e=%2Blok1Cpy4gCBBj6rXQ4QnXmjJ7U0WCkfwOIhDHWwvQY%3D&nw=2&ucf=1&res=1&l2p=0&lpb=&usup=1&lebo=0
    @GET("/v1/restserver/ting")
    Observable<SongDetailInfoBean> getSongInfo(@Query("from") String from,
                                               @Query("version") String version,
                                               @Query("channel") String channel,
                                               @Query("operator") int operator,
                                               @Query("method") String method,
                                               @Query("format") String format,
                                               @Query("songid") String songid,
                                               @Query("ts") long ts,
                                               @Query(value = "e" ,encoded = true) String e,
                                               @Query("nw") int nw,
                                               @Query("ucf") int ucf,
                                               @Query("res") int res,
                                               @Query("l2p") int l2p,
                                               @Query("lpb") String lpb,
                                               @Query("usup") int usup,
                                               @Query("lebo") int lebo);
}
