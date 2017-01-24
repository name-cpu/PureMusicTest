package com.example.kaizhiwei.puremusictest.Util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.kaizhiwei.puremusictest.PureMusicApplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by kaizhiwei on 16/11/6.
 */
public class DeviceUtil {
    private static final String TAG = "DeviceUtil";

    public static ArrayList<String> getStorageDirectories(){
        BufferedReader bufReader = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            list.add(Environment.getExternalStorageDirectory().getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> typeWL = Arrays.asList("vfat", "exfat", "sdcards", "fuse", "ntfs", "fat32", "ext3"
                , "ext4", "esdfs");
        List<String> typeBL = Arrays.asList("tmpfs");

        String[] mountWL = {"/mnt", "/Removable", "/Storage"};
        String[] mountBL = {"/mnt/secure", "/mnt/shell", "/mnt/asec", "/mnt/obb", "/mnt/media_rw/extsdCard",
            "/mnt/media_rw/sdcard", "/storage/emulated"};

        String[] deviceWL = {"/dev/block/vold", "/dev/fuse", "/mnt/media_rw"};

        try {
            bufReader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            while((line = bufReader.readLine()) != null){
                StringTokenizer tokener = new StringTokenizer(line);
                String device = tokener.nextToken();
                String mountpoint = tokener.nextToken();
                String type = tokener.nextToken();

                if(list.contains(mountpoint) || typeBL.contains(type) || StringUtil.startsWith(mountBL, mountpoint))
                    continue ;

                if(StringUtil.startsWith(deviceWL, device) && (typeWL.contains(type) || StringUtil.startsWith(mountWL, mountpoint)))
                {
                    boolean bContain = StringUtil.contains(list, StringUtil.getName(mountpoint));
                    if(bContain){
                        list.remove(mountpoint);
                    }
                    else{
                        list.add(mountpoint);
                        Log.i(TAG, mountpoint);
                    }
                }
            }

            bufReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取文件夹大小
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(java.io.File file){

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);

                }else{
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    public static String getIMEI(){
        TelephonyManager tm = (TelephonyManager) PureMusicApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String str = tm.getSubscriberId();
        String str1 = tm.getDeviceId();
        return "869804025132064";
    }

    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            String str = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            Log.i("weikaizhi", "UUID " + str);
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        }
        catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        String str = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        Log.i("weikaizhi", "UUID " + str);
        return str;
    }
}
