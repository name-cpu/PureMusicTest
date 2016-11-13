package com.example.kaizhiwei.puremusictest.Util;

import android.text.TextUtils;

import java.io.File;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/6.
 */
public class StringUtil {

    public static boolean startsWith(List<String> list, String text){
        for(int i = 0;i <list.size();i++){
            if(text.startsWith(list.get(i)))
                return true;
        }

        return false;
    }

    public static boolean startsWith(String[] items, String text){
        for(String item : items) {
            if(text.startsWith(item))
                return true;
        }

        return false;
    }

    public static boolean contains(String[] items, String text){
        for(String item : items) {
            if(text.equals(item))
                return true;
        }

        return false;
    }

    public static boolean contains(List<String> list, String text){
        for(int i = 0;i <list.size();i++){
            if(text.equals(list.get(i)))
                return true;
        }

        return false;
    }

    public static String getName(String path){
        if(TextUtils.isEmpty(path))
            return "";

        int index = path.lastIndexOf(File.separator);
        if(index < 0)
            return "";

        return path.substring(index + 1);
    }

    public static String getExtention(String path){
        if(TextUtils.isEmpty(path))
            return "";

        int index = path.lastIndexOf(".");
        if(index < 0)
            return "";

        return path.substring(index);
    }
}
