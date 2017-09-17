package com.example.kaizhiwei.puremusictest.bean;

import java.util.List;

/**
 * Created by kaizhiwei on 17/9/16.
 */

public class SearchLrcPicBean {
    /**
     * error_code : 22000
     * songinfo : [{"lrclink":"http://musicdata.baidu.com/data2/lrc/882b1027f133d4e2530bd7997ccdf774/267901658/267901658.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"37fceeaa0523ec06d283e7eb1eb0d17b","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":309267,"pic_s180":"http://musicdata.baidu.com/data2/pic/2b903c7f51d8dd6f324ce19b78690025/7314613/7314613.jpg@s_1,w_180,h_180","title":"冷战","pic_s1000":"http://musicdata.baidu.com/data2/pic/2b903c7f51d8dd6f324ce19b78690025/7314613/7314613.jpg@s_1,w_1000,h_1000","album_id":7314613,"pic_s500":"http://musicdata.baidu.com/data2/pic/2b903c7f51d8dd6f324ce19b78690025/7314613/7314613.jpg@s_1,w_500,h_500","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/251697012/251697012.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"65febf54272d51ef54c9d3816a216a55","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":935634,"pic_s180":"http://musicdata.baidu.com/data2/pic/a3380ad7396ddd13803979bf7d9af40d/7322954/7322954.jpg@s_1,w_180,h_180","title":"冷战","pic_s1000":"","album_id":7322954,"pic_s500":"http://musicdata.baidu.com/data2/pic/a3380ad7396ddd13803979bf7d9af40d/7322954/7322954.jpg@s_1,w_500,h_500","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/1265919/1265919.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"4df94683ae07516a4fd1b98caf4f7987","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":1265918,"pic_s180":"http://musicdata.baidu.com/data2/music/79A8D8777C66DDD6CC1AD4B1880783B9/252467823/252467823.jpg@s_0,w_180","title":"冷战","pic_s1000":"http://musicdata.baidu.com/data2/music/79A8D8777C66DDD6CC1AD4B1880783B9/252467823/252467823.jpg","album_id":7326069,"pic_s500":"http://musicdata.baidu.com/data2/music/79A8D8777C66DDD6CC1AD4B1880783B9/252467823/252467823.jpg@s_0,w_500","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/13917391/13917391.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"f8641f3bcc64c5a39bbde4c49f7d1084","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":7319774,"pic_s180":"http://musicdata.baidu.com/data2/pic/bc6498cae0cfbee21152d5ff5585f82c/7311896/7311896.jpg@s_1,w_180,h_180","title":"冷战","pic_s1000":"http://musicdata.baidu.com/data2/pic/bc6498cae0cfbee21152d5ff5585f82c/7311896/7311896.jpg@s_1,w_1000,h_1000","album_id":7311896,"pic_s500":"http://musicdata.baidu.com/data2/pic/bc6498cae0cfbee21152d5ff5585f82c/7311896/7311896.jpg@s_1,w_500,h_500","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/58dd88997f4efab1d0f7b2c3f8a09808/267818683/267818683.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"4df94683ae07516a4fd1b98caf4f7987","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":7342380,"pic_s180":"http://musicdata.baidu.com/data2/pic/7be03faba8b44020dcdec748db04ecc7/7325359/7325359.jpg@s_1,w_180,h_180","title":"冷战","pic_s1000":"http://musicdata.baidu.com/data2/pic/7be03faba8b44020dcdec748db04ecc7/7325359/7325359.jpg@s_1,w_1000,h_1000","album_id":7325359,"pic_s500":"http://musicdata.baidu.com/data2/pic/7be03faba8b44020dcdec748db04ecc7/7325359/7325359.jpg@s_1,w_500,h_500","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/65441702/65441702.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"0eb3e3e44deaf0d3772871564f230906","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":51684896,"pic_s180":"http://musicdata.baidu.com/data2/pic/857a8d83d0cb276d85bd871d8ce186ed/51684596/51684596.jpg@s_1,w_180,h_180","title":"冷战","pic_s1000":"","album_id":51684596,"pic_s500":"http://musicdata.baidu.com/data2/pic/857a8d83d0cb276d85bd871d8ce186ed/51684596/51684596.jpg","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/58dd88997f4efab1d0f7b2c3f8a09808/541726444/541726444.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"4df94683ae07516a4fd1b98caf4f7987","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":289142662,"pic_s180":"http://musicdata.baidu.com/data2/pic/2d94c13a05d3de3ccd4cc27126cac833/289135188/289135188.jpg@s_0,w_180","title":"冷战","pic_s1000":"","album_id":289135186,"pic_s500":"http://musicdata.baidu.com/data2/pic/2d94c13a05d3de3ccd4cc27126cac833/289135188/289135188.jpg@s_0,w_500","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/7363377/7363377.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":7320666,"pic_s180":"http://musicdata.baidu.com/data2/pic/32888427b0d09361e630845f4ec4c364/7311583/7311583.jpg@s_1,w_180,h_180","title":"冷战","pic_s1000":"http://musicdata.baidu.com/data2/pic/32888427b0d09361e630845f4ec4c364/7311583/7311583.jpg","album_id":7311583,"pic_s500":"http://musicdata.baidu.com/data2/pic/32888427b0d09361e630845f4ec4c364/7311583/7311583.jpg@s_1,w_500,h_500","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/65502116/65502116.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":59704018,"pic_s180":"http://musicdata.baidu.com/data2/pic/090d9d2f3d34fd0b8b1e18e2e084b3b0/262012387/262012387.jpg@s_0,w_180","title":"冷战","pic_s1000":"","album_id":28660961,"pic_s500":"http://musicdata.baidu.com/data2/pic/090d9d2f3d34fd0b8b1e18e2e084b3b0/262012387/262012387.jpg","song_title":"冷战","artist_id":15},{"lrclink":"http://musicdata.baidu.com/data2/lrc/251699542/251699542.lrc","artist_480_800":"","avatar_s500":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500","pic_type":2,"author":"王菲","artist_1000_1000":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg","artist_640_1136":"","lrc_md5":"32559ec287a1d3dd805023906ddf0256","avatar_s180":"http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180","song_id":730299,"pic_s180":"http://musicdata.baidu.com/data2/pic/c226d4d8de33c0e4ed2307678be22d8c/197842/197842.jpg@s_1,w_180,h_180","title":"冷战","pic_s1000":"","album_id":197842,"pic_s500":"http://musicdata.baidu.com/data2/pic/c226d4d8de33c0e4ed2307678be22d8c/197842/197842.jpg@s_1,w_500,h_500","song_title":"冷战","artist_id":15}]
     */

