package com.example.kaizhiwei.puremusictest.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by kaizhiwei on 17/6/29.
 */

public class ArtistGetListBean {
    /**
     * artist : [{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246669444/246669444.jpg@s_0,w_20","firstchar":"X","ting_uid":"2517","avatar_middle":"http://musicdata.baidu.com/data2/pic/246669444/246669444.jpg@s_0,w_120","name":"薛之谦","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"88","avatar_small":"http://musicdata.baidu.com/data2/pic/246669444/246669444.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246669444/246669444.jpg@s_0,w_240","albums_total":"16","songs_total":"81"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246668350/246668350.jpg@s_0,w_20","firstchar":"Q","ting_uid":"245815","avatar_middle":"http://musicdata.baidu.com/data2/pic/246668350/246668350.jpg@s_0,w_120","name":"祁隆","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"57297","avatar_small":"http://musicdata.baidu.com/data2/pic/246668350/246668350.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246668350/246668350.jpg@s_0,w_240","albums_total":"44","songs_total":"149"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_20","firstchar":"Z","ting_uid":"7994","avatar_middle":"http://musicdata.baidu.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_120","name":"周杰伦","islocate":0,"gender":"0","country":"台湾","piao_id":"0","artist_id":"29","avatar_small":"http://musicdata.baidu.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/046d17bfa056e736d873ec4f891e338f/540336142/540336142.jpg@s_0,w_240","albums_total":"38","songs_total":"408"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246667237/246667237.jpg@s_0,w_20","firstchar":"G","ting_uid":"7898","avatar_middle":"http://musicdata.baidu.com/data2/pic/246667237/246667237.jpg@s_0,w_120","name":"G.E.M.邓紫棋","islocate":0,"gender":"1","country":"香港","piao_id":"0","artist_id":"1814","avatar_small":"http://musicdata.baidu.com/data2/pic/246667237/246667237.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246667237/246667237.jpg@s_0,w_240","albums_total":"20","songs_total":"65"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/07ec751a328b56305a2a6edc440ee74a/542130675/542130675.jpg@s_0,w_20","firstchar":"Z","ting_uid":"90654808","avatar_middle":"http://musicdata.baidu.com/data2/pic/07ec751a328b56305a2a6edc440ee74a/542130675/542130675.jpg@s_0,w_120","name":"赵雷","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"13874366","avatar_small":"http://musicdata.baidu.com/data2/pic/07ec751a328b56305a2a6edc440ee74a/542130675/542130675.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/07ec751a328b56305a2a6edc440ee74a/542130675/542130675.jpg@s_0,w_240","albums_total":"6","songs_total":"35"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/ef48ad0b18aafcff40ed9877a603ff22/267376192/267376192.jpg@s_0,w_20","firstchar":"Q","ting_uid":"1117","avatar_middle":"http://musicdata.baidu.com/data2/pic/ef48ad0b18aafcff40ed9877a603ff22/267376192/267376192.jpg@s_0,w_120","name":"齐秦","islocate":0,"gender":"0","country":"台湾","piao_id":"0","artist_id":"163","avatar_small":"http://musicdata.baidu.com/data2/pic/ef48ad0b18aafcff40ed9877a603ff22/267376192/267376192.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/ef48ad0b18aafcff40ed9877a603ff22/267376192/267376192.jpg@s_0,w_240","albums_total":"31","songs_total":"215"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246707812/246707812.jpgs_0,w_20","firstchar":"L","ting_uid":"1078","avatar_middle":"http://musicdata.baidu.com/data2/pic/246707812/246707812.jpg@s_0,w_120","name":"李玉刚","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"89","avatar_small":"http://musicdata.baidu.com/data2/pic/246707812/246707812.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246707812/246707812.jpg@s_0,w_240","albums_total":"6","songs_total":"21"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/99c89b46db96782245fa64d48ea8a08a/540354163/540354163.jpg@s_0,w_20","firstchar":"L","ting_uid":"1383","avatar_middle":"http://musicdata.baidu.com/data2/pic/99c89b46db96782245fa64d48ea8a08a/540354163/540354163.jpg@s_0,w_120","name":"李健","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"776","avatar_small":"http://musicdata.baidu.com/data2/pic/99c89b46db96782245fa64d48ea8a08a/540354163/540354163.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/99c89b46db96782245fa64d48ea8a08a/540354163/540354163.jpg@s_0,w_240","albums_total":"10","songs_total":"60"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/86426181/86426181.jpg@s_0,w_20","firstchar":"L","ting_uid":"1629","avatar_middle":"http://musicdata.baidu.com/data2/pic/86426181/86426181.jpg@s_0,w_120","name":"冷漠","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"1842","avatar_small":"http://musicdata.baidu.com/data2/pic/86426181/86426181.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/86426181/86426181.jpg@s_0,w_240","albums_total":"29","songs_total":"85"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246709489/246709489.jpg@s_0,w_20","firstchar":"R","ting_uid":"1094","avatar_middle":"http://musicdata.baidu.com/data2/pic/246709489/246709489.jpg@s_0,w_120","name":"任贤齐","islocate":0,"gender":"0","country":"台湾","piao_id":"0","artist_id":"119","avatar_small":"http://musicdata.baidu.com/data2/pic/246709489/246709489.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246709489/246709489.jpg@s_0,w_240","albums_total":"19","songs_total":"310"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246669218/246669218.jpg@s_0,w_20","firstchar":"X","ting_uid":"707709","avatar_middle":"http://musicdata.baidu.com/data2/pic/246669218/246669218.jpg@s_0,w_120","name":"小蓓蕾组合","islocate":0,"gender":"2","country":"中国","piao_id":"0","artist_id":"10367212","avatar_small":"http://musicdata.baidu.com/data2/pic/246669218/246669218.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246669218/246669218.jpg@s_0,w_240","albums_total":"149","songs_total":"2667"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246585512/246585512.jpg@s_0,w_20","firstchar":"L","ting_uid":"1376","avatar_middle":"http://musicdata.baidu.com/data2/pic/246585512/246585512.jpg@s_0,w_120","name":"龙梅子","islocate":0,"gender":"1","country":"中国","piao_id":"0","artist_id":"762","avatar_small":"http://musicdata.baidu.com/data2/pic/246585512/246585512.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246585512/246585512.jpg@s_0,w_240","albums_total":"14","songs_total":"58"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246667743/246667743.jpg@s_0,w_20","firstchar":"G","ting_uid":"617453","avatar_middle":"http://musicdata.baidu.com/data2/pic/246667743/246667743.jpg@s_0,w_120","name":"高安","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"3699","avatar_small":"http://musicdata.baidu.com/data2/pic/246667743/246667743.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246667743/246667743.jpg@s_0,w_240","albums_total":"12","songs_total":"21"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246585973/246585973.jpg@s_0,w_20","firstchar":"W","ting_uid":"821050","avatar_middle":"http://musicdata.baidu.com/data2/pic/246585973/246585973.jpg@s_0,w_120","name":"乌兰图雅","islocate":0,"gender":"1","country":"中国","piao_id":"0","artist_id":"5734662","avatar_small":"http://musicdata.baidu.com/data2/pic/246585973/246585973.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246585973/246585973.jpg@s_0,w_240","albums_total":"19","songs_total":"25"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246710232/246710232.jpg@s_0,w_20","firstchar":"Z","ting_uid":"1097","avatar_middle":"http://musicdata.baidu.com/data2/pic/246710232/246710232.jpg@s_0,w_120","name":"周华健","islocate":0,"gender":"0","country":"香港","piao_id":"0","artist_id":"123","avatar_small":"http://musicdata.baidu.com/data2/pic/246710232/246710232.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246710232/246710232.jpg@s_0,w_240","albums_total":"39","songs_total":"591"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246708144/246708144.jpg@s_0,w_20","firstchar":"L","ting_uid":"1067","avatar_middle":"http://musicdata.baidu.com/data2/pic/246708144/246708144.jpg@s_0,w_120","name":"刘若英","islocate":0,"gender":"1","country":"台湾","piao_id":"0","artist_id":"74","avatar_small":"http://musicdata.baidu.com/data2/pic/246708144/246708144.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246708144/246708144.jpg@s_0,w_240","albums_total":"12","songs_total":"197"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/af739e0109798366b9419230be5253ce/541222074/541222074.jpg@s_0,w_20","firstchar":"C","ting_uid":"1077","avatar_middle":"http://musicdata.baidu.com/data2/pic/af739e0109798366b9419230be5253ce/541222074/541222074.jpg@s_0,w_120","name":"陈奕迅","islocate":0,"gender":"0","country":"香港","piao_id":"0","artist_id":"87","avatar_small":"http://musicdata.baidu.com/data2/pic/af739e0109798366b9419230be5253ce/541222074/541222074.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/af739e0109798366b9419230be5253ce/541222074/541222074.jpg@s_0,w_240","albums_total":"67","songs_total":"1096"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/121900710/121900710.jpg@s_0,w_20","firstchar":"L","ting_uid":"132632388","avatar_middle":"http://musicdata.baidu.com/data2/pic/121900710/121900710.jpg@s_0,w_120","name":"刘珂矣","islocate":1,"gender":"1","country":"","piao_id":"0","artist_id":"119680493","avatar_small":"http://musicdata.baidu.com/data2/pic/121900710/121900710.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/121900710/121900710.jpg@s_0,w_240","albums_total":"6","songs_total":"23"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/262900349/262900349.jpg@s_0,w_20","firstchar":"X","ting_uid":"1579","avatar_middle":"http://musicdata.baidu.com/data2/pic/262900349/262900349.jpg@s_0,w_120","name":"徐佳莹","islocate":0,"gender":"1","country":"台湾","piao_id":"0","artist_id":"1641","avatar_small":"http://musicdata.baidu.com/data2/pic/262900349/262900349.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/262900349/262900349.jpg@s_0,w_240","albums_total":"12","songs_total":"110"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/5de4020c33b2404ad589ff5131479e1b/261054551/261054551.jpg@s_0,w_20","firstchar":"S","ting_uid":"1174","avatar_middle":"http://musicdata.baidu.com/data2/pic/5de4020c33b2404ad589ff5131479e1b/261054551/261054551.jpg@s_0,w_120","name":"宋祖英","islocate":0,"gender":"1","country":"中国","piao_id":"0","artist_id":"285","avatar_small":"http://musicdata.baidu.com/data2/pic/5de4020c33b2404ad589ff5131479e1b/261054551/261054551.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/5de4020c33b2404ad589ff5131479e1b/261054551/261054551.jpg@s_0,w_240","albums_total":"25","songs_total":"171"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/51734e72b779b86086052a5da798d56c/269993940/269993940.jpg@s_0,w_20","firstchar":"J","ting_uid":"232949589","avatar_middle":"http://musicdata.baidu.com/data2/pic/51734e72b779b86086052a5da798d56c/269993940/269993940.jpg@s_0,w_120","name":"贾乃亮","islocate":0,"gender":"0","country":"","piao_id":"0","artist_id":"134394404","avatar_small":"http://musicdata.baidu.com/data2/pic/51734e72b779b86086052a5da798d56c/269993940/269993940.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/51734e72b779b86086052a5da798d56c/269993940/269993940.jpg@s_0,w_240","albums_total":"4","songs_total":"3"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246709740/246709740.jpg@s_0,w_20","firstchar":"W","ting_uid":"1090","avatar_middle":"http://musicdata.baidu.com/data2/pic/246709740/246709740.jpg@s_0,w_120","name":"王杰","islocate":0,"gender":"0","country":"香港","piao_id":"0","artist_id":"115","avatar_small":"http://musicdata.baidu.com/data2/pic/246709740/246709740.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246709740/246709740.jpg@s_0,w_240","albums_total":"19","songs_total":"304"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246708025/246708025.jpg@s_0,w_20","firstchar":"L","ting_uid":"1052","avatar_middle":"http://musicdata.baidu.com/data2/pic/246708025/246708025.jpg@s_0,w_120","name":"林俊杰","islocate":0,"gender":"0","country":"新加坡","piao_id":"0","artist_id":"45","avatar_small":"http://musicdata.baidu.com/data2/pic/246708025/246708025.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246708025/246708025.jpg@s_0,w_240","albums_total":"15","songs_total":"165"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/cd5cf4a564a4db8e01fc7c26153bfeb2/273584035/273584035.jpg@s_0,w_20","firstchar":"W","ting_uid":"56819","avatar_middle":"http://musicdata.baidu.com/data2/pic/cd5cf4a564a4db8e01fc7c26153bfeb2/273584035/273584035.jpg@s_0,w_120","name":"王绎龙","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"1810","avatar_small":"http://musicdata.baidu.com/data2/pic/cd5cf4a564a4db8e01fc7c26153bfeb2/273584035/273584035.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/cd5cf4a564a4db8e01fc7c26153bfeb2/273584035/273584035.jpg@s_0,w_240","albums_total":"18","songs_total":"85"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/07c51ce40aa7ebf333cb40591b00f5b0/542130374/542130374.png@s_0,w_20","firstchar":"L","ting_uid":"1133","avatar_middle":"http://musicdata.baidu.com/data2/pic/07c51ce40aa7ebf333cb40591b00f5b0/542130374/542130374.png@s_0,w_120","name":"林忆莲","islocate":0,"gender":"1","country":"香港","piao_id":"0","artist_id":"212","avatar_small":"http://musicdata.baidu.com/data2/pic/07c51ce40aa7ebf333cb40591b00f5b0/542130374/542130374.png@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/07c51ce40aa7ebf333cb40591b00f5b0/542130374/542130374.png@s_0,w_240","albums_total":"29","songs_total":"500"},{"area":"4","avatar_mini":"http://musicdata.baidu.com/data2/music/A393900E5787D910653060C38E82AA66/252364271/252364271.jpg@s_0,w_20","firstchar":"S","ting_uid":"553065","avatar_middle":"http://musicdata.baidu.com/data2/music/A393900E5787D910653060C38E82AA66/252364271/252364271.jpg@s_0,w_120","name":"少儿歌曲","islocate":0,"gender":"3","country":"","piao_id":"0","artist_id":"53155","avatar_small":"http://musicdata.baidu.com/data2/music/A393900E5787D910653060C38E82AA66/252364271/252364271.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/music/A393900E5787D910653060C38E82AA66/252364271/252364271.jpg@s_0,w_240","albums_total":"9","songs_total":"127"},{"area":"4","avatar_mini":"","firstchar":"X","ting_uid":"239562591","avatar_middle":"","name":"新雅室内乐","islocate":0,"gender":"3","country":"","piao_id":"0","artist_id":"266641340","avatar_small":"","avatar_big":"","albums_total":"19","songs_total":"216"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246586325/246586325.jpg@s_0,w_20","firstchar":"X","ting_uid":"1557","avatar_middle":"http://musicdata.baidu.com/data2/pic/246586325/246586325.jpg@s_0,w_120","name":"许嵩","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"1483","avatar_small":"http://musicdata.baidu.com/data2/pic/246586325/246586325.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246586325/246586325.jpg@s_0,w_240","albums_total":"17","songs_total":"90"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_20","firstchar":"Z","ting_uid":"1118","avatar_middle":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_120","name":"张信哲","islocate":0,"gender":"0","country":"台湾","piao_id":"0","artist_id":"166","avatar_small":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246709056/246709056.jpg@s_0,w_240","albums_total":"33","songs_total":"393"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246707883/246707883.jpg@s_0,w_20","firstchar":"L","ting_uid":"1095","avatar_middle":"http://musicdata.baidu.com/data2/pic/246707883/246707883.jpg@s_0,w_120","name":"梁静茹","islocate":0,"gender":"1","country":"马来西亚","piao_id":"0","artist_id":"120","avatar_small":"http://musicdata.baidu.com/data2/pic/246707883/246707883.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246707883/246707883.jpg@s_0,w_240","albums_total":"16","songs_total":"206"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/ce52b58848ff456cd4d48765f2f3dd1e/267770218/267770218.jpg@s_0,w_20","firstchar":"A","ting_uid":"1115","avatar_middle":"http://musicdata.baidu.com/data2/pic/ce52b58848ff456cd4d48765f2f3dd1e/267770218/267770218.jpg@s_0,w_120","name":"阿杜","islocate":0,"gender":"0","country":"新加坡","piao_id":"0","artist_id":"160","avatar_small":"http://musicdata.baidu.com/data2/pic/ce52b58848ff456cd4d48765f2f3dd1e/267770218/267770218.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/ce52b58848ff456cd4d48765f2f3dd1e/267770218/267770218.jpg@s_0,w_240","albums_total":"10","songs_total":"101"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246586114/246586114.jpg@s_0,w_20","firstchar":"Y","ting_uid":"1581","avatar_middle":"http://musicdata.baidu.com/data2/pic/246586114/246586114.jpg@s_0,w_120","name":"郁可唯","islocate":0,"gender":"1","country":"中国","piao_id":"0","artist_id":"1656","avatar_small":"http://musicdata.baidu.com/data2/pic/246586114/246586114.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246586114/246586114.jpg@s_0,w_240","albums_total":"14","songs_total":"79"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246585831/246585831.jpg@s_0,w_20","firstchar":"S","ting_uid":"1584151","avatar_middle":"http://musicdata.baidu.com/data2/pic/246585831/246585831.jpg@s_0,w_120","name":"孙露","islocate":0,"gender":"1","country":"中国","piao_id":"0","artist_id":"1868","avatar_small":"http://musicdata.baidu.com/data2/pic/246585831/246585831.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246585831/246585831.jpg@s_0,w_240","albums_total":"3","songs_total":"8"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/81ca1375940742aa83931917e405a501/266601521/266601521.jpg@s_0,w_20","firstchar":"W","ting_uid":"211833","avatar_middle":"http://musicdata.baidu.com/data2/pic/81ca1375940742aa83931917e405a501/266601521/266601521.jpg@s_0,w_120","name":"汪苏泷","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"56068","avatar_small":"http://musicdata.baidu.com/data2/pic/81ca1375940742aa83931917e405a501/266601521/266601521.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/81ca1375940742aa83931917e405a501/266601521/266601521.jpg@s_0,w_240","albums_total":"10","songs_total":"49"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246709767/246709767.jpg@s_0,w_20","firstchar":"W","ting_uid":"1107","avatar_middle":"http://musicdata.baidu.com/data2/pic/246709767/246709767.jpg@s_0,w_120","name":"王力宏","islocate":0,"gender":"0","country":"台湾","piao_id":"0","artist_id":"141","avatar_small":"http://musicdata.baidu.com/data2/pic/246709767/246709767.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246709767/246709767.jpg@s_0,w_240","albums_total":"30","songs_total":"444"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/3986b4dd7b2147804a6424447bf56f89/539814536/539814536.jpg@s_0,w_20","firstchar":"Z","ting_uid":"1378","avatar_middle":"http://musicdata.baidu.com/data2/pic/3986b4dd7b2147804a6424447bf56f89/539814536/539814536.jpg@s_0,w_120","name":"郑钧","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"770","avatar_small":"http://musicdata.baidu.com/data2/pic/3986b4dd7b2147804a6424447bf56f89/539814536/539814536.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/3986b4dd7b2147804a6424447bf56f89/539814536/539814536.jpg@s_0,w_240","albums_total":"11","songs_total":"65"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/6e07098a33d46d107bb149a3ccfd6e44/274990815/274990815.jpg@s_0,w_20","firstchar":"H","ting_uid":"10561","avatar_middle":"http://musicdata.baidu.com/data2/pic/6e07098a33d46d107bb149a3ccfd6e44/274990815/274990815.jpg@s_0,w_120","name":"黄龄","islocate":1,"gender":"1","country":"中国","piao_id":"0","artist_id":"1709","avatar_small":"http://musicdata.baidu.com/data2/pic/6e07098a33d46d107bb149a3ccfd6e44/274990815/274990815.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/6e07098a33d46d107bb149a3ccfd6e44/274990815/274990815.jpg@s_0,w_240","albums_total":"8","songs_total":"30"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246584869/246584869.jpg@s_0,w_20","firstchar":"F","ting_uid":"1490","avatar_middle":"http://musicdata.baidu.com/data2/pic/246584869/246584869.jpg@s_0,w_120","name":"凤凰传奇","islocate":0,"gender":"2","country":"中国","piao_id":"0","artist_id":"1231","avatar_small":"http://musicdata.baidu.com/data2/pic/246584869/246584869.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246584869/246584869.jpg@s_0,w_240","albums_total":"3","songs_total":"16"},{"area":"2","avatar_mini":"http://musicdata.baidu.com/data2/music/FB1E04384E51B2B374D7DAF2F0084C49/254484708/254484708.jpg@s_0,w_20","firstchar":"T","ting_uid":"108128682","avatar_middle":"http://musicdata.baidu.com/data2/music/FB1E04384E51B2B374D7DAF2F0084C49/254484708/254484708.jpg@s_0,w_120","name":"The Chainsmokers","islocate":0,"gender":"2","country":"美国","piao_id":"0","artist_id":"84906915","avatar_small":"http://musicdata.baidu.com/data2/music/FB1E04384E51B2B374D7DAF2F0084C49/254484708/254484708.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/music/FB1E04384E51B2B374D7DAF2F0084C49/254484708/254484708.jpg@s_0,w_240","albums_total":"21","songs_total":"63"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/music/C0EF5EAFE8DD870ED9BB7DE2E89EEB7D/252425876/252425876.jpg@s_0,w_20","firstchar":"G","ting_uid":"55532","avatar_middle":"http://musicdata.baidu.com/data2/music/C0EF5EAFE8DD870ED9BB7DE2E89EEB7D/252425876/252425876.jpg@s_0,w_120","name":"龚琳娜","islocate":0,"gender":"1","country":"中国","piao_id":"0","artist_id":"60875","avatar_small":"http://musicdata.baidu.com/data2/music/C0EF5EAFE8DD870ED9BB7DE2E89EEB7D/252425876/252425876.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/music/C0EF5EAFE8DD870ED9BB7DE2E89EEB7D/252425876/252425876.jpg@s_0,w_240","albums_total":"1","songs_total":"1"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/c866b4e2c57ea4b11b85a2a4f75d474d/533504710/533504710.jpg@s_0,w_20","firstchar":"W","ting_uid":"1098","avatar_middle":"http://musicdata.baidu.com/data2/pic/c866b4e2c57ea4b11b85a2a4f75d474d/533504710/533504710.jpg@s_0,w_120","name":"五月天","islocate":0,"gender":"2","country":"台湾","piao_id":"0","artist_id":"127","avatar_small":"http://musicdata.baidu.com/data2/pic/c866b4e2c57ea4b11b85a2a4f75d474d/533504710/533504710.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/c866b4e2c57ea4b11b85a2a4f75d474d/533504710/533504710.jpg@s_0,w_240","albums_total":"15","songs_total":"309"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246680097/246680097.jpg@s_0,w_20","firstchar":"H","ting_uid":"10622","avatar_middle":"http://musicdata.baidu.com/data2/pic/246680097/246680097.jpg@s_0,w_120","name":"胡夏","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"1999","avatar_small":"http://musicdata.baidu.com/data2/pic/246680097/246680097.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246680097/246680097.jpg@s_0,w_240","albums_total":"5","songs_total":"50"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246584633/246584633.jpg@s_0,w_20","firstchar":"D","ting_uid":"43997434","avatar_middle":"http://musicdata.baidu.com/data2/pic/246584633/246584633.jpg@s_0,w_120","name":"DJ小可","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"18135228","avatar_small":"http://musicdata.baidu.com/data2/pic/246584633/246584633.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246584633/246584633.jpg@s_0,w_240","albums_total":"10","songs_total":"33"},{"area":"1","avatar_mini":"http://musicdata.baidu.com/data2/pic/246668318/246668318.jpg@s_0,w_20","firstchar":"L","ting_uid":"1925","avatar_middle":"http://musicdata.baidu.com/data2/pic/246668318/246668318.jpg@s_0,w_120","name":"李宗盛","islocate":0,"gender":"0","country":"台湾","piao_id":"0","artist_id":"4598","avatar_small":"http://musicdata.baidu.com/data2/pic/246668318/246668318.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246668318/246668318.jpg@s_0,w_240","albums_total":"10","songs_total":"226"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/246586345/246586345.jpg@s_0,w_20","firstchar":"X","ting_uid":"1226","avatar_middle":"http://musicdata.baidu.com/data2/pic/246586345/246586345.jpg@s_0,w_120","name":"许巍","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"371","avatar_small":"http://musicdata.baidu.com/data2/pic/246586345/246586345.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246586345/246586345.jpg@s_0,w_240","albums_total":"4","songs_total":"33"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/ff8a54b6819bc6227465f03a868169c8/274144087/274144087.jpg@s_0,w_20","firstchar":"L","ting_uid":"1314","avatar_middle":"http://musicdata.baidu.com/data2/pic/ff8a54b6819bc6227465f03a868169c8/274144087/274144087.jpg@s_0,w_120","name":"老狼","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"537","avatar_small":"http://musicdata.baidu.com/data2/pic/ff8a54b6819bc6227465f03a868169c8/274144087/274144087.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/ff8a54b6819bc6227465f03a868169c8/274144087/274144087.jpg@s_0,w_240","albums_total":"3","songs_total":"21"},{"area":"2","avatar_mini":"http://musicdata.baidu.com/data2/pic/246937288/246937288.jpg@s_0,w_20","firstchar":"M","ting_uid":"83626","avatar_middle":"http://musicdata.baidu.com/data2/pic/246937288/246937288.jpg@s_0,w_120","name":"Michael Jackson","islocate":0,"gender":"0","country":"美国","piao_id":"0","artist_id":"821","avatar_small":"http://musicdata.baidu.com/data2/pic/246937288/246937288.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/246937288/246937288.jpg@s_0,w_240","albums_total":"60","songs_total":"839"},{"area":"0","avatar_mini":"http://musicdata.baidu.com/data2/pic/e0606a30358b0fc62841a7fdc46accbe/537898718/537898718.jpg@s_0,w_20","firstchar":"L","ting_uid":"247684","avatar_middle":"http://musicdata.baidu.com/data2/pic/e0606a30358b0fc62841a7fdc46accbe/537898718/537898718.jpg@s_0,w_120","name":"龙飞","islocate":0,"gender":"0","country":"中国","piao_id":"0","artist_id":"2021740","avatar_small":"http://musicdata.baidu.com/data2/pic/e0606a30358b0fc62841a7fdc46accbe/537898718/537898718.jpg@s_0,w_48","avatar_big":"http://musicdata.baidu.com/data2/pic/e0606a30358b0fc62841a7fdc46accbe/537898718/537898718.jpg@s_0,w_240","albums_total":"10","songs_total":"12"}]
     * havemore : 1
     * nums : 2000
     * noFirstChar :
     */

