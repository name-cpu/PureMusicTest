package com.example.kaizhiwei.puremusictest.bean;

import java.util.List;

/**
 * Created by 24820 on 2017/6/27.
 */
public class UnStandardAdBean {
    /**
     * error_code : 22000
     * material_map : [{"ad_id":1190,"closeable":1,"start_time":1498492800,"end_time":1498579199,"ad_version":"0","ad_end_version":"0","vip_tactics":"0","materials":{"ad_mob_playerskin_turnround":{"name":"播放页翻转","templet_id":60,"display_type":1,"displaytime":5,"display_content":[{"picture":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_149848537177cf856b17776961eb519d6c27179a20.jpg","width":"600","height":"600","weburl":"http://comlog.music.baidu.com/unjump?s=7b226964223a2231313930222c2274656d706c6174655f6964223a36302c2275223a22687474703a5c2f5c2f6d757369632e62616964752e636f6d5c2f636d735c2f776562766965775c2f746f7069635f61637469766974795c2f63697975616e79796a69655c2f222c2274223a313439383536333338372c2266223a22616e64726f6964222c2270223a226d75736963222c226c223a226e6f726d616c222c2263223a223835464231314243463636393336444133383643364143394341323238463243227d&flow=unchangeable","share_url":"","download_url":"","audio_url":"","link_type":"url","link_value":"http://music.baidu.com/cms/webview/topic_activity/ciyuanyyjie/","audio_duration":"","duration":""}]}}}]
     */

    private int error_code;
    private List<MaterialMapBean> material_map;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<MaterialMapBean> getMaterial_map() {
        return material_map;
    }

    public void setMaterial_map(List<MaterialMapBean> material_map) {
        this.material_map = material_map;
    }

    public static class MaterialMapBean {
        /**
         * ad_id : 1190
         * closeable : 1
         * start_time : 1498492800
         * end_time : 1498579199
         * ad_version : 0
         * ad_end_version : 0
         * vip_tactics : 0
         * materials : {"ad_mob_playerskin_turnround":{"name":"播放页翻转","templet_id":60,"display_type":1,"displaytime":5,"display_content":[{"picture":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_149848537177cf856b17776961eb519d6c27179a20.jpg","width":"600","height":"600","weburl":"http://comlog.music.baidu.com/unjump?s=7b226964223a2231313930222c2274656d706c6174655f6964223a36302c2275223a22687474703a5c2f5c2f6d757369632e62616964752e636f6d5c2f636d735c2f776562766965775c2f746f7069635f61637469766974795c2f63697975616e79796a69655c2f222c2274223a313439383536333338372c2266223a22616e64726f6964222c2270223a226d75736963222c226c223a226e6f726d616c222c2263223a223835464231314243463636393336444133383643364143394341323238463243227d&flow=unchangeable","share_url":"","download_url":"","audio_url":"","link_type":"url","link_value":"http://music.baidu.com/cms/webview/topic_activity/ciyuanyyjie/","audio_duration":"","duration":""}]}}
         */

        private int ad_id;
        private int closeable;
        private int start_time;
        private int end_time;
        private String ad_version;
        private String ad_end_version;
        private String vip_tactics;
        private MaterialsBean materials;

        public int getAd_id() {
            return ad_id;
        }

        public void setAd_id(int ad_id) {
            this.ad_id = ad_id;
        }

        public int getCloseable() {
            return closeable;
        }