    private int error_code;
    private List<SonginfoBean> songinfo;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<SonginfoBean> getSonginfo() {
        return songinfo;
    }

    public void setSonginfo(List<SonginfoBean> songinfo) {
        this.songinfo = songinfo;
    }

    public static class SonginfoBean {
        /**
         * lrclink : http://musicdata.baidu.com/data2/lrc/882b1027f133d4e2530bd7997ccdf774/267901658/267901658.lrc
         * artist_480_800 :
         * avatar_s500 : http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_500
         * pic_type : 2
         * author : 王菲
         * artist_1000_1000 : http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg
         * artist_640_1136 :
         * lrc_md5 : 37fceeaa0523ec06d283e7eb1eb0d17b
         * avatar_s180 : http://musicdata.baidu.com/data2/pic/246668439/246668439.jpg@s_0,w_180
         * song_id : 309267
         * pic_s180 : http://musicdata.baidu.com/data2/pic/2b903c7f51d8dd6f324ce19b78690025/7314613/7314613.jpg@s_1,w_180,h_180
         * title : 冷战
         * pic_s1000 : http://musicdata.baidu.com/data2/pic/2b903c7f51d8dd6f324ce19b78690025/7314613/7314613.jpg@s_1,w_1000,h_1000
         * album_id : 7314613
         * pic_s500 : http://musicdata.baidu.com/data2/pic/2b903c7f51d8dd6f324ce19b78690025/7314613/7314613.jpg@s_1,w_500,h_500
         * song_title : 冷战
         * artist_id : 15
         */

        private String lrclink;
        private String artist_480_800;
        private String avatar_s500;
        private int pic_type;
        private String author;
        private String artist_1000_1000;
        private String artist_640_1136;
        private String lrc_md5;
        private String avatar_s180;
        private int song_id;
        private String pic_s180;
        private String title;
        private String pic_s1000;
        private int album_id;
        private String pic_s500;
        private String song_title;
        private int artist_id;

        public String getLrclink() {
            return lrclink;
        }

        public void setLrclink(String lrclink) {
            this.lrclink = lrclink;
        }

        public String getArtist_480_800() {
            return artist_480_800;
        }

        public void setArtist_480_800(String artist_480_800) {
            this.artist_480_800 = artist_480_800;
        }

        public String getAvatar_s500() {
            return avatar_s500;
        }

        public void setAvatar_s500(String avatar_s500) {
            this.avatar_s500 = avatar_s500;
        }

        public int getPic_type() {
            return pic_type;
        }

        public void setPic_type(int pic_type) {
            this.pic_type = pic_type;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getArtist_1000_1000() {
            return artist_1000_1000;
        }

        public void setArtist_1000_1000(String artist_1000_1000) {
            this.artist_1000_1000 = artist_1000_1000;
        }

        public String getArtist_640_1136() {
            return artist_640_1136;
        }

        public void setArtist_640_1136(String artist_640_1136) {
            this.artist_640_1136 = artist_640_1136;
        }

        public String getLrc_md5() {
            return lrc_md5;
        }

        public void setLrc_md5(String lrc_md5) {
            this.lrc_md5 = lrc_md5;
        }

        public String getAvatar_s180() {
            return avatar_s180;
        }

        public void setAvatar_s180(String avatar_s180) {
            this.avatar_s180 = avatar_s180;
        }

        public int getSong_id() {
            return song_id;
        }

        public void setSong_id(int song_id) {
            this.song_id = song_id;
        }

        public String getPic_s180() {
            return pic_s180;
        }

        public void setPic_s180(String pic_s180) {
            this.pic_s180 = pic_s180;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic_s1000() {
            return pic_s1000;
        }

        public void setPic_s1000(String pic_s1000) {
            this.pic_s1000 = pic_s1000;
        }

        public int getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(int album_id) {
            this.album_id = album_id;
        }

        public String getPic_s500() {
            return pic_s500;
        }

        public void setPic_s500(String pic_s500) {
            this.pic_s500 = pic_s500;
        }

        public String getSong_title() {
            return song_title;
        }

        public void setSong_title(String song_title) {
            this.song_title = song_title;
        }

        public int getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(int artist_id) {
            this.artist_id = artist_id;
        }
    }
}
