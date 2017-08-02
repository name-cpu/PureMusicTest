package com.example.kaizhiwei.puremusictest.bean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public class PlayMvBean {
    /**
     * error_code : 22000
     * result : {"video_info":{"video_id":"545985706","mv_id":"545982609","provider":"1","sourcepath":null,"thumbnail":"http://qukufile2.qianqian.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_160,h_90","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_240,h_135","del_status":"0","distribution":"0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000"},"files":{"41":{"video_file_id":"546202774","video_id":"545985706","definition":"41","file_link":"http://qukufile2.qianqian.com/data2/video/545985478/d8441e9fc94c2702f80dd7dc9cae5090/545985478.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"34714649","source_path":""},"31":{"video_file_id":"546202775","video_id":"545985706","definition":"31","file_link":"http://qukufile2.qianqian.com/data2/video/545985500/63d242713b9834ddae012ca680f221be/545985500.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"18521372","source_path":""},"0":{"video_file_id":"546202776","video_id":"545985706","definition":"0","file_link":"http://qukufile2.qianqian.com/data2/video/545985529/da94cd86457e5c05b02f7310ff5b376f/545985529.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"1133732595","source_path":""}},"min_definition":"0","max_definition":"41","mv_info":{"mv_id":"545982609","all_artist_id":"166","title":"说","aliastitle":"","subtitle":"张信哲","play_nums":"2353","publishtime":"1970-01-01","del_status":"0","artist_list":[{"artist_id":"166","ting_uid":"1118","artist_name":"张信哲","artist_480_800":"","artist_640_1136":"","avatar_small":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_48","avatar_mini":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_20","avatar_s180":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_180","avatar_s300":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_300","avatar_s500":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_500","del_status":"0"}],"artist_id":"166","thumbnail":"http://musicdata.baidu.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_160,h_90","thumbnail3":"http://business.cdn.qianqian.com/baidumisic","thumbnail2":"http://musicdata.baidu.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_240,h_135","artist":"张信哲","provider":"1"},"share_url":"http://music.baidu.com/cms/webview/sharevideo?video_id=545982609"}
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

    public boolean parser(String strJson) {
        JSONObject jsonArray = null;
        try {
            jsonArray = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        try {
            JSONObject rootResult = jsonArray.getJSONObject("result");
            error_code = jsonArray.getInt("error_code");
            result = new ResultBean();
            result.parser(rootResult);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public static class ResultBean {
        /**
         * video_info : {"video_id":"545985706","mv_id":"545982609","provider":"1","sourcepath":null,"thumbnail":"http://qukufile2.qianqian.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_160,h_90","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_240,h_135","del_status":"0","distribution":"0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000"}
         * files : {"41":{"video_file_id":"546202774","video_id":"545985706","definition":"41","file_link":"http://qukufile2.qianqian.com/data2/video/545985478/d8441e9fc94c2702f80dd7dc9cae5090/545985478.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"34714649","source_path":""},"31":{"video_file_id":"546202775","video_id":"545985706","definition":"31","file_link":"http://qukufile2.qianqian.com/data2/video/545985500/63d242713b9834ddae012ca680f221be/545985500.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"18521372","source_path":""},"0":{"video_file_id":"546202776","video_id":"545985706","definition":"0","file_link":"http://qukufile2.qianqian.com/data2/video/545985529/da94cd86457e5c05b02f7310ff5b376f/545985529.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"1133732595","source_path":""}}
         * min_definition : 0
         * max_definition : 41
         * mv_info : {"mv_id":"545982609","all_artist_id":"166","title":"说","aliastitle":"","subtitle":"张信哲","play_nums":"2353","publishtime":"1970-01-01","del_status":"0","artist_list":[{"artist_id":"166","ting_uid":"1118","artist_name":"张信哲","artist_480_800":"","artist_640_1136":"","avatar_small":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_48","avatar_mini":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_20","avatar_s180":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_180","avatar_s300":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_300","avatar_s500":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_500","del_status":"0"}],"artist_id":"166","thumbnail":"http://musicdata.baidu.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_160,h_90","thumbnail3":"http://business.cdn.qianqian.com/baidumisic","thumbnail2":"http://musicdata.baidu.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_240,h_135","artist":"张信哲","provider":"1"}
         * share_url : http://music.baidu.com/cms/webview/sharevideo?video_id=545982609
         */

        private VideoInfoBean video_info;
        private FilesBean files;
        private String min_definition;
        private String max_definition;
        private MvInfoBean mv_info;
        private String share_url;

        public boolean parser(JSONObject object){
            if(object == null)
                return false;

            try{
                if(object.has("min_definition")){
                    min_definition = object.getString("min_definition");
                }

                if(object.has("max_definition")){
                    max_definition = object.getString("max_definition");
                }

                if(object.has("share_url")){
                    share_url = object.getString("share_url");
                }

                video_info = new VideoInfoBean();
                if(object.has("video_info")){
                    JSONObject videoObject = object.getJSONObject("video_info");
                    video_info.parser(videoObject);
                }

                files = new FilesBean();
                if(object.has("files")){
                    JSONObject filesObject = object.getJSONObject("files");
                    files.parser(filesObject);
                }

                mv_info = new MvInfoBean();
                if(object.has("mv_info")){
                    JSONObject mvInfoObject = object.getJSONObject("mv_info");
                    mv_info.parser(mvInfoObject);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


            return true;
        }

        public VideoInfoBean getVideo_info() {
            return video_info;
        }

        public void setVideo_info(VideoInfoBean video_info) {
            this.video_info = video_info;
        }

        public FilesBean getFiles() {
            return files;
        }

        public void setFiles(FilesBean files) {
            this.files = files;
        }

        public String getMin_definition() {
            return min_definition;
        }

        public void setMin_definition(String min_definition) {
            this.min_definition = min_definition;
        }

        public String getMax_definition() {
            return max_definition;
        }

        public void setMax_definition(String max_definition) {
            this.max_definition = max_definition;
        }

        public MvInfoBean getMv_info() {
            return mv_info;
        }

        public void setMv_info(MvInfoBean mv_info) {
            this.mv_info = mv_info;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public static class VideoInfoBean {
            /**
             * video_id : 545985706
             * mv_id : 545982609
             * provider : 1
             * sourcepath : null
             * thumbnail : http://qukufile2.qianqian.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_160,h_90
             * thumbnail2 : http://qukufile2.qianqian.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_240,h_135
             * del_status : 0
             * distribution : 0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000
             */

            private String video_id;
            private String mv_id;
            private String provider;
            private String sourcepath;
            private String thumbnail;
            private String thumbnail2;
            private String del_status;
            private String distribution;

            public boolean parser(JSONObject object){
                if(object == null)
                    return false;

                try{
                    if(object.has("video_id")){
                        video_id = object.getString("video_id");
                    }

                    if(object.has("mv_id")){
                        mv_id = object.getString("mv_id");
                    }

                    if(object.has("provider")){
                        provider = object.getString("provider");
                    }

                    if(object.has("sourcepath")){
                        sourcepath = object.getString("sourcepath");
                    }

                    if(object.has("thumbnail")){
                        thumbnail = object.getString("thumbnail");
                    }

                    if(object.has("thumbnail2")){
                        thumbnail2 = object.getString("thumbnail2");
                    }

                    if(object.has("del_status")){
                        del_status = object.getString("del_status");
                    }

                    if(object.has("distribution")){
                        distribution = object.getString("distribution");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            }

            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
            }

            public String getMv_id() {
                return mv_id;
            }

            public void setMv_id(String mv_id) {
                this.mv_id = mv_id;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            public String getSourcepath() {
                return sourcepath;
            }

            public void setSourcepath(String sourcepath) {
                this.sourcepath = sourcepath;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getThumbnail2() {
                return thumbnail2;
            }

            public void setThumbnail2(String thumbnail2) {
                this.thumbnail2 = thumbnail2;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public String getDistribution() {
                return distribution;
            }

            public void setDistribution(String distribution) {
                this.distribution = distribution;
            }
        }

        public static class FilesBean {
            public List<FileItemBean> getListFileItems() {
                return listFileItems;
            }

            public void setListFileItems(List<FileItemBean> listFileItems) {
                this.listFileItems = listFileItems;
            }

            /**
             * 41 : {"video_file_id":"546202774","video_id":"545985706","definition":"41","file_link":"http://qukufile2.qianqian.com/data2/video/545985478/d8441e9fc94c2702f80dd7dc9cae5090/545985478.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"34714649","source_path":""}
             * 31 : {"video_file_id":"546202775","video_id":"545985706","definition":"31","file_link":"http://qukufile2.qianqian.com/data2/video/545985500/63d242713b9834ddae012ca680f221be/545985500.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"18521372","source_path":""}
             * 0 : {"video_file_id":"546202776","video_id":"545985706","definition":"0","file_link":"http://qukufile2.qianqian.com/data2/video/545985529/da94cd86457e5c05b02f7310ff5b376f/545985529.mp4","file_format":"mp4","file_extension":"mp4","file_duration":"0","file_size":"1133732595","source_path":""}
             */
            private List<FileItemBean> listFileItems = new ArrayList<>();

            public boolean parser(JSONObject object){
                if(object == null)
                    return false;

                try{
                    Iterator<String> keys = object.keys();
                    while (keys.hasNext()){
                        String key = keys.next();
                        FileItemBean itemBean = new FileItemBean();
                        itemBean.parser(object.getJSONObject(key));
                        listFileItems.add(itemBean);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            }

            public static class FileItemBean {
                /**
                 * video_file_id : 546202774
                 * video_id : 545985706
                 * definition : 41
                 * file_link : http://qukufile2.qianqian.com/data2/video/545985478/d8441e9fc94c2702f80dd7dc9cae5090/545985478.mp4
                 * file_format : mp4
                 * file_extension : mp4
                 * file_duration : 0
                 * file_size : 34714649
                 * source_path :
                 */

                private String video_file_id;
                private String video_id;
                private String definition;
                private String file_link;
                private String file_format;
                private String file_extension;
                private String file_duration;
                private String file_size;
                private String source_path;

                public boolean parser(JSONObject object){
                    if(object == null)
                        return false;

                    try{
                        if(object.has("video_file_id")){
                            video_file_id = object.getString("video_file_id");
                        }

                        if(object.has("video_id")){
                            video_id = object.getString("video_id");
                        }

                        if(object.has("definition")){
                            definition = object.getString("definition");
                        }

                        if(object.has("file_link")){
                            file_link = object.getString("file_link");
                        }

                        if(object.has("file_format")){
                            file_format = object.getString("file_format");
                        }

                        if(object.has("file_extension")){
                            file_extension = object.getString("file_extension");
                        }

                        if(object.has("file_duration")){
                            file_duration = object.getString("file_duration");
                        }

                        if(object.has("file_size")){
                            file_size = object.getString("file_size");
                        }

                        if(object.has("source_path")){
                            source_path = object.getString("source_path");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    return true;
                }

                public String getVideo_file_id() {
                    return video_file_id;
                }

                public void setVideo_file_id(String video_file_id) {
                    this.video_file_id = video_file_id;
                }

                public String getVideo_id() {
                    return video_id;
                }

                public void setVideo_id(String video_id) {
                    this.video_id = video_id;
                }

                public String getDefinition() {
                    return definition;
                }

                public void setDefinition(String definition) {
                    this.definition = definition;
                }

                public String getFile_link() {
                    return file_link;
                }

                public void setFile_link(String file_link) {
                    this.file_link = file_link;
                }

                public String getFile_format() {
                    return file_format;
                }

                public void setFile_format(String file_format) {
                    this.file_format = file_format;
                }

                public String getFile_extension() {
                    return file_extension;
                }

                public void setFile_extension(String file_extension) {
                    this.file_extension = file_extension;
                }

                public String getFile_duration() {
                    return file_duration;
                }

                public void setFile_duration(String file_duration) {
                    this.file_duration = file_duration;
                }

                public String getFile_size() {
                    return file_size;
                }

                public void setFile_size(String file_size) {
                    this.file_size = file_size;
                }

                public String getSource_path() {
                    return source_path;
                }

                public void setSource_path(String source_path) {
                    this.source_path = source_path;
                }
            }
        }

        public static class MvInfoBean {
            /**
             * mv_id : 545982609
             * all_artist_id : 166
             * title : 说
             * aliastitle :
             * subtitle : 张信哲
             * play_nums : 2353
             * publishtime : 1970-01-01
             * del_status : 0
             * artist_list : [{"artist_id":"166","ting_uid":"1118","artist_name":"张信哲","artist_480_800":"","artist_640_1136":"","avatar_small":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_48","avatar_mini":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_20","avatar_s180":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_180","avatar_s300":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_300","avatar_s500":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_500","del_status":"0"}]
             * artist_id : 166
             * thumbnail : http://musicdata.baidu.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_160,h_90
             * thumbnail3 : http://business.cdn.qianqian.com/baidumisic
             * thumbnail2 : http://musicdata.baidu.com/data2/pic/3740ba7733b00be54a6dd6a9a5257703/545981108/545981108.jpg@s_0,w_240,h_135
             * artist : 张信哲
             * provider : 1
             */

            private String mv_id;
            private String all_artist_id;
            private String title;
            private String aliastitle;
            private String subtitle;
            private String play_nums;
            private String publishtime;
            private String del_status;
            private String artist_id;
            private String thumbnail;
            private String thumbnail3;
            private String thumbnail2;
            private String artist;
            private String provider;
            private List<ArtistListBean> artist_list;

            public boolean parser(JSONObject object){
                if(object == null)
                    return false;

                try{
                    if(object.has("mv_id")){
                        mv_id = object.getString("mv_id");
                    }

                    if(object.has("all_artist_id")){
                        all_artist_id = object.getString("all_artist_id");
                    }

                    if(object.has("title")){
                        title = object.getString("title");
                    }

                    if(object.has("aliastitle")){
                        aliastitle = object.getString("aliastitle");
                    }

                    if(object.has("subtitle")){
                        subtitle = object.getString("subtitle");
                    }

                    if(object.has("play_nums")){
                        play_nums = object.getString("play_nums");
                    }

                    if(object.has("publishtime")){
                        publishtime = object.getString("publishtime");
                    }

                    if(object.has("del_status")){
                        del_status = object.getString("del_status");
                    }

                    if(object.has("artist_id")){
                        artist_id = object.getString("artist_id");
                    }

                    if(object.has("thumbnail")){
                        thumbnail = object.getString("thumbnail");
                    }

                    if(object.has("thumbnail3")){
                        thumbnail3 = object.getString("thumbnail3");
                    }

                    if(object.has("thumbnail2")){
                        thumbnail2 = object.getString("thumbnail2");
                    }

                    if(object.has("artist")){
                        artist = object.getString("artist");
                    }

                    if(object.has("provider")){
                        provider = object.getString("provider");
                    }

                    artist_list = new ArrayList<>();
                    JSONArray jsonArray = object.getJSONArray("artist_list");
                    for(int i = 0; i < jsonArray.length();i++){
                        ArtistListBean artistListBean = new ArtistListBean();
                        artistListBean.parser(jsonArray.getJSONObject(i));
                        artist_list.add(artistListBean);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            }

            public String getMv_id() {
                return mv_id;
            }

            public void setMv_id(String mv_id) {
                this.mv_id = mv_id;
            }

            public String getAll_artist_id() {
                return all_artist_id;
            }

            public void setAll_artist_id(String all_artist_id) {
                this.all_artist_id = all_artist_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAliastitle() {
                return aliastitle;
            }

            public void setAliastitle(String aliastitle) {
                this.aliastitle = aliastitle;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public String getPlay_nums() {
                return play_nums;
            }

            public void setPlay_nums(String play_nums) {
                this.play_nums = play_nums;
            }

            public String getPublishtime() {
                return publishtime;
            }

            public void setPublishtime(String publishtime) {
                this.publishtime = publishtime;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public String getArtist_id() {
                return artist_id;
            }

            public void setArtist_id(String artist_id) {
                this.artist_id = artist_id;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getThumbnail3() {
                return thumbnail3;
            }

            public void setThumbnail3(String thumbnail3) {
                this.thumbnail3 = thumbnail3;
            }

            public String getThumbnail2() {
                return thumbnail2;
            }

            public void setThumbnail2(String thumbnail2) {
                this.thumbnail2 = thumbnail2;
            }

            public String getArtist() {
                return artist;
            }

            public void setArtist(String artist) {
                this.artist = artist;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            public List<ArtistListBean> getArtist_list() {
                return artist_list;
            }

            public void setArtist_list(List<ArtistListBean> artist_list) {
                this.artist_list = artist_list;
            }

            public static class ArtistListBean {
                /**
                 * artist_id : 166
                 * ting_uid : 1118
                 * artist_name : 张信哲
                 * artist_480_800 :
                 * artist_640_1136 :
                 * avatar_small : http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_48
                 * avatar_mini : http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_20
                 * avatar_s180 : http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_180
                 * avatar_s300 : http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_300
                 * avatar_s500 : http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_500
                 * del_status : 0
                 */

                private String artist_id;
                private String ting_uid;
                private String artist_name;
                private String artist_480_800;
                private String artist_640_1136;
                private String avatar_small;
                private String avatar_mini;
                private String avatar_s180;
                private String avatar_s300;
                private String avatar_s500;
                private String del_status;

                public boolean parser(JSONObject object){
                    if(object == null)
                        return false;

                    try{
                        if(object.has("artist_id")){
                            artist_id = object.getString("artist_id");
                        }

                        if(object.has("ting_uid")){
                            ting_uid = object.getString("ting_uid");
                        }

                        if(object.has("artist_name")){
                            artist_name = object.getString("artist_name");
                        }

                        if(object.has("artist_480_800")){
                            artist_480_800 = object.getString("artist_480_800");
                        }

                        if(object.has("artist_640_1136")){
                            artist_640_1136 = object.getString("artist_640_1136");
                        }

                        if(object.has("avatar_small")){
                            avatar_small = object.getString("avatar_small");
                        }

                        if(object.has("avatar_s180")){
                            avatar_s180 = object.getString("avatar_s180");
                        }

                        if(object.has("avatar_s300")){
                            avatar_s300 = object.getString("avatar_s300");
                        }

                        if(object.has("avatar_s500")){
                            avatar_s500 = object.getString("avatar_s500");
                        }

                        if(object.has("del_status")){
                            del_status = object.getString("del_status");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    return true;
                }

                public String getArtist_id() {
                    return artist_id;
                }

                public void setArtist_id(String artist_id) {
                    this.artist_id = artist_id;
                }

                public String getTing_uid() {
                    return ting_uid;
                }

                public void setTing_uid(String ting_uid) {
                    this.ting_uid = ting_uid;
                }

                public String getArtist_name() {
                    return artist_name;
                }

                public void setArtist_name(String artist_name) {
                    this.artist_name = artist_name;
                }

                public String getArtist_480_800() {
                    return artist_480_800;
                }

                public void setArtist_480_800(String artist_480_800) {
                    this.artist_480_800 = artist_480_800;
                }

                public String getArtist_640_1136() {
                    return artist_640_1136;
                }

                public void setArtist_640_1136(String artist_640_1136) {
                    this.artist_640_1136 = artist_640_1136;
                }

                public String getAvatar_small() {
                    return avatar_small;
                }

                public void setAvatar_small(String avatar_small) {
                    this.avatar_small = avatar_small;
                }

                public String getAvatar_mini() {
                    return avatar_mini;
                }

                public void setAvatar_mini(String avatar_mini) {
                    this.avatar_mini = avatar_mini;
                }

                public String getAvatar_s180() {
                    return avatar_s180;
                }

                public void setAvatar_s180(String avatar_s180) {
                    this.avatar_s180 = avatar_s180;
                }

                public String getAvatar_s300() {
                    return avatar_s300;
                }

                public void setAvatar_s300(String avatar_s300) {
                    this.avatar_s300 = avatar_s300;
                }

                public String getAvatar_s500() {
                    return avatar_s500;
                }

                public void setAvatar_s500(String avatar_s500) {
                    this.avatar_s500 = avatar_s500;
                }

                public String getDel_status() {
                    return del_status;
                }

                public void setDel_status(String del_status) {
                    this.del_status = del_status;
                }
            }
        }
    }
}