        public void setCloseable(int closeable) {
            this.closeable = closeable;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

        public String getAd_version() {
            return ad_version;
        }

        public void setAd_version(String ad_version) {
            this.ad_version = ad_version;
        }

        public String getAd_end_version() {
            return ad_end_version;
        }

        public void setAd_end_version(String ad_end_version) {
            this.ad_end_version = ad_end_version;
        }

        public String getVip_tactics() {
            return vip_tactics;
        }

        public void setVip_tactics(String vip_tactics) {
            this.vip_tactics = vip_tactics;
        }

        public MaterialsBean getMaterials() {
            return materials;
        }

        public void setMaterials(MaterialsBean materials) {
            this.materials = materials;
        }

        public static class MaterialsBean {
            /**
             * ad_mob_playerskin_turnround : {"name":"播放页翻转","templet_id":60,"display_type":1,"displaytime":5,"display_content":[{"picture":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_149848537177cf856b17776961eb519d6c27179a20.jpg","width":"600","height":"600","weburl":"http://comlog.music.baidu.com/unjump?s=7b226964223a2231313930222c2274656d706c6174655f6964223a36302c2275223a22687474703a5c2f5c2f6d757369632e62616964752e636f6d5c2f636d735c2f776562766965775c2f746f7069635f61637469766974795c2f63697975616e79796a69655c2f222c2274223a313439383536333338372c2266223a22616e64726f6964222c2270223a226d75736963222c226c223a226e6f726d616c222c2263223a223835464231314243463636393336444133383643364143394341323238463243227d&flow=unchangeable","share_url":"","download_url":"","audio_url":"","link_type":"url","link_value":"http://music.baidu.com/cms/webview/topic_activity/ciyuanyyjie/","audio_duration":"","duration":""}]}
             */

            private AdMobPlayerskinTurnroundBean ad_mob_playerskin_turnround;

            public AdMobPlayerskinTurnroundBean getAd_mob_playerskin_turnround() {
                return ad_mob_playerskin_turnround;
            }

            public void setAd_mob_playerskin_turnround(AdMobPlayerskinTurnroundBean ad_mob_playerskin_turnround) {
                this.ad_mob_playerskin_turnround = ad_mob_playerskin_turnround;
            }

            public static class AdMobPlayerskinTurnroundBean {
                /**
                 * name : 播放页翻转
                 * templet_id : 60
                 * display_type : 1
                 * displaytime : 5
                 * display_content : [{"picture":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_149848537177cf856b17776961eb519d6c27179a20.jpg","width":"600","height":"600","weburl":"http://comlog.music.baidu.com/unjump?s=7b226964223a2231313930222c2274656d706c6174655f6964223a36302c2275223a22687474703a5c2f5c2f6d757369632e62616964752e636f6d5c2f636d735c2f776562766965775c2f746f7069635f61637469766974795c2f63697975616e79796a69655c2f222c2274223a313439383536333338372c2266223a22616e64726f6964222c2270223a226d75736963222c226c223a226e6f726d616c222c2263223a223835464231314243463636393336444133383643364143394341323238463243227d&flow=unchangeable","share_url":"","download_url":"","audio_url":"","link_type":"url","link_value":"http://music.baidu.com/cms/webview/topic_activity/ciyuanyyjie/","audio_duration":"","duration":""}]
                 */

                private String name;
                private int templet_id;
                private int display_type;
                private int displaytime;
                private List<DisplayContentBean> display_content;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getTemplet_id() {
                    return templet_id;
                }

                public void setTemplet_id(int templet_id) {
                    this.templet_id = templet_id;
                }

                public int getDisplay_type() {
                    return display_type;
                }

                public void setDisplay_type(int display_type) {
                    this.display_type = display_type;
                }

                public int getDisplaytime() {
                    return displaytime;
                }

                public void setDisplaytime(int displaytime) {
                    this.displaytime = displaytime;
                }

                public List<DisplayContentBean> getDisplay_content() {
                    return display_content;
                }

                public void setDisplay_content(List<DisplayContentBean> display_content) {
                    this.display_content = display_content;
                }

                public static class DisplayContentBean {
                    /**
                     * picture : http://business.cdn.qianqian.com/qianqian/pic/bos_client_149848537177cf856b17776961eb519d6c27179a20.jpg
                     * width : 600
                     * height : 600
                     * weburl : http://comlog.music.baidu.com/unjump?s=7b226964223a2231313930222c2274656d706c6174655f6964223a36302c2275223a22687474703a5c2f5c2f6d757369632e62616964752e636f6d5c2f636d735c2f776562766965775c2f746f7069635f61637469766974795c2f63697975616e79796a69655c2f222c2274223a313439383536333338372c2266223a22616e64726f6964222c2270223a226d75736963222c226c223a226e6f726d616c222c2263223a223835464231314243463636393336444133383643364143394341323238463243227d&flow=unchangeable
                     * share_url :
                     * download_url :
                     * audio_url :
                     * link_type : url
                     * link_value : http://music.baidu.com/cms/webview/topic_activity/ciyuanyyjie/
                     * audio_duration :
                     * duration :
                     */

                    private String picture;
                    private String width;
                    private String height;
                    private String weburl;
                    private String share_url;
                    private String download_url;
                    private String audio_url;
                    private String link_type;
                    private String link_value;
                    private String audio_duration;
                    private String duration;

                    public String getPicture() {
                        return picture;
                    }

                    public void setPicture(String picture) {
                        this.picture = picture;
                    }

                    public String getWidth() {
                        return width;
                    }

                    public void setWidth(String width) {
                        this.width = width;
                    }

                    public String getHeight() {
                        return height;
                    }

                    public void setHeight(String height) {
                        this.height = height;
                    }

                    public String getWeburl() {
                        return weburl;
                    }

                    public void setWeburl(String weburl) {
                        this.weburl = weburl;
                    }

                    public String getShare_url() {
                        return share_url;
                    }

                    public void setShare_url(String share_url) {
                        this.share_url = share_url;
                    }

                    public String getDownload_url() {
                        return download_url;
                    }

                    public void setDownload_url(String download_url) {
                        this.download_url = download_url;
                    }

                    public String getAudio_url() {
                        return audio_url;
                    }

                    public void setAudio_url(String audio_url) {
                        this.audio_url = audio_url;
                    }

                    public String getLink_type() {
                        return link_type;
                    }

                    public void setLink_type(String link_type) {
                        this.link_type = link_type;
                    }

                    public String getLink_value() {
                        return link_value;
                    }

                    public void setLink_value(String link_value) {
                        this.link_value = link_value;
                    }

                    public String getAudio_duration() {
                        return audio_duration;
                    }

                    public void setAudio_duration(String audio_duration) {
                        this.audio_duration = audio_duration;
                    }

                    public String getDuration() {
                        return duration;
                    }

                    public void setDuration(String duration) {
                        this.duration = duration;
                    }
                }
            }
        }
    }
}
