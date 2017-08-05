package com.example.kaizhiwei.puremusictest.bean;

import java.util.List;

/**
 * Created by kaizhiwei on 17/8/5.
 */

public class RecommMvBean {
    /**
     * error_code : 22000
     * result : {"havemore":1,"total":65,"mv_list":[{"artist":"","mv_id":"549884461","title":"三生三世十里桃花首映礼直播回放","thumbnail":"http://qukufile2.qianqian.com/data2/pic/15a901436e6e8f818151b46df7ba6d4d/549884347/549884347.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/15a901436e6e8f818151b46df7ba6d4d/549884347/549884347.jpg@s_0,w_240,h_135"},{"artist":"洛天依","mv_id":"546209093","title":"【王道音乐】易家扬：从全息虚拟歌姬洛天依，聊聊虚拟偶像的破壁之路","thumbnail":"http://qukufile2.qianqian.com/data2/pic/8a09e7e84828e8e1bdaefedd8daede2a/546205391/546205391.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/8a09e7e84828e8e1bdaefedd8daede2a/546205391/546205391.jpg@s_0,w_240,h_135"},{"artist":"许嵩","mv_id":"545971172","title":"许嵩《青年晚报》北京演唱会独家粉丝探班","thumbnail":"http://qukufile2.qianqian.com/data2/pic/37d4340834b709d9bc1bbf9ab0e2d780/545963477/545963477.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/37d4340834b709d9bc1bbf9ab0e2d780/545963477/545963477.jpg@s_0,w_240,h_135"},{"artist":"","mv_id":"546255414","title":"十八酒坊醇柔之夜演唱会邢台站","thumbnail":"http://qukufile2.qianqian.com/data2/pic/ba5552117367f6c503feb20123b5e669/546250799/546250799.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/ba5552117367f6c503feb20123b5e669/546250799/546250799.jpg@s_0,w_240,h_135"},{"artist":"","mv_id":"546263305","title":"十八酒坊醇柔之夜演唱会保定站","thumbnail":"http://qukufile2.qianqian.com/data2/pic/605db2405ff528401d65f42de0b301e0/546255650/546255650.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/605db2405ff528401d65f42de0b301e0/546255650/546255650.jpg@s_0,w_240,h_135"},{"artist":"王筝","mv_id":"545465667","title":"【王道音乐】王筝：她和她曾经的才华","thumbnail":"http://qukufile2.qianqian.com/data2/pic/cd818db142e54896ca9d02a54228d575/545464356/545464356.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/cd818db142e54896ca9d02a54228d575/545464356/545464356.jpg@s_0,w_240,h_135"},{"artist":"范忆堂","mv_id":"545467752","title":"【王道音乐】追忆永远的哥哥张国荣嘉宾（范忆堂、流水纪）","thumbnail":"http://qukufile2.qianqian.com/data2/pic/6da79d6087737e749553e47624b1ecef/545465895/545465895.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/6da79d6087737e749553e47624b1ecef/545465895/545465895.jpg@s_0,w_240,h_135"},{"artist":"罗中旭","mv_id":"544580067","title":"罗中旭\u201c星光灿烂\u201c20年全国巡回演唱会","thumbnail":"http://qukufile2.qianqian.com/data2/pic/9fe4b025524ed437cd3cc233b69358b3/544579856/544579856.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/9fe4b025524ed437cd3cc233b69358b3/544579856/544579856.jpg@s_0,w_240,h_135"},{"artist":"","mv_id":"544582516","title":"好听到起鸡皮疙瘩！黄榕生尹毓恪灵鹤合唱梦一场","thumbnail":"http://qukufile2.qianqian.com/data2/pic/5d02cbd42b4cf07c3fd0ed922fc8416a/544582100/544582100.JPG@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/5d02cbd42b4cf07c3fd0ed922fc8416a/544582100/544582100.JPG@s_0,w_240,h_135"},{"artist":"","mv_id":"544582534","title":"贾昱《天空》犹如天籁","thumbnail":"http://qukufile2.qianqian.com/data2/pic/37c409023bd3b207bf11894956a1fa27/544582057/544582057.JPG@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/37c409023bd3b207bf11894956a1fa27/544582057/544582057.JPG@s_0,w_240,h_135"}]}
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
        /**
         * havemore : 1
         * total : 65
         * mv_list : [{"artist":"","mv_id":"549884461","title":"三生三世十里桃花首映礼直播回放","thumbnail":"http://qukufile2.qianqian.com/data2/pic/15a901436e6e8f818151b46df7ba6d4d/549884347/549884347.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/15a901436e6e8f818151b46df7ba6d4d/549884347/549884347.jpg@s_0,w_240,h_135"},{"artist":"洛天依","mv_id":"546209093","title":"【王道音乐】易家扬：从全息虚拟歌姬洛天依，聊聊虚拟偶像的破壁之路","thumbnail":"http://qukufile2.qianqian.com/data2/pic/8a09e7e84828e8e1bdaefedd8daede2a/546205391/546205391.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/8a09e7e84828e8e1bdaefedd8daede2a/546205391/546205391.jpg@s_0,w_240,h_135"},{"artist":"许嵩","mv_id":"545971172","title":"许嵩《青年晚报》北京演唱会独家粉丝探班","thumbnail":"http://qukufile2.qianqian.com/data2/pic/37d4340834b709d9bc1bbf9ab0e2d780/545963477/545963477.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/37d4340834b709d9bc1bbf9ab0e2d780/545963477/545963477.jpg@s_0,w_240,h_135"},{"artist":"","mv_id":"546255414","title":"十八酒坊醇柔之夜演唱会邢台站","thumbnail":"http://qukufile2.qianqian.com/data2/pic/ba5552117367f6c503feb20123b5e669/546250799/546250799.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/ba5552117367f6c503feb20123b5e669/546250799/546250799.jpg@s_0,w_240,h_135"},{"artist":"","mv_id":"546263305","title":"十八酒坊醇柔之夜演唱会保定站","thumbnail":"http://qukufile2.qianqian.com/data2/pic/605db2405ff528401d65f42de0b301e0/546255650/546255650.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/605db2405ff528401d65f42de0b301e0/546255650/546255650.jpg@s_0,w_240,h_135"},{"artist":"王筝","mv_id":"545465667","title":"【王道音乐】王筝：她和她曾经的才华","thumbnail":"http://qukufile2.qianqian.com/data2/pic/cd818db142e54896ca9d02a54228d575/545464356/545464356.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/cd818db142e54896ca9d02a54228d575/545464356/545464356.jpg@s_0,w_240,h_135"},{"artist":"范忆堂","mv_id":"545467752","title":"【王道音乐】追忆永远的哥哥张国荣嘉宾（范忆堂、流水纪）","thumbnail":"http://qukufile2.qianqian.com/data2/pic/6da79d6087737e749553e47624b1ecef/545465895/545465895.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/6da79d6087737e749553e47624b1ecef/545465895/545465895.jpg@s_0,w_240,h_135"},{"artist":"罗中旭","mv_id":"544580067","title":"罗中旭\u201c星光灿烂\u201c20年全国巡回演唱会","thumbnail":"http://qukufile2.qianqian.com/data2/pic/9fe4b025524ed437cd3cc233b69358b3/544579856/544579856.jpg@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/9fe4b025524ed437cd3cc233b69358b3/544579856/544579856.jpg@s_0,w_240,h_135"},{"artist":"","mv_id":"544582516","title":"好听到起鸡皮疙瘩！黄榕生尹毓恪灵鹤合唱梦一场","thumbnail":"http://qukufile2.qianqian.com/data2/pic/5d02cbd42b4cf07c3fd0ed922fc8416a/544582100/544582100.JPG@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/5d02cbd42b4cf07c3fd0ed922fc8416a/544582100/544582100.JPG@s_0,w_240,h_135"},{"artist":"","mv_id":"544582534","title":"贾昱《天空》犹如天籁","thumbnail":"http://qukufile2.qianqian.com/data2/pic/37c409023bd3b207bf11894956a1fa27/544582057/544582057.JPG@s_0,w_160,h_90","provider":"","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/37c409023bd3b207bf11894956a1fa27/544582057/544582057.JPG@s_0,w_240,h_135"}]
         */

