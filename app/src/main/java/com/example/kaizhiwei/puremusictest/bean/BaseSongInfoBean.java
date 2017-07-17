package com.example.kaizhiwei.puremusictest.bean;

import java.util.List;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class BaseSongInfoBean {
    /**
     * error_code : 22000
     * result : {"items":[{"artist_id":"786","avatar_small":"http://qukufile2.qianqian.com/data2/pic/246586431/246586431.jpg@s_0,w_48","avatar_big":"http://qukufile2.qianqian.com/data2/pic/246586431/246586431.jpg@s_0,w_240","all_artist_id":"786","album_no":"3","pic_big":"http://musicdata.baidu.com/data2/pic/88555531/88555531.jpg@s_0,w_150","pic_small":"http://musicdata.baidu.com/data2/pic/88555531/88555531.jpg@s_0,w_90","relate_status":"0","resource_type":"0","copy_type":"1","lrclink":"http://musicdata.baidu.com/data2/lrc/239127916/239127916.lrc","pic_radio":"http://musicdata.baidu.com/data2/pic/88555531/88555531.jpg@s_0,w_300","toneid":"0","all_rate":"64,128,256,320,flac","play_type":"","has_mv_mobile":1,"pic_premium":"http://musicdata.baidu.com/data2/pic/88555531/88555531.jpg@s_0,w_500","pic_huge":"","versions":"","bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}","song_id":"976984","title":"彩虹","ting_uid":"1389","author":"羽泉","album_id":"186782","album_title":"冷酷到底","is_first_publish":0,"havehigh":2,"charge":0,"has_mv":1,"learn":1,"song_source":"web","piao_id":"0","korean_bb_song":"0","resource_type_ext":"0","mv_provider":"0100000000","song_title":"彩虹","artist_name":"羽泉"}]}
     */

    private int error_code;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<ItemsBean> items;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * artist_id : 786
             * avatar_small : http://qukufile2.qianqian.com/data2/pic/246586431/246586431.jpg@s_0,w_48
             * avatar_big : http://qukufile2.qianqian.com/data2/pic/246586431/246586431.jpg@s_0,w_240
             * all_artist_id : 786
             * album_no : 3
             * pic_big : http://musicdata.baidu.com/data2/pic/88555531/88555531.jpg@s_0,w_150
             * pic_small : http://musicdata.baidu.com/data2/pic/88555531/88555531.jpg@s_0,w_90
             * relate_status : 0
             * resource_type : 0
             * copy_type : 1
             * lrclink : http://musicdata.baidu.com/data2/lrc/239127916/239127916.lrc
             * pic_radio : http://musicdata.baidu.com/data2/pic/88555531/88555531.jpg@s_0,w_300
             * toneid : 0
             * all_rate : 64,128,256,320,flac
             * play_type :
             * has_mv_mobile : 1
             * pic_premium : http://musicdata.baidu.com/data2/pic/88555531/88555531.jpg@s_0,w_500
             * pic_huge :
             * versions :
             * bitrate_fee : {"0":"0|0","1":"0|0"}
             * song_id : 976984
             * title : 彩虹
             * ting_uid : 1389
             * author : 羽泉
             * album_id : 186782
             * album_title : 冷酷到底
             * is_first_publish : 0
             * havehigh : 2
             * charge : 0
             * has_mv : 1
             * learn : 1
             * song_source : web
             * piao_id : 0
             * korean_bb_song : 0
             * resource_type_ext : 0
             * mv_provider : 0100000000
             * song_title : 彩虹
             * artist_name : 羽泉
             */

            private String artist_id;
            private String avatar_small;
            private String avatar_big;
            private String all_artist_id;
            private String album_no;
            private String pic_big;
            private String pic_small;
            private String relate_status;
            private String resource_type;
            private String copy_type;
            private String lrclink;
            private String pic_radio;
            private String toneid;
            private String all_rate;
            private String play_type;
            private int has_mv_mobile;
            private String pic_premium;
            private String pic_huge;
            private String versions;
            private String bitrate_fee;
            private String song_id;
            private String title;
            private String ting_uid;
            private String author;
            private String album_id;
            private String album_title;
            private int is_first_publish;
            private int havehigh;
            private int charge;
            private int has_mv;
            private int learn;
            private String song_source;
            private String piao_id;
            private String korean_bb_song;
            private String resource_type_ext;
            private String mv_provider;
            private String song_title;
            private String artist_name;

            public String getArtist_id() {
                return artist_id;
            }

            public void setArtist_id(String artist_id) {
                this.artist_id = artist_id;
            }

            public String getAvatar_small() {
                return avatar_small;
            }

            public void setAvatar_small(String avatar_small) {
                this.avatar_small = avatar_small;
            }

            public String getAvatar_big() {
                return avatar_big;
            }

            public void setAvatar_big(String avatar_big) {
                this.avatar_big = avatar_big;
            }

            public String getAll_artist_id() {
                return all_artist_id;
            }

            public void setAll_artist_id(String all_artist_id) {
                this.all_artist_id = all_artist_id;
            }

            public String getAlbum_no() {
                return album_no;
            }

            public void setAlbum_no(String album_no) {
                this.album_no = album_no;
            }

            public String getPic_big() {
                return pic_big;
            }

            public void setPic_big(String pic_big) {
                this.pic_big = pic_big;
            }

            public String getPic_small() {
                return pic_small;
            }

            public void setPic_small(String pic_small) {
                this.pic_small = pic_small;
            }

            public String getRelate_status() {
                return relate_status;
            }

            public void setRelate_status(String relate_status) {
                this.relate_status = relate_status;
            }

            public String getResource_type() {
                return resource_type;
            }

            public void setResource_type(String resource_type) {
                this.resource_type = resource_type;
            }

            public String getCopy_type() {
                return copy_type;
            }

            public void setCopy_type(String copy_type) {
                this.copy_type = copy_type;
            }

            public String getLrclink() {
                return lrclink;
            }

            public void setLrclink(String lrclink) {
                this.lrclink = lrclink;
            }

            public String getPic_radio() {
                return pic_radio;
            }

            public void setPic_radio(String pic_radio) {
                this.pic_radio = pic_radio;
            }

            public String getToneid() {
                return toneid;
            }

            public void setToneid(String toneid) {
                this.toneid = toneid;
            }

            public String getAll_rate() {
                return all_rate;
            }

            public void setAll_rate(String all_rate) {
                this.all_rate = all_rate;
            }

            public String getPlay_type() {
                return play_type;
            }

            public void setPlay_type(String play_type) {
                this.play_type = play_type;
            }

            public int getHas_mv_mobile() {
                return has_mv_mobile;
            }

            public void setHas_mv_mobile(int has_mv_mobile) {
                this.has_mv_mobile = has_mv_mobile;
            }

            public String getPic_premium() {
                return pic_premium;
            }

            public void setPic_premium(String pic_premium) {
                this.pic_premium = pic_premium;
            }

            public String getPic_huge() {
                return pic_huge;
            }

            public void setPic_huge(String pic_huge) {
                this.pic_huge = pic_huge;
            }

            public String getVersions() {
                return versions;
            }

            public void setVersions(String versions) {
                this.versions = versions;
            }

            public String getBitrate_fee() {
                return bitrate_fee;
            }

            public void setBitrate_fee(String bitrate_fee) {
                this.bitrate_fee = bitrate_fee;
            }

            public String getSong_id() {
                return song_id;
            }

            public void setSong_id(String song_id) {
                this.song_id = song_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTing_uid() {
                return ting_uid;
            }

            public void setTing_uid(String ting_uid) {
                this.ting_uid = ting_uid;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getAlbum_id() {
                return album_id;
            }

            public void setAlbum_id(String album_id) {
                this.album_id = album_id;
            }

            public String getAlbum_title() {
                return album_title;
            }

            public void setAlbum_title(String album_title) {
                this.album_title = album_title;
            }

            public int getIs_first_publish() {
                return is_first_publish;
            }

            public void setIs_first_publish(int is_first_publish) {
                this.is_first_publish = is_first_publish;
            }

            public int getHavehigh() {
                return havehigh;
            }

            public void setHavehigh(int havehigh) {
                this.havehigh = havehigh;
            }

            public int getCharge() {
                return charge;
            }

            public void setCharge(int charge) {
                this.charge = charge;
            }

            public int getHas_mv() {
                return has_mv;
            }

            public void setHas_mv(int has_mv) {
                this.has_mv = has_mv;
            }

            public int getLearn() {
                return learn;
            }

            public void setLearn(int learn) {
                this.learn = learn;
            }

            public String getSong_source() {
                return song_source;
            }

            public void setSong_source(String song_source) {
                this.song_source = song_source;
            }

            public String getPiao_id() {
                return piao_id;
            }

            public void setPiao_id(String piao_id) {
                this.piao_id = piao_id;
            }

            public String getKorean_bb_song() {
                return korean_bb_song;
            }

            public void setKorean_bb_song(String korean_bb_song) {
                this.korean_bb_song = korean_bb_song;
            }

            public String getResource_type_ext() {
                return resource_type_ext;
            }

            public void setResource_type_ext(String resource_type_ext) {
                this.resource_type_ext = resource_type_ext;
            }

            public String getMv_provider() {
                return mv_provider;
            }

            public void setMv_provider(String mv_provider) {
                this.mv_provider = mv_provider;
            }

            public String getSong_title() {
                return song_title;
            }

            public void setSong_title(String song_title) {
                this.song_title = song_title;
            }

            public String getArtist_name() {
                return artist_name;
            }

            public void setArtist_name(String artist_name) {
                this.artist_name = artist_name;
            }
        }
    }
}
