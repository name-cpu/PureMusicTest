package com.example.kaizhiwei.puremusictest.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 24820 on 2017/6/27.
 */
public class ActiveIndexBean extends BaseBean{
    /**
     * error_code : 22000
     * result : [{"type":3,"switch":0,"activity_id":92,"pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1495186149c7714e7973ac2de69d28de343bb15293.jpg","web_url":"http://music.baidu.com/cms/webview/game_ad/yaowang/","end_time":1500456516},{"type":3,"switch":0,"activity_id":95,"pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1496314575af534f53df19d8de26b73c44b94d7a88.jpg","web_url":"http://music.baidu.com/cms/webview/vip_activity/20170601/index.html","end_time":1501584964},{"type":3,"switch":0,"activity_id":91,"pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14943859733b4642dcae2fd0eecafa9cf8fa2312af.jpg","web_url":"http://music.baidu.com/cms/webview/vip_activity/youxin_vip/index.html","end_time":1502334745}]
     */
    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * type : 3
         * switch : 0
         * activity_id : 92
         * pic : http://business.cdn.qianqian.com/qianqian/pic/bos_client_1495186149c7714e7973ac2de69d28de343bb15293.jpg
         * web_url : http://music.baidu.com/cms/webview/game_ad/yaowang/
         * end_time : 1500456516
         */

        private int type;
        @SerializedName("switch")
        private int switchX;
        private int activity_id;
        private String pic;
        private String web_url;
        private int end_time;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSwitchX() {
            return switchX;
        }

        public void setSwitchX(int switchX) {
            this.switchX = switchX;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }
    }
}