        private int havemore;
        private int total;
        private List<MvListBean> mv_list;

        public int getHavemore() {
            return havemore;
        }

        public void setHavemore(int havemore) {
            this.havemore = havemore;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<MvListBean> getMv_list() {
            return mv_list;
        }

        public void setMv_list(List<MvListBean> mv_list) {
            this.mv_list = mv_list;
        }

        public static class MvListBean {
            /**
             * artist :
             * mv_id : 549884461
             * title : 三生三世十里桃花首映礼直播回放
             * thumbnail : http://qukufile2.qianqian.com/data2/pic/15a901436e6e8f818151b46df7ba6d4d/549884347/549884347.jpg@s_0,w_160,h_90
             * provider :
             * thumbnail2 : http://qukufile2.qianqian.com/data2/pic/15a901436e6e8f818151b46df7ba6d4d/549884347/549884347.jpg@s_0,w_240,h_135
             */

            private String artist;
            private String mv_id;
            private String title;
            private String thumbnail;
            private String provider;
            private String thumbnail2;

            public String getArtist() {
                return artist;
            }

            public void setArtist(String artist) {
                this.artist = artist;
            }

            public String getMv_id() {
                return mv_id;
            }

            public void setMv_id(String mv_id) {
                this.mv_id = mv_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            public String getThumbnail2() {
                return thumbnail2;
            }

            public void setThumbnail2(String thumbnail2) {
                this.thumbnail2 = thumbnail2;
            }
        }
    }
}