    private int havemore;
    private int nums;
    private String noFirstChar;
    private List<ArtistBean> artist;

    public int getHavemore() {
        return havemore;
    }

    public void setHavemore(int havemore) {
        this.havemore = havemore;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getNoFirstChar() {
        return noFirstChar;
    }

    public void setNoFirstChar(String noFirstChar) {
        this.noFirstChar = noFirstChar;
    }

    public List<ArtistBean> getArtist() {
        return artist;
    }

    public void setArtist(List<ArtistBean> artist) {
        this.artist = artist;
    }

    public static class ArtistBean implements Parcelable{
        /**
         * area : 0
         * avatar_mini : http://musicdata.baidu.com/data2/pic/246669444/246669444.jpg@s_0,w_20
         * firstchar : X
         * ting_uid : 2517
         * avatar_middle : http://musicdata.baidu.com/data2/pic/246669444/246669444.jpg@s_0,w_120
         * name : 薛之谦
         * islocate : 0
         * gender : 0
         * country : 中国
         * piao_id : 0
         * artist_id : 88
         * avatar_small : http://musicdata.baidu.com/data2/pic/246669444/246669444.jpg@s_0,w_48
         * avatar_big : http://musicdata.baidu.com/data2/pic/246669444/246669444.jpg@s_0,w_240
         * albums_total : 16
         * songs_total : 81
         */

        private String area;
        private String avatar_mini;
        private String firstchar;
        private String ting_uid;
        private String avatar_middle;
        private String name;
        private int islocate;
        private String gender;
        private String country;
        private String piao_id;
        private String artist_id;
        private String avatar_small;
        private String avatar_big;
        private String albums_total;
        private String songs_total;

        protected ArtistBean(Parcel in) {
            area = in.readString();
            avatar_mini = in.readString();
            firstchar = in.readString();
            ting_uid = in.readString();
            avatar_middle = in.readString();
            name = in.readString();
            islocate = in.readInt();
            gender = in.readString();
            country = in.readString();
            piao_id = in.readString();
            artist_id = in.readString();
            avatar_small = in.readString();
            avatar_big = in.readString();
            albums_total = in.readString();
            songs_total = in.readString();
        }

        public static final Creator<ArtistBean> CREATOR = new Creator<ArtistBean>() {
            @Override
            public ArtistBean createFromParcel(Parcel in) {
                return new ArtistBean(in);
            }

            @Override
            public ArtistBean[] newArray(int size) {
                return new ArtistBean[size];
            }
        };

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAvatar_mini() {
            return avatar_mini;
        }

        public void setAvatar_mini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
        }

        public String getFirstchar() {
            return firstchar;
        }

        public void setFirstchar(String firstchar) {
            this.firstchar = firstchar;
        }

        public String getTing_uid() {
            return ting_uid;
        }

        public void setTing_uid(String ting_uid) {
            this.ting_uid = ting_uid;
        }

        public String getAvatar_middle() {
            return avatar_middle;
        }

        public void setAvatar_middle(String avatar_middle) {
            this.avatar_middle = avatar_middle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIslocate() {
            return islocate;
        }

        public void setIslocate(int islocate) {
            this.islocate = islocate;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPiao_id() {
            return piao_id;
        }

        public void setPiao_id(String piao_id) {
            this.piao_id = piao_id;
        }

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

        public String getAlbums_total() {
            return albums_total;
        }

        public void setAlbums_total(String albums_total) {
            this.albums_total = albums_total;
        }

        public String getSongs_total() {
            return songs_total;
        }

        public void setSongs_total(String songs_total) {
            this.songs_total = songs_total;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(area);
            dest.writeString(avatar_mini);
            dest.writeString(firstchar);
            dest.writeString(ting_uid);
            dest.writeString(avatar_middle);
            dest.writeString(name);
            dest.writeInt(islocate);
            dest.writeString(gender);
            dest.writeString(country);
            dest.writeString(piao_id);
            dest.writeString(artist_id);
            dest.writeString(avatar_small);
            dest.writeString(avatar_big);
            dest.writeString(albums_total);
            dest.writeString(songs_total);
        }
    }
}
