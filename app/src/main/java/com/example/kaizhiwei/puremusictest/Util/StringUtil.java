package com.example.kaizhiwei.puremusictest.Util;

import android.text.TextUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/6.
 */
public class StringUtil {

    public static boolean startsWith(List<String> list, String text) {
        for (int i = 0; i < list.size(); i++) {
            if (text.startsWith(list.get(i)))
                return true;
        }

        return false;
    }

    public static boolean startsWith(String[] items, String text) {
        for (String item : items) {
            if (text.startsWith(item))
                return true;
        }

        return false;
    }

    public static boolean contains(String[] items, String text) {
        for (String item : items) {
            if (text.equals(item))
                return true;
        }

        return false;
    }

    public static boolean contains(List<String> list, String text) {
        for (int i = 0; i < list.size(); i++) {
            if (text.equals(list.get(i)))
                return true;
        }

        return false;
    }

    public static String getName(String path) {
        if (TextUtils.isEmpty(path))
            return "";

        int index = path.lastIndexOf(File.separator);
        if (index < 0)
            return "";

        return path.substring(index + 1);
    }

    public static String getExtention(String path) {
        if (TextUtils.isEmpty(path))
            return "";

        int index = path.lastIndexOf(".");
        if (index < 0)
            return "";

        return path.substring(index);
    }

    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }
    }

    public static String unicode2hanzi(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') ||
                        (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char)
                                Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }
}
