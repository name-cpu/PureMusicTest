package com.example.kaizhiwei.puremusictest.MediaData;

public class ScanFile{

    /**
     * 要扫描的媒体文件路劲或包含媒体文件的文件夹路径
     */
    public String filePaths;

    /**
     * 要扫描的媒体文件类型 eg: audio/mp3  media/*  application/ogg
     *             image/jpeg  image/png  video/mpeg   video/3gpp
     *             ......
     */
    public String mineType;

    public ScanFile(String filePaths, String mineType) {
        this.filePaths = filePaths;
        this.mineType = mineType;
    }
}
